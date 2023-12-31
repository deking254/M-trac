package com.example.m_track;

import android.annotation.SuppressLint;
import android.content.Context;
import android.database.Cursor;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import org.greenrobot.eventbus.EventBus;

public class PeopleAdapter extends RecyclerView.Adapter<PeopleAdapter.PeopleViewHolder> {
    private Cursor cursor;
    public static final  String PERSON_ID = "person_id";
    public static final  String NET_TOTAL = "net";

    IdHandler id;
    private Context context;

    public PeopleAdapter(Cursor cursor, Context context) {
        this.cursor = cursor;
        this.context = context;
    }

    @Override
    public PeopleViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.person_item, parent, false);
//        view.setOnLongClickListener(new View.OnLongClickListener() {
//            @Override
//            public boolean onLongClick(View view) {
//                @SuppressLint("Range") int id_selected = cursor.getInt(cursor.getColumnIndex("id"));
//                id = new IdHandler(id_selected);
//                EventBus.getDefault().post(id);
//                return false;
//            }
//        });
        return new PeopleViewHolder(view);
    }

    @Override
    public void onBindViewHolder(PeopleViewHolder holder, int position) {
        if (!cursor.moveToPosition(position)) {
            return;
        }

        // Replace these with the actual column indices from your database

        @SuppressLint("Range") String fullname = cursor.getString(cursor.getColumnIndex(MyDatabaseHelper.PEOPLES_FULL_NAME));
        @SuppressLint("Range") String net = cursor.getString(cursor.getColumnIndex(NET_TOTAL));
        @SuppressLint("Range") int id = cursor.getInt(cursor.getColumnIndex(MyDatabaseHelper.PEOPLES_ID));
        holder.fullname.setText(fullname);
        holder.net.setText(net);
        holder.id = id;
        holder.itemView.setOnLongClickListener(new View.OnLongClickListener() {
            @SuppressLint("Range")
            @Override
            public boolean onLongClick(View view) {
                Bundle update_info = new Bundle();
                Update_personinfoHandler updates;
                if (cursor.getInt(cursor.getColumnIndex(MyDatabaseHelper.PEOPLES_ID)) != -1) {
                    update_info.putInt(PERSON_ID, id);
                }
                else{
                    return false;
                }
                if (cursor.getInt(cursor.getColumnIndex(MyDatabaseHelper.PEOPLES_FULL_NAME)) != -1) {
                    update_info.putString(MyDatabaseHelper.PEOPLES_FULL_NAME, fullname);
                } else{
                    return false;
                }
                updates  = new Update_personinfoHandler(update_info);
                EventBus.getDefault().post(updates);
                return false;
            }
        });
    }

    @Override
    public int getItemCount() {
        return cursor.getCount();
    }

    public class PeopleViewHolder extends RecyclerView.ViewHolder {
        public TextView fullname;
        public TextView net;
        public int id;

        public PeopleViewHolder(View itemView) {
            super(itemView);
            fullname = itemView.findViewById(R.id.fullname);
            net = itemView.findViewById(R.id.netTotal);
        }
    }
}

