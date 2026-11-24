import dao.AccountDAO;
import dao.CustomerDAO;
import dao.DatabaseConnection;
import model.*;
import java.time.LocalDate;

public class SeedData {
    public static void main(String[] args) throws Exception {
        DatabaseConnection.initializeDatabase();
        CustomerDAO customers = new CustomerDAO();
        AccountDAO accounts = new AccountDAO();

        String[] first = {"Thato", "Neo", "Kabo", "Lesego", "Mpho", "Tumelo", "Boitumelo", "Onkabetse"};
        String[] last = {"Moeti", "Seretse", "Kgosi", "Molefe", "Dube", "Mokgosi", "Phiri", "Ramotswa"};

        for (int i = 0; i < 8; i++) {
            int contact = 71234567 + i;                       // Thato = 71234567
            Individual c = new Individual(contact, first[i].toLowerCase() + "@bank.bw", "Gaborone", "1234",
                    123456789 + i, first[i], last[i], LocalDate.of(1995 + i % 5, 1 + i, 10),
                    i % 2 == 0 ? "M" : "F");
            save(customers, accounts, c, contact, i);
        }
        for (int i = 1; i <= 2; i++) {
            int contact = 72000000 + i;
            Company c = new Company(contact, "company" + i + "@bank.bw", "Francistown", "1234",
                    5000 + i, "Acme Trading " + i);
            save(customers, accounts, c, contact, 7 + i);
        }
        System.out.println("Sample data ready. All sample customers use the PIN 1234.");
    }

    private static void save(CustomerDAO customers, AccountDAO accounts, Customer c, int contact, int n)
            throws Exception {
        if (customers.findByContact(contact) == null) {
            customers.saveCustomer(c, "1234");
        }
        if (accounts.getAccountsByCustomer(contact).isEmpty()) {
            accounts.saveAccount(new Savings(1001 + n * 10, 1000.0 + n * 100, "Main"), contact);
            if (n % 2 == 0) {
                accounts.saveAccount(new Investment(1002 + n * 10, 1000.0, "Main"), contact);
            }
            if (c instanceof Individual && n % 3 == 0) {
                accounts.saveAccount(new Cheque(1003 + n * 10, 500.0, "Main", "Acme Ltd", "Plot 1, Gaborone"), contact);
            }
        }
        System.out.println(contact + "  " + c.getEmail());
    }
}

