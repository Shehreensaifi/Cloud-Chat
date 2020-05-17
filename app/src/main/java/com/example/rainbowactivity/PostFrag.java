package com.example.rainbowactivity;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;


import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.example.rainbowactivity.databinding.FragmentPostBinding;
import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.firebase.ui.database.FirebaseRecyclerOptions;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;


/**
 * A simple {@link Fragment} subclass.
 */
public class PostFrag extends Fragment {

    RecyclerView recyclerView;
    FragmentPostBinding binding;
    private FirebaseAuth mAuth;



    public PostFrag() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_post, container, false);
    }
    @Override
    public void onStart() {
        super.onStart();
        Query query= FirebaseDatabase.getInstance().getReference().child("post").orderByChild("time");
        query.keepSynced(true);
        FirebaseRecyclerOptions<PostClass> options=new FirebaseRecyclerOptions.Builder<PostClass>()
                .setQuery(query, PostClass.class)
                .setLifecycleOwner(this)
                .build();
        FirebaseRecyclerAdapter adapter=new FirebaseRecyclerAdapter<PostClass,PostHolder>(options) {

            @Override
            protected void onBindViewHolder(@NonNull PostHolder holder, int position, @NonNull PostClass model) {

            holder.tvName.setText(model.getName());
            holder.tvMessage.setText(model.getMessage());
                Glide.with(PostFrag.this)
                        .load(model.getImageUrl())
                        .into(holder.ivPost);

                if(!model.getProfileUrl().isEmpty())
                {
                    Glide.with(PostFrag.this)
                            .load(model.getProfileUrl())
                            .apply(RequestOptions.circleCropTransform())
                            .into(holder.ibProfile);

                }

            }


            @NonNull
            @Override
            public PostHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

                View view= LayoutInflater.from(parent.getContext()).inflate(R.layout.custom_post,parent,false);
                return new PostHolder(view);
            }
        };
        recyclerView.setAdapter(adapter);

    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        setHasOptionsMenu(true);
        mAuth=FirebaseAuth.getInstance();
        recyclerView=view.findViewById(R.id.recyclerView);
    }



    static class PostHolder extends RecyclerView.ViewHolder
    {
        ImageButton ibProfile;
        TextView tvName,tvMessage;
        ImageView ivPost;


        public PostHolder(@NonNull View itemView) {
            super(itemView);
            ibProfile=itemView.findViewById(R.id.ibProfile);
            tvName=itemView.findViewById(R.id.tvName);
            tvMessage=itemView.findViewById(R.id.tvMessage);
            ivPost=itemView.findViewById(R.id.ivPost);
        }
    }
}
