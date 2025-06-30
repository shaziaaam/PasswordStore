//Form untuk mengedit password yang sudah ada.
package com.passwordmanager.views;

import com.passwordmanager.daos.AdditionalDAO;
import com.passwordmanager.daos.FolderDAO;
import com.passwordmanager.daos.PasswordDAO;
import com.passwordmanager.models.AdditionalInfo;
import com.passwordmanager.models.Folder;
import com.passwordmanager.models.PasswordEntry;
import com.passwordmanager.models.User;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.*;
import javafx.scene.control.cell.TextFieldTableCell;
import javafx.scene.layout.*;

public class EditPasswordForm extends VBox {
    private final PasswordEntry passwordEntry;
    private final User user;
    private final ObservableList<AdditionalInfo> additionalInfoList = FXCollections.observableArrayList();

    public EditPasswordForm(User user, PasswordEntry entry) {
        this.user = user;
        this.passwordEntry = entry;

        setSpacing(15);
        setPadding(new Insets(20));

        buildForm();
    }

    private void buildForm() {
        Label titleLabel = new Label("Edit Password");
        titleLabel.setStyle("-fx-font-size: 20px; -fx-font-weight: bold;");

        GridPane formGrid = new GridPane();
        formGrid.setHgap(10);
        formGrid.setVgap(10);
        formGrid.setPadding(new Insets(10));

        TextField nameField = new TextField(passwordEntry.getName());
        TextField usernameField = new TextField(passwordEntry.getUsername());
        PasswordField passwordField = new PasswordField();
        passwordField.setText(passwordEntry.getEncryptedPassword());

        ComboBox<String> categoryBox = new ComboBox<>();
        categoryBox.getItems().addAll("Whatsapp", "Instagram", "Facebook", "Email", "E-Wallet", "Lainnya");
        categoryBox.setValue(passwordEntry.getCategory());

        ComboBox<String> folderBox = new ComboBox<>();
        for (Folder folder : FolderDAO.getFoldersByUserId(user.getId())) {
            folderBox.getItems().add(folder.getName());
        }
        folderBox.setValue(FolderDAO.getNameById(passwordEntry.getFolderId()));

        formGrid.add(new Label("Nama Akun:"), 0, 0);
        formGrid.add(nameField, 1, 0);
        formGrid.add(new Label("Username / Email:"), 0, 1);
        formGrid.add(usernameField, 1, 1);
        formGrid.add(new Label("Password:"), 0, 2);
        formGrid.add(passwordField, 1, 2);
        formGrid.add(new Label("Kategori:"), 0, 3);
        formGrid.add(categoryBox, 1, 3);
        formGrid.add(new Label("Folder:"), 0, 4);
        formGrid.add(folderBox, 1, 4);

        Label additionalLabel = new Label("Informasi Tambahan:");
        TableView<AdditionalInfo> additionalTable = new TableView<>(additionalInfoList);
        additionalTable.setEditable(true);
        additionalTable.setColumnResizePolicy(TableView.CONSTRAINED_RESIZE_POLICY);

        TableColumn<AdditionalInfo, String> keyCol = new TableColumn<>("Key");
        keyCol.setCellValueFactory(new javafx.scene.control.cell.PropertyValueFactory<>("entryKey"));
        keyCol.setCellFactory(TextFieldTableCell.forTableColumn());
        keyCol.setOnEditCommit(e -> e.getRowValue().setEntryKey(e.getNewValue()));

        TableColumn<AdditionalInfo, String> valueCol = new TableColumn<>("Value");
        valueCol.setCellValueFactory(new javafx.scene.control.cell.PropertyValueFactory<>("entryValue"));
        valueCol.setCellFactory(TextFieldTableCell.forTableColumn());
        valueCol.setOnEditCommit(e -> e.getRowValue().setEntryValue(e.getNewValue()));

        additionalTable.getColumns().addAll(keyCol, valueCol);

        Button addRowBtn = new Button("Tambah Info");
        addRowBtn.setOnAction(e -> additionalInfoList.add(new AdditionalInfo(0, "", "", false, passwordEntry.getId())));

        Button deleteRowBtn = new Button("Hapus Info");
        deleteRowBtn.setOnAction(e -> {
            AdditionalInfo selected = additionalTable.getSelectionModel().getSelectedItem();
            if (selected != null) additionalInfoList.remove(selected);
        });

        HBox rowButtons = new HBox(10, addRowBtn, deleteRowBtn);
        rowButtons.setAlignment(Pos.CENTER_LEFT);

        // Load existing additional info
        additionalInfoList.addAll(AdditionalDAO.getByPasswordId(passwordEntry.getId()));

        Button saveBtn = new Button("Simpan");
        saveBtn.setOnAction(e -> {
            String name = nameField.getText().trim();
            String username = usernameField.getText().trim();
            String password = passwordField.getText();
            String category = categoryBox.getValue();
            String folderName = folderBox.getValue();

            if (name.isEmpty() || password.isEmpty() || category == null || folderName == null) {
                new Alert(Alert.AlertType.ERROR, "Semua field wajib diisi.").show();
                return;
            }

            int folderId = FolderDAO.getIdByName(folderName, user.getId());

            passwordEntry.setName(name);
            passwordEntry.setUsername(username);
            passwordEntry.setEncryptedPassword(password);
            passwordEntry.setCategory(category);
            passwordEntry.setFolderId(folderId);

            PasswordDAO.update(passwordEntry);

            // Update additional info
            for (AdditionalInfo info : additionalInfoList) {
                info.setPasswordId(passwordEntry.getId());
            }
            AdditionalDAO.deleteByPasswordId(passwordEntry.getId());
            AdditionalDAO.insertAll(user.getId(), passwordEntry.getId(), additionalInfoList);

            new Alert(Alert.AlertType.INFORMATION, "Password berhasil diperbarui.").show();

            // Optional: clear form or close window / notify MainView to refresh
        });

        Button cancelBtn = new Button("Batal");
        cancelBtn.setOnAction(e -> {
            // Contoh logika batal: clear atau tutup form
            getChildren().clear();
        });

        HBox actionButtons = new HBox(10, saveBtn, cancelBtn);
        actionButtons.setAlignment(Pos.CENTER_RIGHT);

        getChildren().addAll(titleLabel, formGrid, additionalLabel, additionalTable, rowButtons, actionButtons);
    }

    public EditPasswordForm(User user, PasswordEntry entry, MainView mainView) {
        this(user, entry); // Panggil constructor yang ada
        // Setelah menyimpan atau membatalkan, kembali ke detail
        Button cancelBtn = new Button("Batal");
        cancelBtn.setOnAction(e -> {
            PasswordDetailView detailView = new PasswordDetailView(user, entry, mainView);
            mainView.setRightContent(detailView);
        });

        getChildren().add(cancelBtn);
    }
}
