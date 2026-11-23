package view;

import controller.AccountController;
import javafx.geometry.Insets;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;
import model.*;

    public class AccountView {
        private Stage stage;
        private Customer customer;
        private AccountController accountController;

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
            welcomeLabel.setStyle("-fx-font-size: 16px; -fx-font-weight: bold;");

            ListView<Account> accountList = new ListView<>();
            accountList.getItems().addAll(customer.getAccounts());

            // Custom display formatting in the ListView
            accountList.setCellFactory(param -> new ListCell<>() {
                @Override
                protected void updateItem(Account acc, boolean empty) {
                    super.updateItem(acc, empty);
                    if (empty || acc == null) {
                        setText(null);
                    } else {
                        setText(acc.getClass().getSimpleName() + " #" + acc.getAccountNumber()
                                + " (" + acc.getBranch() + ") — Balance: BWP " + String.format("%.2f", acc.getBalance()));
                    }
                }
            });

            TextField amountField = new TextField();
            amountField.setPromptText("Enter amount in BWP (e.g. 150.00)");

            Button depositBtn = new Button("Deposit");
            Button withdrawBtn = new Button("Withdraw");
            Button interestBtn = new Button("Pay Monthly Interest");
            Button logoutBtn = new Button("Logout");

            Label feedbackLabel = new Label();

            depositBtn.setOnAction(e -> {
                Account selected = accountList.getSelectionModel().getSelectedItem();
                if (selected == null) {
                    feedbackLabel.setText("Please select an account first.");
                    return;
                }
                try {
                    double amt = Double.parseDouble(amountField.getText());
                    accountController.deposit(selected, amt);
                    accountList.refresh();
                    amountField.clear();
                    feedbackLabel.setText("Deposited BWP " + amt);
                } catch (Exception ex) {
                    feedbackLabel.setText("Error: " + ex.getMessage());
                }
            });

            withdrawBtn.setOnAction(e -> {
                Account selected = accountList.getSelectionModel().getSelectedItem();
                if (selected == null) {
                    feedbackLabel.setText("Please select an account first.");
                    return;
                }
                try {
                    double amt = Double.parseDouble(amountField.getText());
                    accountController.withdraw(selected, amt);
                    accountList.refresh();
                    amountField.clear();
                    feedbackLabel.setText("Withdrew BWP " + amt);
                } catch (Exception ex) {
                    feedbackLabel.setText("Error: " + ex.getMessage());
                }
            });

            interestBtn.setOnAction(e -> {
                Account selected = accountList.getSelectionModel().getSelectedItem();
                if (selected == null) {
                    feedbackLabel.setText("Please select an account first.");
                    return;
                }
                try {
                    accountController.payMonthlyInterest(selected);
                    accountList.refresh();
                    feedbackLabel.setText("Monthly interest applied!");
                } catch (Exception ex) {
                    feedbackLabel.setText("Error: " + ex.getMessage());
                }
            });

            logoutBtn.setOnAction(e -> new LoginView(stage).show());

            HBox buttonBar = new HBox(10, depositBtn, withdrawBtn, interestBtn, logoutBtn);
            VBox root = new VBox(12, welcomeLabel, accountList, amountField, buttonBar, feedbackLabel);
            root.setPadding(new Insets(20));

            stage.setScene(new Scene(root, 550, 420));
            stage.setTitle("Account Dashboard");
            stage.show();
        }
    }


