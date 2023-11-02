package com.example.m_track;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.Manifest;
import android.content.ContentValues;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.media.Image;
import android.os.Bundle;
import android.util.Log;
import android.view.ContextMenu;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.material.floatingactionbutton.FloatingActionButton;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.joda.time.DateTime;

import java.sql.Date;

public class MainActivity extends AppCompatActivity{
    RecyclerView recyclerView;
    int selected_id;
    Bundle update_info;
    Bundle account_last;
    MyDatabaseHelper dbHelper = new MyDatabaseHelper(this);
    SQLiteDatabase db;
    TransactionAdapter adapter;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        String [] wer = new String[]{"android.permission.RECEIVE_SMS"};
        if (ContextCompat.checkSelfPermission(MainActivity.this, Manifest.permission.RECEIVE_SMS) == -1) {
            requestPermissions(wer, 20);
        }
        setContentView(R.layout.activity_main);
        account_last = new Bundle();
        db = dbHelper.getWritableDatabase();
        recyclerView = findViewById(R.id.recyclerView);
        registerForContextMenu(recyclerView);
        reload_list(getTransactionDataFromDatabase(), this, recyclerView);
        totals_calculator();
        ImageButton totals = findViewById(R.id.totals);
        totals.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent totals_page = new Intent(MainActivity.this, Totals.class);
                startActivity(totals_page);
            }
        });
        ImageButton peoplebutton = (ImageButton) findViewById(R.id.people_main);
        peoplebutton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent people_intent = new Intent(MainActivity.this, People.class);
                startActivity(people_intent);
            }
        });
        FloatingActionButton add_tx = (FloatingActionButton) findViewById(R.id.addtx);
        add_tx.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent addtx = new Intent(MainActivity.this, UpdateTx.class);
                account_last.putString("request", "addnew");
                addtx.putExtras(account_last);
                startActivity(addtx);
            }
        });
    }
    @Override
    protected void onStart() {
        super.onStart();
        reload_list(getTransactionDataFromDatabase(), this, recyclerView);
        EventBus.getDefault().register(this);
    }
    @Override
    public void onCreateContextMenu(ContextMenu menu, View v, ContextMenu.ContextMenuInfo menuInfo) {
        super.onCreateContextMenu(menu, v, menuInfo);
        if (v.getId() == recyclerView.getId()) {
            MenuInflater inflater = getMenuInflater();
            inflater.inflate(R.menu.context_menu, menu);
            Cursor pple = getPeople();
            if(pple != null){
                while(pple.moveToNext()){
                    if (pple.getInt(pple.getColumnIndex("id")) == update_info.getInt("person_id")){
                        update_info.putInt("personposition", pple.getPosition());
                    }
                }
            }
        }
    }

    @Override
    public boolean onContextItemSelected(MenuItem item) {
        //AdapterView.AdapterContextMenuInfo info = (AdapterView.AdapterContextMenuInfo) item.getMenuInfo();
        //int selectedItemPosition = info.position;

        if (item.getItemId() == R.id.action_edit){
                // Handle edit action
                // Example: Start an edit activity or show a dialog
            Intent intent = new Intent(this, UpdateTx.class);
            update_info.putString("request", "update");
            intent.putExtras(update_info);
            startActivity(intent);
            return true;
        }
            if (item.getItemId() == R.id.action_delete) {
                // Handle delete action
                // Example: Remove the item from your data source and update the RecyclerView
                delete_tx(update_info.getInt("tx_id"));
                reload_list(getTransactionDataFromDatabase(), this, recyclerView);
                return true;
            }
        if (item.getItemId() == R.id.action_transactional) {
            ContentValues transactional_tx = new ContentValues();
            // Handle delete action
            // Example: Remove the item from your data source and update the RecyclerView
            if (update_info.getString("nature").matches("Mpesa")){
                transactional_tx.put("person", update_info.getInt("person_id"));
                transactional_tx.put("description", update_info.getString("description") + " -Transactional");
                transactional_tx.put("date", update_info.getString("date"));
                if (update_info.getString("type").matches("Income")){
                    transactional_tx.put("type", "Debt");
                    transactional_tx.put("amount", update_info.getInt("amount_int") * -1);
                }else{
                    transactional_tx.put("type", "Income");
                    transactional_tx.put("amount", update_info.getInt("amount_int") * -1);
                }
                transactional_tx.put("nature", "Cash");
                transactional_tx.put("account", update_info.getInt("acc_id"));
                try {
                    db.insert("transactions", "person", transactional_tx);
                    reload_list(getTransactionDataFromDatabase(), this, recyclerView);
                }catch (Exception e){
                    Toast.makeText(this, "Error", Toast.LENGTH_SHORT);
                }
            }else{
                Toast.makeText(this, "Error not Mpesa tx", Toast.LENGTH_SHORT).show();
            }

            return true;
        }else {
                return super.onContextItemSelected(item);
            }
        }
        @Override
        protected void onStop() {
        EventBus.getDefault().unregister(this);
        super.onStop();
    }
    @Subscribe
    public void onDatabaseUpdateEvent(EventHandler event) {
        // Update your UI or perform any other actions based on the event data
        String message = event.getMessage();
        if (message.matches("new tx")){
            reload_list(getTransactionDataFromDatabase(), this, recyclerView);
        }
        if (message.matches("success")){
            reload_list(getTransactionDataFromDatabase(), this, recyclerView);
        }
        // Update the UI with the new data or message
    }
    @Subscribe
    public void onListItemselected(IdHandler event) {
        // Update your UI or perform any other actions based on the event data
        selected_id = event.getMessage();
        // Update the UI with the new data or message
    }
    @Subscribe
    public void onListItemUpdate(Update_infoHandler event) {
        // Update your UI or perform any other actions based on the event data
        update_info = event.getUpdateinfo();
                // Update the UI with the new data or message
    }
    private Cursor getTransactionDataFromDatabase() {
        SQLiteDatabase db = dbHelper.getWritableDatabase();
        Cursor accounts = getLastAccount();
        Cursor people = getPeople();
        if (accounts.getCount() <= 0) {
            Intent account_add = new Intent(this, addaccount.class);
            startActivity(account_add);
        } else {
            if (!accounts.moveToPosition(accounts.getCount() - 1)) {
                return null;
            }
            account_last.putInt("acc_id", accounts.getInt(accounts.getColumnIndex("id")));
        }
        if (people.getCount() <= 0) {
            Intent personadd = new Intent(this, addperson.class);
            startActivity(personadd);
        }
        Cursor tx = db.rawQuery("SELECT transactions.id AS _id, people.fullname, person, transactions.description, transactions.date, transactions.type, transactions.nature, transactions.amount, transactions.account FROM transactions INNER JOIN people WHERE transactions.person=people.id ORDER BY date DESC", null);
//        totals_calculator(accounts);
        return tx;
    }
    private Cursor getLastAccount(){
        Cursor latest_account = db.rawQuery("SELECT * FROM accounts ORDER BY id DESC LIMIT 1", null);
        return latest_account;
    }
    private Cursor getPeople(){
      try {
          Cursor people = db.query("people", null, null, null, null, null,null);
          return people;
      }catch (Exception e){
          Toast.makeText(this, "Error", Toast.LENGTH_SHORT).show();
          return null;
      }
    }
    public void reload_list(Cursor txs, Context context, RecyclerView recyclerView){
        recyclerView.setLayoutManager(new LinearLayoutManager(context));
        // Initialize and set the adapter
        adapter = new TransactionAdapter(getTransactionDataFromDatabase(), context); // Implement this function
        recyclerView.setAdapter(adapter);
        recyclerView.getAdapter().notifyDataSetChanged();
    }
    public void delete_tx(int id){
        MyDatabaseHelper dbHelper = new MyDatabaseHelper(this);
        SQLiteDatabase db = dbHelper.getWritableDatabase();
        String[] args = new String[]{String.valueOf(id)};
        db.delete("transactions", "id = ?", args);
    }
    public void totals_calculator(){
        Cursor accounts = db.rawQuery("SELECT * FROM accounts ORDER BY id DESC LIMIT 1", null);
        TextView cash = (TextView) findViewById(R.id.cashvalue);
        TextView mpesa = (TextView) findViewById(R.id.mpesavalue);
        Cursor mpesa_txs = db.rawQuery("SELECT SUM(amount) AS sum FROM transactions WHERE nature=\"'Mpesa'\"", null);
        Cursor cash_txs = db.rawQuery("SELECT SUM(amount) AS sum FROM transactions WHERE nature=\"'Cash'\"", null);
        if (mpesa_txs.moveToPosition(1)) {
            int total_mpesa = accounts.getInt(accounts.getColumnIndex("mpesa"));
            total_mpesa += mpesa_txs.getInt(0);
            mpesa.setText(total_mpesa);
        }
        if (cash_txs.moveToPosition(1)) {
            int total_cash = accounts.getInt(accounts.getColumnIndex("cash"));
            total_cash += cash_txs.getInt(0);
            cash.setText(total_cash);
        }
    }
}
