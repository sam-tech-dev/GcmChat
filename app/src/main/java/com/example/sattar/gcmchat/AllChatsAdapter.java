package com.example.sattar.gcmchat;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import java.text.SimpleDateFormat;
import java.util.ArrayList;

/**
 * Created by Sattar on 6/5/2016.
 */
public class AllChatsAdapter extends RecyclerView.Adapter<AllChatsAdapter.ViewHolder >{

    private Context context;
    private ArrayList<AllChatsWrapper> allChatList;

    private RCVClickListener listener;


     public AllChatsAdapter(Context context, ArrayList<AllChatsWrapper> listChats) {
        this.context = context;
        this.allChatList = listChats;
    }

    public void setOnRCVClickListener(RCVClickListener listener) {
        this.listener = listener;
    }


    @Override
    public AllChatsAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View rootView = LayoutInflater.from(parent.getContext()).inflate(R.layout.activity_all_chats_adapter, parent, false);
        ViewHolder holder = new ViewHolder(rootView);

        return holder;
    }

    @Override
    public void onBindViewHolder(AllChatsAdapter.ViewHolder holder, int position) {

        AllChatsWrapper obj = allChatList.get(position);

        if(obj.get_name().equals("Unknown Person")){
            holder.addresseTv.setText(obj.get_number()); }
        else{
            holder.addresseTv.setText(obj.get_name()); }

        holder.countTv.setText(obj.get_count());
        holder.contentTv.setText(obj.get_content());

        long date = System.currentTimeMillis();

        SimpleDateFormat sdf = new SimpleDateFormat("dd MMM");
        String dateString = sdf.format(date);

        if(obj.get_date().equals(dateString)){
            holder.dateTv.setText(obj.get_time()); }
        else{
            holder.dateTv.setText(obj.get_date()); }

    }

    @Override
    public int getItemCount() {
        return allChatList.size();
    }


    public class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

        TextView addresseTv,contentTv,dateTv,countTv;
        LinearLayout itemHolder;

        public ViewHolder(View view) {
            super(view);

            addresseTv = (TextView) view.findViewById(R.id.personaddress);
            countTv = (TextView) view.findViewById(R.id.noofSms);
            contentTv = (TextView) view.findViewById(R.id.messagecontent);
            dateTv = (TextView) view.findViewById(R.id.dateofmsg);

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
