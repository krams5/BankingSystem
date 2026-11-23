package controller;

import dao.AccountDAO;
import model.*;

import java.sql.SQLException;

    public class AccountController {
        private AccountDAO accountDAO = new AccountDAO();

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


