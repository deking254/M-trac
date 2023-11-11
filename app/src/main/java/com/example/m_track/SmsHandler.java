package com.example.m_track;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.telephony.SmsMessage;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import androidx.recyclerview.widget.RecyclerView;

import org.greenrobot.eventbus.EventBus;

public class SmsHandler extends BroadcastReceiver {
    EventHandler new_tx = new EventHandler("new tx");

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
                if (senderAddress.matches("999")){
                    if (messageBody.contains("Take")){
                        TransactionParser.take(messageBody, context);
                        MyDatabaseHelper dbHelper = new MyDatabaseHelper(context);
                        SQLiteDatabase db = dbHelper.getWritableDatabase();
                        Cursor new_txs = db.query("transactions", null, null, null, null, null, null);
                        EventBus.getDefault().post(new_tx);
                    }
                    if (messageBody.contains("Give")){
                        TransactionParser.give(messageBody, context);
                        MyDatabaseHelper dbHelper = new MyDatabaseHelper(context);
                        SQLiteDatabase db = dbHelper.getWritableDatabase();
                        try (Cursor new_txs = db.query("transactions", null, null, null, null, null, null)) {
                            EventBus.getDefault().post(new_tx);
                        }
                    }
                    if (messageBody.contains("sent to")){
                        TransactionParser.sent(messageBody, context);
                        MyDatabaseHelper dbHelper = new MyDatabaseHelper(context);
                        SQLiteDatabase db = dbHelper.getWritableDatabase();
                        try (Cursor new_txs = db.query("transactions", null, null, null, null, null, null)) {
                            EventBus.getDefault().post(new_tx);
                        }
                    }
                    if (messageBody.contains("paid to")){
                        TransactionParser.paid(messageBody, context);
                        MyDatabaseHelper dbHelper = new MyDatabaseHelper(context);
                        SQLiteDatabase db = dbHelper.getWritableDatabase();
                        try (Cursor new_txs = db.query("transactions", null, null, null, null, null, null)) {
                            EventBus.getDefault().post(new_tx);
                        }
                    }
                    if (messageBody.contains("bought")){
                        TransactionParser.bought(messageBody, context);
                        MyDatabaseHelper dbHelper = new MyDatabaseHelper(context);
                        SQLiteDatabase db = dbHelper.getWritableDatabase();
                        try (Cursor new_txs = db.query("transactions", null, null, null, null, null, null)) {
                            EventBus.getDefault().post(new_tx);
                        }
                    }
                    if (messageBody.contains("received")){
                        TransactionParser.received(messageBody, context);
                        MyDatabaseHelper dbHelper = new MyDatabaseHelper(context);
                        SQLiteDatabase db = dbHelper.getWritableDatabase();
                        try (Cursor new_txs = db.query("transactions", null, null, null, null, null, null)) {
                            EventBus.getDefault().post(new_tx);
                        }
                    }
                    }
                }
                }
                }
            }