package com.example.m_track;

import androidx.appcompat.app.AppCompatActivity;

import android.content.ContentValues;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.SimpleCursorAdapter;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import org.greenrobot.eventbus.EventBus;

public class UpdateTx extends AppCompatActivity {
    MyDatabaseHelper dbHelper;
    SQLiteDatabase db;
    Cursor acc;
    int id;
    public Bundle bundleinfo;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_update_tx);
        dbHelper = new MyDatabaseHelper(this);
        db = dbHelper.getWritableDatabase();
        update_ui(db);
        bundleinfo = getIntent().getExtras();
        Button save = findViewById(R.id.save);
        Spinner people = findViewById(R.id.people);
        Spinner natureView = findViewById(R.id.nature);
        Spinner type_text = findViewById(R.id.type);
        TextView amountView = (TextView) findViewById(R.id.textInputEditamont);
        TextView descriptionView = (TextView) findViewById(R.id.textInputEditdescription);
        save.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                EventHandler inserted = new EventHandler("success");
                String[] wherearg;
                if (bundleinfo != null) {
                    if (bundleinfo.getString("request").matches("update")) {
                        int tx_id = bundleinfo.getInt("tx_id");
                        wherearg = new String[]{String.valueOf(tx_id)};
                        ContentValues newtx = new ContentValues();
                        int amount = Integer.parseInt(amountView.getText().toString());
                        String type = type_text.getSelectedItem().toString();
                        Log.i("agora", String.valueOf(tx_id));
                        if (type.matches("Debt")) {
                            if (amount > 0) {
                                amount = amount * -1;
                            }
                        }else{
                            if (amount < 0){
                                amount = amount * -1;
                            }
                        }
                        int person = bundleinfo.getInt("person_id");
                        Log.i("super2", String.valueOf(person));
                        String nature = natureView.getSelectedItem().toString();
                        String description = descriptionView.getText().toString();
                        newtx.put("amount", amount);
                        newtx.put("account", bundleinfo.getInt("account"));
                        newtx.put("nature", nature);
                        newtx.put("type", type);
                        newtx.put("description", description);
                        newtx.put("person", person);
                        Log.i("tyrone", newtx.getAsString("amount"));
                        try {
                            db.update("transactions", newtx, "id = ?", wherearg);
                            finish();
                        } catch (Exception e) {
                            Toast.makeText(UpdateTx.this, "Error", Toast.LENGTH_SHORT).show();
                        }
                    } else if (bundleinfo.getString("request").matches("addnew")) {
                        ContentValues new_insert = new ContentValues();
                        int amount = Integer.parseInt(amountView.getText().toString());
                        String type = type_text.getSelectedItem().toString();
                        Log.i("adding new and the selected type is", type);
                        if (type.matches("Debt")) {
                            if (amount > 0) {
                                amount = amount * -1;
                            }
                        }else{
                            if (amount < 0){
                                amount = amount * -1;
                            }
                        }
                        int person = people.getSelectedView().getId();
                        Log.i("chumbo", String.valueOf(id));
                        String nature = natureView.getSelectedItem().toString();
                        String description = descriptionView.getText().toString();
                        new_insert.put("amount", amount);
                        new_insert.put("account", bundleinfo.getInt("acc_id"));
                        new_insert.put("nature", nature);
                        new_insert.put("type", type);
                        new_insert.put("description", description);
                        new_insert.put("person", id);
                        Log.i("iddde", String.valueOf(id));
                        try {
                            db.insert("transactions", "amount", new_insert);
                            finish();
                        } catch (Exception e) {
                            Toast.makeText(UpdateTx.this, "Error", Toast.LENGTH_SHORT).show();
                        }
                    }
                }
            }
        });
    }

    public void update_ui(SQLiteDatabase db) {
        Bundle updateinfo = getIntent().getExtras();
        String[] projection = {"id AS _id", "fullname"};
        Cursor people = db.query("people", projection, null, null, null, null, null);
        Spinner people_list = (Spinner) findViewById(R.id.people);
        Spinner nature_list = (Spinner) findViewById(R.id.nature);
        Spinner type_list = (Spinner) findViewById(R.id.type);
        TextView amount = (TextView) findViewById(R.id.textInputEditamont);
        TextView description = (TextView) findViewById(R.id.textInputEditdescription);
        int[] to = new int[]{android.R.id.text1};
        String[] columns = new String[]{"fullname", "_id"};
        people_list.setAdapter(new SimpleCursorAdapter(this, android.R.layout.simple_list_item_1, people, columns, to, 0));
        nature_list.setAdapter(new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1, new String[]{"Cash", "Mpesa"}));
        type_list.setAdapter(new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1, new String[]{"Debt", "Income", "Transactional"}));
        people_list.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                if (people.moveToPosition(i)){
                    id = people.getInt(people.getColumnIndex("_id"));
                    bundleinfo.putInt("person_id", id);
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {
                if(people.moveToFirst()){
                    id = people.getInt(people.getColumnIndex("_id"));
                    if (updateinfo.getString("request").matches("update")){
                        return;
                    }else{
                        bundleinfo.putInt("person_id", id);
                    }
                }
            }
        });
        if (updateinfo != null) {
            if (updateinfo.getString("request").matches("update")) {
                if (updateinfo.getString("nature").matches("Cash")) {
                    nature_list.setSelection(0);
                } else {
                    nature_list.setSelection(1);
                }
                if (updateinfo.getString("type").matches("Debt")) {
                    type_list.setSelection(0);
                } else {
                    type_list.setSelection(1);
                }
                people_list.setSelection(updateinfo.getInt("personposition"));
                amount.setText(updateinfo.getString("amount"));
                description.setText(updateinfo.getString("description"));
            }else{
                nature_list.setSelection(0);
                type_list.setSelection(0);
            }
        }
    }
}