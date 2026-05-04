package cz.miou.h2.mariadb.spatial.function.geohash;

import static cz.miou.h2.mariadb.spatial.GeometryUtil.MAX_LATITUDE;
import static cz.miou.h2.mariadb.spatial.GeometryUtil.MAX_LONGITUDE;
import static cz.miou.h2.mariadb.spatial.GeometryUtil.MIN_LATITUDE;
import static cz.miou.h2.mariadb.spatial.GeometryUtil.MIN_LONGITUDE;

class GeoHashEncoder {

    private static final char[] BASE32 = "0123456789bcdefghjkmnpqrstuvwxyz".toCharArray();

    private GeoHashEncoder() {
    }

    static String encode(double longitude, double latitude, int length) {
        double tmpMaxLatitude = MAX_LATITUDE;
        double tmpMinLatitude = MIN_LATITUDE;
        double tmpMaxLongitude = MAX_LONGITUDE;
        double tmpMinLongitude = MIN_LONGITUDE;

        boolean switchBit = true;
        StringBuilder str = new StringBuilder(length);

        for (int i = 0; i < length; i++) {
            int baseSet = 0;

            for (int bitIndex = 0; bitIndex < 5; bitIndex++) {
                BitResult result;
                if (switchBit) {
                    result = setBit(tmpMaxLongitude, tmpMinLongitude, longitude, baseSet, bitIndex);
                    tmpMaxLongitude = result.maxValue;
                    tmpMinLongitude = result.minValue;
                } else {
                    result = setBit(tmpMaxLatitude, tmpMinLatitude, latitude, baseSet, bitIndex);
                    tmpMaxLatitude = result.maxValue;
                    tmpMinLatitude = result.minValue;
                }
                baseSet = result.baseSet;

                switchBit = !switchBit;
            }

            int charIndex = baseSet;
            assert charIndex <= 31;

            str.append(BASE32[charIndex]);

            if (latitude == ((tmpMaxLatitude + tmpMinLatitude) / 2.0) && longitude == ((tmpMaxLongitude + tmpMinLongitude) / 2.0)) {
                break;
            }
        }

        return str.toString();
    }

    private static BitResult setBit(double maxValue, double minValue, double targetValue, int baseSet, int bitIndex) {
        double midValue = (maxValue + minValue) / 2.0;

        if (targetValue >= midValue) {
            baseSet |= 1 << (4 - bitIndex);
            minValue = midValue;
        } else {
            baseSet &= ~(1 << (4 - bitIndex));
            maxValue = midValue;
        }

        return new BitResult(maxValue, minValue, baseSet);
    }

    private static final class BitResult {
        final double maxValue;
        final double minValue;
        final int baseSet;

        BitResult(double maxValue, double minValue, int baseSet) {
            this.maxValue = maxValue;
            this.minValue = minValue;
            this.baseSet = baseSet;
        }
    }
}
