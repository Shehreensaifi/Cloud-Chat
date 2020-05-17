package com.example.rainbowactivity;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentActivity;
import androidx.viewpager2.adapter.FragmentStateAdapter;

public class AdapterViewPager extends FragmentStateAdapter {

    public AdapterViewPager(@NonNull FragmentActivity fragmentActivity) {
        super(fragmentActivity);
    }

    @NonNull
    @Override
    public Fragment createFragment(int position) {
        switch (position)
        {
            case 0:
                return new PostFrag();
            case 1:
                return new MyPostFrag();
            case 2:
                return new AddPostFarg();
        }
        return null;
    }

    @Override
    public int getItemCount() {
        return 3;
    }
}
