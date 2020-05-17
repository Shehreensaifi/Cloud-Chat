package com.example.rainbowactivity;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.google.firebase.auth.FirebaseAuth;

import java.util.List;

public class MessageAdapter extends RecyclerView.Adapter<MessageAdapter.MessageViewHolder>
{
    private List<MessageClass> mMessageList;
    private FirebaseAuth mAuth;
    private OnLongClicked context;
    private static final int sender=1;
    private static final int receiver=0;
    public interface OnLongClicked
    {
        void clicked(int index);
    }


    public MessageAdapter(List<MessageClass> mMessageList,OnLongClicked context) {
        this.mMessageList = mMessageList;
        this.context=context;
    }

    @Override
    public int getItemViewType(int position) {
        mAuth=FirebaseAuth.getInstance();
        String current_user_id=mAuth.getCurrentUser().getUid();
        MessageClass c=mMessageList.get(position);
        String from_user=c.getFrom();
        if(current_user_id.equals(from_user))
        {
           return sender;
        }
        return receiver;
    }

    @NonNull
    @Override
    public MessageViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        if(viewType==sender) {
            View view = LayoutInflater.from(parent.getContext())
                    .inflate(R.layout.message2_custom, parent, false);
            return new MessageViewHolder(view);
        }
        else
        {
            View view = LayoutInflater.from(parent.getContext())
                    .inflate(R.layout.send_message_custom, parent, false);
            return new MessageViewHolder(view);
        }

    }


    @Override
    public void onBindViewHolder(@NonNull MessageViewHolder holder, final int position)
    {
        MessageClass c=mMessageList.get(position);
        holder.tvSendText.setText(c.getMessage());
        holder.messageView.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View v) {
                context.clicked(position);
                return true;
            }
        });


    }
    @Override
    public int getItemCount() {
        return mMessageList.size();
    }


    public static class MessageViewHolder extends RecyclerView.ViewHolder
    {
        public TextView tvSendText;
        View messageView;
        public MessageViewHolder(@NonNull View itemView) {
            super(itemView);
            messageView=itemView;
            tvSendText=itemView.findViewById(R.id.tvSendText);
        }
    }
}
