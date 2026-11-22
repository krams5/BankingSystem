package model;

public class Savings extends Account implements InterestBearing {
    private static final double monthlyInterestRate = 0.0005;

    public Savings(int accountNumber, double balance, String branch) {
        super(accountNumber, balance, branch);
    }

    @Override
    public double calculateInterest() {
        double interest = getBalance() * monthlyInterestRate;
        return Math.round(interest * 100.00) / 100.00;
    }


    @Override
    public void payInterest() {
        double interest = calculateInterest();
        deposit(interest);
    }
    @Override
    public double getBalance() {
        System.out.println("Balance: P" + super.getBalance());
        return super.getBalance();
    }
    public void deposit(double amount){

        if(amount > 0){
            super.deposit(amount);
            System.out.println("Deposit successful");}
            else{
                throw new IllegalArgumentException(
                        "Deposit not successful:deposit must be more than zero");
            }
        }
    }
