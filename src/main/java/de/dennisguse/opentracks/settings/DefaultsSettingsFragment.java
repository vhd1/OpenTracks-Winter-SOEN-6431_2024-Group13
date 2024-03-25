package de.dennisguse.opentracks.settings;

import android.app.AlertDialog;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.util.Log;
import android.view.View;
import android.widget.NumberPicker;

import androidx.fragment.app.DialogFragment;
import androidx.preference.ListPreference;
import androidx.preference.Preference;
import androidx.preference.PreferenceFragmentCompat;

import java.text.DateFormatSymbols;
import java.util.Locale;

import de.dennisguse.opentracks.R;
import de.dennisguse.opentracks.data.models.ActivityType;
import de.dennisguse.opentracks.fragments.ChooseActivityTypeDialogFragment;

public class DefaultsSettingsFragment extends PreferenceFragmentCompat
        implements ChooseActivityTypeDialogFragment.ChooseActivityTypeCaller {

    // Used to forward update from ChooseActivityTypeDialogFragment; TODO Could be
    // replaced with LiveData.y
    private ActivityTypePreference.ActivityPreferenceDialog activityPreferenceDialog;

    private final SharedPreferences.OnSharedPreferenceChangeListener sharedPreferenceChangeListener = (
            sharedPreferences, key) -> {
        if (PreferencesUtils.isKey(R.string.stats_units_key, key)) {
            getActivity().runOnUiThread(this::updateUnits);
        }
    };

    @Override
    public boolean onPreferenceTreeClick(Preference preference) {
        if (preference.getKey().equals(getString(R.string.ski_season_start_key))) {
            showCustomDatePickerDialog(); // Call method to show the dialog
            return true;
        }
        return super.onPreferenceTreeClick(preference);
    }

    @Override
    public void onCreatePreferences(Bundle savedInstanceState, String rootKey) {
        addPreferencesFromResource(R.xml.settings_defaults);
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

    private void showCustomDatePickerDialog() {
        SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(getContext());
        String defaultStartDate = prefs.getString(getString(R.string.ski_season_start_key), "09-01");

        AlertDialog.Builder builder = new AlertDialog.Builder(requireContext());
        View dialogView = getLayoutInflater().inflate(R.layout.custom_date_picker_dialog, null);
        builder.setView(dialogView);

        NumberPicker monthPicker = dialogView.findViewById(R.id.monthPicker);
        NumberPicker dayPicker = dialogView.findViewById(R.id.dayPicker);

        // Customize month picker with retrieved date or default
        String[] months = new DateFormatSymbols().getShortMonths();
        monthPicker.setMinValue(0);
        monthPicker.setMaxValue(months.length - 1);
        monthPicker.setDisplayedValues(months);

        // Customize day picker
        dayPicker.setMinValue(1);
        dayPicker.setMaxValue(31);

        // Set picker values to saved date or default
        String[] dateParts = defaultStartDate.split("-");
        int defaultMonth = Integer.parseInt(dateParts[0]) - 1;
        int defaultDay = Integer.parseInt(dateParts[1]);
        monthPicker.setValue(defaultMonth);
        dayPicker.setValue(defaultDay);

        builder.setTitle("Select Date");
        builder.setPositiveButton("OK", (dialog, which) -> {
            int selectedMonth = monthPicker.getValue();
            int selectedDay = dayPicker.getValue();
            String selectedDate = String.format(Locale.getDefault(), "%02d-%02d", selectedMonth + 1, selectedDay);
            SharedPreferences.Editor editor = prefs.edit();
            editor.putString(getString(R.string.ski_season_start_key), selectedDate);
            editor.apply();
        });
        builder.setNegativeButton("Cancel", (dialog, which) -> dialog.dismiss());

        AlertDialog dialog = builder.create();
        dialog.show();
    }

    private void updateUnits() {
        UnitSystem unitSystem = PreferencesUtils.getUnitSystem();
        ListPreference statsRatePreferences = findPreference(getString(R.string.stats_rate_key));

        // Check for null to avoid NullPointerException
        if (statsRatePreferences != null) {
            int entriesId = switch (unitSystem) {
                case METRIC -> R.array.stats_rate_metric_options;
                case IMPERIAL_FEET, IMPERIAL_METER -> R.array.stats_rate_imperial_options;
                case NAUTICAL_IMPERIAL -> R.array.stats_rate_nautical_options;
            };

            String[] entries = getResources().getStringArray(entriesId);
            statsRatePreferences.setEntries(entries);

            // Further actions could be added here if needed, e.g., updating other related
            // preferences.
        } else {
            Log.w("DefaultsSettingsFragment", "Stats rate preference is not found.");
        }
    }

    @Override
    public void onChooseActivityTypeDone(ActivityType activityType) {
        if (activityPreferenceDialog != null) {
            activityPreferenceDialog.updateUI(activityType);
        }
    }
}
