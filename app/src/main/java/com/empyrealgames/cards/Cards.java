package com.empyrealgames.cards;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

public class Cards extends AppCompatActivity {
    int NUM_PAGES ;
    private Button mPreviousButton, mNextButton, mRestartButton;
    private ViewPager mCards;
    private TextView pageNumberTextView;
    private PagerAdapter pagerAdapter;
    private String data[] = new String[1];
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_cards);

        /*getting all the data from intent*/
        data = getIntent().getStringArrayExtra(MainActivity.JSON);


        NUM_PAGES = data.length;
        // Instantiate a ViewPager and a PagerAdapter.


        /*getting reference of all views from
        layout resource file*/
        mCards =  findViewById(R.id.pager);
        mCards.setPageTransformer(true, new Cards.ZoomOutPageTransformer());
        pagerAdapter = new Cards.ScreenSlidePagerAdapter(getSupportFragmentManager());
        mCards.setAdapter(pagerAdapter);
        pageNumberTextView = findViewById(R.id.pageNumberTextView);
        pageNumberTextView.setText(1 + "/" + NUM_PAGES);
        mPreviousButton = findViewById(R.id.previousButton);
        mNextButton = findViewById(R.id.nextButton);
        mRestartButton = findViewById(R.id.restartButton);

        /*setting click listeners of all buttons*/
        mNextButton.setOnClickListener(
                new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        if(mNextButton.getText() == "Finish") {
                            /*here we can do the action which will happen
                            after user view all the cards*/
                        }
                        else {
                            mCards.setCurrentItem(mCards.getCurrentItem()+ 1);
                        }
                    }
                }
        );
        /* Set first card as current card */
        mRestartButton.setOnClickListener(
                new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        mCards.setCurrentItem(0);
                    }
                }
        );
        mPreviousButton.setOnClickListener(
                new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        mCards.setCurrentItem(mCards.getCurrentItem() -  1);
                    }
                }
        );


        /* adding a listener to cards if, so that we can do some actions when cards are swiped*/
        mCards.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

            }

            @Override
            public void onPageSelected(int position) {
                if(position == 0) {
                    mNextButton.setEnabled(true);
                    mNextButton.setVisibility(View.VISIBLE);
                    mPreviousButton.setEnabled(false);
                    mPreviousButton.setVisibility(View.INVISIBLE);
                    mNextButton.setText("Next");
                }
                else if(position == NUM_PAGES - 1){
                    mNextButton.setEnabled(true);
                    mNextButton.setVisibility(View.VISIBLE);
                    mPreviousButton.setEnabled(true);
                    mPreviousButton.setVisibility(View.VISIBLE);
                    mNextButton.setText("Finish");
                    mPreviousButton.setText("Previous");
                }
                else{
                    mNextButton.setEnabled(true);
                    mNextButton.setVisibility(View.VISIBLE);
                    mPreviousButton.setEnabled(true);
                    mPreviousButton.setVisibility(View.VISIBLE);
                    mNextButton.setText("Next");
                    mPreviousButton.setText("Previous");
                }
                pageNumberTextView.setText(position+1 + "/" + NUM_PAGES);
            }

            @Override
            public void onPageScrollStateChanged(int state) {

            }
        });

    }


    private class ScreenSlidePagerAdapter extends FragmentStatePagerAdapter {
        public ScreenSlidePagerAdapter(FragmentManager fm) {
            super(fm);
        }

        @Override
        public Fragment getItem(int position) {
            ScreenSlidePageFragment fragment = new ScreenSlidePageFragment();
            Bundle b = new Bundle();
            b.putString(MainActivity.DATA, data[position]);
            fragment.setArguments(b);
            return fragment;
        }
        @Override
        public int getCount() {
            return NUM_PAGES;
        }
    }


/*This class is to add zoom out effect when card is slided*/
    class ZoomOutPageTransformer implements ViewPager.PageTransformer {
        private static final float MIN_SCALE = 0.85f;
        private static final float MIN_ALPHA = 0.5f;

        public void transformPage(View view, float position) {
            int pageWidth = view.getWidth();
            int pageHeight = view.getHeight();

            if (position < -1) { // [-Infinity,-1)
                // This page is way off-screen to the left.
                view.setAlpha(0f);

            } else if (position <= 1) { // [-1,1]
                // Modify the default slide transition to shrink the page as well
                float scaleFactor = Math.max(MIN_SCALE, 1 - Math.abs(position));
                float vertMargin = pageHeight * (1 - scaleFactor) / 2;
                float horzMargin = pageWidth * (1 - scaleFactor) / 2;
                if (position < 0) {
                    view.setTranslationX(horzMargin - vertMargin / 2);
                } else {
                    view.setTranslationX(-horzMargin + vertMargin / 2);
                }

                // Scale the page down (between MIN_SCALE and 1)
                view.setScaleX(scaleFactor);
                view.setScaleY(scaleFactor);

                // Fade the page relative to its size.
                view.setAlpha(MIN_ALPHA +
                        (scaleFactor - MIN_SCALE) /
                                (1 - MIN_SCALE) * (1 - MIN_ALPHA));

            } else { // (1,+Infinity]
                // This page is way off-screen to the right.
                view.setAlpha(0f);
            }
        }
    }


}
