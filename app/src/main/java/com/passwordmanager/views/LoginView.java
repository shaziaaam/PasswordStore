//Form login, validasi input, dan arahkan ke MainView jika sukses.
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

public class LoginView {
    private final Stage stage;

    public LoginView(Stage stage) {
        this.stage = stage;
        showLoginScreen();
    }

    private void showLoginScreen() {
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
        formContainer.setPadding(new Insets(40));
        formContainer.setPrefWidth(400);

        Label lblLogin = new Label("Login");
        lblLogin.setStyle("-fx-font-size: 20px; -fx-font-weight: bold;");

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

        Label lblUsernameError = new Label("Username minimal 4 karakter");
        lblUsernameError.setStyle("-fx-text-fill: red; -fx-font-size: 10px;");
        lblUsernameError.setVisible(false);

        Label lblPasswordError = new Label("Password minimal 8 karakter");
        lblPasswordError.setStyle("-fx-text-fill: red; -fx-font-size: 10px;");
        lblPasswordError.setVisible(false);

        formGrid.add(lblUsername, 0, 0);
        formGrid.add(tfUsername, 1, 0);
        formGrid.add(lblPassword, 0, 1);
        formGrid.add(tfPassword, 1, 1);

        Button btnLogin = new Button("Login");
        btnLogin.setPrefWidth(220);
        btnLogin.setStyle("-fx-background-color: #2196F3; -fx-text-fill: white; -fx-font-weight: bold;");

        Button btnRegister = new Button("Register");
        btnRegister.setPrefWidth(220);

        Label lblMessage = new Label();
        lblMessage.setStyle("-fx-text-fill: red;");

        btnLogin.setOnAction(e -> {
            lblUsernameError.setVisible(false);
            lblPasswordError.setVisible(false);
            lblMessage.setText("");

            String username = tfUsername.getText().trim();
            String password = tfPassword.getText();

            boolean valid = true;
            if (username.length() < 4) {
                lblUsernameError.setVisible(true);
                valid = false;
            }
            if (password.length() < 8) {
                lblPasswordError.setVisible(true);
                valid = false;
            }

            if (!valid) return;

            // Kirim password mentah ke UserDAO, hashing dilakukan di dalam login()
            User user = UserDAO.login(username, password);

            if (user != null) {
                new MainView(stage, user);
            } else {
                lblMessage.setText("Username atau password salah.");
            }
        });

        btnRegister.setOnAction(e -> new RegisterView(stage));

        formContainer.getChildren().addAll(
                lblLogin,
                formGrid,
                lblUsernameError,
                lblPasswordError,
                btnLogin,
                btnRegister,
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
