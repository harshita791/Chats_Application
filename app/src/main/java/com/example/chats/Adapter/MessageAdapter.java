package com.example.chats.Adapter;

import android.content.Context;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.example.chats.MessageActivity;
import com.example.chats.Model.Chat;
import com.example.chats.Model.User;
import com.example.chats.R;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

import java.util.List;

public class MessageAdapter  extends RecyclerView.Adapter<MessageAdapter.ViewHolder> {

    public static final int MSG_TYPE_LEFT=0;
    public static final int MSG_TYPE_RIGHT=1;

    private Context mContext;
    private List<Chat> mChat;
    private String imageurl;

    FirebaseUser fuser;


    public MessageAdapter(Context mContext,List<Chat> mChat,String imageurl){
        this.mChat=mChat;
        this.mContext=mContext;
        this.imageurl=imageurl;
    }



    @NonNull
    @Override
    public MessageAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {

        if(i==MSG_TYPE_RIGHT) {
            View view = LayoutInflater.from(mContext).inflate(R.layout.chat_item_right, viewGroup, false);
            return new MessageAdapter.ViewHolder(view);
        }
        else{
            View view = LayoutInflater.from(mContext).inflate(R.layout.chat_item_left, viewGroup, false);
            return new MessageAdapter.ViewHolder(view);
        }
    }

    @Override
    public void onBindViewHolder(@NonNull MessageAdapter.ViewHolder viewHolder, int i) {

        Chat chat=mChat.get(i);
        viewHolder.show_msg.setText(chat.getMessage());
        if(imageurl.equals("default")){
            viewHolder.profile_img.setImageResource(R.drawable.ic_person_black_24dp);
        }
        else {
            Glide.with(mContext).load(imageurl).into(viewHolder.profile_img);
        }

        if(i==mChat.size()-1){
            if(chat.isIsseen()){
                viewHolder.txt_seen.setText("Seen");
            }
            else
                viewHolder.txt_seen.setText("Delievered");
        }
        else {
            viewHolder.txt_seen.setVisibility(View.GONE);
        }

    }



    @Override
    public int getItemCount() {
        return mChat.size();
    }


    public class ViewHolder extends RecyclerView.ViewHolder{
        public TextView show_msg;
        public ImageView profile_img;
        public TextView txt_seen;

        public ViewHolder(View itemView){
            super(itemView);

            show_msg=itemView.findViewById(R.id.show_msg);
            profile_img=itemView.findViewById(R.id.profile_img);
            txt_seen=itemView.findViewById(R.id.seen);
        }
    }

    @Override
    public int getItemViewType(int position) {
        fuser= FirebaseAuth.getInstance().getCurrentUser();
        if(mChat.get(position).getSender().equals(fuser.getUid())){
            return MSG_TYPE_RIGHT;
        }
        else{
            return MSG_TYPE_LEFT;
        }
    }
}


