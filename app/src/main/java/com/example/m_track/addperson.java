package com.example.m_track;

import androidx.appcompat.app.AppCompatActivity;

import android.content.ContentValues;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.google.android.material.textfield.TextInputEditText;

public class addperson extends AppCompatActivity {
MyDatabaseHelper dbHelper;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        dbHelper = new MyDatabaseHelper(this);
        ContentValues person = new ContentValues();
        SQLiteDatabase db = dbHelper.getWritableDatabase();
        setContentView(R.layout.activity_addperson);
        Button personsave = findViewById(R.id.personsave);
        TextInputEditText fullname = findViewById(R.id.fullnameEdit);
        personsave.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                person.put("fullname", fullname.getText().toString());
                try {
                    db.insert("people", "fullname", person);
                    finish();
                }catch (Exception e){
                    Toast.makeText(addperson.this, "Error", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }
}