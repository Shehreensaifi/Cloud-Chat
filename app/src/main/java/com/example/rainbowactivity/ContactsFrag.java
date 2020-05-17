package com.example.rainbowactivity;

import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.example.rainbowactivity.databinding.FragmentAddPostFargBinding;
import com.example.rainbowactivity.databinding.FragmentContactsBinding;
import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.firebase.ui.database.FirebaseRecyclerOptions;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;


/**
 * A simple {@link Fragment} subclass.
 */
public class ContactsFrag extends Fragment {
    FragmentContactsBinding binding;
    private FirebaseAuth mUserAuth;
    private DatabaseReference mUserRef;
    Query query;

    public ContactsFrag() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        binding = FragmentContactsBinding.inflate(inflater,container,false);
        return binding.getRoot();

    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        mUserAuth=FirebaseAuth.getInstance();
        mUserRef= FirebaseDatabase.getInstance().getReference().child("user").child(mUserAuth.getUid());
    }
    @Override
   public void onStart() {
        super.onStart();
        query=FirebaseDatabase.getInstance().getReference().child("user");
        query.keepSynced(true);
        FirebaseRecyclerOptions<AllUserClass> options=new FirebaseRecyclerOptions.Builder<AllUserClass>()
                .setQuery(query,AllUserClass.class)
                .setLifecycleOwner(this)
                .build();
        FirebaseRecyclerAdapter adapter=new FirebaseRecyclerAdapter<AllUserClass,AllUser>(options) {


            @NonNull
            @Override
            public AllUser onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
                View view= LayoutInflater.from(parent.getContext())
                        .inflate(R.layout.contacts_custom,parent,false);

                return new AllUser(view);
            }
            @Override
            protected void onBindViewHolder(@NonNull AllUser holder, int position, @NonNull final AllUserClass model)
            {
                holder.tvName2.setText(model.getName());
                if(!model.getImageUrl().isEmpty())
                {
                    Glide.with(ContactsFrag.this)
                            .load(model.getImageUrl())
                            .apply(RequestOptions.circleCropTransform())
                            .into(holder.ibProfile2);
                }
                else
                {
                    Glide.with(ContactsFrag.this)
                            .load(model.getImageUrl())
                            .placeholder(R.drawable.person2)
                            .apply(RequestOptions.circleCropTransform())
                            .into(holder.ibProfile2);
                }
                holder.UserView.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Intent intentUser=new Intent(getActivity(),ChatActivity.class);
                        intentUser.putExtra("uid",model.getUid());
                        intentUser.putExtra("name",model.getName());
                        intentUser.putExtra("imageUrl",model.getImageUrl());
                        startActivity(intentUser);
                    }
                });

            }
        };
        binding.recyclerView.setAdapter(adapter);

    }
    static class AllUser extends RecyclerView.ViewHolder
    {
        View UserView;
        ImageButton ibProfile2;
        TextView tvName2;
        public AllUser(@NonNull View itemView) {
            super(itemView);
            UserView=itemView;
            ibProfile2=itemView.findViewById(R.id.ibProfile2);
            tvName2=itemView.findViewById(R.id.tvName2);

        }
    }
}
