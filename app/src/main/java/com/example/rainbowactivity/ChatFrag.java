package com.example.rainbowactivity;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.RecyclerView;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Adapter;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.example.rainbowactivity.databinding.FragmentChatBinding;
import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.firebase.ui.database.FirebaseRecyclerOptions;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;


/**
 * A simple {@link Fragment} subclass.
 */
public class ChatFrag extends Fragment implements AdapterLastClass.OnItemClicked {

  FragmentChatBinding binding;
  private FirebaseAuth mAuth;
  Query mRootRef;
  private String mCurrentUserId;
  private  AdapterLastClass adapter;
  private final List<ChatLastMessageClass> messageList=new ArrayList<>();


    public ChatFrag() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        binding=FragmentChatBinding.inflate(inflater);
        return binding.getRoot();

    }

    @Override
    public void onStart() {
        super.onStart();
        mAuth=FirebaseAuth.getInstance();
        mCurrentUserId=mAuth.getCurrentUser().getUid();
        mRootRef=FirebaseDatabase.getInstance().getReference().child("message").child(mCurrentUserId)
                .orderByChild("time");
        adapter=new AdapterLastClass(messageList,this);

        binding. recyclerView.setHasFixedSize(true);
        binding. recyclerView.setAdapter(adapter);
        mRootRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                messageList.clear();
                try {
                    for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                        String uid = snapshot.getKey();
                        String from = snapshot.child("from").getValue().toString();
                        String message = snapshot.child("message").getValue().toString().trim();
                        String name = snapshot.child("name").getValue().toString();
                        String imageUrl = snapshot.child("imageUrl").getValue().toString();
                        if(message.length()>20)
                        {
                            message=message.substring(0,18)+"...";
                        }

                        messageList.add(new ChatLastMessageClass(name, imageUrl, uid, message, from));

                    }
                    adapter.notifyDataSetChanged();
                }
                catch (Exception e)
                {

                }



            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });

    }


    @Override
    public void itemClicked(int index) {
      Intent intent=new Intent(getContext(),ChatActivity.class);
      intent.putExtra("uid",messageList.get(index).getUid());
      intent.putExtra("name",messageList.get(index).getName());
      intent.putExtra("imageUrl",messageList.get(index).getImageUrl());
      startActivity(intent);
    }

    @Override
    public void longClickedDelete(final int index) {
        AlertDialog.Builder dialog = new AlertDialog.Builder(getActivity());

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
                DatabaseReference mRef=FirebaseDatabase.getInstance().getReference().child("message")
                        .child(mCurrentUserId).child(messageList.get(index).getUid());
                mRef.removeValue();

            }
        });
        dialog.show();
    }
}
