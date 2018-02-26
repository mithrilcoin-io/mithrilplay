package io.mithrilcoin.mithrilplay.view.adapter;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;

import io.mithrilcoin.mithrilplay.view.RewardFragment;
import io.mithrilcoin.mithrilplay.view.RewardHistoryFragment;
import io.mithrilcoin.mithrilplay.view.RewardTodayFragment;


/**
 *  HomeActivity adapter
 */
public class TabPagerAdapter extends FragmentStatePagerAdapter {

    // Count number of tabs
    private int tabCount;

    public TabPagerAdapter(FragmentManager fm, int tabCount) {
        super(fm);
        this.tabCount = tabCount;
    }

    @Override
    public Fragment getItem(int position) {

        // Returning the current tabs
        switch (position) {
//            case 0:
//                RewardFragment rewardFragment = new RewardFragment();
//                return rewardFragment;
            case 0:
                RewardTodayFragment rewardTodayFragment = new RewardTodayFragment();
                return rewardTodayFragment;
            case 1:
            RewardHistoryFragment rewardHistoryFragment = new RewardHistoryFragment();
            return rewardHistoryFragment;

            default:
                return null;
        }
    }

    @Override
    public int getCount() {
        return tabCount;
    }

    @Override
    public int getItemPosition(Object object) {
        return POSITION_NONE;
    }
}