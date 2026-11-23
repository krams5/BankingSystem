package test;

import model.*;

import java.time.LocalDate;

    public class IntergrationTest {
        public static void main(String[] args) {
            // 1. Test Model & Polymorphism
            Customer customer = new Individual(
                    71234567, "a@b.com", "Gaborone", "1234",
                    123456789, "Thato", "Moeti", LocalDate.of(2000, 5, 1), "M"
            );
            Account savings = new Savings(1001, 500.0, "Main");
            customer.openAccount(savings);
            System.out.println("Model Test Passed - Accounts count: " + customer.getAccounts().size());


        }
    }


