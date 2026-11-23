package view;

import controller.AccountController;
import controller.LoginController;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;
import model.Customer;

    public class LoginView {
        private Stage stage;
        private LoginController loginController = new LoginController();

        public LoginView(Stage stage) {
            this.stage = stage;
        }

        public void show() {
            Label title = new Label("Botswana Banking System");
            title.setStyle("-fx-font-size: 18px; -fx-font-weight: bold;");

            TextField contactField = new TextField();
            contactField.setPromptText("Contact Number (e.g. 71234567)");

            PasswordField pinField = new PasswordField();
            pinField.setPromptText("4-Digit PIN");

            Button loginBtn = new Button("Login");
            loginBtn.setMaxWidth(Double.MAX_VALUE);

            Label statusLabel = new Label();
            statusLabel.setStyle("-fx-text-fill: red;");

            loginBtn.setOnAction(e -> {
                try {
                    Customer customer = loginController.login(contactField.getText(), pinField.getText());
                    AccountView accountView = new AccountView(stage, customer, new AccountController());
                    accountView.show();
                } catch (Exception ex) {
                    statusLabel.setText(ex.getMessage());
                }
            });

            VBox root = new VBox(12, title, contactField, pinField, loginBtn, statusLabel);
            root.setPadding(new Insets(25));
            root.setAlignment(Pos.CENTER);

            stage.setScene(new Scene(root, 360, 260));
            stage.setTitle("Login");
            stage.show();
        }
    }

