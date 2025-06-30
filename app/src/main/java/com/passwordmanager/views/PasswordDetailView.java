//Tampilan read-only dari entri password.
package com.passwordmanager.views;

import com.passwordmanager.models.PasswordEntry;
import com.passwordmanager.models.User;
import javafx.geometry.Insets;
import javafx.scene.control.*;
import javafx.scene.layout.*;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;

public class PasswordDetailView extends VBox {
    private final PasswordEntry entry;
    private final User user;
    private final MainView mainView;

    public PasswordDetailView(User user, PasswordEntry entry, MainView mainView) {
        this.entry = entry;
        this.user = user;
        this.mainView = mainView;

        setSpacing(15);
        setPadding(new Insets(20));

        Label title = new Label("Detail Password");
        title.setFont(Font.font("System", FontWeight.BOLD, 20));

        GridPane grid = new GridPane();
        grid.setVgap(10);
        grid.setHgap(10);
        grid.setPadding(new Insets(10));

        addField(grid, "Nama Akun:", entry.getName(), 0);
        addField(grid, "Username:", entry.getUsername(), 1);
        addField(grid, "Kategori:", entry.getCategory(), 2);
        addField(grid, "Password (Terenkripsi):", entry.getEncryptedPassword(), 3);
        addField(grid, "Website:", entry.getWebsite(), 4);
        addField(grid, "Catatan:", entry.getNotes(), 5);

        Button editButton = new Button("Edit");
        editButton.setOnAction(e -> {
            EditPasswordForm editForm = new EditPasswordForm(user, entry, mainView);
            mainView.setRightContent(editForm);
        });

        Button backButton = new Button("Kembali");
        backButton.setOnAction(e -> {
            // Kembali ke folder view sebelumnya
            mainView.refreshFolderTree();
        });

        HBox buttonBox = new HBox(10, editButton, backButton);
        buttonBox.setPadding(new Insets(10));
        buttonBox.setSpacing(10);

        getChildren().addAll(title, grid, buttonBox);
    }

    private void addField(GridPane grid, String label, String value, int row) {
        Label lbl = new Label(label);
        TextField field = new TextField(value);
        field.setEditable(false);
        field.setStyle("-fx-background-color: #f0f0f0;");
        grid.add(lbl, 0, row);
        grid.add(field, 1, row);
    }
}
