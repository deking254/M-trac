package com.example.m_track;

import androidx.appcompat.app.AppCompatActivity;

import android.content.ContentValues;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;
import com.google.android.material.textfield.TextInputEditText;

public class add_account extends AppCompatActivity {
//    This function displays the page to add the cash and m-pesa amounts.
ContentValues starting_amounts;
String MPESA = "mpesa";
String CASH = "cash";
String ERROR_ACCOUNTS = "The account could not be created";
String ERROR_GETTING_PEOPLE = "The people could not be loaded, Please Ensure that you have added a person";

MyDatabaseHelper dbHelper;
SQLiteDatabase db;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
//        fires up when the app activity is loaded
        super.onCreate(savedInstanceState);
        dbHelper = new MyDatabaseHelper(this);
        SQLiteDatabase db = dbHelper.getWritableDatabase();
        Cursor people = getPeople();
        setContentView(R.layout.activity_addaccount);
        Button save_starting = findViewById(R.id.starting_save);
        TextInputEditText mpesa_Input_Edit_TExt = findViewById(R.id.mpesaEdit);
        TextInputEditText cash_Input_Edit_TExt = findViewById(R.id.cashEdit);
        save_starting.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
            //This function fires up the save button is and the account
                // details are added to the database
                starting_amounts = new ContentValues();
                starting_amounts.put(MPESA, Integer.parseInt(mpesa_Input_Edit_TExt.getText().toString()));
                starting_amounts.put(CASH, Integer.parseInt(cash_Input_Edit_TExt.getText().toString()));
                try {
                    db.insert(MyDatabaseHelper.ACCOUNTS_TABLE, MPESA, starting_amounts);
                    finish();
                    if (people != null) {
                        if (people.getCount() <= 0) {
                            Intent person_add = new Intent(add_account.this, addperson.class);
                            startActivity(person_add);
                        }
                    }else {
                        Intent person_add = new Intent(add_account.this, addperson.class);
                        startActivity(person_add);
                    }
                    }catch (Exception e){
                    Toast.makeText(add_account.this, ERROR_ACCOUNTS, Toast.LENGTH_SHORT);
                }
            }
        });

    }
    private Cursor getPeople() {
//        This function get the values of the people table. It returns null if the data could not be extracted.
        try {
            Cursor people = db.query(MyDatabaseHelper.PEOPLES_TABLE, null, null, null, null, null,null);
            return people;
        }catch (Exception e){
            Toast.makeText(this, ERROR_GETTING_PEOPLE, Toast.LENGTH_LONG).show();
            return null;
        }
    }
}