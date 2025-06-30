//Menangani folder penyimpanan password
package com.passwordmanager.daos;

import com.passwordmanager.models.Folder;
import com.passwordmanager.utils.Database;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.List;

public class FolderDAO {
    // Menyimpan folder baru
    public static boolean insert(int userId, String folderName) {
        String sql = "INSERT INTO folder (name, user_id) VALUES (?, ?)";
        try (Connection conn = Database.getConnection();
                PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setString(1, folderName);
            stmt.setInt(2, userId);
            stmt.executeUpdate();
            return true;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return false;
    }
    //Ambil semua folder milik user
    public static List<Folder> getFoldersByUserId(int userId) {
        List<Folder> folders = new ArrayList<>();
        String sql = "SELECT * FROM folder WHERE user_id = ?";
        try (Connection conn = Database.getConnection();
                PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setInt(1, userId);
            ResultSet rs = stmt.executeQuery();
            while (rs.next()) {
                folders.add(new Folder(
                        rs.getInt("id"),
                        rs.getString("name"),
                        rs.getInt("user_id")
                ));
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return folders;
    }
    //Ambil id folder berdasarkan nama 
    public static Folder getFolderByName(int userId, String folderName) {
        String sql = "SELECT * FROM folder WHERE user_id = ? AND name = ?";
        try (Connection conn = Database.getConnection();
                PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setInt(1, userId);
            stmt.setString(2, folderName);

            ResultSet rs = stmt.executeQuery();
            if (rs.next()) {
                return new Folder(rs.getInt("id"), rs.getString("name"), rs.getInt("user_id"));
            }

        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    public static int getIdByName(String name, int userId) {
        String sql = "SELECT id FROM folder WHERE name = ? AND user_id = ?";
        try (Connection conn = Database.getConnection();
                PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setString(1, name);
            stmt.setInt(2, userId);
            ResultSet rs = stmt.executeQuery();
            if (rs.next()) return rs.getInt("id");
        } catch (Exception e) {
            e.printStackTrace();
        }
        return -1;
    }

    public static String getNameById(int id) {
        String sql = "SELECT name FROM folder WHERE id = ?";
        try (Connection conn = Database.getConnection();
                PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setInt(1, id);
            ResultSet rs = stmt.executeQuery();
            if (rs.next()) return rs.getString("name");
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    public static List<Folder> getAll(int userId) {
    List<Folder> folders = new ArrayList<>();
    String sql = "SELECT * FROM folder WHERE user_id = ?";
        try (Connection conn = Database.getConnection();
            PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setInt(1, userId);
            ResultSet rs = stmt.executeQuery();
            while (rs.next()) {
                folders.add(new Folder(
                    rs.getInt("id"),
                    rs.getString("name"),
                    rs.getInt("user_id")
                ));
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return folders;
    }
}
