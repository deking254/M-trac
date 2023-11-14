package com.example.m_track;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.view.ContextMenu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;

import com.google.android.material.floatingactionbutton.FloatingActionButton;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;

public class People extends AppCompatActivity {
    RecyclerView recyclerView;
    int selected_id;
    public static final String REQUEST_UPDATE_PERSON = "update_person";
    public static final String REQUEST_NEW_PERSON = "add_new_person";
    public static final String RESPONSE_SUCCESS = "success";
    public static final String RESPONSE_FAIL = "fail";
    public static final String REQUEST = "request";
    Bundle update_info;
    PeopleAdapter adapter;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_people);
        recyclerView = findViewById(R.id.peoplerecyclerView);
        registerForContextMenu(recyclerView);
        reload_list(getPeopleDataFromDatabase(), this, recyclerView);
        FloatingActionButton add_tx = (FloatingActionButton) findViewById(R.id.addpersontx);
        add_tx.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent addtx = new Intent(People.this, add_person.class);
                startActivity(addtx);
            }
        });
    }
    @Override
    protected void onStart() {
        super.onStart();
        reload_list(getPeopleDataFromDatabase(), this, recyclerView);
        EventBus.getDefault().register(this);
    }
    @Override
    public void onCreateContextMenu(ContextMenu menu, View v, ContextMenu.ContextMenuInfo menuInfo) {
        super.onCreateContextMenu(menu, v, menuInfo);
        if (v.getId() == recyclerView.getId()) {
            MenuInflater inflater = getMenuInflater();
            inflater.inflate(R.menu.people_context, menu);
        }
    }

    @Override
    public boolean onContextItemSelected(MenuItem item) {
        if (item.getItemId() == R.id.action_editperson){
            // Opens up the addperson activity with the deatils of the texview already populated
            Intent add_person_intent = new Intent(this, add_person.class);
            update_info.putString(REQUEST, REQUEST_UPDATE_PERSON);
            add_person_intent.putExtras(update_info);
            startActivity(add_person_intent);
            return true;}
        if (item.getItemId() == R.id.action_deleteperson) {
            // Handle delete action
            // Example: Remove the item from your data source and update the RecyclerView
            delete_tx(update_info.getInt(TransactionAdapter.PERSON_ID));
            reload_list(getPeopleDataFromDatabase(), this, recyclerView);
            return true;
        }
        if (item.getItemId() == R.id.action_transactional) {
            // Handle delete action
            // Example: Remove the item from your data source and update the RecyclerView
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
        if (message.matches(REQUEST_NEW_PERSON)){
            reload_list(getPeopleDataFromDatabase(), this, recyclerView);
        }
        if (message.matches(RESPONSE_SUCCESS)){
            reload_list(getPeopleDataFromDatabase(), this, recyclerView);
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
    public void onListItemUpdate(Update_personinfoHandler event) {
        // Update your UI or perform any other actions based on the event data
        update_info = event.getUpdateinfo();
        // Update the UI with the new data or message
    }
    private Cursor getPeopleDataFromDatabase() {
//        Returns a cursor of all the records in the people table
        MyDatabaseHelper dbHelper = new MyDatabaseHelper(this);
        SQLiteDatabase db = dbHelper.getWritableDatabase();
        String[] projection={MyDatabaseHelper.PEOPLES_DOT_ID,MyDatabaseHelper.PEOPLES_DOT_FULL_NAME, "SUM(" +
                MyDatabaseHelper.TRANSACTIONS_DOT_AMOUNT + ") AS " + PeopleAdapter.NET_TOTAL};
        String query = "SELECT " + MyDatabaseHelper.PEOPLES_DOT_ID + ", " +
        MyDatabaseHelper.PEOPLES_DOT_FULL_NAME + ", " + "SUM(" + MyDatabaseHelper.TRANSACTIONS_DOT_AMOUNT + ")" +
                " AS " + PeopleAdapter.NET_TOTAL + " FROM " + MyDatabaseHelper.PEOPLES_TABLE + " INNER JOIN " +
                MyDatabaseHelper.TRANSACTIONS_TABLE +
                " ON " + MyDatabaseHelper.PEOPLES_DOT_ID + " = " + MyDatabaseHelper.TRANSACTIONS_DOT_PERSON +
                " GROUP BY " + MyDatabaseHelper.PEOPLES_DOT_ID;
        Cursor accounts = db.rawQuery(query, null);
        return accounts;
    }
    public void reload_list(Cursor txs, Context context, RecyclerView recyclerView){
        recyclerView.setLayoutManager(new LinearLayoutManager(context));
        // Initialize and set the adapter
        adapter = new PeopleAdapter(getPeopleDataFromDatabase(), context); // Implement this function
        recyclerView.setAdapter(adapter);
        recyclerView.getAdapter().notifyDataSetChanged();
    }
    public void delete_tx(int id){
        MyDatabaseHelper dbHelper = new MyDatabaseHelper(this);
        SQLiteDatabase db = dbHelper.getWritableDatabase();
        String[] args = new String[]{String.valueOf(update_info.getInt(PeopleAdapter.PERSON_ID))};
        db.delete(MyDatabaseHelper.PEOPLES_TABLE, MyDatabaseHelper.PEOPLES_ID + " = ?", args);
    }
}