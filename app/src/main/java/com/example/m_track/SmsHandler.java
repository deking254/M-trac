package com.example.m_track;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.telephony.SmsMessage;
import android.widget.Toast;
import org.greenrobot.eventbus.EventBus;

public class SmsHandler extends BroadcastReceiver {
    public static final String NEW_TRANSACTION_EVENT = "new transaction";
    public static final String MPESA_ADDRESS = "MPESA";

    EventHandler new_tx = new EventHandler(NEW_TRANSACTION_EVENT);

    public void onReceive(Context context, Intent intent) {
        Bundle bundle = intent.getExtras();
        if (bundle != null) {
            Object[] pdus = (Object[]) bundle.get("pdus");
            for (Object pdu : pdus) {
                byte[] pduBytes = (byte[]) pdu;
                SmsMessage smsMessage = SmsMessage.createFromPdu(pduBytes);
                String messageBody = smsMessage.getMessageBody();
                Toast.makeText(context, messageBody, Toast.LENGTH_SHORT).show();
                String senderAddress = smsMessage.getOriginatingAddress();
                if (senderAddress.matches(MPESA_ADDRESS)){
                    if (messageBody.contains(TransactionParser.AGENT_SIM_CASH_FROM_CUSTOMER_KEY_WORDS[1])){
                        TransactionParser.take(messageBody, context);
                        MyDatabaseHelper dbHelper = new MyDatabaseHelper(context);
                        SQLiteDatabase db = dbHelper.getWritableDatabase();
                        Cursor new_txs = db.query(MyDatabaseHelper.TRANSACTIONS_TABLE, null, null, null, null, null, null);
                        EventBus.getDefault().post(new_tx);
                    }
                    if (messageBody.contains(TransactionParser.AGENT_SIM_CASH_TO_CUSTOMER_KEY_WORDS[1])){
                        TransactionParser.give(messageBody, context);
                        MyDatabaseHelper dbHelper = new MyDatabaseHelper(context);
                        SQLiteDatabase db = dbHelper.getWritableDatabase();
                        try (Cursor new_txs = db.query(MyDatabaseHelper.TRANSACTIONS_TABLE, null, null, null, null, null, null)) {
                            EventBus.getDefault().post(new_tx);
                        }
                    }
                    if (messageBody.contains(TransactionParser.PERSONAL_SIM_MPESA_SENT_TO_PERSON_KEY_WORDS[1])){
                        TransactionParser.sent(messageBody, context);
                        MyDatabaseHelper dbHelper = new MyDatabaseHelper(context);
                        SQLiteDatabase db = dbHelper.getWritableDatabase();
                        try (Cursor new_txs = db.query(MyDatabaseHelper.TRANSACTIONS_TABLE, null, null, null, null, null, null)) {
                            EventBus.getDefault().post(new_tx);
                        }
                    }
                    if (messageBody.contains(TransactionParser.PERSONAL_SIM_MPESA_PAID_TO_TILL_KEY_WORDS[1])){
                        TransactionParser.paid(messageBody, context);
                        MyDatabaseHelper dbHelper = new MyDatabaseHelper(context);
                        SQLiteDatabase db = dbHelper.getWritableDatabase();
                        try (Cursor new_txs = db.query(MyDatabaseHelper.TRANSACTIONS_TABLE, null, null, null, null, null, null)) {
                            EventBus.getDefault().post(new_tx);
                        }
                    }
                    if (messageBody.contains(TransactionParser.PERSONAL_SIM_MPESA_BOUGHT_AIRTIME_FOR_PERSON_KEY_WORDS[1])){
                        TransactionParser.bought(messageBody, context);
                        MyDatabaseHelper dbHelper = new MyDatabaseHelper(context);
                        SQLiteDatabase db = dbHelper.getWritableDatabase();
                        try (Cursor new_txs = db.query(MyDatabaseHelper.TRANSACTIONS_TABLE, null, null, null, null, null, null)) {
                            EventBus.getDefault().post(new_tx);
                        }
                    }
                    if (messageBody.contains(TransactionParser.PERSONAL_SIM_MPESA_RECEIVED_FROM_PERSON_KEY_WORDS[1])){
                        TransactionParser.received(messageBody, context);
                        MyDatabaseHelper dbHelper = new MyDatabaseHelper(context);
                        SQLiteDatabase db = dbHelper.getWritableDatabase();
                        try (Cursor new_txs = db.query(MyDatabaseHelper.TRANSACTIONS_TABLE, null, null, null, null, null, null)) {
                            EventBus.getDefault().post(new_tx);
                        }
                    }
                    }
                }
                }
                }
            }