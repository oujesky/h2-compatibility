package cz.miou.h2.mariadb.json;

import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.PrettyPrinter;
import com.fasterxml.jackson.core.util.DefaultIndenter;
import com.fasterxml.jackson.core.util.DefaultPrettyPrinter;
import com.fasterxml.jackson.core.util.Separators;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ArrayNode;
import com.fasterxml.jackson.databind.node.BooleanNode;
import com.fasterxml.jackson.databind.node.DecimalNode;
import com.fasterxml.jackson.databind.node.DoubleNode;
import com.fasterxml.jackson.databind.node.IntNode;
import com.fasterxml.jackson.databind.node.LongNode;
import com.fasterxml.jackson.databind.node.NullNode;
import com.fasterxml.jackson.databind.node.NumericNode;
import com.fasterxml.jackson.databind.node.TextNode;
import com.jayway.jsonpath.Configuration;
import com.jayway.jsonpath.DocumentContext;
import com.jayway.jsonpath.InvalidJsonException;
import com.jayway.jsonpath.JsonPath;
import com.jayway.jsonpath.JsonPathException;
import com.jayway.jsonpath.Option;
import com.jayway.jsonpath.ParseContext;
import com.jayway.jsonpath.internal.path.ArrayIndexToken;
import com.jayway.jsonpath.internal.path.CompiledPath;
import com.jayway.jsonpath.internal.path.PathCompiler;
import com.jayway.jsonpath.internal.path.PropertyPathToken;
import com.jayway.jsonpath.spi.json.JacksonJsonNodeJsonProvider;
import com.jayway.jsonpath.spi.mapper.JacksonMappingProvider;
import org.h2.value.Value;
import org.h2.value.ValueBigint;
import org.h2.value.ValueBoolean;
import org.h2.value.ValueDecfloat;
import org.h2.value.ValueDouble;
import org.h2.value.ValueInteger;
import org.h2.value.ValueJson;
import org.h2.value.ValueNull;
import org.h2.value.ValueNumeric;
import org.h2.value.ValueVarchar;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.util.Comparator;
import java.util.Map;
import java.util.Objects;
import java.util.Spliterator;
import java.util.Spliterators;
import java.util.stream.Stream;
import java.util.stream.StreamSupport;

class JsonUtil {

    private static final Logger LOG = LoggerFactory.getLogger(JsonUtil.class);

    private static final int DEFAULT_TAB_SIZE = 4;

    private static ObjectMapper objectMapper;
    private static ParseContext jsonPath;
    private static Configuration jsonPathConfiguration;
    private static DefaultPrettyPrinter defaultPrinter;
    private static DefaultPrettyPrinter detailedPrinter;
    private static JsonEqualsComparator equalsComparator;

    private JsonUtil() {}

    public static <T> T readJson(byte[] json, Class<T> type) {
        try {
            return getObjectMapper().readValue(json, type);
        } catch (IOException e) {
            LOG.debug("Failed to parse json: {}", json, e);
            return null;
        }
    }

    private static DocumentContext readJsonDocument(byte[] json) {
        try {
            return getJsonPath().parse(json);
        } catch (InvalidJsonException e) {
            LOG.debug("Failed to parse json: {}", json, e);
            return null;
        }
    }

    private static DocumentContext readJsonDocument(String json) {
        try {
            return getJsonPath().parse(json);
        } catch (InvalidJsonException e) {
            LOG.debug("Failed to parse json: {}", json, e);
            return null;
        }
    }

    public static DocumentContext createJsonDocument(JsonNode node) {
        try {
            return getJsonPath().parse(node);
        } catch (InvalidJsonException e) {
            LOG.debug("Failed to create document from JSON node: {}", node.getNodeType(), e);
            return null;
        }
    }

    public static JsonNode readJsonNode(String json) {
        try {
            return getObjectMapper().readTree(json);
        } catch (JsonProcessingException e) {
            LOG.debug("Failed to parse json: {}", json, e);
            return null;
        }
    }

    public static <T extends JsonNode> T readJsonNode(String json, Class<T> type) {
        try {
            return getObjectMapper().readValue(json, type);
        } catch (JsonProcessingException e) {
            LOG.debug("Failed to parse json: {}", json, e);
            return null;
        }
    }

    public static Value toJsonValue(DocumentContext document) {
        try {
            return ValueJson.fromJson(document.jsonString());
        } catch (Exception e) {
            LOG.debug("Failed to output json: {}", document, e);
            return ValueNull.INSTANCE;
        }
    }

    public static Value toJsonValue(Object object) {
        try {
            var bytes = getObjectMapper()
                .writerWithDefaultPrettyPrinter()
                .writeValueAsBytes(object);
            return ValueJson.getInternal(bytes);
        } catch (JsonProcessingException e) {
            LOG.debug("Failed to output json: {}", object, e);
            return ValueNull.INSTANCE;
        }
    }

    public static Value toCompactJsonValue(Object object) {
        try {
            var bytes = getObjectMapper()
                .writeValueAsBytes(object);
            return ValueJson.getInternal(bytes);
        } catch (JsonProcessingException e) {
            LOG.debug("Failed to output json: {}", object, e);
            return ValueNull.INSTANCE;
        }
    }

    public static Value toDetailedJsonValue(Object object, int tabSize) {
        try {
            var bytes = getObjectMapper()
                .writer(getDetailedPrinter(tabSize))
                .writeValueAsBytes(object);
            return ValueJson.getInternal(bytes);
        } catch (JsonProcessingException e) {
            LOG.debug("Failed to output json: {}", object, e);
            return ValueNull.INSTANCE;
        }
    }

    private static ObjectMapper getObjectMapper() {
        if (objectMapper == null) {
            var prettyPrinter = getDefaultPrinter();

            objectMapper = new ObjectMapper()
                .setDefaultPrettyPrinter(prettyPrinter);
        }

        return objectMapper;
    }

    private static PrettyPrinter getDefaultPrinter() {
        if (defaultPrinter == null) {
            defaultPrinter = createDefaultPrinter();
        }
        return defaultPrinter;
    }

    private static PrettyPrinter getDetailedPrinter(int tabSize) {
        if (tabSize != DEFAULT_TAB_SIZE) {
            return createDetailedPrinter(tabSize);
        }

        if (detailedPrinter == null) {
            detailedPrinter = createDetailedPrinter(DEFAULT_TAB_SIZE);
        }

        return detailedPrinter;
    }

    private static DefaultPrettyPrinter createDefaultPrinter() {
        var separators = Separators.createDefaultInstance()
            .withArrayEmptySeparator("")
            .withObjectEmptySeparator("")
            .withArrayValueSpacing(Separators.Spacing.AFTER)
            .withObjectFieldValueSpacing(Separators.Spacing.AFTER)
            .withObjectEntrySpacing(Separators.Spacing.AFTER);

        return new DefaultPrettyPrinter()
            .withArrayIndenter(new DefaultPrettyPrinter.NopIndenter())
            .withObjectIndenter(new DefaultPrettyPrinter.NopIndenter())
            .withSeparators(separators)
            .createInstance();
    }

    private static DefaultPrettyPrinter createDetailedPrinter(int tabSize) {
        var indenter = new DefaultIndenter(" ".repeat(tabSize), "\n");

        var separators = Separators.createDefaultInstance()
            .withObjectFieldValueSpacing(Separators.Spacing.AFTER)
            .withArrayEmptySeparator("");

        var printer = new DefaultPrettyPrinter()
            .withArrayIndenter(indenter)
            .withObjectIndenter(indenter)
            .withSeparators(separators)
            .createInstance();

        return new DetailedPrinter(printer);
    }

    public static boolean jsonEquals(JsonNode a, JsonNode b) {
        if (equalsComparator == null) {
            equalsComparator = new JsonEqualsComparator();
        }

        return a.equals(equalsComparator, b);
    }

    public static boolean jsonPathExists(DocumentContext document, String path) {
        return jsonPathExists(document, JsonPath.compile(path));
    }

    public static boolean jsonPathExists(DocumentContext document, JsonPath path) {
        var node = document.read(path, JsonNode.class);
        return node != null && !node.isMissingNode();
    }

    public static Stream<Map.Entry<String, JsonNode>> getEntries(JsonNode node) {
        return StreamSupport.stream(Spliterators.spliteratorUnknownSize(node.fields(), Spliterator.ORDERED), false);
    }

    public static DocumentContext jsonDocumentFromValue(Value json) {
        if (json == null || json instanceof ValueNull) {
            return null;
        }

        return json instanceof ValueJson
            ? readJsonDocument(json.getBytesNoCopy())
            : readJsonDocument(json.getString());
    }

    public static JsonNode jsonNodeFromValue(Value json) {
        return json instanceof ValueJson
            ? readJson(json.getBytesNoCopy(), JsonNode.class)
            : readJsonNode(json.getString());
    }

    public static <T extends JsonNode> T jsonNodeFromValue(Value json, Class<T> type) {
        return json instanceof ValueJson
            ? readJson(json.getBytesNoCopy(), type)
            : readJsonNode(json.getString(), type);
    }

    private static ParseContext getJsonPath() {
        if (jsonPath == null) {
            var configuration = getJsonPathConfiguration();
            jsonPath = JsonPath.using(configuration);
        }

        return jsonPath;
    }

    public static Configuration getJsonPathConfiguration() {
        if (jsonPathConfiguration == null) {
            var mapper = getObjectMapper();
            jsonPathConfiguration = Configuration.builder()
                .jsonProvider(new JsonProvider(mapper))
                .mappingProvider(new MappingProvider(mapper))
                .options(Option.SUPPRESS_EXCEPTIONS)
                .build();
        }
        return jsonPathConfiguration;
    }

    public static Value valueFromJsonNode(JsonNode node) {
        if (node.isNull()) {
            return ValueNull.INSTANCE;
        }

        if (node.isBoolean()) {
            return ValueBoolean.get(node.asBoolean());
        }

        if (node.isInt()) {
            return ValueInteger.get(node.asInt());
        }

        if (node.isBigInteger() || node.isLong()) {
            return ValueBigint.get(node.asLong());
        }

        if (node.isDouble() || node.isFloat()) {
            return ValueDouble.get(node.asDouble());
        }

        if (node.isBigDecimal()) {
            return ValueNumeric.get(node.decimalValue());
        }

        if (node.isTextual()) {
            return ValueVarchar.get(node.asText());
        }

        return ValueNull.INSTANCE;
    }

    public static JsonNode scalarValueToJsonNode(Value value) {
        if (value instanceof ValueNull) {
            return NullNode.getInstance();
        }

        if (value instanceof ValueInteger) {
            return IntNode.valueOf(value.getInt());
        }

        if (value instanceof ValueBigint) {
            return LongNode.valueOf(value.getLong());
        }

        if (value instanceof ValueDouble) {
            return DoubleNode.valueOf(value.getDouble());
        }

        if (value instanceof ValueNumeric || value instanceof ValueDecfloat) {
            return DecimalNode.valueOf(value.getBigDecimal());
        }

        if (value instanceof ValueBoolean) {
            return BooleanNode.valueOf(value.getBoolean());
        }

        if (value instanceof ValueJson) {
            return readJson(value.getBytesNoCopy(), JsonNode.class);
        }

        return TextNode.valueOf(value.getString());
    }

    public static DocumentContext createJsonArrayDocument() {
        var node = getObjectMapper().createArrayNode();
        var document = createJsonDocument(node);
        return Objects.requireNonNull(document);
    }

    public static boolean isRootPath(JsonPath path) {
        return path.getPath().equals("$");
    }

    private static class JsonProvider extends JacksonJsonNodeJsonProvider {

        public JsonProvider(ObjectMapper objectMapper) {
            super(objectMapper);
        }

        @Override
        public String toJson(Object obj) {
            if (!(obj instanceof JsonNode)) {
                throw new JsonPathException("Not a JSON Node");
            }
            try {
                return objectMapper
                    .writerWithDefaultPrettyPrinter()
                    .writeValueAsString(obj);
            } catch (JsonProcessingException e) {
                throw new JsonPathException("Error writing JSON", e);
            }
        }

        @Override
        public Object getArrayIndex(Object obj, int idx) {
            return ((ArrayNode) obj).path(idx);
        }

    }

    private static class MappingProvider extends JacksonMappingProvider {

        public MappingProvider(ObjectMapper objectMapper) {
            super(objectMapper);
        }

        @SuppressWarnings("unchecked")
        @Override
        public <T> T map(Object source, Class<T> targetType, Configuration configuration) {
            if (source == null) {
                return null;
            }
            if (targetType.isAssignableFrom(source.getClass())) {
                return (T) source;
            }
            return super.map(source, targetType, configuration);
        }
    }

    private static class JsonEqualsComparator implements Comparator<JsonNode> {

        @SuppressWarnings("ComparatorMethodParameterNotUsed")
        @Override
        public int compare(JsonNode o1, JsonNode o2) {
            if (o1.equals(o2)) {
                return 0;
            }

            if (o1 instanceof NumericNode && o2 instanceof NumericNode) {
                double d1 = o1.asDouble();
                double d2 = o2.asDouble();
                if (d1 == d2) {
                    return 0;
                }
            }

            return 1;
        }

    }
    public static PathWithParent getPathWithParent(String path) {
        return getPathWithParent(JsonPath.compile(path));
    }

    public static PathWithParent getPathWithParent(JsonPath path) {
        var compiledPath = (CompiledPath) PathCompiler.compile(path.getPath());
        var root = compiledPath.getRoot();
        var tail = root.getTail();
        var fullPath = root.toString();
        var parentPath = JsonPath.compile(fullPath.substring(0, fullPath.length() - tail.toString().length()));

        if (tail instanceof PropertyPathToken) {
            var token = (PropertyPathToken) tail;
            var key = token.getProperties().get(0);
            return new ObjectPathWithParent(parentPath, key);
        }

        if (tail instanceof ArrayIndexToken) {
            var token = (ArrayIndexToken) tail;
            var fragment = token.getPathFragment();
            var index = Integer.parseInt(fragment.substring(1, fragment.length() - 1));
            return new ArrayPathWithParent(parentPath, index);
        }

        return null;
    }

    public interface PathWithParent {

        JsonPath getParentPath();
    }
    public static class ArrayPathWithParent implements PathWithParent {

        private final JsonPath parentPath;
        private final int index;
        public ArrayPathWithParent(JsonPath parentPath, int index) {
            this.parentPath = parentPath;
            this.index = index;
        }

        @Override
        public JsonPath getParentPath() {
            return parentPath;
        }

        public int getIndex() {
            return index;
        }

    }
    public static class ObjectPathWithParent implements PathWithParent {

        private final JsonPath parentPath;
        private final String key;
        public ObjectPathWithParent(JsonPath parentPath, String key) {
            this.parentPath = parentPath;
            this.key = key;
        }

        @Override
        public JsonPath getParentPath() {
            return parentPath;
        }

        public String getKey() {
            return key;
        }

    }
    enum OneOrAll {
        ONE,
        ALL;

        public static OneOrAll fromString(String mode) {
            try {
                return OneOrAll.valueOf(mode.toUpperCase());
            } catch (IllegalArgumentException e) {
                return null;
            }
        }

    }

    private static class DetailedPrinter extends DefaultPrettyPrinter {
        public DetailedPrinter(DefaultPrettyPrinter base) {
            super(base);
        }

        @Override
        public void writeStartObject(JsonGenerator g) throws IOException {
            if (_nesting > 0) {
                _objectIndenter.writeIndentation(g, _nesting);
            }
            super.writeStartObject(g);
        }

        @Override
        public void writeStartArray(JsonGenerator g) throws IOException {
            if (_nesting > 0) {
                _objectIndenter.writeIndentation(g, _nesting);
            }
            super.writeStartArray(g);
        }

        @Override
        public DefaultPrettyPrinter createInstance() {
            return new DetailedPrinter(this);
        }
    }

}
