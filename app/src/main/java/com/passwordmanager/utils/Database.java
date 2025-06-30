//Membuka koneksi ke SQLite.
package com.passwordmanager.utils;

import java.sql.Connection;
import java.sql.DriverManager;

public class Database {
    private static final String DB_URL = "jdbc:sqlite:storepassword.db";

    public static Connection getConnection() throws Exception {
        return DriverManager.getConnection(DB_URL);
    }
}
