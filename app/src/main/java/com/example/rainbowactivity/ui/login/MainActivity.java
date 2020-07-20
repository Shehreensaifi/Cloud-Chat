package com.example.rainbowactivity.ui.login;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Toast;

import com.example.rainbowactivity.R;
import com.example.rainbowactivity.databinding.ActivityMainBinding;
import com.example.rainbowactivity.ui.home.PostActivity;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class MainActivity extends AppCompatActivity {

   ActivityMainBinding binding;
   private FirebaseAuth mAuth;
   private FirebaseUser currentUser;
    DatabaseReference mRef;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        setTitle("Rainbow");
        binding=ActivityMainBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        mAuth=FirebaseAuth.getInstance();
       mRef= FirebaseDatabase.getInstance().getReference().child("user");
        currentUser=mAuth.getCurrentUser();
        if(currentUser!=null)
        {
            startActivity(new Intent(MainActivity.this, PostActivity.class));
            finish();
        }
    }

    public void signUpClicked(View view) {
        final String name=binding.etName.getText().toString().trim();
        final String email=binding.etEmail.getText().toString().trim();
        final String password=binding.etPassword.getText().toString().trim();
        if(name.isEmpty()||email.isEmpty()||password.isEmpty())
        {
            Toast.makeText(MainActivity.this, "Please enter all fields!", Toast.LENGTH_SHORT).show();
        }
        else {
            mAuth.createUserWithEmailAndPassword(email, password).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                @Override
                public void onComplete(@NonNull Task<AuthResult> task) {
                    if (task.isSuccessful()) {
                        startActivity(new Intent(MainActivity.this, PostActivity.class));
                        mRef.child(mAuth.getUid()).child("imageUrl").setValue("");
                        mRef.child(mAuth.getUid()).child("name").setValue(name);
                        mRef.child(mAuth.getUid()).child("uid").setValue(mAuth.getUid());
                        finish();

                    } else {
                        Toast.makeText(MainActivity.this, task.getException().getMessage(), Toast.LENGTH_SHORT).show();
                    }
                }
            });
        }
    }

    public void logInClicked(View view) {

        final String email = binding.etEmail.getText().toString().trim();
        final String password = binding.etPassword.getText().toString().trim();
        if ( email.isEmpty() || password.isEmpty()) {
            Toast.makeText(MainActivity.this, "please enter all fields!", Toast.LENGTH_LONG).show();
        } else {
            mAuth.signInWithEmailAndPassword(email, password).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                @Override
                public void onComplete(@NonNull Task<AuthResult> task) {


                    if (task.isSuccessful()) {
                        startActivity(new Intent(MainActivity.this, PostActivity.class));
                        finish();
                    } else {
                        Toast.makeText(MainActivity.this, task.getException().getMessage(), Toast.LENGTH_SHORT).show();
                    }

                }
            });
        }
    }

    public void forgetPasswordClicked(View view)
    {
        final String email = binding.etEmail.getText().toString().trim();
        if(email.isEmpty())
        {
            Toast.makeText(this, "enter your email!", Toast.LENGTH_LONG).show();
        }
        else{
            mAuth.sendPasswordResetEmail(email).addOnCompleteListener(new OnCompleteListener<Void>() {
                @Override
                public void onComplete(@NonNull Task<Void> task) {
                    if(task.isSuccessful())
                    {

                        Toast.makeText(MainActivity.this, "password reset link send to your email!", Toast.LENGTH_SHORT).show();
                    }
                }
            });
        }

    }

}
