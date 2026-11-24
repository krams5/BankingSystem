package controller;

import dao.AccountDAO;
import dao.CustomerDAO;
import model.Account;
import model.Customer;

import java.sql.SQLException;
import java.util.List;

public class LoginController {
    private CustomerDAO customerDAO = new CustomerDAO();
    private AccountDAO accountDAO = new AccountDAO();

    public Customer login(String emailInput, String pinInput) throws Exception {
        if (emailInput == null || emailInput.isBlank() || pinInput == null || pinInput.isBlank()) {
            throw new IllegalArgumentException("Email and PIN are required.");
        }

        Customer customer = customerDAO.findByEmail(emailInput.trim());
        if (customer == null || !customer.verifyPin(pinInput.trim())) {
            throw new IllegalArgumentException("Invalid email or PIN.");
        }

        List<Account> savedAccounts = accountDAO.getAccountsByCustomer(customer.getContact());
        for (Account acc : savedAccounts) {
            customer.openAccount(acc);
        }

        return customer;
    }

    public void registerCustomer(Customer customer, String pin) throws SQLException {
        customerDAO.saveCustomer(customer, pin);
    }
}

