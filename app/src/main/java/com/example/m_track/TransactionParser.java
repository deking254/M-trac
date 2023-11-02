package com.example.m_track;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;
import android.widget.Toast;

import java.util.regex.*;

public class TransactionParser {
    public static void take(String transactionMessage, Context context) {
        // Define the regular expressions for date and amount
        ContentValues tx_data = new ContentValues();
        String firstWordRegex = "\\w+";
        String dateRegex = "\\d{1,2}/\\d{1,2}/\\d{1,2}";
        String amountRegex = "Ksh[\\d,]+\\.\\d{2}";
        String personRegex = "\\bfrom\\s+([^\\s]+)\\s+([^\\s]+)";
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
            String person = personMatcher.group().replaceAll("from", "");
            tx_data.put("description", person);
        }
        // Find and print the amount
        if (amountMatcher.find()) {
            String amount = amountMatcher.group().replaceAll("Ksh|,", "");
            Float fi = Float.parseFloat(amount);
            Integer amt = fi.intValue();
            tx_data.put("amount", amt * -1);

        }
        tx_data.put("nature", "Mpesa");
        tx_data.put("type", "Debt");
        MyDatabaseHelper dbHelper = new MyDatabaseHelper(context);
        SQLiteDatabase db = dbHelper.getWritableDatabase();
        try {
            Cursor people = db.query("people", null, null, null, null, null, null);
            Cursor accounts = db.query("accounts", null, null, null, null, null, null);
            if (people.moveToFirst()){
                tx_data.put("person", people.getInt(people.getColumnIndex("id")));
            }
            if (accounts.moveToLast()){
                tx_data.put("account", accounts.getInt(accounts.getColumnIndex("id")));
            }
            db.insert("transactions", "person", tx_data);
        }catch (Exception e){
            Toast.makeText(context, "Error", Toast.LENGTH_SHORT).show();
        }
    }
    public static void give(String transactionMessage, Context context) {
        // Define the regular expressions for date and amount
        ContentValues tx_data = new ContentValues();
        String firstWordRegex = "\\w+";
        String dateRegex = "\\d{1,2}/\\d{1,2}/\\d{1,2}";
        String amountRegex = "Ksh[\\d,]+\\.\\d{2}";
        String personRegex = "\\bto\\s+([^\\s]+)\\s+([^\\s]+)";
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
            String person = personMatcher.group().replaceAll("to", "");
            tx_data.put("description", person);
        }
        // Find and print the amount
        if (amountMatcher.find()) {
            String amount = amountMatcher.group().replaceAll("Ksh|,", "");
            Float fi = Float.parseFloat(amount);
            Integer amt = fi.intValue();
            tx_data.put("amount", amt);

        }
        tx_data.put("nature", "Mpesa");
        tx_data.put("type", "Income");
        MyDatabaseHelper dbHelper = new MyDatabaseHelper(context);
        SQLiteDatabase db = dbHelper.getWritableDatabase();
        try {
            Cursor people = db.query("people", null, null, null, null, null, null);
            Cursor accounts = db.query("accounts", null, null, null, null, null, null);
            if (people.moveToFirst()){
                tx_data.put("person", people.getInt(people.getColumnIndex("id")));
            }
            if (accounts.moveToLast()){
                tx_data.put("account", accounts.getInt(accounts.getColumnIndex("id")));
            }
            db.insert("transactions", "person", tx_data);
        }catch (Exception e){
            Toast.makeText(context, "Error", Toast.LENGTH_SHORT).show();
        }
    }
    public static void received(String transactionMessage, Context context) {
        // Define the regular expressions for date and amount
        ContentValues tx_data = new ContentValues();
        String firstWordRegex = "\\w+";
        String dateRegex = "\\d{1,2}/\\d{1,2}/\\d{1,2}";
        String amountRegex = "Ksh[\\d,]+\\.\\d{2}";
        String personRegex = "\\bfrom\\s+([^\\s]+)\\s+([^\\s]+)";
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
            String person = personMatcher.group().replaceAll("from", "");
            tx_data.put("description", person);
        }
        // Find and print the amount
        if (amountMatcher.find()) {
            String amount = amountMatcher.group().replaceAll("Ksh|,", "");
            Float fi = Float.parseFloat(amount);
            Integer amt = fi.intValue();
            tx_data.put("amount", amt);

        }
        tx_data.put("nature", "Mpesa");
        tx_data.put("type", "Income");
        MyDatabaseHelper dbHelper = new MyDatabaseHelper(context);
        SQLiteDatabase db = dbHelper.getWritableDatabase();
        try {
            Cursor people = db.query("people", null, null, null, null, null, null);
            Cursor accounts = db.query("accounts", null, null, null, null, null, null);
            if (people.moveToFirst()){
                tx_data.put("person", people.getInt(people.getColumnIndex("id")));
            }
            if (accounts.moveToLast()){
                tx_data.put("account", accounts.getInt(accounts.getColumnIndex("id")));
            }
            db.insert("transactions", "person", tx_data);
        }catch (Exception e){
            Toast.makeText(context, "Error", Toast.LENGTH_SHORT).show();
        }
    }
    public static void sent(String transactionMessage, Context context) {
        // Define the regular expressions for date and amount
        ContentValues tx_data = new ContentValues();
        String firstWordRegex = "\\w+";
        String dateRegex = "\\d{1,2}/\\d{1,2}/\\d{1,2}";
        String amountRegex = "Ksh[\\d,]+\\.\\d{2}";
        String personRegex = "\\bto\\s+([^\\s]+)\\s+([^\\s]+)";
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
            String person = personMatcher.group().replaceAll("to", "");
            tx_data.put("description", person);
        }
        // Find and print the amount
        if (amountMatcher.find()) {
            String amount = amountMatcher.group().replaceAll("Ksh|,", "");
            Float fi = Float.parseFloat(amount);
            Integer amt = fi.intValue();
            tx_data.put("amount", amt * -1);

        }
        tx_data.put("nature", "Mpesa");
        tx_data.put("type", "Debt");
        MyDatabaseHelper dbHelper = new MyDatabaseHelper(context);
        SQLiteDatabase db = dbHelper.getWritableDatabase();
        try {
            Cursor people = db.query("people", null, null, null, null, null, null);
            Cursor accounts = db.query("accounts", null, null, null, null, null, null);
            if (people.moveToFirst()){
                tx_data.put("person", people.getInt(people.getColumnIndex("id")));
            }
            if (accounts.moveToLast()){
                tx_data.put("account", accounts.getInt(accounts.getColumnIndex("id")));
            }
            db.insert("transactions", "person", tx_data);
        }catch (Exception e){
            Toast.makeText(context, "Error", Toast.LENGTH_SHORT).show();
        }
    }
    public static void paid(String transactionMessage, Context context) {
        // Define the regular expressions for date and amount
        ContentValues tx_data = new ContentValues();
        String firstWordRegex = "\\w+";
        String dateRegex = "\\d{1,2}/\\d{1,2}/\\d{1,2}";
        String amountRegex = "Ksh[\\d,]+\\.\\d{2}";
        String personRegex = "\\bto\\s+([^\\s]+)\\s+([^\\s]+)";
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
            String person = personMatcher.group().replaceAll("to", "");
            tx_data.put("description", person);
        }
        // Find and print the amount
        if (amountMatcher.find()) {
            String amount = amountMatcher.group().replaceAll("Ksh|,", "");
            Float fi = Float.parseFloat(amount);
            Integer amt = fi.intValue();
            tx_data.put("amount", amt * -1);

        }
        tx_data.put("nature", "Mpesa");
        tx_data.put("type", "Debt");
        MyDatabaseHelper dbHelper = new MyDatabaseHelper(context);
        SQLiteDatabase db = dbHelper.getWritableDatabase();
        try {
            Cursor people = db.query("people", null, null, null, null, null, null);
            Cursor accounts = db.query("accounts", null, null, null, null, null, null);
            if (people.moveToFirst()){
                tx_data.put("person", people.getInt(people.getColumnIndex("id")));
            }
            if (accounts.moveToLast()){
                tx_data.put("account", accounts.getInt(accounts.getColumnIndex("id")));
            }
            db.insert("transactions", "person", tx_data);
        }catch (Exception e){
            Toast.makeText(context, "Error", Toast.LENGTH_SHORT).show();
        }
    }
    public static void bought(String transactionMessage, Context context) {
        // Define the regular expressions for date and amount
        ContentValues tx_data = new ContentValues();
        String firstWordRegex = "\\w+";
        String dateRegex = "\\d{1,2}/\\d{1,2}/\\d{1,2}";
        String amountRegex = "Ksh[\\d,]+\\.\\d{2}";
        String personRegex = "\\bof\\s+([^\\s]+)";
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
            String person = personMatcher.group().replaceAll("of", "");
            tx_data.put("description", person);
        }
        // Find and print the amount
        if (amountMatcher.find()) {
            String amount = amountMatcher.group().replaceAll("Ksh|,", "");
            Float fi = Float.parseFloat(amount);
            Integer amt = fi.intValue();
            tx_data.put("amount", amt * -1);

        }
        tx_data.put("nature", "Mpesa");
        tx_data.put("type", "Debt");
        MyDatabaseHelper dbHelper = new MyDatabaseHelper(context);
        SQLiteDatabase db = dbHelper.getWritableDatabase();
        try {
            Cursor people = db.query("people", null, null, null, null, null, null);
            Cursor accounts = db.query("accounts", null, null, null, null, null, null);
            if (people.moveToFirst()){
                tx_data.put("person", people.getInt(people.getColumnIndex("id")));
            }
            if (accounts.moveToLast()){
                tx_data.put("account", accounts.getInt(accounts.getColumnIndex("id")));
            }
            db.insert("transactions", "person", tx_data);
        }catch (Exception e){
            Toast.makeText(context, "Error", Toast.LENGTH_SHORT).show();
        }
    }


}

