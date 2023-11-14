package com.example.m_track;

import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.util.Log;
import android.widget.TextView;

public class Totals extends AppCompatActivity {
MyDatabaseHelper dbHelper;
SQLiteDatabase db;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_totals);
        dbHelper = new MyDatabaseHelper(this);
        db = dbHelper.getReadableDatabase();
        totals_calculator();
    }
    public void totals_calculator() {
        Cursor accounts = null;
        try {
            accounts = db.rawQuery("SELECT mpesa, cash FROM accounts ORDER BY id DESC LIMIT 1", null);
        } catch (Exception e) {
            return;
        }
        TextView cash = (TextView) findViewById(R.id.cashvalue);
        TextView mpesa = (TextView) findViewById(R.id.mpesavalue);
        Cursor mpesa_txs = db.rawQuery("SELECT SUM(amount) AS sum FROM " + MyDatabaseHelper.TRANSACTIONS_TABLE + " WHERE nature=" + "'" + TransactionParser.NATURE_VALUE_MPESA + "'", null);
        Cursor cash_txs = db.rawQuery("SELECT SUM(amount) AS sum FROM " + MyDatabaseHelper.TRANSACTIONS_TABLE + " WHERE nature=" + "'" +  MainActivity.NATURE_VALUE_CASH+ "'", null);
        if (accounts.moveToLast()){
            if (mpesa_txs.moveToLast()) {
                @SuppressLint("Range") int total_mpesa = accounts.getInt(accounts.getColumnIndex(MyDatabaseHelper.ACCOUNTS_MPESA));

                total_mpesa += mpesa_txs.getInt(0);
                mpesa.setText(String.valueOf(total_mpesa));
            }
        if (cash_txs.moveToLast()) {
            @SuppressLint("Range") int total_cash = accounts.getInt(accounts.getColumnIndex(MyDatabaseHelper.ACCOUNTS_CASH));
            total_cash += cash_txs.getInt(0);
            cash.setText(String.valueOf(total_cash));
        }
    }
    }
}