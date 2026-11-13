package model;

public class Cheque extends Account {
    private String companyName;
    private String companyAddress;

    public Cheque(int accountNumber, double balance, String branch,String companyName,String companyAddress) {
        super(accountNumber,balance,branch);
        this.companyName = companyName;
        this.companyAddress = companyAddress;

    }
}
