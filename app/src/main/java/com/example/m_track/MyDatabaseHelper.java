package com.example.m_track;
import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

public class MyDatabaseHelper extends SQLiteOpenHelper {
    private static final String DATABASE_NAME = "test.db";
    private static final int DATABASE_VERSION = 1;

    public MyDatabaseHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        // Create your "accounts" table
        db.execSQL("CREATE TABLE accounts (id INTEGER NOT NULL PRIMARY KEY AUTOINCREMENT, mpesa INTEGER, cash INTEGER, date DATETIME NOT NULL DEFAULT (datetime('now')) )");
        db.execSQL("CREATE TABLE people (id integer NOT NULL PRIMARY KEY AUTOINCREMENT UNIQUE,fullname text NOT NULL UNIQUE)");
        db.execSQL("CREATE TABLE transactions (id integer NOT NULL PRIMARY KEY AUTOINCREMENT UNIQUE,person integer NOT NULL,description text,date DATETIME NOT NULL DEFAULT (datetime('now')),type text NOT NULL,nature text NOT NULL, amount integer NOT NULL,account integer NOT NULL, FOREIGN KEY (person) REFERENCES people(id) ON DELETE CASCADE, FOREIGN KEY (account) REFERENCES accounts(id) ON DELETE CASCADE)");
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        // Handle database upgrades here
    }
}

