//Form untuk menambahkan password baru, termasuk info tambahan.
package com.passwordmanager.views;

import com.passwordmanager.daos.AdditionalDAO;
import com.passwordmanager.daos.FolderDAO;
import com.passwordmanager.daos.PasswordDAO;
import com.passwordmanager.models.AdditionalInfo;
import com.passwordmanager.models.Folder;
import com.passwordmanager.models.PasswordEntry;
import com.passwordmanager.models.User;
import javafx.beans.property.SimpleStringProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.*;
import javafx.scene.control.cell.TextFieldTableCell;
import javafx.scene.layout.*;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;

public class PasswordForm extends VBox {
    private final MainView mainView;
    private final User user;
    private final TextField nameField = new TextField();
    private final TextField usernameField = new TextField();
    private final PasswordField passwordField = new PasswordField();
    private final PasswordField confirmPasswordField = new PasswordField();
    private final TextField visiblePasswordField = new TextField();
    private final TextField visibleConfirmPasswordField = new TextField();
    private final CheckBox showPasswordCheckBox = new CheckBox("Tampilkan Password");
    private final ComboBox<String> categoryBox = new ComboBox<>();
    private final ComboBox<String> folderBox = new ComboBox<>();
    private final ObservableList<AdditionalInfo> additionalInfoList = FXCollections.observableArrayList();

    public PasswordForm(User user, MainView mainView) {
        this.user = user;
        this.mainView = mainView;

        setSpacing(15);
        setPadding(new Insets(20));

        Label titleLabel = new Label("Tambah Password Baru");
        titleLabel.setFont(Font.font("System", FontWeight.BOLD, 20));

        GridPane formGrid = new GridPane();
        formGrid.setHgap(10);
        formGrid.setVgap(10);
        formGrid.setPadding(new Insets(10));

        formGrid.add(new Label("Nama Akun:"), 0, 0);
        formGrid.add(nameField, 1, 0);

        formGrid.add(new Label("Username / Email:"), 0, 1);
        formGrid.add(usernameField, 1, 1);

        formGrid.add(new Label("Password:"), 0, 2);
        StackPane passwordStack = new StackPane(passwordField, visiblePasswordField);
        visiblePasswordField.setManaged(false);
        visiblePasswordField.setVisible(false);
        formGrid.add(passwordStack, 1, 2);

        formGrid.add(new Label("Konfirmasi Password:"), 0, 3);
        StackPane confirmStack = new StackPane(confirmPasswordField, visibleConfirmPasswordField);
        visibleConfirmPasswordField.setManaged(false);
        visibleConfirmPasswordField.setVisible(false);
        formGrid.add(confirmStack, 1, 3);

        showPasswordCheckBox.setOnAction(e -> {
            boolean visible = showPasswordCheckBox.isSelected();
            visiblePasswordField.setVisible(visible);
            visiblePasswordField.setManaged(visible);
            passwordField.setVisible(!visible);
            passwordField.setManaged(!visible);

            visibleConfirmPasswordField.setVisible(visible);
            visibleConfirmPasswordField.setManaged(visible);
            confirmPasswordField.setVisible(!visible);
            confirmPasswordField.setManaged(!visible);

            if (visible) {
                visiblePasswordField.setText(passwordField.getText());
                visibleConfirmPasswordField.setText(confirmPasswordField.getText());
            } else {
                passwordField.setText(visiblePasswordField.getText());
                confirmPasswordField.setText(visibleConfirmPasswordField.getText());
            }
        });
        formGrid.add(showPasswordCheckBox, 1, 4);

        formGrid.add(new Label("Kategori:"), 0, 5);
        categoryBox.getItems().addAll("Whatsapp", "Instagram", "Facebook", "Email", "E-Wallet", "Lainnya");
        formGrid.add(categoryBox, 1, 5);

        formGrid.add(new Label("Folder:"), 0, 6);
        for (Folder folder : FolderDAO.getFoldersByUserId(user.getId())) {
            folderBox.getItems().add(folder.getName());
        }
        formGrid.add(folderBox, 1, 6);

        Label additionalLabel = new Label("Informasi Tambahan (Key - Value):");

        TableView<AdditionalInfo> additionalTable = new TableView<>(additionalInfoList);
        additionalTable.setEditable(true);
        additionalTable.setColumnResizePolicy(TableView.CONSTRAINED_RESIZE_POLICY);

        TableColumn<AdditionalInfo, String> keyCol = new TableColumn<>("Key");
        keyCol.setCellValueFactory(cellData -> new SimpleStringProperty(cellData.getValue().getEntryKey()));
        keyCol.setCellFactory(TextFieldTableCell.forTableColumn());
        keyCol.setOnEditCommit(e -> e.getRowValue().setEntryKey(e.getNewValue()));

        TableColumn<AdditionalInfo, String> valueCol = new TableColumn<>("Value");
        valueCol.setCellValueFactory(cellData -> new SimpleStringProperty(cellData.getValue().getEntryValue()));
        valueCol.setCellFactory(TextFieldTableCell.forTableColumn());
        valueCol.setOnEditCommit(e -> e.getRowValue().setEntryValue(e.getNewValue()));

        additionalTable.getColumns().addAll(keyCol, valueCol);

        Button addRowBtn = new Button("Tambah Info");
        addRowBtn.setOnAction(e -> additionalInfoList.add(new AdditionalInfo(0, "", "", false, 0)));

        Button deleteRowBtn = new Button("Hapus Info");
        deleteRowBtn.setOnAction(e -> {
            AdditionalInfo selected = additionalTable.getSelectionModel().getSelectedItem();
            if (selected != null) {
                additionalInfoList.remove(selected);
            }
        });

        HBox rowButtons = new HBox(10, addRowBtn, deleteRowBtn);
        rowButtons.setAlignment(Pos.CENTER_LEFT);

        Button saveButton = new Button("Simpan");
        saveButton.setOnAction(e -> {
            String name = nameField.getText().trim();
            String username = usernameField.getText().trim();
            String password = showPasswordCheckBox.isSelected() ? visiblePasswordField.getText().trim() : passwordField.getText().trim();
            String confirmPassword = showPasswordCheckBox.isSelected() ? visibleConfirmPasswordField.getText().trim() : confirmPasswordField.getText().trim();
            String category = categoryBox.getValue();
            String folderName = folderBox.getValue();

            if (name.isEmpty() || password.isEmpty() || confirmPassword.isEmpty() || folderName == null || category == null) {
                new Alert(Alert.AlertType.ERROR, "Semua field wajib diisi.").show();
                return;
            }

            if (!password.equals(confirmPassword)) {
                new Alert(Alert.AlertType.ERROR, "Password dan konfirmasi tidak cocok.").show();
                return;
            }

            int folderId = FolderDAO.getIdByName(folderName, user.getId());
            if (folderId == -1) {
                new Alert(Alert.AlertType.ERROR, "Folder tidak ditemukan.").show();
                return;
            }

            PasswordEntry newEntry = new PasswordEntry();
            newEntry.setUserId(user.getId());
            newEntry.setFolderId(folderId);
            newEntry.setName(name);
            newEntry.setUsername(username);
            newEntry.setEncryptedPassword(password);
            newEntry.setCategory(category);
            newEntry.setWebsite("");
            newEntry.setNotes("");
            newEntry.setHashkey("default");
            newEntry.setScore(0);
            newEntry.setEncryptionKey("defaultKey");

            boolean success = PasswordDAO.insert(newEntry);
            if (success) {
                int passwordId = PasswordDAO.getLastInsertedId(user.getId(), name);
                if (passwordId == -1) {
                    new Alert(Alert.AlertType.ERROR, "Gagal mengambil ID password.").show();
                    return;
                }

                for (AdditionalInfo info : additionalInfoList) {
                    info.setPasswordId(passwordId);
                }
                AdditionalDAO.insertAll(user.getId(), passwordId, additionalInfoList);

                new Alert(Alert.AlertType.INFORMATION, "Password berhasil disimpan.").show();
                clearForm();
                mainView.refreshFolderTree();

                newEntry.setId(passwordId);
                mainView.setRightContent(new PasswordDetailView(user, newEntry, mainView));
            } else {
                new Alert(Alert.AlertType.ERROR, "Gagal menyimpan password.").show();
            }
        });

        Button editButton = new Button("Edit");
        Button copyButton = new Button("Salin Password");
        HBox actionButtons = new HBox(10, editButton, saveButton, copyButton);
        actionButtons.setAlignment(Pos.CENTER_RIGHT);

        getChildren().addAll(titleLabel, formGrid, additionalLabel, additionalTable, rowButtons, actionButtons);
    }

    private void clearForm() {
        nameField.clear();
        usernameField.clear();
        passwordField.clear();
        confirmPasswordField.clear();
        visiblePasswordField.clear();
        visibleConfirmPasswordField.clear();
        categoryBox.getSelectionModel().clearSelection();
        folderBox.getSelectionModel().clearSelection();
        additionalInfoList.clear();
    }
}
