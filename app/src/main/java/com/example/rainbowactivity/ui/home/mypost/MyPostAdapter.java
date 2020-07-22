package com.example.rainbowactivity.ui.home.mypost;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.example.rainbowactivity.R;
import com.example.rainbowactivity.model.PostClass;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

import java.util.List;

public class MyPostAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder>{

    private List<ItemClass> items;
    Context context;
    OnClickedListener post;

    interface OnClickedListener{
        void onLongClickedProfile();
    }

    public MyPostAdapter(List<ItemClass> items, Context context,OnClickedListener post) {
        this.items = items;
        this.context = context;
        this.post=post;
    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        if(viewType==0){
             return new ProfileHolder(LayoutInflater.from(parent.getContext())
             .inflate(R.layout.profile_custom,parent,false));
        }else{
            return new PostHolder(LayoutInflater.from(parent.getContext())
                    .inflate(R.layout.delete_custom,parent,false));
        }
    }


    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {

        if(getItemViewType(position)==0){
           ProfileClass profile=(ProfileClass)items.get(position).getObject();
            ((ProfileHolder)holder).setProfie(profile);
            ((ProfileHolder)holder).ibProfile.setOnLongClickListener(new View.OnLongClickListener() {
                @Override
                public boolean onLongClick(View v) {
                    post.onLongClickedProfile();
                    return true;
                }
            });
        }else {
            PostClass post=(PostClass)items.get(position).getObject();
            ((PostHolder)holder).setPost((post));
        }

    }

    @Override
    public int getItemCount() {
        return items.size();
    }
    @Override
    public int getItemViewType(int position) {
        return items.get(position).getType();
    }

    class PostHolder extends RecyclerView.ViewHolder
    {
        ImageButton ibProfile;
        TextView tvName,tvMessage;
        ImageView ivPost;
        ImageButton ibDeletePost;

        public PostHolder(@NonNull View itemView) {
            super(itemView);
            ibProfile=itemView.findViewById(R.id.ibProfile);
            tvName=itemView.findViewById(R.id.tvName);
            tvMessage=itemView.findViewById(R.id.tvMessage);
            ivPost=itemView.findViewById(R.id.ivPost);
            ibDeletePost=itemView.findViewById(R.id.ibDeletePost);
        }
        void setPost(final PostClass post){

            tvName.setText(post.getName());
            tvMessage.setText(post.getMessage());
            Glide.with(context)
                    .load(post.getImageUrl())
                    .into(ivPost);
            if (!post.getProfileUrl().isEmpty()) {
                Glide.with(context)
                        .load(post.getProfileUrl())
                        .apply(RequestOptions.circleCropTransform())
                        .into(ibProfile);
            }
            ibDeletePost.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    AlertDialog.Builder dialog = new AlertDialog.Builder(context);
                    dialog.setTitle("Delete");
                    dialog.setMessage("Are you sure you want to delete this post......");
                    dialog.setNeutralButton("Yes", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            StorageReference mStorage = FirebaseStorage.getInstance().getReference().child(post.getUid()).child(post.getImageName());
                            mStorage.delete();
                            DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference().child("post").child(post.getImageName());
                            databaseReference.removeValue();
                        }
                    });
                    dialog.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            dialog.cancel();
                        }
                    });
                    dialog.show();
                }
            });

        }
    }

    class ProfileHolder extends RecyclerView.ViewHolder{
        ImageView ibProfile;
        TextView tvName;
        public ProfileHolder(@NonNull View itemView) {
            super(itemView);
            ibProfile=itemView.findViewById(R.id.ibProfile);
            tvName=itemView.findViewById((R.id.tvName));
        }
        void setProfie(ProfileClass profile){
            if (!profile.getProfileUrl().isEmpty()) {
                Glide.with(context)
                        .load(profile.getProfileUrl())
                        .apply(RequestOptions.circleCropTransform())
                        .into(ibProfile);
            }
            tvName.setText(profile.getName());
        }
    }
}
