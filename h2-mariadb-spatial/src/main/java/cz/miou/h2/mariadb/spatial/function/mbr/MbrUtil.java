package cz.miou.h2.mariadb.spatial.function.mbr;

import org.h2.value.Value;
import org.locationtech.jts.geom.Envelope;

import java.util.Optional;
import java.util.function.BiPredicate;

import static cz.miou.h2.mariadb.spatial.GeometryUtil.ensureGeometry;

class MbrUtil {

    private MbrUtil() {}

    public static Optional<Boolean> compareMbr(String operation, Value value1, Value value2, BiPredicate<Envelope, Envelope> compare) {
        if (value1.containsNull() || value2.containsNull()) {
            return Optional.empty();
        }

        var envelope1 = prepareEnvelope(operation, value1);
        var envelope2 = prepareEnvelope(operation, value2);

        return Optional.of(compare.test(envelope1, envelope2));
    }

    private static Envelope prepareEnvelope(String operation, Value value) {
        var geometry = ensureGeometry(operation, value);
        return geometry.getEnvelopeInternal();
    }
}
