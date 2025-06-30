//Menangani entri password
package com.passwordmanager.daos;

import com.passwordmanager.models.PasswordEntry;
import com.passwordmanager.utils.Database;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.List;

public class PasswordDAO {
    //Menyimpan password ke dataabase
    public static boolean insert(PasswordEntry entry) {
        String sql = "INSERT INTO passwords (user_id, folder_id, name, username, encrypted_password, category, website, notes, hashkey, score, encryption_key) " +
                        "VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?)";

        try (Connection conn = Database.getConnection();
                PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setInt(1, entry.getUserId());
            stmt.setInt(2, entry.getFolderId());
            stmt.setString(3, entry.getName());
            stmt.setString(4, entry.getUsername());
            stmt.setString(5, entry.getEncryptedPassword());
            stmt.setString(6, entry.getCategory());
            stmt.setString(7, entry.getWebsite());
            stmt.setString(8, entry.getNotes());
            stmt.setString(9, entry.getHashkey());
            stmt.setInt(10, entry.getScore());
            stmt.setString(11, entry.getEncryptionKey());

            return stmt.executeUpdate() > 0;
        } catch (Exception e) {
            System.err.println("Gagal menyimpan password:");
            e.printStackTrace();
            return false;
        }
    }
    //ambil ID password terakhir yang baru dimasukkan
    public static int getLastInsertedId(int userId, String name) {
        String sql = "SELECT id FROM passwords WHERE user_id = ? AND name = ? ORDER BY id DESC LIMIT 1";
        try (Connection conn = Database.getConnection();
                PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setInt(1, userId);
            stmt.setString(2, name);

            try (ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) {
                    return rs.getInt("id");
                }
            }
        } catch (Exception e) {
            System.err.println("Gagal mengambil ID terakhir:");
            e.printStackTrace();
        }
        return -1;
    }
    //ambil semua password berdasarkan nama folder
    public static List<PasswordEntry> getByFolderName(int userId, String folderName) {
        List<PasswordEntry> list = new ArrayList<>();
        String sql = "SELECT p.* FROM passwords p " +
                        "JOIN folder f ON p.folder_id = f.id " + "WHERE p.user_id = ? AND f.name = ?";

        try (Connection conn = Database.getConnection();
                PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setInt(1, userId);
            stmt.setString(2, folderName);

            try (ResultSet rs = stmt.executeQuery()) {
                while (rs.next()) {
                    PasswordEntry entry = new PasswordEntry();
                    entry.setId(rs.getInt("id"));
                    entry.setUserId(rs.getInt("user_id"));
                    entry.setFolderId(rs.getInt("folder_id"));
                    entry.setName(rs.getString("name"));
                    entry.setUsername(rs.getString("username"));
                    entry.setEncryptedPassword(rs.getString("encrypted_password"));
                    entry.setCategory(rs.getString("category"));
                    entry.setWebsite(rs.getString("website"));
                    entry.setNotes(rs.getString("notes"));
                    entry.setHashkey(rs.getString("hashkey"));
                    entry.setScore(rs.getInt("score"));
                    entry.setEncryptionKey(rs.getString("encryption_key"));
                    list.add(entry);
                }
            }
        } catch (Exception e) {
            System.err.println("Gagal mengambil password berdasarkan folder:");
            e.printStackTrace();
        }

        return list;
    }

    public static boolean update(PasswordEntry entry) {
        String sql = "UPDATE passwords SET name = ?, username = ?, encrypted_password = ?, category = ?, folder_id = ?, website = ?, notes = ?, hashkey = ?, score = ?, encryption_key = ? WHERE id = ?";

        try (Connection conn = Database.getConnection();
                PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setString(1, entry.getName());
            stmt.setString(2, entry.getUsername());
            stmt.setString(3, entry.getEncryptedPassword());
            stmt.setString(4, entry.getCategory());
            stmt.setInt(5, entry.getFolderId());
            stmt.setString(6, entry.getWebsite());
            stmt.setString(7, entry.getNotes());
            stmt.setString(8, entry.getHashkey());
            stmt.setInt(9, entry.getScore());
            stmt.setString(10, entry.getEncryptionKey());
            stmt.setInt(11, entry.getId());

            return stmt.executeUpdate() > 0;
        } catch (Exception e) {
            System.err.println("Gagal memperbarui password:");
            e.printStackTrace();
            return false;
        }
    }

    public static boolean delete(int id) {
        String sql = "DELETE FROM passwords WHERE id = ?";
        try (Connection conn = Database.getConnection();
                PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setInt(1, id);
            return stmt.executeUpdate() > 0;
        } catch (Exception e) {
            System.err.println("Gagal menghapus password:");
            e.printStackTrace();
            return false;
        }
    }
    //ambil 1 password tertentu dari user dan folder
    public static PasswordEntry getByName(int userId, String folderName, String passwordName) {
    String sql = "SELECT p.* FROM passwordstore p " +
                    "JOIN folder f ON p.folder_id = f.id " +
                    "WHERE p.user_id = ? AND f.name = ? AND p.name = ?";
        try (Connection conn = Database.getConnection();
            PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setInt(1, userId);
            stmt.setString(2, folderName);
            stmt.setString(3, passwordName);
            ResultSet rs = stmt.executeQuery();
            if (rs.next()) {
                PasswordEntry entry = new PasswordEntry();
                entry.setId(rs.getInt("id"));
                entry.setUserId(rs.getInt("user_id"));
                entry.setFolderId(rs.getInt("folder_id"));
                entry.setName(rs.getString("name"));
                entry.setUsername(rs.getString("username"));
                entry.setEncryptedPassword(rs.getString("encrypted_password"));
                entry.setCategory(rs.getString("category"));
                entry.setWebsite(rs.getString("website"));
                entry.setNotes(rs.getString("notes"));
                entry.setHashkey(rs.getString("hashkey"));
                entry.setScore(rs.getInt("score"));
                entry.setEncryptionKey(rs.getString("encryption_key"));
                return entry;
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }
}
