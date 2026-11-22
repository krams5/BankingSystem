package model;

import java.time.LocalDate;

public class Individual extends Customer{
private int nationalID;
private String firstname;
private String surname;
private LocalDate dateOfBirth;
private String gender;


    public Individual(int contact, String email,String address ,String pin, int nationalID, String firstname, String surname, LocalDate dateOfBirth, String gender) {
        super(contact, email, address, pin);
        this.nationalID = nationalID;
        this.firstname = firstname; 
        this.surname = surname;
        this.dateOfBirth = dateOfBirth;
        this.gender = gender;
 
    }

    public int getNationalID() {
        return nationalID;
    }

    public void setNationalID(int nationalID) {
        this.nationalID = nationalID;
    }

    public String getFirstname() {
        return firstname;
    }

    public void setFirstname(String firstname) {
        this.firstname = firstname;
    }

    public String getSurname() {
        return surname;
    }

    public void setSurname(String surname) {
        this.surname = surname;
    }

    public LocalDate getDateOfBirth() {
        return dateOfBirth;
    }

    public void setDateOfBirth(LocalDate dateOfBirth) {
        this.dateOfBirth = dateOfBirth;
    }

    public String getGender() {
        return gender;
    }

    public void setGender(String gender) {
        this.gender = gender;
    }
}
