package cz.miou.h2.mariadb.string;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.io.TempDir;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.sql.SQLException;

import static cz.miou.h2.test.MariaDbTestQueryUtil.verifyQuery;
import static org.assertj.core.api.Assertions.assertThat;

class LoadFileTest {

    @Test
    void testLoadFile(@TempDir Path tempDir) throws IOException, SQLException {
        final Path tempFile = Files.createFile(tempDir.resolve("test.txt"));
        Files.writeString(tempFile, "TEST");

        verifyQuery(
            "SELECT LOAD_FILE(?)",
            st -> st.setString(1, tempFile.toString()),
            rs -> assertThat(rs.getString(1)).isEqualTo("TEST")
        );
    }

}