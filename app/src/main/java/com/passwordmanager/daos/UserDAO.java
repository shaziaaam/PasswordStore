//Autentikasi dan registrasi user
package com.passwordmanager.daos;

import com.passwordmanager.models.User;
import com.passwordmanager.utils.Database;
import com.passwordmanager.utils.HashUtils;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;

public class UserDAO {
    //mencocokkan username & password
    public static User login(String username, String password) {
        String hash = HashUtils.hashPassword(password);
        String sql = "SELECT * FROM userdata WHERE username = ? AND password_hash = ?";

        try (Connection conn = Database.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setString(1, username);
            stmt.setString(2, hash);
            ResultSet rs = stmt.executeQuery();

            if (rs.next()) {
                return new User(
                        rs.getInt("id"),
                        rs.getString("username"),
                        rs.getString("fullname")
                );
            }

        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    public static boolean isUsernameAvailable(String username) {
        String sql = "SELECT id FROM userdata WHERE username = ?";
        try (Connection conn = Database.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setString(1, username);
            ResultSet rs = stmt.executeQuery();
            return !rs.next(); // true jika tidak ditemukan
        } catch (Exception e) {
            e.printStackTrace();
        }
        return false;
    }
    //menyimpan user baru
    public static User register(String username, String password, String fullname) {
        if (!isUsernameAvailable(username)) return null;

        String hash = HashUtils.hashPassword(password);
        String sql = "INSERT INTO userdata (username, password_hash, fullname) VALUES (?, ?, ?)";

        try (Connection conn = Database.getConnection();
            PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setString(1, username);
            stmt.setString(2, hash);
            stmt.setString(3, fullname);
            stmt.executeUpdate();

            // Ambil ID terakhir yang berhasil di-insert
            ResultSet rs = conn.createStatement().executeQuery("SELECT last_insert_rowid() as id");
            if (rs.next()) {
                int id = rs.getInt("id");
                return new User(id, username, fullname);
            }

        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    public static boolean updateProfile(int userId, String username, String fullname) {
        String sql = "UPDATE userdata SET username = ?, fullname = ? WHERE id = ?";
        try (Connection conn = Database.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setString(1, username);
            stmt.setString(2, fullname);
            stmt.setInt(3, userId);
            stmt.executeUpdate();
            return true;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return false;
    }

}
