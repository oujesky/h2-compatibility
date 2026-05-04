package cz.miou.h2.mariadb.spatial.function.geohash;

import org.locationtech.jts.geom.Coordinate;
import org.locationtech.jts.geom.GeometryFactory;
import org.locationtech.jts.geom.Point;
import org.locationtech.jts.geom.PrecisionModel;

import java.util.Arrays;

import static cz.miou.h2.mariadb.spatial.GeometryUtil.MAX_LATITUDE;
import static cz.miou.h2.mariadb.spatial.GeometryUtil.MAX_LONGITUDE;
import static cz.miou.h2.mariadb.spatial.GeometryUtil.MIN_LATITUDE;
import static cz.miou.h2.mariadb.spatial.GeometryUtil.MIN_LONGITUDE;

class GeoHashDecoder {

    private static final int INVALID_CHARACTER = 255;
    private static final int[] GEOHASH_ALPHABET = new int[256];

    static {
        Arrays.fill(GEOHASH_ALPHABET, INVALID_CHARACTER);
        var alphabet = "0123456789bcdefghjkmnpqrstuvwxyz";
        for (int i = 0; i < alphabet.length(); i++) {
            var character = alphabet.charAt(i);
            GEOHASH_ALPHABET[character] = i;
            GEOHASH_ALPHABET[Character.toUpperCase(character)] = i;
        }
    }

    private GeoHashDecoder() {}

    static Point decode(String geohash, int srid, String operationName) {
        if (geohash == null) {
            return null;
        }

        if (geohash.isEmpty()) {
            throw invalidGeohash(geohash, operationName);
        }

        var latitude = new GeoHashCoordinate(MIN_LATITUDE, MAX_LATITUDE);
        var longitude = new GeoHashCoordinate(MIN_LONGITUDE, MAX_LONGITUDE);
        var decodeLongitude = true;

        for (int i = 0; i < geohash.length(); i++) {
            var convertedCharacter = convertCharacter(geohash.charAt(i));
            if (convertedCharacter == INVALID_CHARACTER) {
                throw invalidGeohash(geohash, operationName);
            }

            for (int bitNumber = 4; bitNumber >= 0; bitNumber--) {
                var upperHalf = (convertedCharacter & (1 << bitNumber)) != 0;
                if (decodeLongitude) {
                    longitude.narrow(upperHalf);
                } else {
                    latitude.narrow(upperHalf);
                }
                decodeLongitude = !decodeLongitude;
            }

            if (latitude.error() <= 0.0 || longitude.error() <= 0.0) {
                break;
            }
        }

        return createPoint(srid, longitude, latitude);
    }

    private static Point createPoint(int srid, GeoHashCoordinate longitude, GeoHashCoordinate latitude) {
        var geometryFactory = new GeometryFactory(new PrecisionModel(), srid);
        return geometryFactory.createPoint(new Coordinate(longitude.roundedCenter(), latitude.roundedCenter()));
    }

    private static int convertCharacter(char character) {
        return character < GEOHASH_ALPHABET.length ? GEOHASH_ALPHABET[character] : INVALID_CHARACTER;
    }

    private static IllegalArgumentException invalidGeohash(String geohash, String operationName) {
        return new IllegalArgumentException(String.format("Incorrect geohash value: '%s' for function %s", geohash, operationName));
    }

    private static final class GeoHashCoordinate {
        private double center;
        private double error;

        private GeoHashCoordinate(double min, double max) {
            this.center = (max + min) / 2.0;
            this.error = (max - min) / 2.0;
        }

        private void narrow(boolean upperHalf) {
            error /= 2.0;
            center += upperHalf ? error : -error;
        }

        private double error() {
            return error;
        }

        private double roundedCenter() {
            var errorRange = error * 2.0;
            if (errorRange == 0.0) {
                return center;
            }

            var numberOfDecimals = 0;
            while (errorRange <= 0.1 && numberOfDecimals <= 15) {
                numberOfDecimals++;
                errorRange *= 10.0;
            }

            var lowerLimit = center - error;
            var upperLimit = center + error;
            double returnValue;
            do {
                returnValue = round(center, numberOfDecimals);
                numberOfDecimals++;
            } while ((lowerLimit > returnValue || returnValue > upperLimit) && numberOfDecimals <= 15);

            if (lowerLimit > returnValue || returnValue > upperLimit) {
                returnValue = center;
            }
            return returnValue + 0.0;
        }

        private double round(double value, int numberOfDecimals) {
            if (numberOfDecimals <= 0) {
                return Math.rint(value);
            }
            var scale = Math.pow(10, numberOfDecimals);
            return Math.rint(value * scale) / scale;
        }
    }

}
