package de.dennisguse.opentracks.ui.aggregatedStatistics.SeasonStats;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.ImageButton;


import androidx.fragment.app.Fragment;

import de.dennisguse.opentracks.R;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link ChairsStatFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class ChairsStatFragment extends Fragment {

    private static final String ARG_SEASON_NAME = "seasonName";
    private static final String ARG_SEASON_DAYS = "days";
    private static final String ARG_SEASON_TALLEST_CHAIR = "tallestChair";
    private static final String ARG_SEASON_TOTAL_DAYS_CHAIRLIFT_USED = "totalNumberOfDaysChairliftUsed";
    private static final String[] ARG_SEASON_FAVORITE_CHAIRS = {"favoriteChair1", "favoriteChair2", "favoriteChair3", "favoriteChair4", "favoriteChair5"};
    private static final String ARG_MOST_COMMON_TRAIL="mostCommonTrail";

    private String mMostCommonTrail;
    private String mSeasonName;

    private String mDays;
    private String mTallestChair;
    private String mTotalDaysChairliftUsed;
    private String[] mFavoriteChairs = new String[5];

    public ChairsStatFragment() {
        // Required empty public constructor
    }

    public static ChairsStatFragment newInstance(String seasonName,String mostCommonTrail, String days, String tallestChair, String totalDaysChairliftUsed, String[] favoriteChairs) {
        ChairsStatFragment fragment = new ChairsStatFragment();
        Bundle args = new Bundle();
        args.putString(ARG_SEASON_NAME, seasonName);
        args.putString(ARG_SEASON_DAYS, days);
        args.putString(ARG_SEASON_TALLEST_CHAIR, tallestChair);
        args.putString(ARG_SEASON_TOTAL_DAYS_CHAIRLIFT_USED, totalDaysChairliftUsed);        
        args.putString(ARG_MOST_COMMON_TRAIL,mostCommonTrail);
        for(int i = 0; i < 5; i++){
            args.putString(ARG_SEASON_FAVORITE_CHAIRS[i], favoriteChairs[i]);
        }

        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_chairs_stat, container, false);
        if (getArguments() != null) {
            mSeasonName = getArguments().getString(ARG_SEASON_NAME);
            mDays = getArguments().getString(ARG_SEASON_DAYS);
            mTallestChair = getArguments().getString(ARG_SEASON_TALLEST_CHAIR);
            mTotalDaysChairliftUsed = getArguments().getString(ARG_SEASON_TOTAL_DAYS_CHAIRLIFT_USED);
            mMostCommonTrail=getArguments().getString(ARG_MOST_COMMON_TRAIL);
            for (int i = 0; i < 5; i++) {
                mFavoriteChairs[i] = getArguments().getString(ARG_SEASON_FAVORITE_CHAIRS[i]);
            }
        }

        TextView seasonNameTextView = view.findViewById(R.id.seasonNameTextView);
        TextView daysTextView = view.findViewById(R.id.days_tv);
        TextView tallestChairTextView = view.findViewById(R.id.tallestChair_tv);
        TextView totalDaysChairliftUsedTextView = view.findViewById(R.id.totalDaysChairliftUsed_tv);
        TextView mostCommonTrail= view.findViewById(R.id.name_of_common_trail);
        TextView favoriteChairsTextView1 = view.findViewById(R.id.favChairTextView1);
        TextView favoriteChairsTextView2 = view.findViewById(R.id.favChairTextView2);
        TextView favoriteChairsTextView3 = view.findViewById(R.id.favChairTextView3);
        TextView favoriteChairsTextView4 = view.findViewById(R.id.favChairTextView4);
        TextView favoriteChairsTextView5 = view.findViewById(R.id.favChairTextView5);

         // Find the ImageButton by its ID
         ImageButton backButton = view.findViewById(R.id.imageButton);
                 // Set OnClickListener to handle the click event
        backButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Finish the current activity to go back to the previous one
                getActivity().finish();
            }
        });

        if (seasonNameTextView != null && mSeasonName != null) {
            seasonNameTextView.setText(mSeasonName);
            daysTextView.setText(mDays);
            tallestChairTextView.setText(mTallestChair);
            totalDaysChairliftUsedTextView.setText(mTotalDaysChairliftUsed);            
            mostCommonTrail.setText(mMostCommonTrail);
            favoriteChairsTextView1.setText(mFavoriteChairs[0]);
            favoriteChairsTextView2.setText(mFavoriteChairs[1]);
            favoriteChairsTextView3.setText(mFavoriteChairs[2]);
            favoriteChairsTextView4.setText(mFavoriteChairs[3]);
            favoriteChairsTextView5.setText(mFavoriteChairs[4]);

        }
        return view;
    }
    
}
