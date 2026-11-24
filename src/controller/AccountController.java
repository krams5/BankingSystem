package controller;

import dao.AccountDAO;
import model.*;

import java.sql.SQLException;

public class AccountController {
    private AccountDAO accountDAO = new AccountDAO();

    /** Builds the account the user chose, enforces the opening rules, saves it and adds it to the customer. */
    public Account openAccount(Customer customer, String type, String depositText,
                               String employer, String employerAddress) throws SQLException {
        if (type == null) {
            throw new IllegalArgumentException("Choose an account type");
        }

        double deposit = 0;
        if (depositText != null && !depositText.isBlank()) {
            try {
                deposit = Double.parseDouble(depositText.trim());
            } catch (NumberFormatException e) {
                throw new IllegalArgumentException("Enter a valid deposit amount");
            }
        }
        if (deposit < 0) {
            throw new IllegalArgumentException("Initial deposit cannot be negative");
        }

        // simple account number from the clock (100000 to 999999)
        int number = 100000 + (int) Math.floorMod(System.currentTimeMillis(), 900000L);

        // the model enforces the rules: Investment throws below BWP500, so nothing is saved
        Account account = switch (type) {
            case "Savings" -> new Savings(number, deposit, "Main");
            case "Investment" -> new Investment(number, deposit, "Main");
            case "Cheque" -> {
                if (!(customer instanceof Individual)) {
                    throw new IllegalArgumentException("Only individual customers can open a Cheque account");
                }
                if (employer == null || employer.isBlank() || employerAddress == null || employerAddress.isBlank()) {
                    throw new IllegalArgumentException("A Cheque account requires employer name and address");
                }
                yield new Cheque(number, deposit, "Main", employer.trim(), employerAddress.trim());
            }
            default -> throw new IllegalArgumentException("Choose an account type");
        };

        openNewAccount(customer, account);
        return account;
    }

    public void openNewAccount(Customer customer, Account newAccount) throws SQLException {
        accountDAO.saveAccount(newAccount, customer.getContact());
        customer.openAccount(newAccount);
    }

    public void deposit(Account account, double amount) throws SQLException {
        account.deposit(amount);
        accountDAO.updateBalance(account.getAccountNumber(), account.getBalance());
    }

    public void withdraw(Account account, double amount) throws SQLException {
        if (account instanceof Savings) {
            throw new UnsupportedOperationException("Savings accounts do not allow withdrawals.");
        }
        account.withdraw(amount);
        accountDAO.updateBalance(account.getAccountNumber(), account.getBalance());
    }

    public void payMonthlyInterest(Account account) throws SQLException {
        if (account instanceof InterestBearing bearingAccount) {
            bearingAccount.payInterest();
            accountDAO.updateBalance(account.getAccountNumber(), account.getBalance());
        } else {
            throw new UnsupportedOperationException("This account type does not earn interest.");
        }
    }
}

