package com.example.rainbowactivity;

import android.app.ProgressDialog;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.example.rainbowactivity.databinding.ActivityMainBinding;
import com.example.rainbowactivity.databinding.FragmentAddPostFargBinding;
import com.example.rainbowactivity.databinding.FragmentPostBinding;
import com.google.android.gms.tasks.Continuation;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
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
public class AddPostFarg extends Fragment {

    Uri imageUri=null;
    static final int GALLERY_INTENT_REQUEST_CODE=1;
    FragmentAddPostFargBinding binding;
    private DatabaseReference postRef;
    private StorageReference postStorage;
    private FirebaseAuth mAuth;
    private DatabaseReference currentUserRef;
    ProgressDialog dialog;
    String profileUrl="";
    String userName="";

    public AddPostFarg() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        binding = FragmentAddPostFargBinding.inflate(inflater,container,false);
        return binding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        mAuth=FirebaseAuth.getInstance();

        postStorage=FirebaseStorage.getInstance().getReference().child(mAuth.getUid());
        dialog=new ProgressDialog(getActivity());
        dialog.setMessage("loading....");
        dialog.setCancelable(false);
        currentUserRef=FirebaseDatabase.getInstance().getReference().child("user").child(mAuth.getUid());
        currentUserRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                        profileUrl=dataSnapshot.child("imageUrl").getValue().toString();
                        userName=dataSnapshot.child("name").getValue().toString();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
        binding.ibAddPost.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                CropImage.activity()
                        .setGuidelines(CropImageView.Guidelines.ON)
                        .setAspectRatio(1,1)
                        .start(getContext(),AddPostFarg.this);
            }
        });
        binding.btnAddPost.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(imageUri==null)
                {
                    Toast.makeText(getContext(), "please select an image!", Toast.LENGTH_LONG).show();
                }
                else
                {
                    postRef= FirebaseDatabase.getInstance().getReference().child("post").push();
                    dialog.show();
                    final String imageName=postRef.getKey();
                    final StorageReference mStorage=postStorage.child(imageName);
                    UploadTask uploadTask=mStorage.putFile(imageUri);

                    Task<Uri> urlTask=uploadTask.continueWithTask(new Continuation<UploadTask.TaskSnapshot, Task<Uri>>() {
                        @Override
                        public Task<Uri> then(@NonNull Task<UploadTask.TaskSnapshot> task) throws Exception {
                            if(!task.isSuccessful())
                            {
                                throw task.getException();
                            }
                            return mStorage.getDownloadUrl();
                        }
                    }).addOnCompleteListener(new OnCompleteListener<Uri>() {
                        @Override
                        public void onComplete(@NonNull Task<Uri> task) {
                            String message=binding.etAddMessage.getText().toString().trim();
                            Long time=-System.currentTimeMillis();
                            if(task.isSuccessful())
                            {
                                Uri downloadUri=task.getResult();
                                postRef.child("message").setValue(message);
                                postRef.child("time").setValue(time);
                                postRef.child("name").setValue(userName);
                                postRef.child("imageUrl").setValue(downloadUri.toString());
                                postRef.child("imageName").setValue(imageName);
                                postRef.child("profileUrl").setValue(profileUrl);
                                postRef.child("uid").setValue(mAuth.getUid());
                                dialog.dismiss();
                                imageUri=null;
                                Glide.with(AddPostFarg.this)
                                        .load(R.drawable.profile2)
                                        .into(binding.ibAddPost);
                                binding.etAddMessage.setText("");

                            }
                            else{
                                Toast.makeText(getContext(), task.getException().getMessage(), Toast.LENGTH_LONG).show();
                                dialog.dismiss();
                            }
                        }
                    });
                }
            }
        });
    }


    @Override
    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == CropImage.CROP_IMAGE_ACTIVITY_REQUEST_CODE) {
            CropImage.ActivityResult result = CropImage.getActivityResult(data);
            if (resultCode == RESULT_OK) {
                imageUri = result.getUri();
                Glide.with(this)
                        .load(imageUri)
                        .into(binding.ibAddPost);
            } else if (resultCode == CropImage.CROP_IMAGE_ACTIVITY_RESULT_ERROR_CODE) {
                Exception error = result.getError();
            }
        }

    }
}
