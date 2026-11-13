package model;

public class Customer {
private int contact;
private String email;
private String address;


    public int getContact() {
        return contact;
    }

    public void setContact(int contact) {
        this.contact = contact;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public Customer(int contact, String email, String address ){
    this.contact= contact;
    this.email= email;
    this.address=address;


}

}

