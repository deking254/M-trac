package com.example.m_track;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.Manifest;
import android.annotation.SuppressLint;
import android.content.ContentValues;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.view.ContextMenu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;

public class MainActivity extends AppCompatActivity{
//    This is the main activity the app will land on when it is opened
//    It displays the list of transactions that have happended.
    RecyclerView recyclerView;
    public static final String REQUEST_UPDATE_TRANSACTION = "update_transaction";
    public static final String REQUEST_NEW_TRANSACTION = "add_new_transactions";
    public final static String REQUEST = "request";
    public final static String MPESA = "Mpesa";
    public final static String CASH = "Cash";
    int selected_id;
    Bundle update_info;
    Bundle account_last;
    MyDatabaseHelper dbHelper = new MyDatabaseHelper(this);
    SQLiteDatabase db;
    TransactionAdapter adapter;
    public final static String PERSMISSION = "android.permission.RECEIVE_SMS";
    public final static String PERSON_POSITION = "person_position";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        String [] wer = new String[]{PERSMISSION};
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
        totals.setOnClickListener(view -> {
            Intent totals_page = new Intent(MainActivity.this, Totals.class);
            startActivity(totals_page);
        });
        ImageButton people_button = (ImageButton) findViewById(R.id.people_main);
        people_button.setOnClickListener(view -> {
            Intent people_intent = new Intent(MainActivity.this, People.class);
            startActivity(people_intent);
        });
        FloatingActionButton add_tx = (FloatingActionButton) findViewById(R.id.addtx);
        add_tx.setOnClickListener(view -> {
            Intent add_transaction = new Intent(MainActivity.this, UpdateTx.class);
            account_last.putString(add_account.REQUEST, REQUEST_NEW_TRANSACTION);
            add_transaction.putExtras(account_last);
            startActivity(add_transaction);
        });
    }
    @Override
    protected void onStart() {
        super.onStart();
        reload_list(getTransactionDataFromDatabase(), this, recyclerView);
        EventBus.getDefault().register(this);
    }
    @SuppressLint("Range")
    @Override
    public void onCreateContextMenu(ContextMenu menu, View v, ContextMenu.ContextMenuInfo menuInfo) {
        super.onCreateContextMenu(menu, v, menuInfo);
        if (v.getId() == recyclerView.getId()) {
            MenuInflater inflater = getMenuInflater();
            inflater.inflate(R.menu.context_menu, menu);
            Cursor people_cursor = getPeople();
            if(people_cursor != null){
                while(people_cursor.moveToNext()){
                    if (people_cursor.getInt(people_cursor.getColumnIndex(MyDatabaseHelper.PEOPLES_ID)) == update_info.getInt(MyDatabaseHelper.TRANSACTIONS_PERSON)){
                        update_info.putInt(PERSON_POSITION, people_cursor.getPosition());
                    }
                }
            }
        }
    }

    @Override
    public boolean onContextItemSelected(MenuItem item) {
        //fires up a menu when a transaction is long pressed.

        if (item.getItemId() == R.id.action_edit){
                // Handle edit action
                // Example: Start an edit activity or show a dialog
            Intent update_transaction_activity = new Intent(this, UpdateTx.class);
            update_info.putString(REQUEST, REQUEST_UPDATE_TRANSACTION);
            update_transaction_activity.putExtras(update_info);
            startActivity(update_transaction_activity);
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
            String INSERT_ERROR = "Error inserting transaction";
            String NOT_MPESA = "Error not Mpesa transaction";
            // Handle delete action
            // Example: Remove the item from your data source and update the RecyclerView
            if (update_info.getString(MyDatabaseHelper.TRANSACTIONS_NATURE).matches(MPESA)){
                transactional_tx.put(MyDatabaseHelper.TRANSACTIONS_PERSON, update_info.getInt("person_id"));
                transactional_tx.put(MyDatabaseHelper.TRANSACTIONS_DESCRIPTION, update_info.getString("description") + " -Transactional");
                transactional_tx.put(MyDatabaseHelper.TRANSACTIONS_DATE, update_info.getString("date"));
                if (update_info.getString("type").matches("Income")){
                    transactional_tx.put(MyDatabaseHelper.TRANSACTIONS_TYPE, "Debt");
                    transactional_tx.put(MyDatabaseHelper.TRANSACTIONS_AMOUNT, update_info.getInt("amount_int") * -1);
                }else{
                    transactional_tx.put(MyDatabaseHelper.TRANSACTIONS_TYPE, "Income");
                    transactional_tx.put(MyDatabaseHelper.TRANSACTIONS_AMOUNT, update_info.getInt("amount_int") * -1);
                }
                transactional_tx.put(MyDatabaseHelper.TRANSACTIONS_NATURE, "Cash");
                transactional_tx.put(MyDatabaseHelper.TRANSACTIONS_ACCOUNT, update_info.getInt("acc_id"));
                try {
                    db.insert(MyDatabaseHelper.TRANSACTIONS_TABLE, MyDatabaseHelper.TRANSACTIONS_PERSON, transactional_tx);
                    reload_list(getTransactionDataFromDatabase(), this, recyclerView);
                }catch (Exception e){
                    Toast.makeText(this, INSERT_ERROR, Toast.LENGTH_LONG);
                }
            }else{
                Toast.makeText(this, NOT_MPESA, Toast.LENGTH_LONG).show();
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
        // Update the UI with the new data or message
        String message = event.getMessage();
        if (message.matches("new tx")){
            reload_list(getTransactionDataFromDatabase(), this, recyclerView);
        }
        if (message.matches("success")){
            reload_list(getTransactionDataFromDatabase(), this, recyclerView);
        }

    }
    @Subscribe
    public void onListItemselected(IdHandler event) {
        // Update your UI or perform any other actions based on the event data
        // Update the UI with the new data or message
        selected_id = event.getMessage();
    }
    @Subscribe
    public void onListItemUpdate(Update_infoHandler event) {
        // Update your UI or perform any other actions based on the event data
        // Update the UI with the new data or message
        update_info = event.getUpdateinfo();
    }
    @SuppressLint("Range")
    private Cursor getTransactionDataFromDatabase() {
//        Gets the cursor for the transactions in the table.
        final String ERROR_ACCOUNTS = "Accounts Error";
        SQLiteDatabase db = dbHelper.getWritableDatabase();
        Cursor accounts = getLastAccount();
        try {
            if (accounts.getCount() <= 0) {
                Intent account_add = new Intent(this, add_account.class);
                startActivity(account_add);
            } else {
                if (!accounts.moveToPosition(accounts.getCount() - 1)) {
                    return null;
                }
                account_last.putInt("acc_id", accounts.getInt(accounts.getColumnIndex("id")));
            }
        }catch (Exception e){
            Toast.makeText(this, ERROR_ACCOUNTS, Toast.LENGTH_LONG).show();
        }
        Cursor tx = db.rawQuery("SELECT transactions.id AS _id, people.fullname, person, transactions.description, transactions.date, transactions.type, transactions.nature, transactions.amount, transactions.account FROM transactions INNER JOIN people WHERE transactions.person=people.id ORDER BY date DESC", null);
//        totals_calculator(accounts);
        return tx;
    }
    private Cursor getLastAccount(){
//        returns a cursor with one object which is the last entry into the accounts table
        Cursor latest_account = db.rawQuery("SELECT * FROM "
                + MyDatabaseHelper.ACCOUNTS_TABLE +
                " ORDER BY id DESC LIMIT 1", null);
        return latest_account;
    }
    private Cursor getPeople(){
//        Returns a cursor to all the records in the people table
        final String PEOPLE_ERROR = "Could not retrieve people";
      try {
          Cursor people = db.query(MyDatabaseHelper.PEOPLES_TABLE, null, null, null, null, null,null);
          return people;
      }catch (Exception e){
          Toast.makeText(this, PEOPLE_ERROR, Toast.LENGTH_SHORT).show();
          return null;
      }
    }
    public void reload_list(Cursor txs, Context context, RecyclerView recyclerView){
//        Refreshes the recyclerview by adding a new cursor
        recyclerView.setLayoutManager(new LinearLayoutManager(context));
        // Initialize and set the adapter
        adapter = new TransactionAdapter(getTransactionDataFromDatabase(), context); // Implement this function
        recyclerView.setAdapter(adapter);
        recyclerView.getAdapter().notifyDataSetChanged();
    }
    public void delete_tx(int id){
//        Deletes a transaction with the same id as the one provided.
        MyDatabaseHelper dbHelper = new MyDatabaseHelper(this);
        SQLiteDatabase db = dbHelper.getWritableDatabase();
        String[] args = new String[]{String.valueOf(id)};
        db.delete(MyDatabaseHelper.TRANSACTIONS_TABLE, MyDatabaseHelper.TRANSACTIONS_ID + " = ?", args);
    }
    public void totals_calculator(){
//        Calculates the total amount of transactions with nature as mpesa as well the totals for those with cash.
        Cursor accounts = db.rawQuery("SELECT * FROM " + MyDatabaseHelper.ACCOUNTS_TABLE +
                " ORDER BY id DESC LIMIT 1", null);
        TextView cash = (TextView) findViewById(R.id.cashvalue);
        TextView mpesa = (TextView) findViewById(R.id.mpesavalue);
        Cursor mpesa_txs = db.rawQuery("SELECT SUM(amount) AS sum FROM " +
                MyDatabaseHelper.TRANSACTIONS_TABLE +
                " WHERE nature=\"'Mpesa'\"", null);
        Cursor cash_txs = db.rawQuery("SELECT SUM(amount) AS sum FROM " +
                MyDatabaseHelper.TRANSACTIONS_TABLE +
                " WHERE nature=\"'Cash'\"", null);
        if (mpesa_txs.moveToPosition(1)) {
            @SuppressLint("Range") int total_mpesa = accounts.getInt(accounts.getColumnIndex(MyDatabaseHelper.ACCOUNTS_MPESA));
            total_mpesa += mpesa_txs.getInt(0);
            mpesa.setText(total_mpesa);
        }
        if (cash_txs.moveToPosition(1)) {
            @SuppressLint("Range") int total_cash = accounts.getInt(accounts.getColumnIndex(MyDatabaseHelper.ACCOUNTS_CASH));
            total_cash += cash_txs.getInt(0);
            cash.setText(total_cash);
        }
    }
}
