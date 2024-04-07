package de.dennisguse.opentracks.settings;

import android.content.SharedPreferences;
import android.os.Bundle;

import androidx.fragment.app.DialogFragment;
import androidx.preference.ListPreference;
import androidx.preference.Preference;
import androidx.preference.PreferenceFragmentCompat;
import androidx.preference.SwitchPreferenceCompat;

import de.dennisguse.opentracks.R;
import de.dennisguse.opentracks.data.models.ActivityType;
import de.dennisguse.opentracks.fragments.ChooseActivityTypeDialogFragment;
import android.util.Log;
import android.preference.PreferenceManager;



public class DefaultsSettingsFragment extends PreferenceFragmentCompat implements ChooseActivityTypeDialogFragment.ChooseActivityTypeCaller {

    // Used to forward update from ChooseActivityTypeDialogFragment; TODO Could be replaced with LiveData.
    private ActivityTypePreference.ActivityPreferenceDialog activityPreferenceDialog;
    private SwitchPreferenceCompat autoDiscardSwitch;
    private ListPreference recordLengthList;

    private int minDuration = 5;

    private final SharedPreferences.OnSharedPreferenceChangeListener sharedPreferenceChangeListener = (sharedPreferences, key) -> {
        if (PreferencesUtils.isKey(R.string.stats_units_key, key)) {
            getActivity().runOnUiThread(this::updateUnits);
        }
    };

    @Override
    public void onCreatePreferences(Bundle savedInstanceState, String rootKey) {
        addPreferencesFromResource(R.xml.settings_defaults);
        autoDiscardSwitch = findPreference(getString(R.string.auto_discard_key));
        recordLengthList = findPreference(getString(R.string.record_length_options));

        autoDiscardSwitch.setOnPreferenceChangeListener((preference, newValue) -> {
            boolean autoDiscardEnabled = (boolean) newValue;
            recordLengthList.setEnabled(autoDiscardEnabled);
            Log.d("DefaultsSettingsFragment", "Auto-Discard preference changed: " + autoDiscardEnabled);

            // Save the new value to SharedPreferences
            updateAutoDiscardSwitch(autoDiscardEnabled);

            return true;
        });

        recordLengthList.setOnPreferenceChangeListener((preference, newValue) -> {
            int selectedLength = Integer.parseInt((String) newValue);
            minDuration = selectedLength;
            Log.d("DefaultsSettingsFragment", "minDuration: " + minDuration);
            Log.d("DefaultsSettingsFragment", "Record Length preference changed: " + selectedLength + " seconds");

            // Save the new value to SharedPreferences
            updateRecordLengthList(selectedLength);

            return true;
        });


    }

    @Override
    public void onStart() {
        super.onStart();
        ((SettingsActivity) getActivity()).getSupportActionBar().setTitle(R.string.settings_defaults_title);
    }

    @Override
    public void onResume() {
        super.onResume();
        PreferencesUtils.registerOnSharedPreferenceChangeListener(sharedPreferenceChangeListener);
        updateUnits();
    }

    @Override
    public void onPause() {
        super.onPause();
        PreferencesUtils.unregisterOnSharedPreferenceChangeListener(sharedPreferenceChangeListener);
    }

    @Override
    public void onDisplayPreferenceDialog(Preference preference) {
        DialogFragment dialogFragment = null;
        if (preference instanceof ActivityTypePreference) {
            activityPreferenceDialog = ActivityTypePreference.ActivityPreferenceDialog.newInstance(preference.getKey());
            dialogFragment = activityPreferenceDialog;
        }

        if (dialogFragment != null) {
            dialogFragment.setTargetFragment(this, 0);
            dialogFragment.show(getParentFragmentManager(), getClass().getSimpleName());
            return;
        }

        super.onDisplayPreferenceDialog(preference);
    }

    private void updateUnits() {
        UnitSystem unitSystem = PreferencesUtils.getUnitSystem();

        ListPreference statsRatePreferences = findPreference(getString(R.string.stats_rate_key));

        int entriesId = switch (unitSystem) {
            case METRIC -> R.array.stats_rate_metric_options;
            case IMPERIAL_FEET, IMPERIAL_METER ->
                    R.array.stats_rate_imperial_options;
            case NAUTICAL_IMPERIAL ->
                    R.array.stats_rate_nautical_options;
        };

        String[] entries = getResources().getStringArray(entriesId);
        statsRatePreferences.setEntries(entries);

        HackUtils.invalidatePreference(statsRatePreferences);
    }

    @Override
    public void onChooseActivityTypeDone(ActivityType activityType) {
        if (activityPreferenceDialog != null) {
            activityPreferenceDialog.updateUI(activityType);
        }
    }

    // Method to update autoDiscardSwitch preference
    private void updateAutoDiscardSwitch(boolean autoDiscardEnabled) {
        PreferencesUtils.setBoolean(R.string.auto_discard_key, autoDiscardEnabled);
    }

    // Method to update recordLengthList preference
    private void updateRecordLengthList(int selectedLength) {
        PreferencesUtils.setString(R.string.record_length_default, String.valueOf(selectedLength));
    }

    // Method to check if an exercise record should be discarded based on its duration



}
