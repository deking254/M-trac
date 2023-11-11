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

public class addperson extends AppCompatActivity {
MyDatabaseHelper dbHelper;
Bundle updates;
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
            if (getIntent().getExtras().getString("request").matches("update")) {
                fullname.setText(updates.getString("fullname"));
            }
        }
        personsave.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                person.put("fullname", fullname.getText().toString());
                try {
                    if (updates != null) {
                        if (updates.getString("request").matches("update")) {
                            String[] condition = new String[]{String.valueOf(updates.getInt("person_id"))};
                            db.update("people", person, "id = ?", condition);
                            Intent tx = new Intent(addperson.this, MainActivity.class);
                            startActivity(tx);
                        }
                    }else {
                        try {
                            db.insert("people", "fullname", person);
                        }catch (Exception e){
                            Toast.makeText(addperson.this, "Error inserting person", Toast.LENGTH_SHORT).show();
                        }
                        Intent tx = new Intent(addperson.this, MainActivity.class);
                        startActivity(tx);
                    }
                }catch (Exception e){
                    Toast.makeText(addperson.this, "Error", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }
}