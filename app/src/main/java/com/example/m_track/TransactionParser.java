package com.example.m_track;

import android.annotation.SuppressLint;
import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.widget.Toast;

import java.util.regex.*;

public class TransactionParser {
    public static final String ERROR_TRANSACTION_INSERT = "Error inserting the message";
    public static final String NATURE_VALUE_MPESA = "Mpesa";
    public static final String OUT_TYPE_VALUE_ = "Debt";
    public static final String IN_TYPE_VALUE_ = "Income";
    public static final String CURRENCY = "Ksh";
//
    public static final String[] AGENT_SIM_CASH_FROM_CUSTOMER_KEY_WORDS= {"from", "Take"};
    public static final String[] AGENT_SIM_CASH_TO_CUSTOMER_KEY_WORDS= {"to", "Give"};
    public static final String[] PERSONAL_SIM_MPESA_RECEIVED_FROM_PERSON_KEY_WORDS= {"from", "received"};
    public static final String[] PERSONAL_SIM_MPESA_SENT_TO_PERSON_KEY_WORDS = {"to", "sent to"};
    public static final String[] PERSONAL_SIM_MPESA_BOUGHT_AIRTIME_FOR_PERSON_KEY_WORDS = {"of", "bought"};
    public static final String[] PERSONAL_SIM_MPESA_PAID_TO_TILL_KEY_WORDS= {"to", "paid to"};

    @SuppressLint("Range")
    public static void take(String transactionMessage, Context context) {
        // Define the regular expressions for date and amount
        ContentValues tx_data = new ContentValues();
        String firstWordRegex = "\\w+";
        String dateRegex = "\\d{1,2}/\\d{1,2}/\\d{1,2}";
        String amountRegex = CURRENCY+ "[\\d,]+\\.\\d{2}";
        String personRegex = "\\b"+ AGENT_SIM_CASH_FROM_CUSTOMER_KEY_WORDS[0] +"\\s+([^\\s]+)\\s+([^\\s]+)";
        // Create Pattern objects
        Pattern datePattern = Pattern.compile(dateRegex);
        Pattern amountPattern = Pattern.compile(amountRegex);
        Pattern personPattern = Pattern.compile(personRegex);
        // Create Matcher objects
        Matcher dateMatcher = datePattern.matcher(transactionMessage);
        Matcher amountMatcher = amountPattern.matcher(transactionMessage);
        Matcher personMatcher = personPattern.matcher(transactionMessage);
        // Find and print the date
        if (dateMatcher.find()) {
            String date = dateMatcher.group();

        }
        if (personMatcher.find()){
            String person = personMatcher.group().replaceAll(AGENT_SIM_CASH_FROM_CUSTOMER_KEY_WORDS[0], "");
            tx_data.put(MyDatabaseHelper.TRANSACTIONS_DESCRIPTION, person);
        }
        // Find and print the amount
        if (amountMatcher.find()) {
            String amount = amountMatcher.group().replaceAll(CURRENCY + "|,", "");
            Float fi = Float.parseFloat(amount);
            Integer amt = fi.intValue();
            tx_data.put(MyDatabaseHelper.TRANSACTIONS_AMOUNT, amt * -1);

        }
        tx_data.put(MyDatabaseHelper.TRANSACTIONS_NATURE, NATURE_VALUE_MPESA);
        tx_data.put(MyDatabaseHelper.TRANSACTIONS_TYPE, OUT_TYPE_VALUE_);
        MyDatabaseHelper dbHelper = new MyDatabaseHelper(context);
        SQLiteDatabase db = dbHelper.getWritableDatabase();
        try {
            Cursor people = db.query(MyDatabaseHelper.PEOPLES_TABLE, null, null, null, null, null, null);
            Cursor accounts = db.query(MyDatabaseHelper.ACCOUNTS_TABLE, null, null, null, null, null, null);
            if (people.moveToFirst()){
                tx_data.put(MyDatabaseHelper.TRANSACTIONS_PERSON, people.getInt(people.getColumnIndex(MyDatabaseHelper.PEOPLES_ID)));
            }
            if (accounts.moveToLast()){
                tx_data.put(MyDatabaseHelper.TRANSACTIONS_ACCOUNT, accounts.getInt(accounts.getColumnIndex(MyDatabaseHelper.ACCOUNTS_ID)));
            }
            db.insert(MyDatabaseHelper.TRANSACTIONS_TABLE, MyDatabaseHelper.TRANSACTIONS_PERSON, tx_data);
        }catch (Exception e){
            Toast.makeText(context, ERROR_TRANSACTION_INSERT, Toast.LENGTH_SHORT).show();
        }
    }
    @SuppressLint("Range")
    public static void give(String transactionMessage, Context context) {
        // Define the regular expressions for date and amount
        ContentValues tx_data = new ContentValues();
        String firstWordRegex = "\\w+";
        String dateRegex = "\\d{1,2}/\\d{1,2}/\\d{1,2}";
        String amountRegex = CURRENCY + "[\\d,]+\\.\\d{2}";
        String personRegex = "\\b" + AGENT_SIM_CASH_TO_CUSTOMER_KEY_WORDS[0] + "\\s+([^\\s]+)\\s+([^\\s]+)";
        // Create Pattern objects
        Pattern datePattern = Pattern.compile(dateRegex);
        Pattern amountPattern = Pattern.compile(amountRegex);
        Pattern personPattern = Pattern.compile(personRegex);
        // Create Matcher objects
        Matcher dateMatcher = datePattern.matcher(transactionMessage);
        Matcher amountMatcher = amountPattern.matcher(transactionMessage);
        Matcher personMatcher = personPattern.matcher(transactionMessage);
        // Find and print the date
        if (dateMatcher.find()) {
            String date = dateMatcher.group();

        }
        if (personMatcher.find()){
            String person = personMatcher.group().replaceAll(AGENT_SIM_CASH_TO_CUSTOMER_KEY_WORDS[0], "");
            tx_data.put(MyDatabaseHelper.TRANSACTIONS_DESCRIPTION, person);
        }
        // Find and print the amount
        if (amountMatcher.find()) {
            String amount = amountMatcher.group().replaceAll(CURRENCY + "|,", "");
            Float fi = Float.parseFloat(amount);
            Integer amt = fi.intValue();
            tx_data.put(MyDatabaseHelper.TRANSACTIONS_AMOUNT, amt);

        }
        tx_data.put(MyDatabaseHelper.TRANSACTIONS_NATURE, NATURE_VALUE_MPESA);
        tx_data.put(MyDatabaseHelper.TRANSACTIONS_TYPE, IN_TYPE_VALUE_);
        MyDatabaseHelper dbHelper = new MyDatabaseHelper(context);
        SQLiteDatabase db = dbHelper.getWritableDatabase();
        try {
            Cursor people = db.query(MyDatabaseHelper.PEOPLES_TABLE, null, null, null, null, null, null);
            Cursor accounts = db.query(MyDatabaseHelper.ACCOUNTS_TABLE, null, null, null, null, null, null);
            if (people.moveToFirst()){
                tx_data.put(MyDatabaseHelper.TRANSACTIONS_PERSON, people.getInt(people.getColumnIndex(MyDatabaseHelper.PEOPLES_ID)));
            }
            if (accounts.moveToLast()){
                tx_data.put(MyDatabaseHelper.TRANSACTIONS_ACCOUNT, accounts.getInt(accounts.getColumnIndex(MyDatabaseHelper.ACCOUNTS_ID)));
            }
            db.insert(MyDatabaseHelper.TRANSACTIONS_TABLE, MyDatabaseHelper.TRANSACTIONS_PERSON, tx_data);
        }catch (Exception e){
            Toast.makeText(context, ERROR_TRANSACTION_INSERT, Toast.LENGTH_SHORT).show();
        }
    }
    @SuppressLint("Range")
    public static void received(String transactionMessage, Context context) {
        // Define the regular expressions for date and amount
        ContentValues tx_data = new ContentValues();
        String firstWordRegex = "\\w+";
        String dateRegex = "\\d{1,2}/\\d{1,2}/\\d{1,2}";
        String amountRegex = CURRENCY + "[\\d,]+\\.\\d{2}";
        String personRegex = "\\b" + PERSONAL_SIM_MPESA_RECEIVED_FROM_PERSON_KEY_WORDS[0] + "\\s+([^\\s]+)\\s+([^\\s]+)";
        // Create Pattern objects
        Pattern datePattern = Pattern.compile(dateRegex);
        Pattern amountPattern = Pattern.compile(amountRegex);
        Pattern personPattern = Pattern.compile(personRegex);
        // Create Matcher objects
        Matcher dateMatcher = datePattern.matcher(transactionMessage);
        Matcher amountMatcher = amountPattern.matcher(transactionMessage);
        Matcher personMatcher = personPattern.matcher(transactionMessage);
        // Find and print the date
        if (dateMatcher.find()) {
            String date = dateMatcher.group();

        }
        if (personMatcher.find()){
            String person = personMatcher.group().replaceAll(PERSONAL_SIM_MPESA_RECEIVED_FROM_PERSON_KEY_WORDS[0], "");
            tx_data.put(MyDatabaseHelper.TRANSACTIONS_DESCRIPTION, person);
        }
        // Find and print the amount
        if (amountMatcher.find()) {
            String amount = amountMatcher.group().replaceAll(CURRENCY + "|,", "");
            Float fi = Float.parseFloat(amount);
            Integer amt = fi.intValue();
            tx_data.put(MyDatabaseHelper.TRANSACTIONS_AMOUNT, amt);

        }
        tx_data.put(MyDatabaseHelper.TRANSACTIONS_NATURE, NATURE_VALUE_MPESA);
        tx_data.put(MyDatabaseHelper.TRANSACTIONS_TYPE, IN_TYPE_VALUE_);
        MyDatabaseHelper dbHelper = new MyDatabaseHelper(context);
        SQLiteDatabase db = dbHelper.getWritableDatabase();
        try {
            Cursor people = db.query(MyDatabaseHelper.PEOPLES_TABLE, null, null, null, null, null, null);
            Cursor accounts = db.query(MyDatabaseHelper.ACCOUNTS_TABLE, null, null, null, null, null, null);
            if (people.moveToFirst()){
                tx_data.put(MyDatabaseHelper.TRANSACTIONS_PERSON, people.getInt(people.getColumnIndex(MyDatabaseHelper.PEOPLES_ID)));
            }
            if (accounts.moveToLast()){
                tx_data.put(MyDatabaseHelper.TRANSACTIONS_ACCOUNT, accounts.getInt(accounts.getColumnIndex(MyDatabaseHelper.ACCOUNTS_ID)));
            }
            db.insert(MyDatabaseHelper.TRANSACTIONS_TABLE, MyDatabaseHelper.TRANSACTIONS_PERSON, tx_data);
        }catch (Exception e){
            Toast.makeText(context, ERROR_TRANSACTION_INSERT, Toast.LENGTH_SHORT).show();
        }
    }
    @SuppressLint("Range")
    public static void sent(String transactionMessage, Context context) {
        // Define the regular expressions for date and amount
        ContentValues tx_data = new ContentValues();
        String firstWordRegex = "\\w+";
        String dateRegex = "\\d{1,2}/\\d{1,2}/\\d{1,2}";
        String amountRegex = CURRENCY + "[\\d,]+\\.\\d{2}";
        String personRegex = "\\b" + PERSONAL_SIM_MPESA_SENT_TO_PERSON_KEY_WORDS[0] + "\\s+([^\\s]+)\\s+([^\\s]+)";
        // Create Pattern objects
        Pattern datePattern = Pattern.compile(dateRegex);
        Pattern amountPattern = Pattern.compile(amountRegex);
        Pattern personPattern = Pattern.compile(personRegex);
        // Create Matcher objects
        Matcher dateMatcher = datePattern.matcher(transactionMessage);
        Matcher amountMatcher = amountPattern.matcher(transactionMessage);
        Matcher personMatcher = personPattern.matcher(transactionMessage);
        // Find and print the date
        if (dateMatcher.find()) {
            String date = dateMatcher.group();

        }
        if (personMatcher.find()){
            String person = personMatcher.group().replaceAll(PERSONAL_SIM_MPESA_SENT_TO_PERSON_KEY_WORDS[0], "");
            tx_data.put(MyDatabaseHelper.TRANSACTIONS_DESCRIPTION, person);
        }
        // Find and print the amount
        if (amountMatcher.find()) {
            String amount = amountMatcher.group().replaceAll(CURRENCY + "|,", "");
            Float fi = Float.parseFloat(amount);
            Integer amt = fi.intValue();
            tx_data.put(MyDatabaseHelper.TRANSACTIONS_AMOUNT, amt * -1);

        }
        tx_data.put(MyDatabaseHelper.TRANSACTIONS_NATURE, NATURE_VALUE_MPESA);
        tx_data.put(MyDatabaseHelper.TRANSACTIONS_TYPE, OUT_TYPE_VALUE_);
        MyDatabaseHelper dbHelper = new MyDatabaseHelper(context);
        SQLiteDatabase db = dbHelper.getWritableDatabase();
        try {
            Cursor people = db.query(MyDatabaseHelper.PEOPLES_TABLE, null, null, null, null, null, null);
            Cursor accounts = db.query(MyDatabaseHelper.ACCOUNTS_TABLE, null, null, null, null, null, null);
            if (people.moveToFirst()){
                tx_data.put(MyDatabaseHelper.TRANSACTIONS_PERSON, people.getInt(people.getColumnIndex(MyDatabaseHelper.PEOPLES_ID)));
            }
            if (accounts.moveToLast()){
                tx_data.put(MyDatabaseHelper.TRANSACTIONS_ACCOUNT, accounts.getInt(accounts.getColumnIndex(MyDatabaseHelper.ACCOUNTS_ID)));
            }
            db.insert(MyDatabaseHelper.TRANSACTIONS_TABLE, MyDatabaseHelper.TRANSACTIONS_PERSON, tx_data);
        }catch (Exception e){
            Toast.makeText(context, ERROR_TRANSACTION_INSERT, Toast.LENGTH_SHORT).show();
        }
    }
    @SuppressLint("Range")
    public static void paid(String transactionMessage, Context context) {
        // Define the regular expressions for date and amount
        ContentValues tx_data = new ContentValues();
        String firstWordRegex = "\\w+";
        String dateRegex = "\\d{1,2}/\\d{1,2}/\\d{1,2}";
        String amountRegex = CURRENCY + "[\\d,]+\\.\\d{2}";
        String personRegex = "\\b" + PERSONAL_SIM_MPESA_PAID_TO_TILL_KEY_WORDS[0] + "\\s+([^\\s]+)\\s+([^\\s]+)";
        // Create Pattern objects
        Pattern datePattern = Pattern.compile(dateRegex);
        Pattern amountPattern = Pattern.compile(amountRegex);
        Pattern personPattern = Pattern.compile(personRegex);
        // Create Matcher objects
        Matcher dateMatcher = datePattern.matcher(transactionMessage);
        Matcher amountMatcher = amountPattern.matcher(transactionMessage);
        Matcher personMatcher = personPattern.matcher(transactionMessage);
        // Find and print the date
        if (dateMatcher.find()) {
            String date = dateMatcher.group();

        }
        if (personMatcher.find()){
            String person = personMatcher.group().replaceAll(PERSONAL_SIM_MPESA_PAID_TO_TILL_KEY_WORDS[0], "");
            tx_data.put(MyDatabaseHelper.TRANSACTIONS_DESCRIPTION, person);
        }
        // Find and print the amount
        if (amountMatcher.find()) {
            String amount = amountMatcher.group().replaceAll(CURRENCY + "|,", "");
            Float fi = Float.parseFloat(amount);
            Integer amt = fi.intValue();
            tx_data.put(MyDatabaseHelper.TRANSACTIONS_AMOUNT, amt * -1);

        }
        tx_data.put(MyDatabaseHelper.TRANSACTIONS_NATURE, NATURE_VALUE_MPESA);
        tx_data.put(MyDatabaseHelper.TRANSACTIONS_TYPE, OUT_TYPE_VALUE_);
        MyDatabaseHelper dbHelper = new MyDatabaseHelper(context);
        SQLiteDatabase db = dbHelper.getWritableDatabase();
        try {
            Cursor people = db.query(MyDatabaseHelper.PEOPLES_TABLE, null, null, null, null, null, null);
            Cursor accounts = db.query(MyDatabaseHelper.ACCOUNTS_TABLE, null, null, null, null, null, null);
            if (people.moveToFirst()){
                tx_data.put(MyDatabaseHelper.TRANSACTIONS_PERSON, people.getInt(people.getColumnIndex(MyDatabaseHelper.PEOPLES_ID)));
            }
            if (accounts.moveToLast()){
                tx_data.put(MyDatabaseHelper.TRANSACTIONS_ACCOUNT, accounts.getInt(accounts.getColumnIndex(MyDatabaseHelper.ACCOUNTS_ID)));
            }
            db.insert(MyDatabaseHelper.TRANSACTIONS_TABLE, MyDatabaseHelper.TRANSACTIONS_PERSON, tx_data);
        }catch (Exception e){
            Toast.makeText(context, ERROR_TRANSACTION_INSERT, Toast.LENGTH_SHORT).show();
        }
    }
    @SuppressLint("Range")
    public static void bought(String transactionMessage, Context context) {
        // Define the regular expressions for date and amount
        ContentValues tx_data = new ContentValues();
        String firstWordRegex = "\\w+";
        String dateRegex = "\\d{1,2}/\\d{1,2}/\\d{1,2}";
        String amountRegex = CURRENCY + "[\\d,]+\\.\\d{2}";
        String personRegex = "\\b" + PERSONAL_SIM_MPESA_BOUGHT_AIRTIME_FOR_PERSON_KEY_WORDS[0] + "\\s+([^\\s]+)\\s+([^\\s]+)\\s+([^\\s]+)";
        // Create Pattern objects
        Pattern datePattern = Pattern.compile(dateRegex);
        Pattern amountPattern = Pattern.compile(amountRegex);
        Pattern personPattern = Pattern.compile(personRegex);
        // Create Matcher objects
        Matcher dateMatcher = datePattern.matcher(transactionMessage);
        Matcher amountMatcher = amountPattern.matcher(transactionMessage);
        Matcher personMatcher = personPattern.matcher(transactionMessage);
        // Find and print the date
        if (dateMatcher.find()) {
            String date = dateMatcher.group();

        }
        if (personMatcher.find()) {
            String person = personMatcher.group().replaceAll(PERSONAL_SIM_MPESA_BOUGHT_AIRTIME_FOR_PERSON_KEY_WORDS[0], "");
            tx_data.put(MyDatabaseHelper.TRANSACTIONS_DESCRIPTION, person);
        }
        // Find and print the amount
        if (amountMatcher.find()) {
            String amount = amountMatcher.group().replaceAll(CURRENCY + "|,", "");
            Float fi = Float.parseFloat(amount);
            Integer amt = fi.intValue();
            tx_data.put(MyDatabaseHelper.TRANSACTIONS_AMOUNT, amt * -1);

        }
        tx_data.put(MyDatabaseHelper.TRANSACTIONS_NATURE, NATURE_VALUE_MPESA);
        tx_data.put(MyDatabaseHelper.TRANSACTIONS_TYPE, OUT_TYPE_VALUE_);
        MyDatabaseHelper dbHelper = new MyDatabaseHelper(context);
        SQLiteDatabase db = dbHelper.getWritableDatabase();
        try {
            Cursor people = db.query(MyDatabaseHelper.PEOPLES_TABLE, null, null, null, null, null, null);
            Cursor accounts = db.query(MyDatabaseHelper.ACCOUNTS_TABLE, null, null, null, null, null, null);
            if (people.moveToFirst()){
                tx_data.put(MyDatabaseHelper.TRANSACTIONS_PERSON, people.getInt(people.getColumnIndex(MyDatabaseHelper.PEOPLES_ID)));
            }
            if (accounts.moveToLast()){
                tx_data.put(MyDatabaseHelper.TRANSACTIONS_ACCOUNT, accounts.getInt(accounts.getColumnIndex(MyDatabaseHelper.ACCOUNTS_ID)));
            }
            db.insert(MyDatabaseHelper.TRANSACTIONS_TABLE, MyDatabaseHelper.TRANSACTIONS_PERSON, tx_data);
        }catch (Exception e){
            Toast.makeText(context, ERROR_TRANSACTION_INSERT, Toast.LENGTH_SHORT).show();
        }
    }


}

