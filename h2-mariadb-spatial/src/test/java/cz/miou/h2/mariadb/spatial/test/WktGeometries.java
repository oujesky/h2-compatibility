package cz.miou.h2.mariadb.spatial.test;

import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.List;
import java.util.stream.Collectors;

public class WktGeometries {

    private final List<String> geometries;

    public WktGeometries(List<String> geometries) {
        this.geometries = geometries;
    }

    public String getPlaceholders() {
        return geometries.stream()
            .map(wkt -> "ST_GeomFromText(?)")
            .collect(Collectors.joining(", "));
    }

    public void bind(PreparedStatement statement) throws SQLException {
        for (var i = 0; i < geometries.size(); i++) {
            statement.setString(i + 1, geometries.get(i));
        }
    }

}
