import model.Account;
import model.Individual;
import model.Investment;
import model.Savings;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;


    public class Main {
        public static void main(String[] args) {
            List<Savings> Accounts = new ArrayList<>();

            Individual c = new Individual(71234567, "a@b.com", "Gaborone", "1234",
                    123456789, "Thato", "Moeti", LocalDate.of(2000, 5, 1), "M");
            c.openAccount(new Savings(1001, 0.0, "Maun"));
            c.openAccount(new Investment(1002, 10.0, "Maun"));

            System.out.println("Accounts held: " + c.getAccounts().size());   // expect 2

            for (Account a : c.getAccounts()) {
                System.out.println(a.getClass().getSimpleName() + " " + a.getAccountNumber()
                        + ": " + a.getBalance());
            }

        }
    }

