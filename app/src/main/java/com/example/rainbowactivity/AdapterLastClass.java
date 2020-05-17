package com.example.rainbowactivity;

import android.app.AlertDialog;
import android.content.Intent;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.ListView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.core.Context;

import java.util.List;

public class AdapterLastClass extends RecyclerView.Adapter<AdapterLastClass.ChatHolder>
{
    private List<ChatLastMessageClass> mMessageList;
    private ChatFrag current;
    private FirebaseAuth mAuth;

    public interface OnItemClicked
    {
        void itemClicked(int index);
        void longClickedDelete(int index);

    }

    public AdapterLastClass(List<ChatLastMessageClass> mMessageList, ChatFrag current) {
        this.mMessageList = mMessageList;
        this.current = current;
        this.mAuth=FirebaseAuth.getInstance();
    }

    @NonNull
    @Override
    public ChatHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view= LayoutInflater.from(parent.getContext())
                .inflate(R.layout.chat_custom,parent,false);
        return new ChatHolder(view);
    }


    @Override
    public void onBindViewHolder(@NonNull ChatHolder holder, final int position) {

        holder.tvNameChat.setText(mMessageList.get(position).getName());
        holder.tvLastText.setText(mMessageList.get(position).getMessage());
        String from=mMessageList.get(position).getFrom();
        String image=mMessageList.get(position).getImageUrl();
        if(image!=null) {
            Glide.with(current)
                    .load(image)
                    .placeholder(R.drawable.person2)
                    .apply(RequestOptions.circleCropTransform())
                    .into(holder.ibProfileChat);
        }

        holder.UserView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                current.itemClicked(position);
            }
        });
        if(from.equals(mAuth.getUid()))
        {
           holder.tvLastText.setTextColor(Color.GRAY);
        }
        else
        {
            holder.tvLastText.setTextColor(Color.BLUE);
        }
        holder.UserView.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View v) {
                current.longClickedDelete(position);
                return true;
            }
        });
    }


    @Override
    public int getItemCount() {
        return mMessageList.size();
    }
    public  static class ChatHolder extends RecyclerView.ViewHolder
    {
        ImageButton ibProfileChat;
        TextView tvNameChat,tvLastText;
        View UserView;
        public ChatHolder(@NonNull View itemView) {
            super(itemView);
            UserView=itemView;
            ibProfileChat=itemView.findViewById(R.id.ibProfile);
            tvNameChat=itemView.findViewById(R.id.tvNameChat);
            tvLastText=itemView.findViewById(R.id.tvLastText);
        }
    }
}
