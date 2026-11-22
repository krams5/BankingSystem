package model;
import java.util.ArrayList;
import java.util.List;
import java.time.LocalDate;


public abstract class Account {
    private int accountNumber;
    private double balance;
    private String branch;

    public int getAccountNumber() {
        return accountNumber;
    }

    public void setAccountNumber(int accountNumber) {
        this.accountNumber = accountNumber;
    }

    public double getBalance() {
        return balance;
    }

    public void setBalance(double balance) {
        this.balance = balance;
    }

    public String getBranch() {
        return branch;
    }

    public void setBranch(String branch) {
        this.branch = branch;
    }

    public Account(int accountNumber, double balance, String branch) {
        this.accountNumber = accountNumber;
        this.balance = balance;
        this.branch = branch;
    }


    public void deposit(double amount) {
        if (amount <= 0) {
            throw new IllegalArgumentException("Deposit must be greater than zero");
        }
        balance += amount;
    }

    protected void increaseBalance(double amount) {
        balance += amount;
    }

    public void withdraw(double amount) {
    }

    protected void reduceBalance(double amount) {
        balance -= amount;
    }

}
