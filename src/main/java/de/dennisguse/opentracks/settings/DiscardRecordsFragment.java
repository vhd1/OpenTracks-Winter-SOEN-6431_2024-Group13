package de.dennisguse.opentracks.settings;

import android.os.Bundle;
import androidx.preference.Preference;
import androidx.preference.PreferenceFragmentCompat;
import androidx.preference.SwitchPreferenceCompat;
import androidx.preference.ListPreference;

import de.dennisguse.opentracks.R;

public class DiscardRecordsFragment extends PreferenceFragmentCompat {
    private SwitchPreferenceCompat autoDiscardSwitch;
    private ListPreference recordLengthList;
    private int minDuration = 0;

    @Override
    public void onCreatePreferences(Bundle savedInstanceState, String rootKey) {
        setPreferencesFromResource(R.xml.settings_defaults, rootKey);

        autoDiscardSwitch = findPreference(getString(R.string.auto_discard_title));
        recordLengthList = findPreference(getString(R.string.record_length_options));

        autoDiscardSwitch.setOnPreferenceChangeListener(new Preference.OnPreferenceChangeListener() {
            @Override
            public boolean onPreferenceChange(Preference preference, Object newValue) {
                boolean autoDiscardEnabled = (boolean) newValue;
                recordLengthList.setEnabled(autoDiscardEnabled);
                return true;
            }
        });

        recordLengthList.setOnPreferenceChangeListener(new Preference.OnPreferenceChangeListener() {
            @Override
            public boolean onPreferenceChange(Preference preference, Object newValue) {
                int selectedLength = Integer.parseInt((String) newValue); // Convert the selected value to an integer
                minDuration = selectedLength;



                return true;
            }
        });
    }

    // Method to check if an exercise record should be discarded based on its duration
    private boolean shouldDiscardRecord(int recordDuration) {
        return recordDuration < minDuration;
    }

    // Example method to handle storing or discarding exercise records
    private void processExerciseRecord(int recordDuration) {
        if (shouldDiscardRecord(recordDuration)) {
            // Discard the record
            // Add your logic here for discarding the record
            // For example: show a message to the user or log the event
        } else {
            // Store the record
            // Add your logic here for storing the record
            // For example: save the record to a database or perform further processing
        }
    }
}
