package com.example.studentrv;



//This is a Model Class
public class Student {

    private String id;
    private String name;

    private String lastname;

    private String dateOfBirth;



    // Compulsory Empty constructor For Firebase
    public Student() {
    }

    //Constructor : This Constructor is called in MainActivity
    public Student(String id, String name,String lastname, String dateOfBirth) {
        this.id = id;
        this.name = name;
        this.lastname = lastname;
        this.dateOfBirth = dateOfBirth;

    }

    //Getter and Setter : This Getter and Setter are used in AdapterClass in onBindViewHolder()
    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getLastname() {
        return lastname;
    }

    public void setLastname(String lastname) {
        this.lastname = lastname;
    }

    public String getDateOfBirth() {
        return dateOfBirth;
    }

    public void setDateOfBirth(String dateOfBirth) {
        this.dateOfBirth = dateOfBirth;
    }


}
