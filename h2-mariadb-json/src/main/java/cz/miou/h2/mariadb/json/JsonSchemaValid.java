package cz.miou.h2.mariadb.json;

import com.networknt.schema.SchemaRegistry;
import com.networknt.schema.SpecificationVersion;
import cz.miou.h2.api.FunctionDefinition;
import org.h2.value.Value;

import static cz.miou.h2.mariadb.json.JsonUtil.jsonNodeFromValue;

/**
 * <a href="https://mariadb.com/kb/en/json_schema_valid/">JSON_SCHEMA_VALID</a>
 */
public class JsonSchemaValid implements FunctionDefinition {

    private static final SchemaRegistry SCHEMA_REGISTRY = SchemaRegistry.withDefaultDialect(SpecificationVersion.DRAFT_2020_12);

    @Override
    public String getName() {
        return "JSON_SCHEMA_VALID";
    }

    @Override
    public String getMethodName() {
        return "jsonSchemaValid";
    }

    @Override
    public boolean isDeterministic() {
        return true;
    }

    @SuppressWarnings("unused")
    public static Boolean jsonSchemaValid(Value schema, Value document) {
        if (schema.containsNull() || document.containsNull()) {
            return null;
        }

        var schemaNode = jsonNodeFromValue(schema);
        var documentNode = jsonNodeFromValue(document);
        if (schemaNode == null || documentNode == null) {
            return false;
        }

        try {
            return SCHEMA_REGISTRY
                .getSchema(schemaNode)
                .validate(documentNode)
                .isEmpty();
        } catch (RuntimeException e) {
            return false;
        }
    }
}
