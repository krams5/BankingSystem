package view;

import controller.TellerController;
import javafx.beans.property.SimpleStringProperty;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;
import java.util.List;

/** Boundary class for the bank teller: sign-in, a registration form and a table of the customers stored in the database. */
public class TellerView {
    private final Stage stage;
    private final TellerController controller = new TellerController();
    private final Label feedbackLabel = new Label();
    private static final String BACKGROUND = "-fx-background-color: #eef4fb;";
    private static final String PRIMARY = "-fx-background-color: #1f5fa8; -fx-text-fill: white;";

    public TellerView(Stage stage) {
        this.stage = stage;
    }

    /** Teller sign-in screen. */
    public void show() {
        Label title = new Label("Bank Teller Sign-in");
        title.setStyle("-fx-font-size: 18px; -fx-font-weight: bold; -fx-text-fill: #12396b;");

        TextField staffIdField = new TextField();
        staffIdField.setPromptText("Staff ID");
        PasswordField pinField = new PasswordField();
        pinField.setPromptText("Staff PIN");

        Button signInBtn = new Button("Sign in");
        signInBtn.setMaxWidth(Double.MAX_VALUE);
        signInBtn.setDefaultButton(true);
        signInBtn.setStyle(PRIMARY);
        Button backBtn = new Button("Back to customer login");
        backBtn.setMaxWidth(Double.MAX_VALUE);

        Label statusLabel = new Label();
        statusLabel.setStyle("-fx-text-fill: red;");

        signInBtn.setOnAction(e -> {
            try {
                controller.signIn(staffIdField.getText(), pinField.getText());
                showRegistration();
            } catch (Exception ex) {
                statusLabel.setText(messageOf(ex));
                pinField.clear();
            }
        });
        backBtn.setOnAction(e -> new LoginView(stage).show());

        VBox root = new VBox(12, title, staffIdField, pinField, signInBtn, backBtn, statusLabel);
        root.setPadding(new Insets(25));
        root.setAlignment(Pos.CENTER);
        root.setStyle(BACKGROUND);

        stage.setScene(new Scene(root, 360, 320));
        stage.setTitle("Teller Sign-in");
        stage.show();
    }

    /** Registration form and customer table, shown after a successful sign-in. */
    private void showRegistration() {
        Label title = new Label("Register a new customer");
        title.setStyle("-fx-font-size: 18px; -fx-font-weight: bold; -fx-text-fill: #12396b;");

        // common fields
        ComboBox<String> typeBox = new ComboBox<>();
        typeBox.getItems().addAll("Individual", "Company");
        typeBox.setValue("Individual");
        TextField email = new TextField();
        TextField contact = new TextField();
        contact.setPromptText("e.g. 71234567");
        TextField address = new TextField();
        PasswordField pin = new PasswordField();
        pin.setPromptText("4 digits");
        // accepts digits only, and no more than 4 of them
        pin.setTextFormatter(new TextFormatter<String>(
                change -> change.getControlNewText().matches("[0-9]{0,4}") ? change : null));

        GridPane common = newGrid();
        addRow(common, 0, "Customer type", typeBox);
        addRow(common, 1, "Email", email);
        addRow(common, 2, "Contact number", contact);
        addRow(common, 3, "Address", address);
        addRow(common, 4, "Initial PIN", pin);

        // individual fields
        TextField nationalId = new TextField();
        TextField firstname = new TextField();
        TextField surname = new TextField();
        DatePicker dob = new DatePicker();
        ComboBox<String> gender = new ComboBox<>();
        gender.getItems().addAll("M", "F");

        GridPane individualGrid = newGrid();
        addRow(individualGrid, 0, "National ID", nationalId);
        addRow(individualGrid, 1, "First name", firstname);
        addRow(individualGrid, 2, "Surname", surname);
        addRow(individualGrid, 3, "Date of birth", dob);
        addRow(individualGrid, 4, "Gender", gender);

        // company fields
        TextField registrationId = new TextField();
        TextField companyName = new TextField();

        GridPane companyGrid = newGrid();
        addRow(companyGrid, 0, "Registration ID", registrationId);
        addRow(companyGrid, 1, "Company name", companyName);

        // show only the section that matches the customer type
        individualGrid.visibleProperty().bind(typeBox.valueProperty().isEqualTo("Individual"));
        individualGrid.managedProperty().bind(individualGrid.visibleProperty());
        companyGrid.visibleProperty().bind(typeBox.valueProperty().isEqualTo("Company"));
        companyGrid.managedProperty().bind(companyGrid.visibleProperty());

        // customers read back from the database
        TableView<String[]> table = new TableView<>();
        String[] headings = {"Contact", "Email", "Type", "Name"};
        double[] widths = {100, 210, 90, 170};
        for (int i = 0; i < headings.length; i++) {
            final int index = i;
            TableColumn<String[], String> column = new TableColumn<>(headings[i]);
            column.setCellValueFactory(cell -> new SimpleStringProperty(cell.getValue()[index]));
            column.setPrefWidth(widths[i]);
            table.getColumns().add(column);
        }
        table.setPrefHeight(200);
        loadTable(table, null);

        Button registerBtn = new Button("Register customer");
        registerBtn.setStyle(PRIMARY);
        Button refreshBtn = new Button("Refresh list");
        Button signOutBtn = new Button("Sign out");

        registerBtn.setOnAction(e -> {
            String mail = email.getText().trim().toLowerCase();
            try {
                if ("Company".equals(typeBox.getValue())) {
                    controller.registerCompany(email.getText(), contact.getText(), address.getText(),
                            pin.getText(), registrationId.getText(), companyName.getText());
                } else {
                    controller.registerIndividual(email.getText(), contact.getText(), address.getText(),
                            pin.getText(), nationalId.getText(), firstname.getText(), surname.getText(),
                            dob.getValue(), gender.getValue());
                }
                for (TextInputControl f : new TextInputControl[]{email, contact, address, pin, nationalId,
                        firstname, surname, registrationId, companyName}) {
                    f.clear();
                }
                dob.setValue(null);
                gender.setValue(null);

                int stored = loadTable(table, mail);   // read the customers back from the database
                showOk("Customer registered successfully and saved to the database."
                        + (stored > 0 ? " Customers stored: " + stored + "." : ""));
            } catch (Exception ex) {
                showError(messageOf(ex));
            }
        });
        refreshBtn.setOnAction(e -> loadTable(table, null));
        signOutBtn.setOnAction(e -> new LoginView(stage).show());

        feedbackLabel.setText("");
        feedbackLabel.setWrapText(true);

        Label tableTitle = new Label("Customers stored in the database");
        tableTitle.setStyle("-fx-font-weight: bold;");

        VBox root = new VBox(12, title, common, individualGrid, companyGrid,
                new HBox(10, registerBtn, refreshBtn, signOutBtn), feedbackLabel, tableTitle, table);
        root.setPadding(new Insets(20));
        root.setStyle(BACKGROUND);

        ScrollPane scroll = new ScrollPane(root);
        scroll.setFitToWidth(true);
        scroll.setStyle("-fx-background: #eef4fb;");

        stage.setScene(new Scene(scroll, 700, 720));
        stage.setTitle("Teller - Register Customer");
        stage.show();
    }

    /** Reloads the table from the database. Returns the number of customers, or -1 if loading failed. */
    private int loadTable(TableView<String[]> table, String selectEmail) {
        try {
            List<String[]> rows = controller.customerRows();
            table.getItems().setAll(rows);
            if (selectEmail != null) {
                for (String[] row : rows) {
                    if (row[1].equals(selectEmail)) {
                        table.getSelectionModel().select(row);
                        table.scrollTo(row);
                    }
                }
            }
            return rows.size();
        } catch (Exception ex) {
            showError(messageOf(ex));
            return -1;
        }
    }

    private GridPane newGrid() {
        GridPane grid = new GridPane();
        grid.setHgap(12);
        grid.setVgap(8);
        return grid;
    }

    private void addRow(GridPane grid, int row, String label, Control field) {
        field.setPrefWidth(280);
        grid.add(new Label(label), 0, row);
        grid.add(field, 1, row);
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