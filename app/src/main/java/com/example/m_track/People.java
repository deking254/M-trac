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
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
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
                Intent addtx = new Intent(People.this, addperson.class);
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
        //AdapterView.AdapterContextMenuInfo info = (AdapterView.AdapterContextMenuInfo) item.getMenuInfo();
        //int selectedItemPosition = info.position;

        if (item.getItemId() == R.id.action_editperson){
            // Handle edit action
            // Example: Start an edit activity or show a dialog
            Intent intent = new Intent(this, addperson.class);
            update_info.putString("request", "update");
            intent.putExtras(update_info);
            startActivity(intent);
            return true;}
        if (item.getItemId() == R.id.action_deleteperson) {
            // Handle delete action
            // Example: Remove the item from your data source and update the RecyclerView
            delete_tx(update_info.getInt("person_id"));
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
        if (message.matches("new tx")){
            reload_list(getPeopleDataFromDatabase(), this, recyclerView);
        }
        if (message.matches("success")){
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
        MyDatabaseHelper dbHelper = new MyDatabaseHelper(this);
        SQLiteDatabase db = dbHelper.getWritableDatabase();
        String[] projection={"people.id","people.fullname", "SUM(transactions.amount) AS net"};
        String query = "SELECT people.id, people.fullname, SUM(transactions.amount) AS net FROM people INNER JOIN transactions ON people.id = transactions.person GROUP BY people.id";
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
        String[] args = new String[]{String.valueOf(update_info.getInt("person_id"))};
        db.delete("people", "id = ?", args);
    }
}