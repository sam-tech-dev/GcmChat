package com.example.sattar.gcmchat;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import java.sql.Wrapper;
import java.text.SimpleDateFormat;
import java.util.ArrayList;

/**
 * Created by Sattar on 6/5/2016.
 */
public class ContactsAdapter extends RecyclerView.Adapter<ContactsAdapter.ViewHolder >{

    private Context context;
    private ArrayList<ContactsWrapper> ContactList;

    private RCVClickListener listener;


    public ContactsAdapter(Context context, ArrayList<ContactsWrapper> listChats) {
        this.context = context;
        this.ContactList = listChats;
    }

    public void setOnRCVClickListener(RCVClickListener listener) {
        this.listener = listener;
    }


    @Override
    public ContactsAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View rootView = LayoutInflater.from(parent.getContext()).inflate(R.layout.activity_contacts_adapter, parent, false);
        ViewHolder holder = new ViewHolder(rootView);

        return holder;
    }

    @Override
    public void onBindViewHolder(ContactsAdapter.ViewHolder holder, int position) {

        ContactsWrapper obj = ContactList.get(position);


        holder.nameTv.setText(obj.get_name());
        holder.numberTv.setText(obj.get_numbers());
        holder.statusTv.setText(obj.get_status());




    }

    @Override
    public int getItemCount() {
        return ContactList.size();
    }


    public class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

        TextView nameTv,numberTv,statusTv;
        LinearLayout itemHolder;

        public ViewHolder(View view) {
            super(view);

            nameTv = (TextView) view.findViewById(R.id.personNmae);
            numberTv = (TextView) view.findViewById(R.id.mobile);
            statusTv = (TextView) view.findViewById(R.id.status);
            itemHolder = (LinearLayout) itemView.findViewById(R.id.itemHolder);
            itemHolder.setOnClickListener(this);

        }

        @Override
        public void onClick(View v) {
            if (listener != null) {
                listener.onRCVClick(v, getAdapterPosition());
            }
        }
    }


}
