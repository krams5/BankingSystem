package model;

import java.util.ArrayList;
import java.util.List;

public class Customer {
    private int contact;
    private String email;
    private String address;
    private String pin;
    private List<Account> accounts = new ArrayList<>();

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


    public Customer(int contact, String email, String address, String pin) {
        this.contact = contact;
        this.email = email;
        this.address = address;
        this.pin = pin;

    }
        private void setPin (String Pin){
            if (Pin == null || !Pin.matches("[0-9]{4}")){
                throw new IllegalArgumentException("PIN must be exactly 4 digits");
            }
           this.pin=Pin;
        }
        public boolean verifyPin(String attempt){
        return pin != null && pin.equals(attempt);
    }

public void openAccount(Account account) {
    accounts.add(account);
}

public List<Account> getAccounts() {
    return new ArrayList<>(accounts);   // a copy, so outsiders can't change your list
}
}

