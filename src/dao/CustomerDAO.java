package dao;

import model.Company;
import model.Customer;
import model.Individual;

import java.sql.*;
import java.time.LocalDate;

    public class CustomerDAO {

        public void saveCustomer(Customer customer, String pin) throws SQLException {
            String sql = """
            INSERT INTO customers (contact, email, address, pin, customer_type,
            national_id, firstname, surname, dob, gender, registration_id, company_name)
            VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?)
        """;

            try (Connection conn = DatabaseConnection.getConnection();
                 PreparedStatement pstmt = conn.prepareStatement(sql)) {

                pstmt.setInt(1, customer.getContact());
                pstmt.setString(2, customer.getEmail());
                pstmt.setString(3, customer.getAddress());
                pstmt.setString(4, pin);

                if (customer instanceof Individual ind) {
                    pstmt.setString(5, "INDIVIDUAL");
                    pstmt.setInt(6, ind.getNationalID());
                    pstmt.setString(7, ind.getFirstname());
                    pstmt.setString(8, ind.getSurname());
                    pstmt.setDate(9, Date.valueOf(ind.getDateOfBirth()));
                    pstmt.setString(10, ind.getGender());
                    pstmt.setNull(11, Types.INTEGER);
                    pstmt.setNull(12, Types.VARCHAR);
                } else if (customer instanceof Company comp) {
                    pstmt.setString(5, "COMPANY");
                    pstmt.setNull(6, Types.INTEGER);
                    pstmt.setNull(7, Types.VARCHAR);
                    pstmt.setNull(8, Types.VARCHAR);
                    pstmt.setNull(9, Types.DATE);
                    pstmt.setNull(10, Types.VARCHAR);
                    pstmt.setInt(11, comp.getRegistrationID());
                    pstmt.setString(12, comp.getCompanyName());
                }
                pstmt.executeUpdate();
            }
        }

        public Customer findByContact(int contact) throws SQLException {
            String sql = "SELECT * FROM customers WHERE contact = ?";

            try (Connection conn = DatabaseConnection.getConnection();
                 PreparedStatement pstmt = conn.prepareStatement(sql)) {

                pstmt.setInt(1, contact);
                ResultSet rs = pstmt.executeQuery();

                if (rs.next()) {
                    String type = rs.getString("customer_type");
                    String email = rs.getString("email");
                    String address = rs.getString("address");
                    String pin = rs.getString("pin");

                    if ("INDIVIDUAL".equals(type)) {
                        LocalDate dob = rs.getDate("dob").toLocalDate();
                        return new Individual(
                                contact, email, address, pin,
                                rs.getInt("national_id"),
                                rs.getString("firstname"),
                                rs.getString("surname"),
                                dob,
                                rs.getString("gender")
                        );
                    } else {
                        return new Company(
                                contact, email, address, pin,
                                rs.getInt("registration_id"),
                                rs.getString("company_name")
                        );
                    }
                }
            }
            return null;
        }
    }


