package de.dennisguse.opentracks.settings;

import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.provider.MediaStore;
import android.text.InputFilter;
import android.text.InputType;
import android.view.View;
import android.widget.ImageView;
import android.widget.Toast;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.PickVisualMediaRequest;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.preference.CheckBoxPreference;
import androidx.preference.EditTextPreference;
import androidx.preference.ListPreference;
import androidx.preference.Preference;
import androidx.preference.PreferenceFragmentCompat;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import de.dennisguse.opentracks.R;
import android.app.DatePickerDialog;
import java.util.Calendar;
import java.util.Locale;

public class ProfileSettingsFragment extends PreferenceFragmentCompat {
    ImageViewPreference imageViewPreference;
    ActivityResultLauncher<PickVisualMediaRequest> pickMedia;
    Bitmap profilePicture;

    private final SharedPreferences.OnSharedPreferenceChangeListener sharedPreferenceChangeListener = (
            sharedPreferences, key) -> {
        if (PreferencesUtils.isKey(R.string.night_mode_key, key)) {
            getActivity().runOnUiThread(PreferencesUtils::applyNightMode);
        }
    };

    @Override
    public void onCreatePreferences(Bundle savedInstanceState, String rootKey) {
        addPreferencesFromResource(R.xml.settings_profile);
        ListPreference countryPreference = findPreference(getString(R.string.settings_profile_country_key));

        String selectedCountryValue = PreferencesUtils.getSelectedCountry();
        countryPreference.setSummary(selectedCountryValue);

        countryPreference.setOnPreferenceChangeListener((preference, newValue) -> {
            // Save the selected country to SharedPreferences
            PreferencesUtils.setSelectedCountry((String) newValue);

            // Update summary with selected country
            preference.setSummary((String) newValue);
            return true;
        });

        handleProfilePicture();
        Preference dobPreference = findPreference(getString(R.string.settings_profile_dob_key));
        if (dobPreference != null) {
            dobPreference.setOnPreferenceClickListener(preference -> {
                showDatePickerDialog(dobPreference);
                return true;
            });

            updateDateOfBirthPreferenceSummary(dobPreference);
        }

        ListPreference genderPreference = findPreference(getString(R.string.settings_profile_gender_key));

        String selectedGenderValue = PreferencesUtils.getSelectedGender();
        genderPreference.setSummary(selectedGenderValue);

        genderPreference.setOnPreferenceChangeListener((preference, newValue) -> {
            // Save the selected gender to SharedPreferences
            PreferencesUtils.setSelectedGender((String) newValue);

            // Update summary with selected gender
            preference.setSummary((String) newValue);
            return true;
        });

        CheckBoxPreference leaderboardSharePreference = findPreference(
                getString(R.string.settings_profile_leaderboard_share_key));
        leaderboardSharePreference.setSummary(leaderboardSharePreference.isChecked()
                ? getString(R.string.settings_profile_leaderboard_share_summary_on)
                : getString(R.string.settings_profile_leaderboard_share_summary_off));

        leaderboardSharePreference.setOnPreferenceChangeListener((preference, newValue) -> {
            boolean isChecked = (boolean) newValue;
            preference.setSummary(isChecked
                    ? getString(R.string.settings_profile_leaderboard_share_summary_on)
                    : getString(R.string.settings_profile_leaderboard_share_summary_off));

            PreferencesUtils.setBoolean(R.string.settings_profile_leaderboard_share_key, isChecked);

            return true;
        });

        EditTextPreference heightInput = findPreference(getString(R.string.settings_profile_height_key));
        heightInput.setDialogTitle(getString(R.string.settings_profile_height_dialog_title));
        heightInput.setOnBindEditTextListener(editText -> {
            editText.setSingleLine(true);
            editText.setInputType(InputType.TYPE_CLASS_NUMBER | InputType.TYPE_NUMBER_FLAG_DECIMAL); // Allow decimal numbers
            editText.setFilters(new InputFilter[]{new InputFilter.LengthFilter(5)});
            editText.setHint("Feet");
        });

        // Add validation to height input
        heightInput.setOnPreferenceChangeListener((preference, newValue) -> {
            String heightStr = (String) newValue;
            if (isValidHeight(heightStr)) {
                // Save the height to SharedPreferences
                PreferencesUtils.setString(R.string.settings_profile_height_key, heightStr);
                // Set the summary dynamically
                preference.setSummary(heightStr);
                return true;
            } else {
                Toast.makeText(requireContext(), "Please enter a valid height", Toast.LENGTH_SHORT).show();
                return false;
            }
        });
    }

    // Method to validate height input
    private boolean isValidHeight(String heightStr) {
        if (TextUtils.isEmpty(heightStr)) {
            return false;
        }
        try {
            double height = Double.parseDouble(heightStr);
            // Assuming a height between 2 and 8 feet is valid
            return height >= 2 && height <= 8;
        } catch (NumberFormatException e) {
            return false;
        }
    }

    // Method to convert height between units
    private String convertHeight(double height, String newUnit) {
        switch (newUnit) {
            case "feet":
                return String.valueOf(height); // No conversion needed if it is already in feet
            case "meters":
                return String.format(Locale.getDefault(), "%.2f", height * 0.3048); // Convert to meters
            case "centimeters":
                return String.format(Locale.getDefault(), "%.2f", height * 30.48); // Convert to centimeters
            default:
                return String.valueOf(height); // Default to feet
        }
    }

    @Override
    public void onStart() {
        super.onStart();
        ((SettingsActivity) getActivity()).getSupportActionBar().setTitle(R.string.settings_profile_title);
        imageViewPreference = findPreference(getString(R.string.settings_profile_profile_picture_key));
        ImageView imageView = imageViewPreference.getImageView();
        profilePicture = loadProfilePicture();
        if (profilePicture != null && imageView != null) {
            imageView.setImageBitmap(profilePicture);
        }
    }

    @Override
    public void onResume() {
        super.onResume();

        EditTextPreference nickNameInput = findPreference(getString(R.string.settings_profile_nickname_key));
        nickNameInput.setDialogTitle(getString(R.string.settings_profile_nickname_dialog_title));
        nickNameInput.setOnBindEditTextListener(editText -> {
            editText.setSingleLine(true);
            editText.selectAll(); // select all text
            int maxNicknameLength = 20;
            editText.setFilters(new InputFilter[] { new InputFilter.LengthFilter(maxNicknameLength) });
        });

        EditTextPreference heightInput = findPreference(getString(R.string.settings_profile_height_key));
        heightInput.setDialogTitle(getString(R.string.settings_profile_height_dialog_title));
        heightInput.setOnBindEditTextListener(editText -> {
            editText.setSingleLine(true);
            editText.setInputType(InputType.TYPE_CLASS_NUMBER | InputType.TYPE_NUMBER_FLAG_DECIMAL);
            editText.setFilters(new InputFilter[] { new InputFilter.LengthFilter(5) });
            editText.setHint("Feet");
        });

        ListPreference countryPreference = findPreference(getString(R.string.settings_profile_country_key));
        String selectedCountryValue = PreferencesUtils.getSelectedCountry();
        if (selectedCountryValue != null && !selectedCountryValue.isEmpty()) {
            // Update summary with saved selected country
            countryPreference.setSummary(selectedCountryValue);
        }

        PreferencesUtils.registerOnSharedPreferenceChangeListener(sharedPreferenceChangeListener);
    }

    @Override
    public void onPause() {
        super.onPause();
        PreferencesUtils.unregisterOnSharedPreferenceChangeListener(sharedPreferenceChangeListener);
    }

    private void handleProfilePicture() {
        imageViewPreference = findPreference(getString(R.string.settings_profile_profile_picture_key));

        if (imageViewPreference != null) {
            pickMedia = registerForActivityResult(new ActivityResultContracts.PickVisualMedia(), uri -> {
                // Callback is invoked after the user selects a media item or closes the
                // photo picker.
                if (uri != null) {
                    try {
                        ImageView imageView2 = imageViewPreference.getImageView();
                        profilePicture = MediaStore.Images.Media.getBitmap(getContext().getContentResolver(), uri);
                        imageView2.setImageBitmap(profilePicture);

                        try {
                            File file = new File(getContext().getFilesDir(),
                                    getString(R.string.settings_profile_profile_picture_key));
                            FileOutputStream fileOutputStream = new FileOutputStream(file);
                            profilePicture.compress(Bitmap.CompressFormat.JPEG, 100, fileOutputStream);
                            fileOutputStream.close();

                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                } else {
                    Toast.makeText(getContext(), "No media selected", Toast.LENGTH_SHORT).show();
                }
            });
            imageViewPreference.setImageClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    // do whatever you want on image click here
                    pickMedia.launch(new PickVisualMediaRequest.Builder()
                            .setMediaType(ActivityResultContracts.PickVisualMedia.ImageOnly.INSTANCE)
                            .build());
                }
            });
        }
    }

    private Bitmap loadProfilePicture() {
        Bitmap b = null;

        try {
            File f = new File(getContext().getFilesDir(), getString(R.string.settings_profile_profile_picture_key));
            b = BitmapFactory.decodeStream(new FileInputStream(f));
        } catch (FileNotFoundException ex) {
            // No image to display, do nothing?
        }

        return b;
    }

    private void showDatePickerDialog(Preference dobPreference) {
        Calendar calendar = Calendar.getInstance();
        int currentYear = calendar.get(Calendar.YEAR);
        int currentMonth = calendar.get(Calendar.MONTH);
        int currentDay = calendar.get(Calendar.DAY_OF_MONTH);

        DatePickerDialog datePickerDialog = new DatePickerDialog(requireContext(),
                (view, year, monthOfYear, dayOfMonth) -> {
                    Calendar selectedCalendar = Calendar.getInstance();
                    selectedCalendar.set(year, monthOfYear, dayOfMonth);

                    // Calculate 18 years ago
                    Calendar eighteenYearsAgo = Calendar.getInstance();
                    eighteenYearsAgo.add(Calendar.YEAR, -18);

                    // Calculate 120 years ago
                    Calendar hundredtwentyYearsAgo = Calendar.getInstance();
                    hundredtwentyYearsAgo.add(Calendar.YEAR, -120);

                    // Check if the selected date is not in the future and greater or equal to 18
                    // years ago
                    if (selectedCalendar.compareTo(calendar) <= 0) {
                        if (selectedCalendar.compareTo(eighteenYearsAgo) >= 0) {
                            Toast.makeText(requireContext(), "Please select a valid date (at least 18 years ago)",
                                    Toast.LENGTH_SHORT).show();
                        } else {
                            if (selectedCalendar.compareTo(hundredtwentyYearsAgo) >= 0) {
                                // Save the selected date of birth to SharedPreferences
                                String dob = String.format(Locale.getDefault(), "%02d/%02d/%04d", monthOfYear + 1,
                                        dayOfMonth, year);
                                PreferencesUtils.setDateOfBirth(dob);

                                // Update summary with selected date of birth
                                dobPreference.setSummary(dob);
                            } else {
                                Toast.makeText(requireContext(),
                                        "Please select a valid date (no more than 120 years ago)", Toast.LENGTH_SHORT)
                                        .show();
                            }
                        }
                    } else {
                        // Show a toast indicating that the selected date is invalid
                        Toast.makeText(requireContext(), "Please select a valid date (not today or in the future)",
                                Toast.LENGTH_SHORT).show();
                    }
                }, currentYear, currentMonth, currentDay);

        datePickerDialog.show();
    }

    private void updateDateOfBirthPreferenceSummary(Preference dobPreference) {
        String dob = PreferencesUtils.getDateOfBirth();
        if (dob != null && !dob.isEmpty()) {
            dobPreference.setSummary(dob);
        }
    }
}