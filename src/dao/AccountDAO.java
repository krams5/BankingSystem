package dao;


import model.*;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

    public class AccountDAO {

        public void saveAccount(Account account, int customerContact) throws SQLException {
            String sql = """
            INSERT INTO accounts (account_number, balance, branch, account_type,
            company_name, company_address, customer_contact)
            VALUES (?, ?, ?, ?, ?, ?, ?)
        """;

            try (Connection conn = DatabaseConnection.getConnection();
                 PreparedStatement pstmt = conn.prepareStatement(sql)) {

                pstmt.setInt(1, account.getAccountNumber());
                pstmt.setDouble(2, account.getBalance());
                pstmt.setString(3, account.getBranch());

                if (account instanceof Savings) {
                    pstmt.setString(4, "SAVINGS");
                    pstmt.setNull(5, Types.VARCHAR);
                    pstmt.setNull(6, Types.VARCHAR);
                } else if (account instanceof Investment) {
                    pstmt.setString(4, "INVESTMENT");
                    pstmt.setNull(5, Types.VARCHAR);
                    pstmt.setNull(6, Types.VARCHAR);
                } else if (account instanceof Cheque chq) {
                    pstmt.setString(4, "CHEQUE");
                    pstmt.setString(5, chq.getCompanyName());
                    pstmt.setString(6, chq.getCompanyAddress());
                }

                pstmt.setInt(7, customerContact);
                pstmt.executeUpdate();
            }
        }

        public List<Account> getAccountsByCustomer(int customerContact) throws SQLException {
            List<Account> accounts = new ArrayList<>();
            String sql = "SELECT * FROM accounts WHERE customer_contact = ?";

            try (Connection conn = DatabaseConnection.getConnection();
                 PreparedStatement pstmt = conn.prepareStatement(sql)) {

                pstmt.setInt(1, customerContact);
                ResultSet rs = pstmt.executeQuery();

                while (rs.next()) {
                    int accNum = rs.getInt("account_number");
                    double balance = rs.getDouble("balance");
                    String branch = rs.getString("branch");
                    String type = rs.getString("account_type");

                    switch (type) {
                        case "SAVINGS" -> accounts.add(new Savings(accNum, balance, branch));
                        case "INVESTMENT" -> accounts.add(new Investment(accNum, balance, branch));
                        case "CHEQUE" -> accounts.add(new Cheque(
                                accNum, balance, branch,
                                rs.getString("company_name"),
                                rs.getString("company_address")
                        ));
                    }
                }
            }
            return accounts;
        }

        public void updateBalance(int accountNumber, double newBalance) throws SQLException {
            String sql = "UPDATE accounts SET balance = ? WHERE account_number = ?";
            try (Connection conn = DatabaseConnection.getConnection();
                 PreparedStatement pstmt = conn.prepareStatement(sql)) {
                pstmt.setDouble(1, newBalance);
                pstmt.setInt(2, accountNumber);
                pstmt.executeUpdate();
            }
        }
    }


