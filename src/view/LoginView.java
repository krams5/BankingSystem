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

/** Boundary class: customer login with email and a 4-digit PIN, plus a button to the bank teller screen. */
public class LoginView {
    private static final String BACKGROUND = "-fx-background-color: #eef4fb;";
    private static final String PRIMARY = "-fx-background-color: #1f5fa8; -fx-text-fill: white;";
    private static final String SECONDARY = "-fx-background-color: white; -fx-text-fill: #1f5fa8; -fx-border-color: #1f5fa8;";

    private final Stage stage;
    private final LoginController loginController = new LoginController();

    public LoginView(Stage stage) {
        this.stage = stage;
    }

    public void show() {
        Label title = new Label("Botswana Banking System");
        title.setStyle("-fx-font-size: 18px; -fx-font-weight: bold; -fx-text-fill: #12396b;");

        TextField emailField = new TextField();
        emailField.setPromptText("Email address");

        PasswordField pinField = new PasswordField();
        pinField.setPromptText("4-digit PIN");
        // accepts digits only, and no more than 4 of them
        pinField.setTextFormatter(new TextFormatter<String>(
                change -> change.getControlNewText().matches("[0-9]{0,4}") ? change : null));

        Button loginBtn = new Button("Login");
        loginBtn.setMaxWidth(Double.MAX_VALUE);
        loginBtn.setDefaultButton(true);
        loginBtn.setStyle(PRIMARY);

        Button tellerBtn = new Button("Register new customer (bank teller)");
        tellerBtn.setMaxWidth(Double.MAX_VALUE);
        tellerBtn.setStyle(SECONDARY);

        Label statusLabel = new Label();
        statusLabel.setStyle("-fx-text-fill: red;");

        loginBtn.setOnAction(e -> {
            try {
                Customer customer = loginController.login(emailField.getText(), pinField.getText());
                new AccountView(stage, customer, new AccountController()).show();
            } catch (Exception ex) {
                statusLabel.setText(ex.getMessage() == null ? ex.toString() : ex.getMessage());
                pinField.clear();
            }
        });
        tellerBtn.setOnAction(e -> new TellerView(stage).show());

        VBox root = new VBox(12, title, emailField, pinField, loginBtn, statusLabel, tellerBtn);
        root.setPadding(new Insets(25));
        root.setAlignment(Pos.CENTER);
        root.setStyle(BACKGROUND);

        stage.setScene(new Scene(root, 380, 340));
        stage.setTitle("Login");
        stage.show();
    }
}

