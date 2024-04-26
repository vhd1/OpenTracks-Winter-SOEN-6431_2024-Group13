package de.dennisguse.opentracks.introduction;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import de.dennisguse.opentracks.R;
import de.dennisguse.opentracks.data.models.Track;
import de.dennisguse.opentracks.data.tables.TracksColumns;
import de.dennisguse.opentracks.databinding.OsmDashboardBinding;

public class OSMDashboardFragment extends Fragment {

    public static OSMDashboardFragment newInstance() {
        return new OSMDashboardFragment();
    }

    private OsmDashboardBinding viewBinding;

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        viewBinding = OsmDashboardBinding.inflate(inflater, container, false);
        return viewBinding.getRoot();
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        viewBinding = null;
    }

    private void updateMostCommonTrail() {
        // Retrieve track data from Shared Preferences
        List<Track> tracksData = getTracksDataFromSharedPreferences();

        // Identify the most common trail
        List<Track> mostCommonTrail = identifyMostFrequentTrailByName(tracksData);

        if (mostCommonTrail != null) {
            // Update UI elements with information from the mostCommonTrail list
            TextView commonTrailTextView = viewBinding.getRoot().findViewById(R.id.name_of_common_trail);
            updateTrailName(mostCommonTrail, commonTrailTextView);
        } else {
            // Handle case where no common trail is found
            Toast.makeText(requireActivity(), "No common trail found", Toast.LENGTH_SHORT).show();
            updateTrailName(null, null); // Pass nulls if no common trail or TextView found
        }
    }

    private List<Track> getTracksDataFromSharedPreferences() {
        SharedPreferences sharedPref = requireActivity().getSharedPreferences("TrackData", Context.MODE_PRIVATE);
        String serializedTracks = sharedPref.getString("tracks", null);

        List<String> tracksData = new ArrayList<>();
        if (serializedTracks != null) {
            try {
                // Parse the JSON string
                JSONArray jsonArray = new JSONArray(serializedTracks);

                // Loop through each element in the JSON array
                for (int i = 0; i < jsonArray.length(); i++) {
                    JSONObject trackObject = jsonArray.getJSONObject(i);

                    tracksData.add(TracksColumns.MAX_ALTITUDE);
                }
            } catch (JSONException e) {
                e.printStackTrace();
                // Handle parsing exceptions appropriately (e.g., return empty list)
            }
        }

        return new ArrayList<>(); // Empty list if no data is found
    }

    private List<Track> identifyMostFrequentTrailByName(List<Track> tracksData) {
        if (tracksData.isEmpty()) {
            return null;
        }
        // Create a HashMap to store trail name counts
        HashMap<String, Integer> trailNameCounts = new HashMap<>();
        // Count occurrences of each trail name
        for (Track track : tracksData) {
            String trailName = track.getName();
            if (trailNameCounts.containsKey(trailName)) {
                trailNameCounts.put(trailName, trailNameCounts.get(trailName) + 1);
            } else {
                trailNameCounts.put(trailName, 1);
            }
        }

        // Find the trail name with the highest count
        int maxCount = 0;
        String mostFrequentTrailName = null;
        for (Map.Entry<String, Integer> entry : trailNameCounts.entrySet()) {
            if (entry.getValue() > maxCount) {
                maxCount = entry.getValue();
                mostFrequentTrailName = entry.getKey();
            }
        }

        // Filter tracks to get the list with the most frequent name
        List<Track> mostCommonTrail = new ArrayList<>();
        for (Track track : tracksData) {
            if (track.getName().equals(mostFrequentTrailName)) {
                mostCommonTrail.add(track);
            }
        }

        return mostCommonTrail;
    }

    private void updateTrailName(List<Track> mostCommonTrail, TextView commonTrailTextView) {
        if (mostCommonTrail != null && mostCommonTrail.size() > 0) {
            // Assuming the first element in the list is the most common trail
            Track commonTrail = mostCommonTrail.get(0);
            String trailName = commonTrail.getName();
            commonTrailTextView.setText(trailName); // Update the TextView
        } else {
            commonTrailTextView.setText(""); // Clear the TextView
        }
    }

    @Override
    public void onResume() {
        super.onResume();
        updateMostCommonTrail(); // Call to update on fragment resume
    }
}