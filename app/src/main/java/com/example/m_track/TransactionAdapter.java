package com.example.m_track;

import android.annotation.SuppressLint;
import android.content.Context;
import android.database.Cursor;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MenuInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import androidx.recyclerview.widget.RecyclerView;

import org.greenrobot.eventbus.EventBus;
import org.joda.time.DateTime;

import java.sql.Date;

public class TransactionAdapter extends RecyclerView.Adapter<TransactionAdapter.TransactionViewHolder> {
    private Cursor cursor;
    IdHandler id;
    private Context context;
    public static final String TRANSACTION_ID = "transaction_id";
    public static final String PERSON_ID = "person_id";
    public static final String AMOUNT_INT = "amount_int";

    public TransactionAdapter(Cursor cursor, Context context) {
        this.cursor = cursor;
        this.context = context;
    }

    @Override
    public TransactionViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.transaction_item, parent, false);
//        view.setOnLongClickListener(new View.OnLongClickListener() {
//            @Override
//            public boolean onLongClick(View view) {
//                @SuppressLint("Range") int id_selected = cursor.getInt(cursor.getColumnIndex("id"));
//                id = new IdHandler(id_selected);
//                EventBus.getDefault().post(id);
//                return false;
//            }
//        });
        return new TransactionViewHolder(view);
    }

    @Override
    public void onBindViewHolder(TransactionViewHolder holder, int position) {
        if (!cursor.moveToPosition(position)) {
            return;
        }

        // Replace these with the actual column indices from your database
        @SuppressLint("Range") String description = cursor.getString(cursor.getColumnIndex(MyDatabaseHelper.TRANSACTIONS_DESCRIPTION));
        @SuppressLint("Range") String date = cursor.getString(cursor.getColumnIndex(MyDatabaseHelper.TRANSACTIONS_DATE));
//        @SuppressLint("Range") Long date_date = cursor.getLong(cursor.getColumnIndex("date"));
        @SuppressLint("Range") int amount = cursor.getInt(cursor.getColumnIndex(MyDatabaseHelper.TRANSACTIONS_AMOUNT));
        @SuppressLint("Range") int account = cursor.getInt(cursor.getColumnIndex(MyDatabaseHelper.TRANSACTIONS_ACCOUNT));
        @SuppressLint("Range") int person = cursor.getInt(cursor.getColumnIndex(MyDatabaseHelper.TRANSACTIONS_PERSON));
        @SuppressLint("Range") String nature = cursor.getString(cursor.getColumnIndex(MyDatabaseHelper.TRANSACTIONS_NATURE));
        @SuppressLint("Range") String type = cursor.getString(cursor.getColumnIndex(MyDatabaseHelper.TRANSACTIONS_TYPE));
        @SuppressLint("Range") int id = cursor.getInt(cursor.getColumnIndex(UpdateTx.RECYCLER_VIEW_ID));
        @SuppressLint("Range") String fullname = cursor.getString(cursor.getColumnIndex(MyDatabaseHelper.PEOPLES_FULL_NAME));
        holder.descriptionTextView.setText(description);
        holder.date = date;
        //I used the datetextview as the fullname displayer because I was just too lazy to create or refactor. Deal with it!!!
        holder.dateTextView.setText(fullname);
        holder.amountTextView.setText(String.valueOf(amount));
        holder.natureTextView.setText(nature);
        holder.nature = nature;
        holder.type = type;
        holder.id = id;
        holder.person = person;
        holder.date = date;
        holder.amount = amount;
        holder.itemView.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View view) {
                Bundle update_info = new Bundle();
                Update_infoHandler updates;
                update_info.putString(MyDatabaseHelper.TRANSACTIONS_DESCRIPTION, (String) holder.descriptionTextView.getText());
                update_info.putString(MyDatabaseHelper.TRANSACTIONS_AMOUNT, (String) holder.amountTextView.getText());
                update_info.putString(MyDatabaseHelper.TRANSACTIONS_TYPE, holder.type);
                update_info.putString(MyDatabaseHelper.TRANSACTIONS_NATURE, holder.nature);
                update_info.putInt(MyDatabaseHelper.TRANSACTIONS_ACCOUNT, holder.account);
                update_info.putInt(PERSON_ID, holder.person);
                update_info.putInt(TRANSACTION_ID, holder.id);
                update_info.putString(MyDatabaseHelper.TRANSACTIONS_DATE, holder.date);
                update_info.putInt(AMOUNT_INT, holder.amount);
                update_info.putString(MyDatabaseHelper.PEOPLES_FULL_NAME, holder.fullname);
                updates  = new Update_infoHandler(update_info);
                EventBus.getDefault().post(updates);
                return false;
            }
        });
    }

    @Override
    public int getItemCount() {
//     returns number of records in the cursor
        return cursor.getCount();
    }
    public class TransactionViewHolder extends RecyclerView.ViewHolder {
//        This class returns an object of the elements of one view of a transaction
        public TextView descriptionTextView;
        public TextView dateTextView;
        public TextView natureTextView;
        public String date;
        public int amount;
        public TextView amountTextView;
        public int id;
        public String nature;
        public String fullname;
        public String type;
        public int account;
        public int person;

        public TransactionViewHolder(View itemView) {
            super(itemView);
            descriptionTextView = itemView.findViewById(R.id.descriptionTextView);
            dateTextView = itemView.findViewById(R.id.dateTextView);
            amountTextView = itemView.findViewById(R.id.amountTextView);
            natureTextView = itemView.findViewById(R.id.natureTextview);
        }
    }
}

