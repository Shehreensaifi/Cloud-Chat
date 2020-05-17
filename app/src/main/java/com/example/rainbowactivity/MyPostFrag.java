package com.example.rainbowactivity;

import android.app.AlertDialog;
import android.app.Dialog;
import android.app.DownloadManager;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.example.rainbowactivity.databinding.FragmentMyPostBinding;
import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.firebase.ui.database.FirebaseRecyclerOptions;
import com.google.android.gms.tasks.Continuation;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import com.theartofdev.edmodo.cropper.CropImage;
import com.theartofdev.edmodo.cropper.CropImageView;

import static android.app.Activity.RESULT_OK;


/**
 * A simple {@link Fragment} subclass.
 */
public class MyPostFrag extends Fragment {
    Query query;
    FragmentMyPostBinding binding;
    private FirebaseAuth mAuth;
    private DatabaseReference mRef;
    private DatabaseReference userNameRef;
    Dialog dialog;
    EditText etChangeName;
    TextView tvYes,tvCancel;
    ProgressDialog progressDialog;

    public MyPostFrag() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
       binding=FragmentMyPostBinding.inflate(inflater);
       return binding.getRoot();
    }


    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        progressDialog=new ProgressDialog(getActivity());
        progressDialog.setMessage("loading...");
        progressDialog.setCancelable(false);
        mAuth=FirebaseAuth.getInstance();
        query= FirebaseDatabase.getInstance().getReference().child("post")
                .orderByChild("uid").equalTo(mAuth.getUid());
        query.keepSynced(true);
        mRef=FirebaseDatabase.getInstance().getReference().child("user").child(mAuth.getUid());
       mRef.addListenerForSingleValueEvent(new ValueEventListener() {
           @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                String name=dataSnapshot.child("name").getValue().toString();
                String imageurl=dataSnapshot.child("imageUrl").getValue().toString();
                binding.tvName.setText(name);
                if(!imageurl.isEmpty())
                {
                    Glide.with(getActivity())
                            .load(imageurl)
                            .apply(RequestOptions.circleCropTransform())
                            .into(binding.ibProfile);
                }
            }
            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
        userNameRef=FirebaseDatabase.getInstance().getReference().child("user").child(mAuth.getUid());
        binding.ibProfile.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View v) {
                CropImage.activity()
                        .setGuidelines(CropImageView.Guidelines.ON)
                        .start(getContext(),MyPostFrag.this);
                return true;
            }
        });
        dialog=new Dialog(getContext());
       dialog.setContentView(R.layout.rename_custom);
        dialog.getWindow().setLayout(ViewGroup.LayoutParams.MATCH_PARENT,ViewGroup.LayoutParams.WRAP_CONTENT);
        etChangeName=dialog.findViewById(R.id.etChangeName);
        tvYes=dialog.findViewById(R.id.tvYes);
        tvCancel=dialog.findViewById(R.id.tvCancel);
        binding.tvName.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View v) {


                tvYes.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        final String newName=etChangeName.getText().toString();
                        if(newName.isEmpty())
                        {
                            Toast.makeText(getContext(), "please enter your new name!", Toast.LENGTH_SHORT).show();
                        }
                        else
                        {
                            progressDialog.show();
                            userNameRef.child("name").setValue(newName);
                            binding.tvName.setText(newName);
                            query.addListenerForSingleValueEvent(new ValueEventListener() {
                                @Override
                                public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                                    for(DataSnapshot snapshot:dataSnapshot.getChildren())
                                    {
                                        FirebaseDatabase.getInstance().getReference().child("post")
                                                .child(snapshot.getKey()).child("name").setValue(newName);
                                    }
                                    progressDialog.dismiss();
                                }
                                @Override
                                public void onCancelled(@NonNull DatabaseError databaseError) {
                                        progressDialog.dismiss();
                                }
                            });
                            dialog.cancel();

                        }

                    }
                });
                tvCancel.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        dialog.cancel();
                    }
                });
                dialog.show();
                return true;
            }
        });

    }

    @Override
    public void onStart() {
        super.onStart();

        FirebaseRecyclerOptions<PostClass> options=new FirebaseRecyclerOptions.Builder<PostClass>()
                .setQuery(query,PostClass.class)
                .setLifecycleOwner(this)
                .build();
        FirebaseRecyclerAdapter adapter=new FirebaseRecyclerAdapter<PostClass,PostHolder>(options) {
            @NonNull
            @Override
            public PostHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
                View view = LayoutInflater.from(parent.getContext())
                        .inflate(R.layout.delete_custom, parent, false);
                return new PostHolder(view);
            }

            @Override
            protected void onBindViewHolder(@NonNull PostHolder holder, int position, @NonNull final PostClass model) {
                holder.tvName.setText(model.getName());
                holder.tvMessage.setText(model.getMessage());
                Glide.with(MyPostFrag.this)
                        .load(model.getImageUrl())
                        .into(holder.ivPost);
                if (!model.getProfileUrl().isEmpty()) {
                    Glide.with(MyPostFrag.this)
                            .load(model.getProfileUrl())
                            .apply(RequestOptions.circleCropTransform())
                            .into(holder.ibProfile);
                }
                holder.itemView.setOnLongClickListener(new View.OnLongClickListener() {
                    @Override
                    public boolean onLongClick(View v) {
                        AlertDialog.Builder dialog = new AlertDialog.Builder(getActivity());
                        dialog.setTitle("Delete");
                        dialog.setMessage("Are you sure you want to delete this post......");
                        dialog.setNeutralButton("Yes", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                StorageReference mStorage = FirebaseStorage.getInstance().getReference().child(model.getUid()).child(model.getImageName());
                                mStorage.delete();
                                DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference().child("post").child(model.getImageName());
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
                        return true;
                    }
                });
                holder.ibDeletePost.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        AlertDialog.Builder dialog = new AlertDialog.Builder(getActivity());
                        dialog.setTitle("Delete");
                        dialog.setMessage("Are you sure you want to delete this post......");
                        dialog.setNeutralButton("Yes", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                StorageReference mStorage = FirebaseStorage.getInstance().getReference().child(model.getUid()).child(model.getImageName());
                                mStorage.delete();
                                DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference().child("post").child(model.getImageName());
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

        };

        binding.recyclerView.setAdapter(adapter);
    }


    @Override
    public void onActivityResult(int requestCode, int resultCode, @Nullable final Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == CropImage.CROP_IMAGE_ACTIVITY_REQUEST_CODE) {
            CropImage.ActivityResult result = CropImage.getActivityResult(data);
            if (resultCode == RESULT_OK) {
                Uri resultUri = result.getUri();
                final StorageReference ref = FirebaseStorage.getInstance().getReference().child(mAuth.getUid()).child("profile");
                UploadTask uploadTask = ref.putFile(resultUri);

                Task<Uri> urlTask = uploadTask.continueWithTask(new Continuation<UploadTask.TaskSnapshot, Task<Uri>>() {
                    @Override
                    public Task<Uri> then(@NonNull Task<UploadTask.TaskSnapshot> task) throws Exception {
                        if (!task.isSuccessful()) {
                            throw task.getException();
                        }

                        // Continue with the task to get the download URL
                        return ref.getDownloadUrl();
                    }
                }).addOnCompleteListener(new OnCompleteListener<Uri>() {
                    @Override
                    public void onComplete(@NonNull Task<Uri> task) {
                        if (task.isSuccessful()) {
                            progressDialog.show();
                            final Uri downloadUri = task.getResult();
                            mRef.child("imageUrl").setValue(downloadUri.toString());
                            Glide.with(MyPostFrag.this)
                                    .load(downloadUri)
                                    .apply(RequestOptions.circleCropTransform())
                                    .into(binding.ibProfile);
                            query.addListenerForSingleValueEvent(new ValueEventListener() {
                                @Override
                                public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                                    for(DataSnapshot snapshot:dataSnapshot.getChildren())
                                    {
                                        FirebaseDatabase.getInstance().getReference().child("post").child(snapshot.getKey())
                                                .child("profileUrl").setValue(downloadUri.toString());
                                    }
                                   progressDialog.dismiss();
                                }

                                @Override
                                public void onCancelled(@NonNull DatabaseError databaseError) {
                                        progressDialog.dismiss();
                                }
                            });
                        } else {
                            // Handle failures
                            // ...
                        }
                    }
                });
            } else if (resultCode == CropImage.CROP_IMAGE_ACTIVITY_RESULT_ERROR_CODE) {
                Exception error = result.getError();
            }
        }


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
    }

}
