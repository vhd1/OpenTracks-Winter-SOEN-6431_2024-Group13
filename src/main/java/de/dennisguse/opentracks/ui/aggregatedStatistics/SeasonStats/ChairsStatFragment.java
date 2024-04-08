package de.dennisguse.opentracks.ui.aggregatedStatistics.SeasonStats;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

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
    private static final String ARG_SEASON_vertical_distance_m = "vertical_distance_m";
    private static final String Most_Comman_Trail="most_comman_trail";

    private String mMostcommanTrail;
    private String mSeasonName;
    private String mDays;
    private String mTallestChair;
    private String mTotalDaysChairliftUsed;
    private String mVertical_Distance_m;

    public ChairsStatFragment() {
        // Required empty public constructor
    }

    public static ChairsStatFragment newInstance(String seasonName,String mostcommantrail, String days, String tallestChair, String totalDaysChairliftUsed, String vertical_m) {
        ChairsStatFragment fragment = new ChairsStatFragment();
        Bundle args = new Bundle();
        args.putString(ARG_SEASON_NAME, seasonName);
        args.putString(ARG_SEASON_DAYS, days);
        args.putString(ARG_SEASON_TALLEST_CHAIR, tallestChair);
        args.putString(ARG_SEASON_TOTAL_DAYS_CHAIRLIFT_USED, totalDaysChairliftUsed);
        args.putString(ARG_SEASON_vertical_distance_m, vertical_m);
        args.putString(Most_Comman_Trail,mostcommantrail);

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
            mVertical_Distance_m = getArguments().getString(ARG_SEASON_vertical_distance_m);
            mMostcommanTrail=getArguments().getString(Most_Comman_Trail);
        }

        TextView seasonNameTextView = view.findViewById(R.id.seasonNameTextView);
        TextView daysTextView = view.findViewById(R.id.days_tv);
        TextView tallestChairTextView = view.findViewById(R.id.tallestChair_tv);
        TextView totalDaysChairliftUsedTextView = view.findViewById(R.id.totalDaysChairliftUsed_tv);
        TextView verticalDistanceMTextView = view.findViewById(R.id.textView12);
        TextView mostcommantrail= view.findViewById(R.id.mostcommaintrail);

        if (mSeasonName != null) {
            seasonNameTextView.setText(mSeasonName);
            daysTextView.setText(mDays);
            tallestChairTextView.setText(mTallestChair);
            totalDaysChairliftUsedTextView.setText(mTotalDaysChairliftUsed);
            verticalDistanceMTextView.setText(mVertical_Distance_m);
            mostcommantrail.setText(mMostcommanTrail);

        }
        return view;
    }
}
