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
        @SuppressLint("Range") String description = cursor.getString(cursor.getColumnIndex("description"));
        @SuppressLint("Range") String date = cursor.getString(cursor.getColumnIndex("date"));
        @SuppressLint("Range") Long date_date = cursor.getLong(cursor.getColumnIndex("date"));
        @SuppressLint("Range") int amount = cursor.getInt(cursor.getColumnIndex("amount"));
        @SuppressLint("Range") int account = cursor.getInt(cursor.getColumnIndex("account"));
        @SuppressLint("Range") int person = cursor.getInt(cursor.getColumnIndex("person"));
        @SuppressLint("Range") String nature = cursor.getString(cursor.getColumnIndex("nature"));
        @SuppressLint("Range") String type = cursor.getString(cursor.getColumnIndex("type"));
        @SuppressLint("Range") int id = cursor.getInt(cursor.getColumnIndex("_id"));
        @SuppressLint("Range") String fullname = cursor.getString(cursor.getColumnIndex("fullname"));
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
                update_info.putString("description", (String) holder.descriptionTextView.getText());
                update_info.putString("amount", (String) holder.amountTextView.getText());
                update_info.putString("type", holder.type);
                update_info.putString("nature", holder.nature);
                update_info.putInt("account", holder.account);
                update_info.putInt("person_id", holder.person);
                update_info.putInt("tx_id", holder.id);
                update_info.putString("date", holder.date);
                update_info.putInt("amount_int", holder.amount);
                update_info.putLong("date_Long", date_date);
                update_info.putString("fullname", holder.fullname);
                updates  = new Update_infoHandler(update_info);
                EventBus.getDefault().post(updates);
                return false;
            }
        });
    }

    @Override
    public int getItemCount() {
        return cursor.getCount();
    }

    public class TransactionViewHolder extends RecyclerView.ViewHolder {
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

