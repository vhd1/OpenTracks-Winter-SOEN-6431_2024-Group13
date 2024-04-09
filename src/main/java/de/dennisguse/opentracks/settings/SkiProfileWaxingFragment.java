package de.dennisguse.opentracks.settings;

import android.os.Bundle;
import android.text.InputFilter;
import android.text.Spanned;
import android.widget.EditText;
import androidx.annotation.Nullable;
import androidx.preference.PreferenceFragmentCompat;
import androidx.preference.EditTextPreference;
import androidx.preference.ListPreference;
import androidx.preference.Preference;
import java.util.Calendar;
import android.app.DatePickerDialog;
import android.widget.DatePicker;
import android.content.SharedPreferences;
import de.dennisguse.opentracks.R;


public class SkiProfileWaxingFragment extends PreferenceFragmentCompat {
    @Override
    public void onCreatePreferences(@Nullable Bundle savedInstanceState, @Nullable String rootKey) {
        setPreferencesFromResource(R.xml.fragment_ski_profile_waxing_info, rootKey);

        EditTextPreference waxingIntervalPreference = findPreference("waxing_interval");
        if (waxingIntervalPreference != null) {
            waxingIntervalPreference.setOnBindEditTextListener(new EditTextPreference.OnBindEditTextListener() {
                @Override
                public void onBindEditText(EditText editText) {
                    editText.setFilters(new InputFilter[] {new DecimalInputFilter()});
                }
            });

            waxingIntervalPreference.setOnPreferenceChangeListener(new Preference.OnPreferenceChangeListener() {
                @Override
                public boolean onPreferenceChange(Preference preference, Object newValue) {
                    String newValueString = (String) newValue;
                    preference.setSummary(newValueString + " km");
                    return true;
                }
            });

            // Update summary initially with the default value
            String defaultValue = waxingIntervalPreference.getText();
            waxingIntervalPreference.setSummary(defaultValue + " km");
        }

        ListPreference waxingTypePreference = findPreference("type_of_wax");
        if (waxingTypePreference != null) {
            waxingTypePreference.setOnPreferenceChangeListener(new Preference.OnPreferenceChangeListener() {
                @Override
                public boolean onPreferenceChange(Preference preference, Object newValue) {
                    String newValueString = (String) newValue;
                    int index = waxingTypePreference.findIndexOfValue(newValueString);
                    if (index >= 0) {
                        String entry = waxingTypePreference.getEntries()[index].toString();
                        preference.setSummary(entry);
                    }
                    return true;
                }
            });

            // Update summary initially with the default value
            String defaultValue = waxingTypePreference.getValue();
            int index = waxingTypePreference.findIndexOfValue(defaultValue);
            if (index >= 0) {
                String entry = waxingTypePreference.getEntries()[index].toString();
                waxingTypePreference.setSummary(entry);
            }
        }

        Preference lastWaxingDatePreference = findPreference("last_waxing_date");

        // Retrieve the last selected date from SharedPreferences
        SharedPreferences sharedPreferences = getPreferenceManager().getSharedPreferences();
        String lastSelectedDate = sharedPreferences.getString("last_waxing_date", "");

        lastWaxingDatePreference.setSummary(lastSelectedDate);
            lastWaxingDatePreference.setOnPreferenceClickListener(new Preference.OnPreferenceClickListener() {
                @Override
                public boolean onPreferenceClick(Preference preference) {
                    showDatePickerDialog();
                    return true;
                }
            });

        
    }


    // Validation for Waxing interval field
    private class DecimalInputFilter implements InputFilter {
        @Override
        public CharSequence filter(CharSequence source, int start, int end, Spanned dest, int dstart, int dend) {
            StringBuilder builder = new StringBuilder(dest);
            builder.replace(dstart, dend, source.subSequence(start, end).toString());
            if (!isValidDecimal(builder.toString())) {
                if (source.length() == 0) {
                    return dest.subSequence(dstart, dend);
                } else {
                    return "";
                }
            }
            return null;
        }

        private boolean isValidDecimal(String str) {
            return str.matches("^\\d*\\.?\\d*$");
        }
    }


    // Date picker and validation to ignore future dates
    private void showDatePickerDialog() {
        Calendar calendar = Calendar.getInstance();
        int year = calendar.get(Calendar.YEAR);
        int month = calendar.get(Calendar.MONTH);
        int dayOfMonth = calendar.get(Calendar.DAY_OF_MONTH);

        DatePickerDialog datePickerDialog = new DatePickerDialog(
                getActivity(),
                new DatePickerDialog.OnDateSetListener() {
                    @Override
                    public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
                        String selectedDate = dayOfMonth + "/" + (month + 1) + "/" + year;
                        Preference lastWaxingDatePreference = findPreference("last_waxing_date");
                        lastWaxingDatePreference.setSummary(selectedDate);

                        // Handle the selected date here, for example, save it to SharedPreferences
                        SharedPreferences.Editor editor = getPreferenceManager().getSharedPreferences().edit();
                        editor.putString("last_waxing_date", selectedDate);
                        editor.apply();
                    }
                },
                year, month, dayOfMonth
        );
        
        // Set maximum date limit to prevent selection of future dates
        datePickerDialog.getDatePicker().setMaxDate(System.currentTimeMillis());
        
        datePickerDialog.show();
    }

}
