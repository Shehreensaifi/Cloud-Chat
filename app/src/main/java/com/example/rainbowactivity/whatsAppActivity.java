package com.example.rainbowactivity;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Switch;

import com.example.rainbowactivity.databinding.ActivityWhatsAppBinding;
import com.google.android.material.tabs.TabLayout;
import com.google.android.material.tabs.TabLayoutMediator;
import com.google.firebase.auth.FirebaseAuth;

public class whatsAppActivity extends AppCompatActivity {

    ActivityWhatsAppBinding binding;
    private FirebaseAuth mAuth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_whats_app);
        mAuth=FirebaseAuth.getInstance();
        binding=ActivityWhatsAppBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        binding.viewPager.setAdapter(new ChatViewPagerAdapter(this));
        TabLayoutMediator tabLayoutMediator=new TabLayoutMediator(binding.tabLayout, binding.viewPager, new TabLayoutMediator.TabConfigurationStrategy() {
            @Override
            public void onConfigureTab(@NonNull TabLayout.Tab tab, int position) {
                switch(position)
                {
                    case 0:
                    {
                        tab.setText("CHATS");
                        tab.setIcon(R.drawable.chat);
                        break;
                    }
                    case 1:
                    {
                        tab.setText("FRIENDS");
                        tab.setIcon(R.drawable.person2);
                        break;
                    }

                }

            }
        });
        tabLayoutMediator.attach();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.user,menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
     if(item.getItemId()==R.id.logOut)
        {
            mAuth.signOut();
            startActivity(new Intent(this, MainActivity.class));
            finish();
        }
        return super.onOptionsItemSelected(item);
    }
}
