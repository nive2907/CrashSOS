package com.example.roadsaftey;

public class Contact {

    //private variables
    int _id;

    String _phone_number;

    // Empty constructor
    public Contact(){

    }
    // constructor


    public Contact(int _id,String _phone_number){

        this._id= _id;
        this._phone_number = _phone_number;
    }


    // getting ID
    public int getID(){
        return this._id;
    }

    // setting id
    public void setID(int id){
        this._id = id;
    }

    // getting name


    // getting phone number
    public String getPhoneNumber(){
        return this._phone_number;
    }

    // setting phone number
    public void setPhoneNumber(String phone_number){
        this._phone_number = phone_number;
    }
}

