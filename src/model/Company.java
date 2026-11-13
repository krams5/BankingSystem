package model;

public class Company extends Customer{
private int registrationID;
private String companyName;
private String Address;

public Company(int contact,String email, String address,int registrationID,String companyName){
super(contact, email, address);
this.registrationID= registrationID;
this.companyName= companyName;

}

    public int getRegistrationID() {
        return registrationID;
    }

    public void setRegistrationID(int registrationID) {
        this.registrationID = registrationID;
    }

    public String getCompanyName() {
        return companyName;
    }

    public void setCompanyName(String companyName) {
        this.companyName = companyName;
    }

    @Override
    public String getAddress() {
        return Address;
    }

    @Override
    public void setAddress(String address) {
        Address = address;
    }
}
