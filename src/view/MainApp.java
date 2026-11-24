package view;

import dao.DatabaseConnection;
import javafx.application.Application;
import javafx.stage.Stage;

    public class MainApp extends Application {

        @Override
        public void start(Stage primaryStage) {
            // 1. Create H2 database tables automatically if they don't exist yet
            DatabaseConnection.initializeDatabase();

            // 2. Launch the first GUI screen (LoginView)
            LoginView loginView = new LoginView(primaryStage);
            loginView.show();
        }

        public static void main(String[] args) {
            launch(args);
        }
    }


