//Menyimpan & menghapus informasi tambahan untuk password
package com.passwordmanager.daos;

import com.passwordmanager.models.AdditionalInfo;
import com.passwordmanager.utils.Database;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.List;

public class AdditionalDAO {

    public static List<AdditionalInfo> getByPasswordId(int passwordId) {
        List<AdditionalInfo> list = new ArrayList<>();
        String sql = "SELECT * FROM additional_info WHERE password_id = ?";
        try (Connection conn = Database.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setInt(1, passwordId);
            ResultSet rs = stmt.executeQuery();
            while (rs.next()) {
                AdditionalInfo info = new AdditionalInfo(
                    rs.getInt("id"),
                    rs.getString("entry_key"),
                    rs.getString("entry_value"),
                    false,  // As you said, no encryption needed here
                    rs.getInt("password_id")
                );
                list.add(info);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return list;
    }
    //hapus info tambahan berdasarkan password
    public static void deleteByPasswordId(int passwordId) {
        String sql = "DELETE FROM additional_info WHERE password_id = ?";
        try (Connection conn = Database.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setInt(1, passwordId);
            stmt.executeUpdate();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    //simpan banyak info tambahan
    public static void insertAll(int userId, int passwordId, List<AdditionalInfo> list) {
        String sql = "INSERT INTO additional_info (entry_key, entry_value, password_id) VALUES (?, ?, ?)";
        try (Connection conn = Database.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            for (AdditionalInfo info : list) {
                stmt.setString(1, info.getEntryKey());
                stmt.setString(2, info.getEntryValue());
                stmt.setInt(3, passwordId);
                stmt.addBatch();
            }
            stmt.executeBatch();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
