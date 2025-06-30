//Membuat tabel-tabel saat pertama kali dijalankan (jika belum ada).
package com.passwordmanager.utils;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.Statement;

public class DatabaseInitializer {

    private static final String DB_URL = "jdbc:sqlite:storepassword.db";

    public static void initializeDatabase() {
        String sql = """
            CREATE TABLE IF NOT EXISTS userdata (
                id INTEGER PRIMARY KEY AUTOINCREMENT,
                username TEXT NOT NULL UNIQUE,
                password_hash TEXT NOT NULL,
                fullname TEXT NOT NULL
            );

            CREATE TABLE IF NOT EXISTS folder (
                id INTEGER PRIMARY KEY AUTOINCREMENT,
                name TEXT NOT NULL,
                user_id INTEGER NOT NULL,
                FOREIGN KEY (user_id) REFERENCES userdata(id)
            );

            CREATE TABLE IF NOT EXISTS passwords (
                id INTEGER PRIMARY KEY AUTOINCREMENT,
                name TEXT NOT NULL,
                username TEXT NOT NULL,
                encrypted_password TEXT NOT NULL,
                hashkey TEXT NOT NULL,
                score INTEGER,
                category TEXT,
                user_id INTEGER NOT NULL,
                folder_id INTEGER NOT NULL,
                website TEXT,
                notes TEXT,
                encryption_key TEXT,
                FOREIGN KEY (user_id) REFERENCES userdata(id),
                FOREIGN KEY (folder_id) REFERENCES folder(id)
            );

            CREATE TABLE IF NOT EXISTS additional (
                id INTEGER PRIMARY KEY AUTOINCREMENT,
                entry_key TEXT,
                entry_value TEXT,
                is_password INTEGER,
                password_id INTEGER,
                FOREIGN KEY (password_id) REFERENCES passwords(id)
            );
        """;

        try (Connection conn = DriverManager.getConnection(DB_URL);
             Statement stmt = conn.createStatement()) {
            stmt.executeUpdate(sql);
            System.out.println("✅ Database initialized successfully.");
        } catch (Exception e) {
            System.err.println("❌ Failed to initialize database:");
            e.printStackTrace();
        }
    }
}
