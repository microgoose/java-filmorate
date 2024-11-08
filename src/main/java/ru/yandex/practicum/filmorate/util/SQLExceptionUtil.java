package ru.yandex.practicum.filmorate.util;

import java.sql.SQLException;

public class SQLExceptionUtil {
    public static boolean isConstraintViolation(SQLException e) {
        return e.getSQLState().startsWith("23");
    }
}
