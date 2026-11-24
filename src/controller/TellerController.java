package controller;

import dao.CustomerDAO;
import model.Company;
import model.Customer;
import model.Individual;
import java.sql.SQLException;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

    /** Teller use cases: sign in, register new customers and list the customers stored in the database. */
    public class TellerController {
        // Simplification for the assignment: a real system would keep staff accounts in the database.
        private static final String TELLER_ID = "teller";
        private static final String TELLER_PIN = "9999";

        private final CustomerDAO customerDAO = new CustomerDAO();

        public void signIn(String staffId, String pin) {
            if (staffId == null || staffId.isBlank() || pin == null || pin.isBlank()) {
                throw new IllegalArgumentException("Enter the staff ID and PIN");
            }
            if (!TELLER_ID.equalsIgnoreCase(staffId.trim()) || !TELLER_PIN.equals(pin)) {
                throw new SecurityException("Invalid staff ID or PIN");
            }
        }

        public void registerIndividual(String email, String contact, String address, String pin,
                                       String nationalId, String firstname, String surname,
                                       LocalDate dateOfBirth, String gender) throws SQLException {
            int contactNo = parseDigits(contact, "Contact number", 7, 9);
            String mail = checkCommon(email, contactNo, address, pin);
            int nid = parseDigits(nationalId, "National ID", 1, 9);
            require(firstname, "First name");
            require(surname, "Surname");
            if (dateOfBirth == null) {
                throw new IllegalArgumentException("Select a date of birth");
            }
            if (!dateOfBirth.isBefore(LocalDate.now())) {
                throw new IllegalArgumentException("Date of birth must be in the past");
            }
            if (gender == null) {
                throw new IllegalArgumentException("Select a gender");
            }
            Individual customer = new Individual(contactNo, mail, address.trim(), pin, nid,
                    firstname.trim(), surname.trim(), dateOfBirth, gender);
            customerDAO.saveCustomer(customer, pin);
            confirmSaved(contactNo);
        }

        public void registerCompany(String email, String contact, String address, String pin,
                                    String registrationId, String companyName) throws SQLException {
            int contactNo = parseDigits(contact, "Contact number", 7, 9);
            String mail = checkCommon(email, contactNo, address, pin);
            int regId = parseDigits(registrationId, "Registration ID", 1, 9);
            require(companyName, "Company name");
            Company customer = new Company(contactNo, mail, address.trim(), pin, regId, companyName.trim());
            customerDAO.saveCustomer(customer, pin);
            confirmSaved(contactNo);
        }

        /** Every customer read back from the database, as rows of: contact, email, type, name. */
        public List<String[]> customerRows() throws SQLException {
            List<String[]> rows = new ArrayList<>();
            for (Customer c : customerDAO.findAll()) {
                String type = c instanceof Individual ? "Individual" : "Company";
                String name = c instanceof Individual ind
                        ? ind.getFirstname() + " " + ind.getSurname()
                        : ((Company) c).getCompanyName();
                rows.add(new String[]{String.valueOf(c.getContact()), c.getEmail(), type, name});
            }
            return rows;
        }

        /** Success is only reported when the customer can be read back from the database. */
        private void confirmSaved(int contactNo) throws SQLException {
            if (customerDAO.findByContact(contactNo) == null) {
                throw new IllegalStateException("The customer could not be confirmed in the database. Nothing was registered.");
            }
        }

        /** Checks the details every customer needs and returns the email in lower case. */
        private String checkCommon(String email, int contactNo, String address, String pin) throws SQLException {
            require(email, "Email");
            String mail = email.trim().toLowerCase();
            int at = mail.indexOf('@');
            if (at < 1 || at == mail.length() - 1) {
                throw new IllegalArgumentException("Enter a valid email address");
            }
            require(address, "Address");
            if (pin == null || !pin.matches("[0-9]{4}")) {
                throw new IllegalArgumentException("PIN must be exactly 4 digits");
            }
            if (customerDAO.findByContact(contactNo) != null) {
                throw new IllegalArgumentException("A customer with that contact number already exists");
            }
            if (customerDAO.findByEmail(mail) != null) {
                throw new IllegalArgumentException("A customer with that email already exists");
            }
            return mail;
        }

        private void require(String value, String field) {
            if (value == null || value.isBlank()) {
                throw new IllegalArgumentException(field + " is required");
            }
        }

        private int parseDigits(String text, String field, int minDigits, int maxDigits) {
            String t = text == null ? "" : text.trim();
            if (!t.matches("[0-9]{" + minDigits + "," + maxDigits + "}")) {
                throw new IllegalArgumentException(field + " must be " + minDigits + " to " + maxDigits + " digits");
            }
            return Integer.parseInt(t);
        }
    }

