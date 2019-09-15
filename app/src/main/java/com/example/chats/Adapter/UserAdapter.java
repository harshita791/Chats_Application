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
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.List;
import java.util.zip.Inflater;

public class UserAdapter extends RecyclerView.Adapter<UserAdapter.ViewHolder> {

    private Context mContext;
    private boolean isChat;
    private List<User> mUsers;
    String theLastMessage;



    public UserAdapter( Context mContext,List<User> mUsers, boolean isChat){
        this.mUsers=mUsers;
       this.mContext=mContext;
        this.isChat=isChat;
    }



    @NonNull
    @Override
    public UserAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i){


       View view= LayoutInflater.from(mContext).inflate(R.layout.user_item,viewGroup,false);
        return new UserAdapter.ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder viewHolder, int i) {

        final User user=mUsers.get(i);
        viewHolder.username.setText(user.getUsername());
        if(user.getImageURL().equals("default")){
            viewHolder.profile_img.setImageResource(R.drawable.ic_person_black_24dp);
        }
        else {
            Glide.with(mContext).load(user.getImageURL()).into(viewHolder.profile_img);
        }

        if(isChat){
            lastMessage(user.getId(),viewHolder.last_msg);
        }
        else {
            viewHolder.last_msg.setVisibility(View.GONE);
        }
      if(isChat) {
          if (user.getStatus().equals("online")) {
              viewHolder.img_on.setVisibility(View.VISIBLE);
              viewHolder.img_off.setVisibility(View.GONE);
          }
          else {
              viewHolder.img_off.setVisibility(View.VISIBLE);
              viewHolder.img_on.setVisibility(View.GONE);
          }
      }
      else {
          viewHolder.img_on.setVisibility(View.GONE);
          viewHolder.img_off.setVisibility(View.GONE);
      }
        viewHolder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i=new Intent(mContext, MessageActivity.class);
                i.putExtra("userid",user.getId());
                mContext.startActivity(i);
            }
        });
    }



    @Override
    public int getItemCount() {
        if(mUsers!=null)
           return mUsers.size();
        else
            return 0;
    }


    public class ViewHolder extends RecyclerView.ViewHolder{
        public TextView username;
        public ImageView profile_img;
        private ImageView img_on;
        private ImageView img_off;
        private TextView last_msg;


        public ViewHolder(View itemView){
            super(itemView);

            username=itemView.findViewById(R.id.username);
            profile_img=itemView.findViewById(R.id.profile_img);
            img_off=itemView.findViewById(R.id.img_off);
            img_on=itemView.findViewById(R.id.img_on);
            last_msg=itemView.findViewById(R.id.last_msg);
        }
    }
    private void lastMessage(final String userid, final TextView last_msg){

        theLastMessage="default";
        final FirebaseUser firebaseUser=FirebaseAuth.getInstance().getCurrentUser();
        DatabaseReference reference= FirebaseDatabase.getInstance().getReference("Chats");

        if(firebaseUser != null)
        reference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                for(DataSnapshot snapshot:dataSnapshot.getChildren()){
                    Chat chat=snapshot.getValue(Chat.class);

                    assert chat != null;
                    try {
                        if (chat.getReciever().equals(firebaseUser.getUid()) && chat.getSender().equals(userid) ||
                                chat.getReciever().equals(userid) && chat.getSender().equals(firebaseUser.getUid())) {
                            theLastMessage = chat.getMessage();
                        }
                    }catch(NullPointerException e){
                        e.printStackTrace();
                    }
                }

                switch (theLastMessage){
                    case "default":
                        last_msg.setText("No message");
                        break;
                        default:
                            last_msg.setText(theLastMessage);
                            break;

                }
                theLastMessage="default";
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }
}
