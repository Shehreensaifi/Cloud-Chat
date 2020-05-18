package com.example.rainbowactivity.ui.chat;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentActivity;
import androidx.viewpager2.adapter.FragmentStateAdapter;

import com.example.rainbowactivity.ui.chat.allchats.ChatFrag;
import com.example.rainbowactivity.ui.chat.allusers.ContactsFrag;

public class ChatViewPagerAdapter extends FragmentStateAdapter {

    public ChatViewPagerAdapter(@NonNull FragmentActivity fragmentActivity) {
        super(fragmentActivity);
    }

    @NonNull
    @Override
    public Fragment createFragment(int position) {

        switch (position)
        {
            case 0:
                return new ChatFrag();
            case 1:
                return new ContactsFrag();

        }
        return null;
    }


    @Override
    public int getItemCount() {
        return 2;
    }
}
