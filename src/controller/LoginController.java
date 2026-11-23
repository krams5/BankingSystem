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

        public Customer login(String contactInput, String pinInput) throws Exception {
            if (contactInput == null || contactInput.isBlank() || pinInput == null || pinInput.isBlank()) {
                throw new IllegalArgumentException("Contact number and PIN are required.");
            }

            int contact;
            try {
                contact = Integer.parseInt(contactInput.trim());
            } catch (NumberFormatException e) {
                throw new IllegalArgumentException("Contact number must be digits only.");
            }

            Customer customer = customerDAO.findByContact(contact);
            if (customer == null || !customer.verifyPin(pinInput.trim())) {
                throw new IllegalArgumentException("Invalid contact number or PIN.");
            }

            // Load customer's accounts from DB into the Customer model object
            List<Account> savedAccounts = accountDAO.getAccountsByCustomer(contact);
            for (Account acc : savedAccounts) {
                customer.openAccount(acc);
            }

            return customer;
        }

        public void registerCustomer(Customer customer, String pin) throws SQLException {
            customerDAO.saveCustomer(customer, pin);
        }
    }


