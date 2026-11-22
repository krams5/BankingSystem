package model;

public class Cheque extends Account {
    private String companyName;
    private String companyAddress;

    public Cheque(int accountNumber, double balance, String branch, String companyName, String companyAddress) {
        super(accountNumber, balance, branch);
        this.companyName = companyName;
        this.companyAddress = companyAddress;

    }

    public void creditSalary(double salary) {
        if (salary > 0) {
            deposit(salary);
            System.out.println("Salary:P" + salary + "Credited From:" + companyName + companyAddress);
        } else {
            System.out.println("Invalid salary amount");
        }
    }

    public String getCompanyName() {
        return companyName;
    }

    public String getCompanyAddress() {
        return companyAddress;
    }

    @Override
    public double getBalance() {
        System.out.println("Balance: P" + super.getBalance());
        return super.getBalance();
    }

    public void withdraw(double amount) {
    }

    @Override
    protected void reduceBalance(double amount) {
        if (amount <= 0 || amount > getBalance()) {
            throw new IllegalArgumentException("Invalid withdrawal amount");
        }
        reduceBalance(amount);
    }
}
