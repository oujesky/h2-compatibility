package cz.miou.h2.mariadb.json;

import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;

import java.sql.SQLException;

import static cz.miou.h2.test.MariaDbTestQueryUtil.verifyQuery;
import static net.javacrumbs.jsonunit.assertj.JsonAssertions.assertThatJson;
import static org.assertj.core.api.Assertions.assertThat;

class JsonSearchTest {

    @ParameterizedTest
    @CsvSource({
        "'[\"A\", [{\"B\": \"1\"}], {\"C\":\"AB\"}, {\"D\":\"BC\", \"E\": [5, 6, 7, {\"F\": 23}]}, [1, 2, 3, \"AA\"]]', 'one', 'NOPE', ",
        "'[\"A\", [{\"B\": \"1\"}], {\"C\":\"AB\"}, {\"D\":\"BC\", \"E\": [5, 6, 7, {\"F\": 23}]}, [1, 2, 3, \"AA\"]]', 'all', 'NOPE', ",

        "'[\"A\", [{\"B\": \"1\"}], {\"C\":\"AB\"}, {\"D\":\"BC\"}].', 'one', 'AB', '\"$[2].C\"'",
        "'[\"A\", [{\"B\": \"1\"}], {\"C\":\"AB\"}, {\"D\":\"BC\"}].', 'all', 'AB', '\"$[2].C\"'",

        "'[\"A\", [{\"B\": \"1\"}], {\"C\":\"AB\", \"D\": \"AB\"}].', 'one', 'AB', '\"$[2].C\"'",
        "'[\"A\", [{\"B\": \"1\"}], {\"C\":\"AB\", \"D\": \"AB\"}].', 'all', 'AB', '[\"$[2].C\", \"$[2].D\"]'",

        "'[\"A\", [{\"B\": {\"C\": {\"D\":\"AB\"}}}]]', 'all', 'AB', '\"$[1][0].B.C.D\"'",

        "'[\"A\", [{\"B\": \"1\"}], {\"C\":\"AB\"}, {\"D\":\"BC\"}]', 'one', '%B%', '\"$[2].C\"'",
        "'[\"A\", [{\"B\": \"1\"}], {\"C\":\"AB\"}, {\"D\":\"BC\"}]', 'all', '%B%', '[\"$[2].C\", \"$[3].D\"]'",

        "'[\"A\", [{\"B\": \"1\"}], {\"C\":\"AB\"}, {\"D\":\"AB\"}, \"AB\"]', 'one', 'AB', '\"$[2].C\"'",
        "'[\"A\", [{\"B\": \"1\"}], {\"C\":\"AB\"}, {\"D\":\"AB\"}, \"AB\"]', 'all', 'AB', '[\"$[2].C\", \"$[3].D\", \"$[4]\"]'",


        "'[\"abc\", [{\"k\": \"10\"}, \"def\"], {\"x\":\"abc\"}, {\"y\":\"bcd\"}]', 'one', 'abc', '\"$[0]\"'",
        "'[\"abc\", [{\"k\": \"10\"}, \"def\"], {\"x\":\"abc\"}, {\"y\":\"bcd\"}]', 'all', 'abc', '[\"$[0]\", \"$[2].x\"]'",
        "'[\"abc\", [{\"k\": \"10\"}, \"def\"], {\"x\":\"abc\"}, {\"y\":\"bcd\"}]', 'all', 'ghi', ",
        "'[\"abc\", [{\"k\": \"10\"}, \"def\"], {\"x\":\"abc\"}, {\"y\":\"bcd\"}]', 'all', '10', '\"$[1][0].k\"'",

        "'[\"abc\", [{\"k\": \"10\"}, \"def\"], {\"x\":\"abc\"}, {\"y\":\"bcd\"}]', 'all', '%a%', '[\"$[0]\", \"$[2].x\"]'",
        "'[\"abc\", [{\"k\": \"10\"}, \"def\"], {\"x\":\"abc\"}, {\"y\":\"bcd\"}]', 'all', '%b%', '[\"$[0]\", \"$[2].x\", \"$[3].y\"]'",

        "'[\"AA\", \"AB\", \"BA\", \"AAA\", \"ABA\", \"BAA\"]', 'all', 'A_', '[\"$[0]\", \"$[1]\"]'",
        "'[\"AA\", \"AB\", \"BA\", \"AAA\", \"ABA\", \"BAA\"]', 'all', 'A__', '[\"$[3]\", \"$[4]\"]'",
        "'[\"AA\", \"AB\", \"BA\", \"AAA\", \"ABA\", \"BAA\"]', 'all', 'A%', '[\"$[0]\", \"$[1]\", \"$[3]\", \"$[4]\"]'",
        "'[\"AA\", \"AB\", \"BA\", \"AAA\", \"ABA\", \"BAA\"]', 'all', '_A', '[\"$[0]\", \"$[2]\"]'",
        "'[\"AA\", \"AB\", \"BA\", \"AAA\", \"ABA\", \"BAA\"]', 'all', '__A', '[\"$[3]\", \"$[4]\", \"$[5]\"]'",
        "'[\"AA\", \"AB\", \"BA\", \"AAA\", \"ABA\", \"BAA\"]', 'all', 'A_A', '[\"$[3]\", \"$[4]\"]'",
        "'[\"AA\", \"AB\", \"BA\", \"AAA\", \"ABA\", \"BAA\"]', 'all', '%A', '[\"$[0]\", \"$[2]\", \"$[3]\", \"$[4]\", \"$[5]\"]'",
        "'[\"AA\", \"AB\", \"BA\", \"AAA\", \"ABA\", \"BAA\"]', 'all', '%A%', '[\"$[0]\", \"$[1]\", \"$[2]\", \"$[3]\", \"$[4]\", \"$[5]\"]'",

        "'[\"A%\", \"A_\", \"A%%\", \"AA\"]', 'all', 'A%', '[\"$[0]\", \"$[1]\", \"$[2]\", \"$[3]\"]'",
        "'[\"A%\", \"A_\", \"A%%\", \"AA\"]', 'all', 'A_', '[\"$[0]\", \"$[1]\", \"$[3]\"]'",
        "'[\"A%\", \"A_\", \"A%%\", \"AA\"]', 'all', 'A\\_', '\"$[1]\"'",
        "'[\"A%\", \"A_\", \"A%%\", \"AA\"]', 'all', 'A\\%', '\"$[0]\"'",
        "'[\"A%\", \"A_\", \"A%%\", \"AA\"]', 'all', 'A\\%_', '\"$[2]\"'",
        "'[\"A%\", \"A_\", \"A%%\", \"AA\"]', 'all', 'A\\%%', '[\"$[0]\", \"$[2]\"]'",
        "'[\"A%\", \"A_\", \"A%%\", \"AA\"]', 'all', 'A\\%\\%', '\"$[2]\"'",
    })
    void testJsonSearch(String json, String returnArg, String searchStr, String expected) throws SQLException {
        verifyQuery(
            "SELECT JSON_SEARCH(?, ?, ?)",
            st -> {
                st.setString(1, json);
                st.setString(2, returnArg);
                st.setString(3, searchStr);
            },
            rs -> assertThatJson(rs.getString(1)).isEqualTo(expected)
        );
    }


    @ParameterizedTest
    @CsvSource({
        "'[\"abc\", [{\"k\": \"10\"}, \"def\"], {\"x\":\"abc\"}, {\"y\":\"bcd\"}]', 'all', '10', , '$', '\"$[1][0].k\"'",
        "'[\"abc\", [{\"k\": \"10\"}, \"def\"], {\"x\":\"abc\"}, {\"y\":\"bcd\"}]', 'all', '10', , '$[*]', '\"$[1][0].k\"'",
//        "'[\"abc\", [{\"k\": \"10\"}, \"def\"], {\"x\":\"abc\"}, {\"y\":\"bcd\"}]', 'all', '10', , '$**.k', '\"$[1][0].k\"'",
        "'[\"abc\", [{\"k\": \"10\"}, \"def\"], {\"x\":\"abc\"}, {\"y\":\"bcd\"}]', 'all', '10', , '$[*][0].k', '\"$[1][0].k\"'",
        "'[\"abc\", [{\"k\": \"10\"}, \"def\"], {\"x\":\"abc\"}, {\"y\":\"bcd\"}]', 'all', '10', , '$[1]', '\"$[1][0].k\"'",
        "'[\"abc\", [{\"k\": \"10\"}, \"def\"], {\"x\":\"abc\"}, {\"y\":\"bcd\"}]', 'all', '10', , '$[1][0]', '\"$[1][0].k\"'",
        "'[\"abc\", [{\"k\": \"10\"}, \"def\"], {\"x\":\"abc\"}, {\"y\":\"bcd\"}]', 'all', 'abc', , '$[2]', '\"$[2].x\"'",

        "'[\"abc\", [{\"k\": \"10\"}, \"def\"], {\"x\":\"abc\"}, {\"y\":\"bcd\"}]', 'all', '%b%', , '$[0]', '\"$[0]\"'",
        "'[\"abc\", [{\"k\": \"10\"}, \"def\"], {\"x\":\"abc\"}, {\"y\":\"bcd\"}]', 'all', '%b%', , '$[2]', '\"$[2].x\"'",
        "'[\"abc\", [{\"k\": \"10\"}, \"def\"], {\"x\":\"abc\"}, {\"y\":\"bcd\"}]', 'all', '%b%', , '$[1]', ",
        "'[\"abc\", [{\"k\": \"10\"}, \"def\"], {\"x\":\"abc\"}, {\"y\":\"bcd\"}]', 'all', '%b%', '', '$[1]', ",
        "'[\"abc\", [{\"k\": \"10\"}, \"def\"], {\"x\":\"abc\"}, {\"y\":\"bcd\"}]', 'all', '%b%', '', '$[3]', \"$[3].y\"",

        "'[\"A%\", \"AA\"]', 'all', 'A%', '|', '$', '[\"$[0]\", \"$[1]\"]'",
        "'[\"A%\", \"AA\"]', 'all', 'A|%', '|', '$', '\"$[0]\"'",
        "'[\"A%\", \"AA\"]', 'all', 'A\\%', '|', '$', ",
    })
    void testJsonSearchFull(String json, String returnArg, String searchStr, String escapeChar, String paths, String expected) throws SQLException {verifyQuery(
            "SELECT JSON_SEARCH(?, ?, ?, ?, ?)",
            st -> {
                st.setString(1, json);
                st.setString(2, returnArg);
                st.setString(3, searchStr);
                st.setString(4, escapeChar);
                st.setString(5, paths);
            },
            rs -> assertThat(rs.getString(1)).isEqualTo(expected)
        );
    }


    
}