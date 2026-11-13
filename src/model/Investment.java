package model;

public class Investment extends Account implements InterestBearing {
    private static final double monthlyInterestRate = 0.05;
    private static final double minimumOpeningDeposit = 500.00;

    @Override
    public double calculateInterest() {
        double interest = getBalance() * monthlyInterestRate;
        return Math.round(interest * 100.00)/100.00;
    }


    @Override
    public void payInterest(){
        new balance= deposit(balance) + double interest;
    }
    public Investment(double monthlyInterestRate, double minimumOpeningDeposit, int accountNumber, double balance, String branch) {
        super(accountNumber, balance, branch);

        if (balance < minimumOpeningDeposit) {
            throw new IllegalArgumentException(
                    "Investment account requires an initial deposit of at least BWP500.00"
            );


        }
    }



    }
