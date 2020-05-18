package com.example.rainbowactivity.ui.home;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;

import androidx.appcompat.widget.Toolbar;

import com.example.rainbowactivity.ui.login.MainActivity;
import com.example.rainbowactivity.R;
import com.example.rainbowactivity.databinding.ActivityPostBinding;
import com.example.rainbowactivity.ui.chat.whatsAppActivity;
import com.google.android.material.tabs.TabLayout;
import com.google.android.material.tabs.TabLayoutMediator;
import com.google.firebase.auth.FirebaseAuth;

public class PostActivity extends AppCompatActivity {
    private FirebaseAuth mAuth;
    private Toolbar toolbar;
   ActivityPostBinding binding;

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu,menu);
        setTitle("Rainbow");
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
        else if(item.getItemId()==R.id.message)
        {
            startActivity(new Intent(this, whatsAppActivity.class));
        }


        return super.onOptionsItemSelected(item);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_post);
        ActionBar actionBar=getSupportActionBar();
        actionBar.setTitle("Cloud Chat");
     /*   actionBar.setIcon(R.drawable.icon);
        actionBar.setDisplayUseLogoEnabled(true);
        actionBar.setDisplayShowHomeEnabled(true);*/

        binding=ActivityPostBinding.inflate(getLayoutInflater());
       setContentView(binding.getRoot());
        mAuth=FirebaseAuth.getInstance();

         binding.viewPager.setAdapter(new AdapterViewPager(this));
         binding.viewPager.setScrollbarFadingEnabled(true);
        TabLayout tabLayout=findViewById(R.id.tabLayout);

        TabLayoutMediator tabLayoutMediator=new TabLayoutMediator(tabLayout, binding.viewPager, new TabLayoutMediator.TabConfigurationStrategy() {
            @Override
            public void onConfigureTab(@NonNull TabLayout.Tab tab, int position) {
                switch (position)
                {
                    case 0: {
                        tab.setIcon(R.drawable.home2);
                        break;
                    }
                    case 1:
                    {
                        tab.setIcon(R.drawable.person2);
                        break;
                    }
                    case 2:
                    {
                        tab.setIcon(R.drawable.add);
                        break;
                    }


                }

            }
        });
        tabLayoutMediator.attach();
    }
}
