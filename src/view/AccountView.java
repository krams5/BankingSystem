package view;

import controller.AccountController;
import javafx.geometry.Insets;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;
import model.*;

/** Boundary class: shows the customer's accounts, lets them open new ones and run transactions. No business logic. */
public class AccountView {
    private final Stage stage;
    private final Customer customer;
    private final AccountController accountController;
    private final Label feedbackLabel = new Label();
    private static final String BACKGROUND = "-fx-background-color: #eef4fb;";
    private static final String PRIMARY = "-fx-background-color: #1f5fa8; -fx-text-fill: white;";

    public AccountView(Stage stage, Customer customer, AccountController accountController) {
        this.stage = stage;
        this.customer = customer;
        this.accountController = accountController;
    }

    public void show() {
        String displayName = (customer instanceof Individual ind)
                ? ind.getFirstname() + " " + ind.getSurname()
                : ((Company) customer).getCompanyName();

        Label welcomeLabel = new Label("Welcome, " + displayName);
        welcomeLabel.setStyle("-fx-font-size: 16px; -fx-font-weight: bold; -fx-text-fill: #12396b;");

        // ---- account list ----
        ListView<Account> accountList = new ListView<>();
        accountList.setPlaceholder(new Label("You have no accounts yet. Open one below."));
        accountList.getItems().addAll(customer.getAccounts());
        accountList.setPrefHeight(190);
        accountList.setCellFactory(param -> new ListCell<Account>() {
            @Override
            protected void updateItem(Account acc, boolean empty) {
                super.updateItem(acc, empty);
                if (empty || acc == null) {
                    setText(null);
                } else {
                    setText(acc.getClass().getSimpleName() + " #" + acc.getAccountNumber()
                            + " (" + acc.getBranch() + ") - Balance: BWP " + String.format("%.2f", acc.getBalance()));
                }
            }
        });

        // ---- transactions ----
        TextField amountField = new TextField();
        amountField.setPromptText("Amount in BWP (e.g. 150.00)");

        Button depositBtn = new Button("Deposit");
        Button withdrawBtn = new Button("Withdraw");
        Button interestBtn = new Button("Pay Monthly Interest");

        depositBtn.setOnAction(e -> {
            Account selected = accountList.getSelectionModel().getSelectedItem();
            if (selected == null) {
                showError("Please select an account first.");
                return;
            }
            try {
                double amt = readAmount(amountField);
                accountController.deposit(selected, amt);
                accountList.refresh();
                amountField.clear();
                showOk("Deposited BWP " + String.format("%.2f", amt));
            } catch (Exception ex) {
                showError(messageOf(ex));
            }
        });

        withdrawBtn.setOnAction(e -> {
            Account selected = accountList.getSelectionModel().getSelectedItem();
            if (selected == null) {
                showError("Please select an account first.");
                return;
            }
            try {
                double amt = readAmount(amountField);
                accountController.withdraw(selected, amt);
                accountList.refresh();
                amountField.clear();
                showOk("Withdrew BWP " + String.format("%.2f", amt));
            } catch (Exception ex) {
                showError(messageOf(ex));
            }
        });

        interestBtn.setOnAction(e -> {
            Account selected = accountList.getSelectionModel().getSelectedItem();
            if (selected == null) {
                showError("Please select an account first.");
                return;
            }
            try {
                accountController.payMonthlyInterest(selected);
                accountList.refresh();
                showOk("Monthly interest applied.");
            } catch (Exception ex) {
                showError(messageOf(ex));
            }
        });

        // ---- open a new account ----
        Label openTitle = new Label("Open a new account");
        openTitle.setStyle("-fx-font-weight: bold;");

        ComboBox<String> typeBox = new ComboBox<>();
        typeBox.getItems().addAll("Savings", "Investment", "Cheque");
        typeBox.setPromptText("Account type");

        TextField initialField = new TextField();
        initialField.setPromptText("Initial deposit in BWP");
        initialField.setPrefWidth(260);

        Label investmentRule = new Label("Investment accounts need an initial deposit of at least BWP 500.00.");
        investmentRule.setStyle("-fx-text-fill: #555555; -fx-font-size: 11px;");

        TextField employerField = new TextField();
        employerField.setPromptText("Employer name (Cheque only)");
        TextField employerAddressField = new TextField();
        employerAddressField.setPromptText("Employer address (Cheque only)");
        employerField.disableProperty().bind(typeBox.valueProperty().isNotEqualTo("Cheque"));
        employerAddressField.disableProperty().bind(typeBox.valueProperty().isNotEqualTo("Cheque"));

        Button openBtn = new Button("Open account");
        openBtn.setOnAction(e -> {
            try {
                Account created = accountController.openAccount(customer, typeBox.getValue(),
                        initialField.getText(), employerField.getText(), employerAddressField.getText());
                accountList.getItems().add(created);
                accountList.getSelectionModel().select(created);
                typeBox.setValue(null);
                initialField.clear();
                employerField.clear();
                employerAddressField.clear();
                showOk(created.getClass().getSimpleName() + " account #" + created.getAccountNumber() + " opened.");
            } catch (Exception ex) {
                showError(messageOf(ex));
            }
        });

        Button logoutBtn = new Button("Logout");
        logoutBtn.setOnAction(e -> new LoginView(stage).show());

        feedbackLabel.setWrapText(true);
        for (Button b : new Button[]{depositBtn, withdrawBtn, interestBtn, openBtn}) {
            b.setStyle(PRIMARY);
        }

        VBox root = new VBox(10,
                welcomeLabel,
                new Label("Select an account"), accountList,
                amountField, new HBox(10, depositBtn, withdrawBtn, interestBtn),
                new Separator(),
                openTitle, new HBox(10, typeBox, initialField), investmentRule,
                new HBox(10, employerField, employerAddressField), openBtn,
                new Separator(),
                logoutBtn, feedbackLabel);
        root.setPadding(new Insets(20));
        root.setStyle(BACKGROUND);

        stage.setScene(new Scene(root, 640, 700));
        stage.setTitle("Account Dashboard");
        stage.show();
    }

    private double readAmount(TextField field) {
        try {
            return Double.parseDouble(field.getText().trim());
        } catch (NumberFormatException e) {
            throw new IllegalArgumentException("Enter a valid amount");
        }
    }

    private String messageOf(Exception ex) {
        return ex.getMessage() == null ? ex.toString() : ex.getMessage();
    }

    private void showOk(String message) {
        feedbackLabel.setStyle("-fx-text-fill: #1e8449;");
        feedbackLabel.setText(message);
    }

    private void showError(String message) {
        feedbackLabel.setStyle("-fx-text-fill: #c0392b;");
        feedbackLabel.setText(message);
    }
}


