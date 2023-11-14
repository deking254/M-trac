package com.example.m_track;
import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

public class MyDatabaseHelper extends SQLiteOpenHelper {
    private static final String DATABASE_NAME = "test.db";
    public static final String ACCOUNTS_TABLE = "accounts";
    public static final String ACCOUNTS_ID = "id";
    public static final String ACCOUNTS_MPESA = "mpesa";
    public static final String ACCOUNTS_CASH = "cash";
    public static final String ACCOUNTS_DATE = "date";
    public static final String PEOPLES_ID = "id";
    public static final String PEOPLES_FULL_NAME = "fullname";
    public static final String PEOPLES_TABLE = "people";
    public static final String TRANSACTIONS_TABLE = "transactions";
    public static final String TRANSACTIONS_ID = "id";
    public static final String TRANSACTIONS_PERSON = "person";
    public static final String TRANSACTIONS_DESCRIPTION = "description";
    public static final String TRANSACTIONS_DATE = "date";
    public static final String TRANSACTIONS_TYPE = "type";
    public static final String TRANSACTIONS_NATURE = "nature";
    public static final String TRANSACTIONS_AMOUNT = "amount";
    public static final String TRANSACTIONS_ACCOUNT = "account";
    public static final String PEOPLES_DOT_ID = PEOPLES_TABLE + "." + PEOPLES_ID;
    public static final String PEOPLES_DOT_FULL_NAME = PEOPLES_TABLE + "." + PEOPLES_FULL_NAME;
    public static final String TRANSACTIONS_DOT_AMOUNT = TRANSACTIONS_TABLE + "." + TRANSACTIONS_AMOUNT;
    public static final String TRANSACTIONS_DOT_PERSON = TRANSACTIONS_TABLE + "." + TRANSACTIONS_PERSON;

    private static final int DATABASE_VERSION = 1;

    public MyDatabaseHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        // Create your "accounts" table
        db.execSQL("CREATE TABLE " + ACCOUNTS_TABLE + " (" +
                ACCOUNTS_ID + " INTEGER NOT NULL PRIMARY KEY AUTOINCREMENT, " +
                ACCOUNTS_MPESA + " INTEGER, " + ACCOUNTS_CASH + " INTEGER, " +
                ACCOUNTS_DATE + " DATETIME NOT NULL DEFAULT (datetime('now')))");
        // Create your "people" table
        db.execSQL("CREATE TABLE " + PEOPLES_TABLE +   " (" + PEOPLES_ID +
                " INTEGER NOT NULL PRIMARY KEY AUTOINCREMENT UNIQUE, " +
                PEOPLES_FULL_NAME  + " TEXT NOT NULL UNIQUE)");
        // Create your "transactions" table
        db.execSQL("CREATE TABLE " + TRANSACTIONS_TABLE +  " (" +
                TRANSACTIONS_ID +  " INTEGER NOT NULL PRIMARY KEY AUTOINCREMENT UNIQUE, " +
                TRANSACTIONS_PERSON + " INTEGER NOT NULL, " + TRANSACTIONS_DESCRIPTION + " TEXT, " +
                TRANSACTIONS_DATE + " DATETIME NOT NULL DEFAULT (datetime('now')), " +
                TRANSACTIONS_TYPE + " TEXT NOT NULL, " + TRANSACTIONS_NATURE +
                " TEXT NOT NULL, " + TRANSACTIONS_AMOUNT +
                " INTEGER NOT NULL, " + TRANSACTIONS_ACCOUNT +
                " INTEGER NOT NULL, FOREIGN KEY (person) REFERENCES people(id) ON DELETE CASCADE, FOREIGN KEY (account) REFERENCES accounts(id) ON DELETE CASCADE)");
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        // Deletes all the tables and new ones created.
        db.rawQuery("DROP TABLE transactions", null, null);
        db.execSQL("DROP TABLE " + MyDatabaseHelper.ACCOUNTS_TABLE);
        db.execSQL("DROP TABLE " + MyDatabaseHelper.TRANSACTIONS_TABLE);
        db.execSQL("DROP TABLE " + MyDatabaseHelper.PEOPLES_TABLE);
        db.execSQL("CREATE TABLE " + ACCOUNTS_TABLE + " (" +
                ACCOUNTS_ID + " INTEGER NOT NULL PRIMARY KEY AUTOINCREMENT, " +
                ACCOUNTS_MPESA + " INTEGER, " + ACCOUNTS_CASH + " INTEGER, " +
                ACCOUNTS_DATE + " DATETIME NOT NULL DEFAULT (datetime('now')))");
        // Create your "people" table
        db.execSQL("CREATE TABLE " + PEOPLES_TABLE +   " (" + PEOPLES_ID +
                " INTEGER NOT NULL PRIMARY KEY AUTOINCREMENT UNIQUE, " +
                PEOPLES_FULL_NAME  + " TEXT NOT NULL UNIQUE)");
        // Create your "transactions" table
        db.execSQL("CREATE TABLE " + TRANSACTIONS_TABLE +  " (" +
                TRANSACTIONS_ID +  " INTEGER NOT NULL PRIMARY KEY AUTOINCREMENT UNIQUE, " +
                TRANSACTIONS_PERSON + " INTEGER NOT NULL, " + TRANSACTIONS_DESCRIPTION + " TEXT, " +
                TRANSACTIONS_DATE + " DATETIME NOT NULL DEFAULT (datetime('now')), " +
                TRANSACTIONS_TYPE + " TEXT NOT NULL, " + TRANSACTIONS_NATURE +
                " TEXT NOT NULL, " + TRANSACTIONS_AMOUNT +
                " INTEGER NOT NULL, " + TRANSACTIONS_ACCOUNT +
                " INTEGER NOT NULL, FOREIGN KEY (person) REFERENCES people(id) ON DELETE CASCADE, FOREIGN KEY (account) REFERENCES accounts(id) ON DELETE CASCADE)");

    }
}

