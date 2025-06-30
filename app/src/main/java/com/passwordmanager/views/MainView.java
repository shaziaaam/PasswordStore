//Tampilan utama setelah login:
//Panel kiri: TreeView berisi folder dan password
//Panel kanan: konten dinamis (form tambah/edit/detail)
//Toolbar: tambah folder, password, edit profil, logout
package com.passwordmanager.views;

import com.passwordmanager.daos.FolderDAO;
import com.passwordmanager.daos.PasswordDAO;
import com.passwordmanager.models.Folder;
import com.passwordmanager.models.PasswordEntry;
import com.passwordmanager.models.User;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.input.KeyEvent;
import javafx.scene.layout.*;
import javafx.stage.Stage;

public class MainView {
    private final Stage stage;
    private final User user;
    private final BorderPane root;
    private final TreeView<String> treeView;
    private final VBox rightPanel;
    private final VBox rightTopToolbar;
    private final StackPane rightContentArea;

    public MainView(Stage stage, User user) {
        this.stage = stage;
        this.user = user;
        this.root = new BorderPane();
        this.treeView = new TreeView<>();
        this.rightPanel = new VBox();
        this.rightTopToolbar = new VBox();
        this.rightContentArea = new StackPane();

        setupUI();
    }

    private void setupUI() {
        root.setLeft(buildLeftPanel());
        root.setCenter(buildRightPanel());

        Scene scene = new Scene(root, 900, 600);
        stage.setScene(scene);
        stage.setTitle("StorePassword");
        stage.show();
    }

    private VBox buildLeftPanel() {
        VBox leftPanel = new VBox(10);
        leftPanel.setPadding(new Insets(10));
        leftPanel.setPrefWidth(250);

        TextField searchField = new TextField();
        searchField.setPromptText("Search...");
        searchField.setMaxWidth(Double.MAX_VALUE);

        TreeItem<String> rootItem = new TreeItem<>();
        rootItem.setExpanded(true);
        treeView.setRoot(rootItem);
        treeView.setShowRoot(false);

        refreshFolderTree();

        searchField.addEventHandler(KeyEvent.KEY_RELEASED, e -> {
            // Optional: implement real-time search
        });

        VBox.setVgrow(treeView, Priority.ALWAYS);
        leftPanel.getChildren().addAll(searchField, treeView);
        return leftPanel;
    }

    private VBox buildRightPanel() {
        rightTopToolbar.setPadding(new Insets(10));
        rightTopToolbar.setSpacing(10);
        rightTopToolbar.setAlignment(Pos.TOP_RIGHT);

        HBox toolbarButtons = new HBox(10);
        toolbarButtons.setAlignment(Pos.CENTER_RIGHT);

        Button btnAddFolder = new Button("Tambah Folder");
        Button btnAddPassword = new Button("Tambah Password");
        Button btnEditProfile = new Button("Edit Profil");
        Button btnLogout = new Button("Logout");

        btnAddFolder.setOnAction(e -> showAddFolderDialog());
        btnAddPassword.setOnAction(e -> showAddPasswordForm());
        btnEditProfile.setOnAction(e -> showEditProfileForm());
        btnLogout.setOnAction(e -> logout());

        toolbarButtons.getChildren().addAll(btnAddFolder, btnAddPassword, btnEditProfile, btnLogout);
        rightTopToolbar.getChildren().add(toolbarButtons);

        VBox welcomeBox = new VBox(10);
        welcomeBox.setPadding(new Insets(20));
        welcomeBox.setAlignment(Pos.CENTER);

        Label welcomeLabel = new Label("Selamat datang di StorePassword, " + user.getUsername() + "!");
        welcomeLabel.setStyle("-fx-font-size: 24px; -fx-font-weight: bold;");

        welcomeBox.getChildren().add(welcomeLabel);
        rightContentArea.getChildren().add(welcomeBox);
        VBox.setVgrow(rightContentArea, Priority.ALWAYS);
        rightPanel.getChildren().addAll(rightTopToolbar, rightContentArea);
        return rightPanel;
    }

    private void showAddFolderDialog() {
        TextInputDialog dialog = new TextInputDialog();
        dialog.setTitle("Tambah Folder");
        dialog.setHeaderText(null);
        dialog.setContentText("Nama Folder:");

        dialog.showAndWait().ifPresent(folderName -> {
            if (!folderName.trim().isEmpty()) {
                boolean inserted = FolderDAO.insert(user.getId(), folderName.trim());
                if (inserted) refreshFolderTree();
                else new Alert(Alert.AlertType.ERROR, "Gagal menambahkan folder.").show();
            }
        });
    }

    private void showAddPasswordForm() {
        PasswordForm passwordForm = new PasswordForm(user, this);
        rightContentArea.getChildren().setAll(passwordForm);
    }

    private void showEditProfileForm() {
        EditProfile editProfile = new EditProfile(user, this::reloadAfterProfileUpdate);
        rightContentArea.getChildren().setAll(editProfile);
    }

    private void reloadAfterProfileUpdate() {
        Label updatedLabel = new Label("Profil berhasil diperbarui. Selamat datang kembali!");
        updatedLabel.setStyle("-fx-font-weight: bold; -fx-font-size: 16px;");
        rightContentArea.getChildren().setAll(updatedLabel);
    }

    private void logout() {
        new LoginView(stage);
    }

    public void refreshFolderTree() {
        TreeItem<String> rootItem = treeView.getRoot();
        rootItem.getChildren().clear();

        for (Folder folder : FolderDAO.getAll(user.getId())) {
            TreeItem<String> folderItem = new TreeItem<>(folder.getName());
            for (PasswordEntry entry : PasswordDAO.getByFolderName(user.getId(), folder.getName())) {
                TreeItem<String> passwordItem = new TreeItem<>(entry.getName());
                folderItem.getChildren().add(passwordItem);
            }
            rootItem.getChildren().add(folderItem);
        }

        treeView.getSelectionModel().selectedItemProperty().addListener((obs, oldVal, newVal) -> {
            if (newVal != null && newVal.getParent() != null) {
                String folderName = newVal.getParent().getValue();
                String passwordName = newVal.getValue();
                PasswordEntry selected = PasswordDAO.getByName(user.getId(), folderName, passwordName);
                if (selected != null) {
                    PasswordDetailView detailView = new PasswordDetailView(user, selected, this);
                    rightContentArea.getChildren().setAll(detailView);
                }
            }
        });
    }

    public TreeView<String> getTreeView() {
        return treeView;
    }

    public void showPasswordFormForEdit(PasswordEntry entry) {
        EditPasswordForm editForm = new EditPasswordForm(user, entry, this);
        rightContentArea.getChildren().setAll(editForm);
    }

    public void setRightContent(javafx.scene.Node node) {
        rightContentArea.getChildren().setAll(node);
    }
}
