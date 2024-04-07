package de.dennisguse.opentracks.settings;

import android.os.Bundle;

import androidx.annotation.Nullable;
import androidx.preference.*;

import de.dennisguse.opentracks.R;

public class SkiProfileMaintenanceFragment extends PreferenceFragmentCompat {

    private Preference skiMaintenancePreference;

    @Override
    public void onCreatePreferences(Bundle savedInstanceState, String rootKey) {
        addPreferencesFromResource(R.xml.fragment_ski_profile_ski_maintenance);

        skiMaintenancePreference = findPreference("ski_maintenance");
        updateSharpeningInfo();
    }

    @Override
    public void onResume() {
        super.onResume();
        // Register for updates to sharpening data
    }

    @Override
    public void onPause() {
        super.onPause();
        // Unregister from updates
    }

    private void updateSharpeningInfo() {
        // Access sharpening data
        // Calculate km skied, % of interval reached, months since last sharpening
        // Set summary for sharpeningInfoPreference
        // Schedule notifications if needed
    }
}