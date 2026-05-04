package cz.miou.h2.mariadb.spatial;

import cz.miou.h2.api.FunctionAlias;

/**
 * MariaDB function aliases to ST_* prefixed functions present in H2GIS
 */
final class H2GisFunctionAliases {

    private H2GisFunctionAliases() {}

    private abstract static class H2GisFunctionAlias implements FunctionAlias {

        private final String name;
        private final String className;
        private final String methodName;

        H2GisFunctionAlias(String name, String className, String methodName) {
            this.name = name;
            this.className = className;
            this.methodName = methodName;
        }

        @Override
        public String getName() {
            return name;
        }

        @Override
        public String getClassName() {
            return className;
        }

        @Override
        public String getMethodName() {
            return methodName;
        }

        @Override
        public boolean isDeterministic() {
            return true;
        }
    }

    public static final class Buffer extends H2GisFunctionAlias {
        public Buffer() {
            super("BUFFER", "org.h2gis.functions.spatial.buffer.ST_Buffer", "buffer");
        }
    }

    public static final class ConvexHull extends H2GisFunctionAlias {
        public ConvexHull() {
            super("CONVEXHULL", "org.h2gis.functions.spatial.operators.ST_ConvexHull", "convexHull");
        }
    }

    public static final class PointOnSurface extends H2GisFunctionAlias {
        public PointOnSurface() {
            super("PointOnSurface", "org.h2gis.functions.spatial.properties.ST_PointOnSurface", "getInteriorPoint");
        }
    }

    public static final class Point extends H2GisFunctionAlias {
        public Point() {
            super("POINT", "org.h2gis.functions.spatial.create.ST_Point", "createPoint");
        }
    }

    public static final class Boundary extends H2GisFunctionAlias {
        public Boundary() {
            super("BOUNDARY", "org.h2gis.functions.spatial.properties.ST_Boundary", "getBoundary");
        }
    }

    public static final class Dimension extends H2GisFunctionAlias {
        public Dimension() {
            super("DIMENSION", "org.h2gis.functions.spatial.properties.ST_Dimension", "getDimension");
        }
    }

    public static final class GeometryN extends H2GisFunctionAlias {
        public GeometryN() {
            super("GeometryN", "org.h2gis.functions.spatial.properties.ST_GeometryN", "getGeometryN");
        }
    }

    public static final class GeometryType extends H2GisFunctionAlias {
        public GeometryType() {
            super("GeometryType", "org.h2gis.functions.spatial.properties.ST_GeometryType", "getGeometryType");
        }
    }

    public static final class IsClosed extends H2GisFunctionAlias {
        public IsClosed() {
            super("IsClosed", "org.h2gis.functions.spatial.properties.ST_IsClosed", "isClosed");
        }
    }

    public static final class IsEmpty extends H2GisFunctionAlias {
        public IsEmpty() {
            super("IsEmpty", "org.h2gis.functions.spatial.properties.ST_IsEmpty", "isEmpty");
        }
    }

    public static final class IsRing extends H2GisFunctionAlias {
        public IsRing() {
            super("IsRing", "org.h2gis.functions.spatial.properties.ST_IsRing", "isRing");
        }
    }

    public static final class IsSimple extends H2GisFunctionAlias {
        public IsSimple() {
            super("IsSimple", "org.h2gis.functions.spatial.properties.ST_IsSimple", "isSimple");
        }
    }

    public static final class NumGeometries extends H2GisFunctionAlias {
        public NumGeometries() {
            super("NumGeometries", "org.h2gis.functions.spatial.properties.ST_NumGeometries", "getNumGeometries");
        }
    }

    public static final class Srid extends H2GisFunctionAlias {
        public Srid() {
            super("SRID", "org.h2gis.functions.spatial.properties.ST_SRID", "getSRID");
        }
    }

    public static final class Contains extends H2GisFunctionAlias {
        public Contains() {
            super("CONTAINS", "org.h2gis.functions.spatial.predicates.ST_Contains", "isContains");
        }
    }

    public static final class Crosses extends H2GisFunctionAlias {
        public Crosses() {
            super("CROSSES", "org.h2gis.functions.spatial.predicates.ST_Crosses", "geomCrosses");
        }
    }

    public static final class Disjoint extends H2GisFunctionAlias {
        public Disjoint() {
            super("DISJOINT", "org.h2gis.functions.spatial.predicates.ST_Disjoint", "geomDisjoint");
        }
    }

    public static final class Equals extends H2GisFunctionAlias {
        public Equals() {
            super("EQUALS", "org.h2gis.functions.spatial.predicates.ST_Equals", "geomEquals");
        }
    }

    public static final class Intersects extends H2GisFunctionAlias {
        public Intersects() {
            super("INTERSECTS", "org.h2gis.functions.spatial.predicates.ST_Intersects", "isIntersects");
        }
    }

    public static final class Overlaps extends H2GisFunctionAlias {
        public Overlaps() {
            super("OVERLAPS", "org.h2gis.functions.spatial.predicates.ST_Overlaps", "isOverlaps");
        }
    }

    public static final class Touches extends H2GisFunctionAlias {
        public Touches() {
            super("TOUCHES", "org.h2gis.functions.spatial.predicates.ST_Touches", "geomTouches");
        }
    }

    public static final class Within extends H2GisFunctionAlias {
        public Within() {
            super("WITHIN", "org.h2gis.functions.spatial.predicates.ST_Within", "isWithin");
        }
    }

    public static final class EndPoint extends H2GisFunctionAlias {
        public EndPoint() {
            super("ENDPOINT", "org.h2gis.functions.spatial.properties.ST_EndPoint", "getEndPoint");
        }
    }

    /**
     * <a href="https://www.h2database.com/html/functions-aggregate.html#envelope">ENVELOPE</a> is already present in H2 as a function aggregate
     */
    public static final class Envelope extends H2GisFunctionAlias {
        public Envelope() {
            super("Envelope", "org.h2gis.functions.spatial.properties.ST_Envelope", "getEnvelope");
        }
    }

    public static final class NumPoints extends H2GisFunctionAlias {
        public NumPoints() {
            super("NumPoints", "org.h2gis.functions.spatial.properties.ST_NumPoints", "getNumPoints");
        }
    }

    public static final class PointN extends H2GisFunctionAlias {
        public PointN() {
            super("PointN", "org.h2gis.functions.spatial.properties.ST_PointN", "getPointN");
        }
    }

    public static final class StartPoint extends H2GisFunctionAlias {
        public StartPoint() {
            super("STARTPOINT", "org.h2gis.functions.spatial.properties.ST_StartPoint", "getStartPoint");
        }
    }

    public static final class X extends H2GisFunctionAlias {
        public X() {
            super("X", "org.h2gis.functions.spatial.properties.ST_X", "getX");
        }
    }

    public static final class Y extends H2GisFunctionAlias {
        public Y() {
            super("Y", "org.h2gis.functions.spatial.properties.ST_Y", "getY");
        }
    }

    public static final class Area extends H2GisFunctionAlias {
        public Area() {
            super("AREA", "org.h2gis.functions.spatial.properties.ST_Area", "getArea");
        }
    }

    public static final class Centroid extends H2GisFunctionAlias {
        public Centroid() {
            super("CENTROID", "org.h2gis.functions.spatial.properties.ST_Centroid", "getCentroid");
        }
    }

    public static final class ExteriorRing extends H2GisFunctionAlias {
        public ExteriorRing() {
            super("EXTERIORRING", "org.h2gis.functions.spatial.properties.ST_ExteriorRing", "getExteriorRing");
        }
    }

    public static final class InteriorRingN extends H2GisFunctionAlias {
        public InteriorRingN() {
            super("INTERIORRINGN", "org.h2gis.functions.spatial.properties.ST_InteriorRingN", "getInteriorRing");
        }
    }

    public static final class NumInteriorRings extends H2GisFunctionAlias {
        public NumInteriorRings() {
            super("NumInteriorRings", "org.h2gis.functions.spatial.properties.ST_NumInteriorRings", "getHoles");
        }
    }

    public static final class GLength extends H2GisFunctionAlias {
        public GLength() {
            super("GLENGTH", "org.h2gis.functions.spatial.properties.ST_Length", "getLength");
        }
    }

    public static final class AsText extends H2GisFunctionAlias {
        public AsText() {
            super("AsText", "org.h2gis.functions.spatial.convert.ST_AsText", "asWKT");
        }
    }

    public static final class AsWkt extends H2GisFunctionAlias {
        public AsWkt() {
            super("AsWKT", "org.h2gis.functions.spatial.convert.ST_AsWKT", "asWKT");
        }
    }

    public static final class GeomCollFromText extends H2GisFunctionAlias {
        public GeomCollFromText() {
            super("GeomCollFromText", "org.h2gis.functions.spatial.convert.ST_GeomFromText", "toGeometry");
        }
    }

    public static final class GeomFromText extends H2GisFunctionAlias {
        public GeomFromText() {
            super("GeomFromText", "org.h2gis.functions.spatial.convert.ST_GeomFromText", "toGeometry");
        }
    }

    public static final class GeometryCollectionFromText extends H2GisFunctionAlias {
        public GeometryCollectionFromText() {
            super("GeometryCollectionFromText", "org.h2gis.functions.spatial.convert.ST_GeomFromText", "toGeometry");
        }
    }

    public static final class GeometryFromText extends H2GisFunctionAlias {
        public GeometryFromText() {
            super("GeometryFromText", "org.h2gis.functions.spatial.convert.ST_GeomFromText", "toGeometry");
        }
    }

    public static final class LineFromText extends H2GisFunctionAlias {
        public LineFromText() {
            super("LineFromText", "org.h2gis.functions.spatial.convert.ST_LineFromText", "toGeometry");
        }
    }

    public static final class LineStringFromText extends H2GisFunctionAlias {
        public LineStringFromText() {
            super("LineStringFromText", "org.h2gis.functions.spatial.convert.ST_LineFromText", "toGeometry");
        }
    }

    public static final class MLineFromText extends H2GisFunctionAlias {
        public MLineFromText() {
            super("MLineFromText", "org.h2gis.functions.spatial.convert.ST_MLineFromText", "toGeometry");
        }
    }

    public static final class MPointFromText extends H2GisFunctionAlias {
        public MPointFromText() {
            super("MPointFromText", "org.h2gis.functions.spatial.convert.ST_MPointFromText", "toGeometry");
        }
    }

    public static final class MPolyFromText extends H2GisFunctionAlias {
        public MPolyFromText() {
            super("MPolyFromText", "org.h2gis.functions.spatial.convert.ST_MPolyFromText", "toGeometry");
        }
    }

    public static final class MultiLineStringFromText extends H2GisFunctionAlias {
        public MultiLineStringFromText() {
            super("MultiLineStringFromText", "org.h2gis.functions.spatial.convert.ST_MLineFromText", "toGeometry");
        }
    }

    public static final class MultiPointFromText extends H2GisFunctionAlias {
        public MultiPointFromText() {
            super("MultiPointFromText", "org.h2gis.functions.spatial.convert.ST_MPointFromText", "toGeometry");
        }
    }

    public static final class MultiPolygonFromText extends H2GisFunctionAlias {
        public MultiPolygonFromText() {
            super("MultiPolygonFromText", "org.h2gis.functions.spatial.convert.ST_MPolyFromText", "toGeometry");
        }
    }

    public static final class PointFromText extends H2GisFunctionAlias {
        public PointFromText() {
            super("PointFromText", "org.h2gis.functions.spatial.convert.ST_PointFromText", "toGeometry");
        }
    }

    public static final class PolyFromText extends H2GisFunctionAlias {
        public PolyFromText() {
            super("PolyFromText", "org.h2gis.functions.spatial.convert.ST_PolyFromText", "toGeometry");
        }
    }

    public static final class PolygonFromText extends H2GisFunctionAlias {
        public PolygonFromText() {
            super("PolygonFromText", "org.h2gis.functions.spatial.convert.ST_PolyFromText", "toGeometry");
        }
    }

    public static final class AsBinary extends H2GisFunctionAlias {
        public AsBinary() {
            super("AsBinary", "org.h2gis.functions.spatial.convert.ST_AsBinary", "toBytes");
        }
    }

    public static final class AsWkb extends H2GisFunctionAlias {
        public AsWkb() {
            super("AsWKB", "org.h2gis.functions.spatial.convert.ST_AsBinary", "toBytes");
        }
    }

    public static final class StAsWkb extends H2GisFunctionAlias {
        public StAsWkb() {
            super("ST_AsWKB", "org.h2gis.functions.spatial.convert.ST_AsBinary", "toBytes");
        }
    }

    public static final class StGeometryFromText extends H2GisFunctionAlias {
        public StGeometryFromText() {
            super("ST_GeometryFromText", "org.h2gis.functions.spatial.convert.ST_GeomFromText", "toGeometry");
        }
    }

    public static final class StGeomCollFromText extends H2GisFunctionAlias {
        public StGeomCollFromText() {
            super("ST_GeomCollFromText", "org.h2gis.functions.spatial.convert.ST_GeomFromText", "toGeometry");
        }
    }

    public static final class StGeometryCollectionFromText extends H2GisFunctionAlias {
        public StGeometryCollectionFromText() {
            super("ST_GeometryCollectionFromText", "org.h2gis.functions.spatial.convert.ST_GeomFromText", "toGeometry");
        }
    }

    public static final class StLineStringFromText extends H2GisFunctionAlias {
        public StLineStringFromText() {
            super("ST_LineStringFromText", "org.h2gis.functions.spatial.convert.ST_LineFromText", "toGeometry");
        }
    }

    public static final class StMultiLineStringFromText extends H2GisFunctionAlias {
        public StMultiLineStringFromText() {
            super("ST_MultiLineStringFromText", "org.h2gis.functions.spatial.convert.ST_MLineFromText", "toGeometry");
        }
    }

    public static final class StMultiPointFromText extends H2GisFunctionAlias {
        public StMultiPointFromText() {
            super("ST_MultiPointFromText", "org.h2gis.functions.spatial.convert.ST_MPointFromText", "toGeometry");
        }
    }

    public static final class StMultiPolygonFromText extends H2GisFunctionAlias {
        public StMultiPolygonFromText() {
            super("ST_MultiPolygonFromText", "org.h2gis.functions.spatial.convert.ST_MPolyFromText", "toGeometry");
        }
    }

    public static final class StPolygonFromText extends H2GisFunctionAlias {
        public StPolygonFromText() {
            super("ST_PolygonFromText", "org.h2gis.functions.spatial.convert.ST_PolyFromText", "toGeometry");
        }
    }

    public static final class GeomCollFromWkb extends H2GisFunctionAlias {
        public GeomCollFromWkb() {
            super("GeomCollFromWKB", "org.h2gis.functions.spatial.convert.ST_GeomFromWKB", "toGeometry");
        }
    }

    public static final class GeomFromWkb extends H2GisFunctionAlias {
        public GeomFromWkb() {
            super("GeomFromWKB", "org.h2gis.functions.spatial.convert.ST_GeomFromWKB", "toGeometry");
        }
    }

    public static final class GeometryCollectionFromWkb extends H2GisFunctionAlias {
        public GeometryCollectionFromWkb() {
            super("GeometryCollectionFromWKB", "org.h2gis.functions.spatial.convert.ST_GeomFromWKB", "toGeometry");
        }
    }

    public static final class GeometryFromWkb extends H2GisFunctionAlias {
        public GeometryFromWkb() {
            super("GeometryFromWKB", "org.h2gis.functions.spatial.convert.ST_GeomFromWKB", "toGeometry");
        }
    }

    public static final class LineFromWkb extends H2GisFunctionAlias {
        public LineFromWkb() {
            super("LineFromWKB", "org.h2gis.functions.spatial.convert.ST_LineFromWKB", "toLineString");
        }
    }

    public static final class LineStringFromWkb extends H2GisFunctionAlias {
        public LineStringFromWkb() {
            super("LineStringFromWKB", "org.h2gis.functions.spatial.convert.ST_LineFromWKB", "toLineString");
        }
    }

    public static final class MLineFromWkb extends H2GisFunctionAlias {
        public MLineFromWkb() {
            super("MLineFromWKB", "org.h2gis.functions.spatial.convert.ST_GeomFromWKB", "toGeometry");
        }
    }

    public static final class MPointFromWkb extends H2GisFunctionAlias {
        public MPointFromWkb() {
            super("MPointFromWKB", "org.h2gis.functions.spatial.convert.ST_GeomFromWKB", "toGeometry");
        }
    }

    public static final class MPolyFromWkb extends H2GisFunctionAlias {
        public MPolyFromWkb() {
            super("MPolyFromWKB", "org.h2gis.functions.spatial.convert.ST_GeomFromWKB", "toGeometry");
        }
    }

    public static final class MultiLineStringFromWkb extends H2GisFunctionAlias {
        public MultiLineStringFromWkb() {
            super("MultiLineStringFromWKB", "org.h2gis.functions.spatial.convert.ST_GeomFromWKB", "toGeometry");
        }
    }

    public static final class MultiPointFromWkb extends H2GisFunctionAlias {
        public MultiPointFromWkb() {
            super("MultiPointFromWKB", "org.h2gis.functions.spatial.convert.ST_GeomFromWKB", "toGeometry");
        }
    }

    public static final class MultiPolygonFromWkb extends H2GisFunctionAlias {
        public MultiPolygonFromWkb() {
            super("MultiPolygonFromWKB", "org.h2gis.functions.spatial.convert.ST_GeomFromWKB", "toGeometry");
        }
    }

    public static final class PointFromWkb extends H2GisFunctionAlias {
        public PointFromWkb() {
            super("PointFromWKB", "org.h2gis.functions.spatial.convert.ST_PointFromWKB", "toPoint");
        }
    }

    public static final class PolyFromWkb extends H2GisFunctionAlias {
        public PolyFromWkb() {
            super("PolyFromWKB", "org.h2gis.functions.spatial.convert.ST_PolyFromWKB", "toPolygon");
        }
    }

    public static final class PolygonFromWkb extends H2GisFunctionAlias {
        public PolygonFromWkb() {
            super("PolygonFromWKB", "org.h2gis.functions.spatial.convert.ST_PolyFromWKB", "toPolygon");
        }
    }

    public static final class StGeometryFromWkb extends H2GisFunctionAlias {
        public StGeometryFromWkb() {
            super("ST_GeometryFromWKB", "org.h2gis.functions.spatial.convert.ST_GeomFromWKB", "toGeometry");
        }
    }

    public static final class StGeomCollFromWkb extends H2GisFunctionAlias {
        public StGeomCollFromWkb() {
            super("ST_GeomCollFromWKB", "org.h2gis.functions.spatial.convert.ST_GeomFromWKB", "toGeometry");
        }
    }

    public static final class StGeometryCollectionFromWkb extends H2GisFunctionAlias {
        public StGeometryCollectionFromWkb() {
            super("ST_GeometryCollectionFromWKB", "org.h2gis.functions.spatial.convert.ST_GeomFromWKB", "toGeometry");
        }
    }

    public static final class StLineStringFromWkb extends H2GisFunctionAlias {
        public StLineStringFromWkb() {
            super("ST_LineStringFromWKB", "org.h2gis.functions.spatial.convert.ST_LineFromWKB", "toLineString");
        }
    }

    public static final class StMLineFromWkb extends H2GisFunctionAlias {
        public StMLineFromWkb() {
            super("ST_MLineFromWKB", "org.h2gis.functions.spatial.convert.ST_GeomFromWKB", "toGeometry");
        }
    }

    public static final class StMPointFromWkb extends H2GisFunctionAlias {
        public StMPointFromWkb() {
            super("ST_MPointFromWKB", "org.h2gis.functions.spatial.convert.ST_GeomFromWKB", "toGeometry");
        }
    }

    public static final class StMPolyFromWkb extends H2GisFunctionAlias {
        public StMPolyFromWkb() {
            super("ST_MPolyFromWKB", "org.h2gis.functions.spatial.convert.ST_GeomFromWKB", "toGeometry");
        }
    }

    public static final class StMultiLineStringFromWkb extends H2GisFunctionAlias {
        public StMultiLineStringFromWkb() {
            super("ST_MultiLineStringFromWKB", "org.h2gis.functions.spatial.convert.ST_GeomFromWKB", "toGeometry");
        }
    }

    public static final class StMultiPointFromWkb extends H2GisFunctionAlias {
        public StMultiPointFromWkb() {
            super("ST_MultiPointFromWKB", "org.h2gis.functions.spatial.convert.ST_GeomFromWKB", "toGeometry");
        }
    }

    public static final class StMultiPolygonFromWkb extends H2GisFunctionAlias {
        public StMultiPolygonFromWkb() {
            super("ST_MultiPolygonFromWKB", "org.h2gis.functions.spatial.convert.ST_GeomFromWKB", "toGeometry");
        }
    }

    public static final class StPolygonFromWkb extends H2GisFunctionAlias {
        public StPolygonFromWkb() {
            super("ST_PolygonFromWKB", "org.h2gis.functions.spatial.convert.ST_PolyFromWKB", "toPolygon");
        }
    }

}
