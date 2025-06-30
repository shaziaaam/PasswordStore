//Form untuk memperbarui profil user.
package com.passwordmanager.views;

import com.passwordmanager.daos.UserDAO;
import com.passwordmanager.models.User;
import javafx.geometry.Insets;
import javafx.scene.control.*;
import javafx.scene.layout.VBox;

public class EditProfile extends VBox {
    public EditProfile(User user) {
        this(user, () -> {}); // default: tidak melakukan apa-apa setelah sukses
    }

    public EditProfile(User user, Runnable onSaveSuccess) {
        setPadding(new Insets(20));
        setSpacing(10);

        Label lblTitle = new Label("Edit Profil");

        TextField txtUsername = new TextField(user.getUsername());
        txtUsername.setPromptText("Username");

        TextField txtFullname = new TextField(user.getFullname());
        txtFullname.setPromptText("Nama Lengkap");

        Button btnSave = new Button("Simpan");

        btnSave.setOnAction(e -> {
            String newUsername = txtUsername.getText().trim();
            String newFullname = txtFullname.getText().trim();

            if (newUsername.length() < 4 || newFullname.isEmpty()) {
                Alert alert = new Alert(Alert.AlertType.ERROR, "Username minimal 4 karakter dan nama tidak boleh kosong.");
                alert.show();
                return;
            }

            boolean updated = UserDAO.updateProfile(user.getId(), newUsername, newFullname);
            if (updated) {
                user.setUsername(newUsername);
                user.setFullname(newFullname);
                onSaveSuccess.run();
            } else {
                new Alert(Alert.AlertType.ERROR, "Gagal menyimpan profil.").show();
            }
        });

        getChildren().addAll(lblTitle, txtUsername, txtFullname, btnSave);
    }
}
