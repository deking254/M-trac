package com.example.m_track;

import androidx.appcompat.app.AppCompatActivity;
import android.content.ContentValues;
import android.content.Intent;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;
import com.google.android.material.textfield.TextInputEditText;

public class add_person extends AppCompatActivity {
//    Opens the add person activity.
MyDatabaseHelper dbHelper;
Bundle updates;
String ERROR_ADDING_PERSON = "Error could not add person";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        dbHelper = new MyDatabaseHelper(this);
        ContentValues person = new ContentValues();
        SQLiteDatabase db = dbHelper.getWritableDatabase();
        setContentView(R.layout.activity_addperson);
        Button personsave = findViewById(R.id.personsave);
        TextInputEditText fullname = findViewById(R.id.fullnameEdit);
        updates = getIntent().getExtras();
        if (updates != null) {
            if (getIntent().getExtras().getString(People.REQUEST_UPDATE_PERSON).matches(People.REQUEST_NEW_PERSON)) {
                fullname.setText(updates.getString(MyDatabaseHelper.PEOPLES_FULL_NAME));
            }
        }
        personsave.setOnClickListener(view -> {
//                Fires up when the save button is pressed in the add person activity.
            person.put(MyDatabaseHelper.PEOPLES_FULL_NAME, fullname.getText().toString());
            try {
                if (updates != null) {
                    if (updates.getString(People.REQUEST).matches(People.REQUEST_UPDATE_PERSON)) {
                        String[] condition = new String[]{String.valueOf(updates.getInt(PeopleAdapter.PERSON_ID))};
                        db.update(MyDatabaseHelper.PEOPLES_TABLE, person,  MyDatabaseHelper.PEOPLES_ID + " = ?", condition);
                        Intent main_activity_intent = new Intent(add_person.this, MainActivity.class);
                        startActivity(main_activity_intent);
                    }
                }else {
                    try {
                        db.insert(MyDatabaseHelper.PEOPLES_TABLE, MyDatabaseHelper.PEOPLES_FULL_NAME, person);
                    }catch (Exception e){
                        Toast.makeText(add_person.this, ERROR_ADDING_PERSON, Toast.LENGTH_LONG).show();
                    }
                    Intent ADD_PERSON_ACTIVITY = new Intent(add_person.this, MainActivity.class);
                    startActivity(ADD_PERSON_ACTIVITY);
                }
            }catch (Exception e){
                Toast.makeText(add_person.this, ERROR_ADDING_PERSON, Toast.LENGTH_LONG).show();
            }
        });
    }
}