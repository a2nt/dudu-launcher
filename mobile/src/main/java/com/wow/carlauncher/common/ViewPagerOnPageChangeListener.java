package com.wow.carlauncher.common;

import androidx.viewpager.widget.ViewPager;

public interface ViewPagerOnPageChangeListener extends ViewPager.OnPageChangeListener {
    default void onPageScrolled(int i, float v, int i1) {

    }

    default void onPageSelected(int i) {

    }

    default void onPageScrollStateChanged(int i) {

    }
}
