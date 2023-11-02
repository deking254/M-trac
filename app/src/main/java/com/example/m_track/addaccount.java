package com.example.m_track;

import androidx.appcompat.app.AppCompatActivity;

import android.content.ContentValues;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.google.android.material.textfield.TextInputEditText;

public class addaccount extends AppCompatActivity {
ContentValues starting;
MyDatabaseHelper dbHelper;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        dbHelper = new MyDatabaseHelper(this);
        SQLiteDatabase db = dbHelper.getWritableDatabase();
        setContentView(R.layout.activity_addaccount);
        Button save_starting = findViewById(R.id.starting_save);
        TextInputEditText mpesa = findViewById(R.id.mpesaEdit);
        TextInputEditText cash = findViewById(R.id.cashEdit);
        save_starting.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                starting = new ContentValues();
                starting.put("mpesa", Integer.parseInt(mpesa.getText().toString()));
                starting.put("cash", Integer.parseInt(cash.getText().toString()));
                try {
                    db.insert("accounts", "mpesa", starting);
                    finish();
                }catch (Exception e){
                    Toast.makeText(addaccount.this, "Error", Toast.LENGTH_SHORT);
                }
            }
        });
    }
}