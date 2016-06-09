package com.example.sattar.gcmchat;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import java.text.SimpleDateFormat;
import java.util.ArrayList;

/**
 * Created by Sattar on 6/6/2016.
 */
public class IndividualChatAdapter extends RecyclerView.Adapter<IndividualChatAdapter.ViewHolder >{

    private final static String TAG = IndividualChatAdapter.class.getSimpleName();
    Context context;
    ArrayList<IndividualChatwrapper>  IndividualChatList;


    private RCVClickListener listener;


    public IndividualChatAdapter(Context context, ArrayList<IndividualChatwrapper> listChats) {
        this.context = context;
        this.IndividualChatList = listChats;
    }

    public void setOnRCVClickListener(RCVClickListener listener) {
        this.listener = listener;
    }



    @Override
    public IndividualChatAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View rootView = LayoutInflater.from(parent.getContext()).inflate(R.layout.activity_individual_chat_adapter, parent, false);
        ViewHolder holder = new ViewHolder(rootView);

        return holder;
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {

        IndividualChatwrapper obj =  IndividualChatList.get(position);

        Boolean mySelf=obj.get_type().equals("sent");

        setAlignment(holder,mySelf);

        holder.messageTv.setText(obj.get_content());

        long date = System.currentTimeMillis();

        SimpleDateFormat sdf = new SimpleDateFormat("dd MMM");
        String dateString = sdf.format(date);


        String dateOfMsg=obj.get_date();
        String timeofMsg=obj.get_time();

        if(obj.get_date().equals(dateString)){
            dateOfMsg="Today"; }

        String msgInfo=dateOfMsg+" "+timeofMsg;

        holder.dateTv.setText(msgInfo);


    }



    @Override
    public int getItemCount() {
        return IndividualChatList.size();
    }






    private  void   setAlignment(ViewHolder holder,Boolean mySelf){

        if(!mySelf){

            holder.textBackground.setBackgroundResource(R.drawable.bg1);

            LinearLayout.LayoutParams layoutparameter=(LinearLayout.LayoutParams)holder.textBackground.getLayoutParams();
            layoutparameter.gravity=Gravity.LEFT;
            holder.textBackground.setLayoutParams(layoutparameter);

            RelativeLayout.LayoutParams laypara = (RelativeLayout.LayoutParams) holder.content.getLayoutParams();
            laypara.addRule(RelativeLayout.ALIGN_PARENT_LEFT);
            laypara.addRule(RelativeLayout.ALIGN_PARENT_RIGHT,0);
            holder.content.setLayoutParams(laypara);

            layoutparameter = (LinearLayout.LayoutParams) holder.messageTv.getLayoutParams();
            layoutparameter.gravity = Gravity.LEFT;
            holder.messageTv.setLayoutParams(layoutparameter);

            layoutparameter = (LinearLayout.LayoutParams) holder.dateTv .getLayoutParams();
            layoutparameter.gravity = Gravity.LEFT;
            holder.dateTv .setLayoutParams(layoutparameter);


        }
        else{

            holder.textBackground.setBackgroundResource(R.drawable.bg2);

            LinearLayout.LayoutParams layoutparameter=(LinearLayout.LayoutParams)holder.textBackground.getLayoutParams();
            layoutparameter.gravity=Gravity.RIGHT;
            holder.textBackground.setLayoutParams(layoutparameter);

            RelativeLayout.LayoutParams laypara = (RelativeLayout.LayoutParams) holder.content.getLayoutParams();
            laypara.addRule(RelativeLayout.ALIGN_PARENT_LEFT,0);
            laypara.addRule(RelativeLayout.ALIGN_PARENT_RIGHT);
            holder.content.setLayoutParams(laypara);

            layoutparameter = (LinearLayout.LayoutParams) holder.messageTv.getLayoutParams();
            layoutparameter.gravity = Gravity.RIGHT;
            holder.messageTv.setLayoutParams(layoutparameter);

            layoutparameter = (LinearLayout.LayoutParams) holder.dateTv .getLayoutParams();
            layoutparameter.gravity = Gravity.RIGHT;
            holder.dateTv .setLayoutParams(layoutparameter);




        }

    }



    public class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

        TextView messageTv,dateTv;
        LinearLayout content,textBackground;

        public ViewHolder(View view) {
            super(view);

            textBackground=(LinearLayout)view.findViewById(R.id.textBackground);
            content=(LinearLayout)view.findViewById(R.id.content);
            messageTv = (TextView)view.findViewById(R.id.message);
            dateTv = (TextView) view.findViewById(R.id.dateTime);

        }

        @Override
        public void onClick(View v) {
            if (listener != null) {
                listener.onRCVClick(v, getAdapterPosition());
            }
        }
    }
}
