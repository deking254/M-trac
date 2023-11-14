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
    public static final String RESPONSE_SUCCESS = "success";
    public static final String RESPONSE_FAIL = "fail";
    public static final String ERROR_UPDATING_TRANSACTION = "Could not update the transaction";
    public static final String RECYCLER_VIEW_ID = "_id";
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
                EventHandler inserted = new EventHandler(RESPONSE_SUCCESS);
                String[] wherearg;
                if (bundleinfo != null) {
                    if (bundleinfo.getString(People.REQUEST).matches(MainActivity.REQUEST_UPDATE_TRANSACTION)) {
                        int transaction_id = bundleinfo.getInt(TransactionAdapter.TRANSACTION_ID);
                        wherearg = new String[]{String.valueOf(transaction_id)};
                        ContentValues newtx = new ContentValues();
                        int amount = Integer.parseInt(amountView.getText().toString());
                        String type = type_text.getSelectedItem().toString();
                        if (type.matches(TransactionParser.OUT_TYPE_VALUE_)) {
                            if (amount > 0) {
                                amount = amount * -1;
                            }
                        }else{
                            if (amount < 0){
                                amount = amount * -1;
                            }
                        }
                        int person = bundleinfo.getInt(TransactionAdapter.PERSON_ID);
                        String nature = natureView.getSelectedItem().toString();
                        String description = descriptionView.getText().toString();
                        newtx.put(MyDatabaseHelper.TRANSACTIONS_AMOUNT, amount);
                        newtx.put(MyDatabaseHelper.TRANSACTIONS_ACCOUNT, bundleinfo.getInt(MyDatabaseHelper.TRANSACTIONS_ACCOUNT));
                        newtx.put(MyDatabaseHelper.TRANSACTIONS_NATURE, nature);
                        newtx.put(MyDatabaseHelper.TRANSACTIONS_TYPE, type);
                        newtx.put(MyDatabaseHelper.TRANSACTIONS_DESCRIPTION, description);
                        newtx.put(MyDatabaseHelper.TRANSACTIONS_PERSON, person);
                        try {
                            db.update(MyDatabaseHelper.TRANSACTIONS_TABLE, newtx, MyDatabaseHelper.TRANSACTIONS_ID + " = ?", wherearg);
                            finish();
                        } catch (Exception e) {
                            Toast.makeText(UpdateTx.this, ERROR_UPDATING_TRANSACTION, Toast.LENGTH_SHORT).show();
                        }
                    } else if (bundleinfo.getString(MainActivity.REQUEST).matches(MainActivity.REQUEST_NEW_TRANSACTION)) {
                        ContentValues new_insert = new ContentValues();
                        int amount = Integer.parseInt(amountView.getText().toString());
                        String type = type_text.getSelectedItem().toString();
                        if (type.matches(TransactionParser.OUT_TYPE_VALUE_)) {
                            if (amount > 0) {
                                amount = amount * -1;
                            }
                        }else{
                            if (amount < 0){
                                amount = amount * -1;
                            }
                        }
                        int person = people.getSelectedView().getId();
                        String nature = natureView.getSelectedItem().toString();
                        String description = descriptionView.getText().toString();
                        new_insert.put(MyDatabaseHelper.TRANSACTIONS_AMOUNT, amount);
                        new_insert.put(MyDatabaseHelper.TRANSACTIONS_ACCOUNT, bundleinfo.getInt(MainActivity.ACCOUNT_ID_LABEL));
                        new_insert.put(MyDatabaseHelper.TRANSACTIONS_NATURE, nature);
                        new_insert.put(MyDatabaseHelper.TRANSACTIONS_TYPE, type);
                        new_insert.put(MyDatabaseHelper.TRANSACTIONS_DESCRIPTION, description);
                        new_insert.put(MyDatabaseHelper.TRANSACTIONS_PERSON, id);
                        try {
                            db.insert(MyDatabaseHelper.TRANSACTIONS_TABLE, MyDatabaseHelper.TRANSACTIONS_PERSON, new_insert);
                            finish();
                        } catch (Exception e) {
                            Toast.makeText(UpdateTx.this, TransactionParser.ERROR_TRANSACTION_INSERT, Toast.LENGTH_SHORT).show();
                        }
                    }
                }
            }
        });
    }

    public void update_ui(SQLiteDatabase db) {
        Bundle updateinfo = getIntent().getExtras();
        String[] projection = {MyDatabaseHelper.PEOPLES_ID + " AS " + RECYCLER_VIEW_ID, MyDatabaseHelper.PEOPLES_FULL_NAME};
        Cursor people = db.query(MyDatabaseHelper.PEOPLES_TABLE, projection, null, null, null, null, null);
        Spinner people_list = (Spinner) findViewById(R.id.people);
        Spinner nature_list = (Spinner) findViewById(R.id.nature);
        Spinner type_list = (Spinner) findViewById(R.id.type);
        TextView amount = (TextView) findViewById(R.id.textInputEditamont);
        TextView description = (TextView) findViewById(R.id.textInputEditdescription);
        int[] to = new int[]{android.R.id.text1};
        String[] columns = new String[]{MyDatabaseHelper.PEOPLES_FULL_NAME, RECYCLER_VIEW_ID};
        people_list.setAdapter(new SimpleCursorAdapter(this, android.R.layout.simple_list_item_1, people, columns, to, 0));
        nature_list.setAdapter(new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1, new String[]{MainActivity.NATURE_VALUE_CASH, TransactionParser.NATURE_VALUE_MPESA}));
        type_list.setAdapter(new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1, new String[]{TransactionParser.OUT_TYPE_VALUE_, TransactionParser.IN_TYPE_VALUE_, "Transactional"}));
        people_list.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                if (people.moveToPosition(i)){
                    id = people.getInt(people.getColumnIndex(RECYCLER_VIEW_ID));
                    bundleinfo.putInt(TransactionAdapter.PERSON_ID, id);
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {
                if(people.moveToFirst()){
                    id = people.getInt(people.getColumnIndex(RECYCLER_VIEW_ID));
                    if (updateinfo.getString(MainActivity.REQUEST).matches(MainActivity.REQUEST_UPDATE_TRANSACTION)){
                        return;
                    }else{
                        bundleinfo.putInt(TransactionAdapter.PERSON_ID, id);
                    }
                }
            }
        });
        if (updateinfo != null) {
            if (updateinfo.getString(MainActivity.REQUEST).matches(MainActivity.REQUEST_UPDATE_TRANSACTION)) {
                if (updateinfo.getString(MyDatabaseHelper.TRANSACTIONS_NATURE).matches(MainActivity.NATURE_VALUE_CASH)) {
                    nature_list.setSelection(0);
                } else {
                    nature_list.setSelection(1);
                }
                if (updateinfo.getString(MyDatabaseHelper.TRANSACTIONS_TYPE).matches(TransactionParser.OUT_TYPE_VALUE_)) {
                    type_list.setSelection(0);
                } else {
                    type_list.setSelection(1);
                }
                people_list.setSelection(updateinfo.getInt(MainActivity.PERSON_POSITION));
                amount.setText(updateinfo.getString(MyDatabaseHelper.TRANSACTIONS_AMOUNT));
                description.setText(updateinfo.getString(MyDatabaseHelper.TRANSACTIONS_DESCRIPTION));
            }else{
                nature_list.setSelection(0);
                type_list.setSelection(0);
            }
        }
    }
}