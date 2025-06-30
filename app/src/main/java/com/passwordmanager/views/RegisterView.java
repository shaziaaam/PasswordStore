package com.passwordmanager.views;

import com.passwordmanager.daos.UserDAO;
import com.passwordmanager.models.User;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.*;
import javafx.stage.Stage;

public class RegisterView {
    private final Stage stage;

    public RegisterView(Stage stage) {
        this.stage = stage;
        showRegisterScreen();
    }

    private void showRegisterScreen() {
        // Panel kiri (branding)
        VBox branding = new VBox(10);
        branding.setAlignment(Pos.CENTER);
        branding.setPadding(new Insets(20));
        branding.setStyle("-fx-background-color: #2196F3;");
        branding.setPrefWidth(260);

        ImageView imageView = new ImageView(new Image(getClass().getResourceAsStream("/images/lock.png")));
        imageView.setFitWidth(180);
        imageView.setPreserveRatio(true);

        Label appName = new Label("StorePassword");
        appName.setStyle("-fx-text-fill: white; -fx-font-size: 20px; -fx-font-weight: bold;");
        Label tagline = new Label("Secure Password Manager");
        tagline.setStyle("-fx-text-fill: white;");

        branding.getChildren().addAll(imageView, appName, tagline);

        // Panel kanan (form container)
        VBox formContainer = new VBox(15);
        formContainer.setAlignment(Pos.CENTER);
        formContainer.setPadding(new Insets(40, 40, 40, 40));
        formContainer.setPrefWidth(400);

        Label lblRegister = new Label("Register");
        lblRegister.setStyle("-fx-font-size: 20px; -fx-font-weight: bold;");

        // Form grid layout
        GridPane formGrid = new GridPane();
        formGrid.setVgap(10);
        formGrid.setHgap(10);
        formGrid.setAlignment(Pos.CENTER);

        Label lblUsername = new Label("Username:");
        TextField tfUsername = new TextField();
        tfUsername.setPrefWidth(220);

        Label lblPassword = new Label("Password:");
        PasswordField tfPassword = new PasswordField();
        tfPassword.setPrefWidth(220);

        Label lblConfirm = new Label("Confirm Password:");
        PasswordField tfConfirm = new PasswordField();
        tfConfirm.setPrefWidth(220);

        Label lblFullname = new Label("Full Name:");
        TextField tfFullname = new TextField();
        tfFullname.setPrefWidth(220);

        formGrid.add(lblUsername, 0, 0);
        formGrid.add(tfUsername, 1, 0);
        formGrid.add(lblPassword, 0, 1);
        formGrid.add(tfPassword, 1, 1);
        formGrid.add(lblConfirm, 0, 2);
        formGrid.add(tfConfirm, 1, 2);
        formGrid.add(lblFullname, 0, 3);
        formGrid.add(tfFullname, 1, 3);

        Button btnRegister = new Button("Register");
        btnRegister.setPrefWidth(220);
        btnRegister.setStyle("-fx-background-color: #2196F3; -fx-text-fill: white; -fx-font-weight: bold;");

        Button btnLogin = new Button("Login");
        btnLogin.setPrefWidth(220);

        Label lblMessage = new Label();
        lblMessage.setStyle("-fx-text-fill: red;");

        btnRegister.setOnAction(e -> {
            lblMessage.setText("");
            String username = tfUsername.getText().trim();
            String password = tfPassword.getText();
            String confirm = tfConfirm.getText();
            String fullname = tfFullname.getText().trim();

            if (username.length() < 4) {
                lblMessage.setText("Username minimal 4 karakter.");
                return;
            }
            if (password.length() < 8) {
                lblMessage.setText("Password minimal 8 karakter.");
                return;
            }
            if (!password.equals(confirm)) {
                lblMessage.setText("Konfirmasi password tidak cocok.");
                return;
            }
            if (fullname.isEmpty()) {
                lblMessage.setText("Nama lengkap tidak boleh kosong.");
                return;
            }

            User registeredUser = UserDAO.register(username, password, fullname);

            if (registeredUser != null) {
                new LoginView(stage);
            } else {
                lblMessage.setText("Registrasi gagal. Username mungkin sudah digunakan.");
            }
        });

        btnLogin.setOnAction(e -> new LoginView(stage));

        formContainer.getChildren().addAll(
                lblRegister,
                formGrid,
                btnRegister,
                btnLogin,
                lblMessage
        );

        // Gabungkan kiri-kanan
        HBox layout = new HBox(branding, formContainer);
        layout.setPrefSize(700, 400);

        Scene scene = new Scene(layout);
        stage.setScene(scene);
        stage.setTitle("Authentication");
        stage.show();
    }
}
