package dao;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.sql.Statement;

    public class DatabaseConnection {

        private static final String URL = "jdbc:h2:./bankdb;AUTO_SERVER=TRUE";
        private static final String USER = "sa";
        private static final String PASSWORD = "";

        public static Connection getConnection() throws SQLException {
            return DriverManager.getConnection(URL, USER, PASSWORD);
        }

        public static void initializeDatabase() {
            String createCustomers = """
            CREATE TABLE IF NOT EXISTS customers (
                contact INT PRIMARY KEY,
                email VARCHAR(100),
                address VARCHAR(255),
                pin VARCHAR(4),
                customer_type VARCHAR(20),
                national_id INT,
                firstname VARCHAR(100),
                surname VARCHAR(100),
                dob DATE,
                gender VARCHAR(10),
                registration_id INT,
                company_name VARCHAR(100)
            );
        """;

            String createAccounts = """
            CREATE TABLE IF NOT EXISTS accounts (
                account_number INT PRIMARY KEY,
                balance DOUBLE,
                branch VARCHAR(100),
                account_type VARCHAR(20),
                company_name VARCHAR(100),
                company_address VARCHAR(255),
                customer_contact INT,
                FOREIGN KEY (customer_contact) REFERENCES customers(contact)
            );
        """;

            try (Connection conn = getConnection(); Statement stmt = conn.createStatement()) {
                stmt.execute(createCustomers);
                stmt.execute(createAccounts);
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
    }


