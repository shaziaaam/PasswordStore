// untuk membuat atau mengedit folder.
package com.passwordmanager.views;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;

import com.passwordmanager.daos.FolderDAO;
import com.passwordmanager.models.Folder;
import com.passwordmanager.utils.Database;

import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.*;
import javafx.scene.layout.*;

public class FolderForm extends VBox {
    private final int userId;

    public FolderForm(int userId, Runnable onFolderAdded) {
        this.userId = userId;

        setSpacing(10);
        setPadding(new Insets(20));
        setAlignment(Pos.CENTER);

        Label lblTitle = new Label("Buat Folder Baru");
        lblTitle.setStyle("-fx-font-size: 16pt; -fx-font-weight: bold;");

        Label lblName = new Label("Nama Folder:");
        TextField tfName = new TextField();

        Label lblMessage = new Label();

        Button btnCreate = new Button("Buat Folder");
        btnCreate.setOnAction(e -> {
            String folderName = tfName.getText().trim();
            if (folderName.isEmpty()) {
                lblMessage.setText("Nama folder tidak boleh kosong.");
                return;
            }

            boolean success = FolderDAO.insert(userId, folderName);
            if (success) {
                lblMessage.setText("Folder berhasil dibuat.");
                onFolderAdded.run(); // refresh tree
            } else {
                lblMessage.setText("Gagal membuat folder.");
            }
        });

        getChildren().addAll(lblTitle, lblName, tfName, btnCreate, lblMessage);
    }

    public Folder getByName(String name, int userId) {
    String sql = "SELECT * FROM folder WHERE name = ? AND user_id = ?";
        try (Connection conn = Database.getConnection();
            PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setString(1, name);
            stmt.setInt(2, userId);
            ResultSet rs = stmt.executeQuery();
            if (rs.next()) {
                return new Folder(
                    rs.getInt("id"),
                    rs.getString("name"),
                    rs.getInt("user_id")
                );
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }
}
