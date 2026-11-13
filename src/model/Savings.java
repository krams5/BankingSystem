package model;

public class Savings extends Account implements InterestBearing {
    private static final double monthlyInterestRate = 0.0005;

    public Savings(double monthlyInterestRate, double minimumOpeningDeposit,int accountNumber,double balance,String branch) {
        super(accountNumber, balance, branch);
    }
}