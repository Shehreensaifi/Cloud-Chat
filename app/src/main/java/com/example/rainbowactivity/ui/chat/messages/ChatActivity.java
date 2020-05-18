package com.example.rainbowactivity.ui.chat.messages;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.example.rainbowactivity.R;
import com.example.rainbowactivity.databinding.ActivityChatBinding;
import com.example.rainbowactivity.model.MessageClass;
import com.example.rainbowactivity.ui.chat.whatsAppActivity;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ServerValue;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class ChatActivity extends AppCompatActivity implements MessageAdapter.OnLongClicked {
    ActivityChatBinding binding;
    private String mChatUser,mChatName,imageUrl;
    private DatabaseReference mRootRef;
    private FirebaseAuth mAuth;
    private String mCurrentUserId;
    private final List<MessageClass> messageList=new ArrayList<>();
   private MessageAdapter messageAdapter;
   private String myName="";
   private String myImageUrl="";


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chat);
        getWindow().setBackgroundDrawable(getResources().getDrawable(R.drawable.yoo8));
        binding=ActivityChatBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        setSupportActionBar(binding.toolbar);
        setTitle("");
        mRootRef= FirebaseDatabase.getInstance().getReference();
        mAuth=FirebaseAuth.getInstance();
        mCurrentUserId=mAuth.getCurrentUser().getUid();
        mChatUser=getIntent().getStringExtra("uid");
        mChatName=getIntent().getStringExtra("name");
        imageUrl=getIntent().getStringExtra("imageUrl");
        binding.NameChat.setText(mChatName);
        if(!imageUrl.isEmpty())
        {
            Glide.with(ChatActivity.this)
                    .load(imageUrl)
                    .apply(RequestOptions.circleCropTransform())
                    .into(binding.ProfileChat);
        }

        messageAdapter=new MessageAdapter(messageList,this);

        binding. recyclerViewChat.setHasFixedSize(true);
        binding. recyclerViewChat.setAdapter(messageAdapter);
        loadMessages();

       mRootRef.child("user").child(mCurrentUserId).addListenerForSingleValueEvent(new ValueEventListener() {
           @Override
           public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

               myName=dataSnapshot.child("name").getValue().toString();
               myImageUrl=dataSnapshot.child("imageUrl").getValue().toString();

           }

           @Override
           public void onCancelled(@NonNull DatabaseError databaseError) {

           }
       });
     mRootRef.child("chat").child(mCurrentUserId).addValueEventListener(new ValueEventListener() {
         @Override
         public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
             if(!dataSnapshot.hasChild(mChatUser))
             {
                 Map chatAddMap=new HashMap();
                 chatAddMap.put("seen",false);
                 chatAddMap.put("timestamp",ServerValue.TIMESTAMP);

                 Map chatUserMap=new HashMap();
                 chatUserMap.put("chat/" + mCurrentUserId + "/" + mChatUser,chatAddMap);
                 chatUserMap.put("chat/" + mChatUser + "/" + mCurrentUserId,chatAddMap);

                 mRootRef.updateChildren(chatUserMap, new DatabaseReference.CompletionListener() {
                     @Override
                     public void onComplete(@Nullable DatabaseError databaseError, @NonNull DatabaseReference databaseReference) {
                         if(databaseError!=null)
                         {
                             Log.d("CHAT_LOG",databaseError.getMessage().toString());
                         }
                     }
                 });
             }
         }

         @Override
         public void onCancelled(@NonNull DatabaseError databaseError) {

         }
     });
     binding.btnBack.setOnClickListener(new View.OnClickListener() {
         @Override
         public void onClick(View v) {
             startActivity(new Intent(ChatActivity.this, whatsAppActivity.class));
             finish();
         }
     });
     binding.btnSend.setOnClickListener(new View.OnClickListener() {
         @Override
         public void onClick(View v) {
             sendMessage();
         }
     });
     binding.recyclerViewChat.addOnLayoutChangeListener(new View.OnLayoutChangeListener() {
         @Override
         public void onLayoutChange(View v, int left, int top, int right, int bottom, int oldLeft, int oldTop, int oldRight, int oldBottom) {
             binding.recyclerViewChat.scrollToPosition(messageAdapter.getItemCount()-1);
         }
     });

    }

    private void sendMessage() {
        String message=binding.etMessage.getText().toString().trim();

        if(!TextUtils.isEmpty(message))
        {

            String current_user_ref="message/" + mCurrentUserId + "/" + mChatUser ;
            String chat_user_ref="message/" + mChatUser + "/" + mCurrentUserId  ;

            DatabaseReference user_message_push=mRootRef.child("message")
                    .child(mCurrentUserId).child(mChatUser).child("text").push();

            String push_id=user_message_push.getKey();
            Long time=-System.currentTimeMillis();
            Map messageMap=new HashMap();
            messageMap.put("message",message);
            messageMap.put("seen",false);
            messageMap.put("time",time);
            messageMap.put("from",mCurrentUserId);
            messageMap.put("key",push_id);

            Map messageUserMap=new HashMap();

            messageUserMap.put(current_user_ref + "/" +"text/" + push_id,messageMap);
            messageUserMap.put(chat_user_ref + "/" +"text/"+ push_id,messageMap);


            mRootRef.child("/" + current_user_ref + "/message").setValue(message);
            mRootRef.child("/" + current_user_ref + "/seen").setValue(false);
            mRootRef.child("/" + current_user_ref + "/time").setValue(time);
            mRootRef.child("/" + current_user_ref + "/from").setValue(mCurrentUserId);
            mRootRef.child("/" +current_user_ref + "/name").setValue(mChatName);
            mRootRef.child("/" +current_user_ref + "/imageUrl").setValue(imageUrl);

            mRootRef.child("/" + chat_user_ref + "/message").setValue(message);
            mRootRef.child("/" + chat_user_ref + "/seen").setValue(false);
            mRootRef.child("/" + chat_user_ref + "/time").setValue(time);
            mRootRef.child("/" + chat_user_ref + "/from").setValue(mCurrentUserId);
            mRootRef.child("/" + chat_user_ref + "/name").setValue(myName);
            mRootRef.child("/" + chat_user_ref + "/imageUrl").setValue(myImageUrl);


            binding.etMessage.setText("");

            mRootRef.updateChildren(messageUserMap, new DatabaseReference.CompletionListener() {
              @Override
                public void onComplete(@Nullable DatabaseError databaseError, @NonNull DatabaseReference databaseReference) {
                    if(databaseError!=null)
                   {
                        Log.d("CHATs_LOG",databaseError.getMessage().toString());
                 }
               }
            });
        }
    }


     private void loadMessages() {
        mRootRef.child("message").child(mCurrentUserId).child(mChatUser).child("text").limitToLast(25).addChildEventListener(new ChildEventListener() {
            @Override
          public void onChildAdded(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {
               MessageClass message=dataSnapshot.getValue(MessageClass.class);
                messageList.add(message);
                binding.recyclerViewChat.scrollToPosition(messageList.size()-1);
                messageAdapter.notifyDataSetChanged();
            }

            @Override
            public void onChildChanged(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {

            }

            @Override
           public void onChildRemoved(@NonNull DataSnapshot dataSnapshot) {


            }

            @Override
            public void onChildMoved(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {

           }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
       });
    }

    @Override
    public void clicked(int index) {
        AlertDialog.Builder dialog = new AlertDialog.Builder(this);

        dialog.setTitle("Delete");
        dialog.setMessage("Are you sure you want to delete ......");
        dialog.setPositiveButton("Cancel", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.cancel();
            }
        });

        dialog.setNeutralButton("Yes", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
              DatabaseReference mRef=FirebaseDatabase.getInstance().getReference().child("message").child(mCurrentUserId).child(mChatUser).child("text").child("key");
              mRef.removeValue();

            }
        });
       // dialog.show();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.icon,menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        if(item.getItemId()==R.id.clearChat)
        {
            mRootRef.child("message").child(mCurrentUserId).child(mChatUser).child("message").setValue("");
           mRootRef.child("message").child(mCurrentUserId).child(mChatUser).child("text").removeValue();
           messageList.clear();
           messageAdapter.notifyDataSetChanged();
        }
        return super.onOptionsItemSelected(item);
    }
}
