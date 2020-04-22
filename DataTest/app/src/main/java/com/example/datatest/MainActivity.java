package com.example.datatest;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.util.Log;

import java.util.List;

import Data.DataBaseHandlers;
import Model.Contact;

public class MainActivity extends AppCompatActivity {
    private static final String TAG = "MainActivity";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        DataBaseHandlers db= new DataBaseHandlers(this);

        //insert COntact

        Log.d(TAG, "insert: ");
        db.addContact(new Contact("Paul","687387382"));
        db.addContact(new Contact("steve","687453453"));
        db.addContact(new Contact("sHAINICE","687453453"));
        db.addContact(new Contact("Bella","687453453"));

        // Read Them Back
        Log.d(TAG, "Reading: ");
        List<Contact> contactList=db.getAllContact();
        for (Contact C : contactList){
            String log = "ID :" + C.getId()+", Name : " + C.getName() +", Number :"+ C.getPhoneNumber();
            Log.d(TAG, "cellule : "+ log);
        }

        Contact oneContact = db.getContact(2);
        oneContact.setName("Pauloooooo");
        oneContact.setPhoneNumber("000000");
        db.delete(oneContact);

        Log.d(TAG, "onCreate: " + " Nom " + oneContact.getName() + " phone " + oneContact.getPhoneNumber());
        int newContact = db.upDateContact(oneContact);
        Log.d(TAG, "onCreate: "+"updated row " + String.valueOf(newContact) + " Nom " + oneContact.getName() + " phone " + oneContact.getPhoneNumber());

    }



}
