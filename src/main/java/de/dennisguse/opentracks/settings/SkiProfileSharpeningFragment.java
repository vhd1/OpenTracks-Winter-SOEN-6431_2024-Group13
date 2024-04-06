package de.dennisguse.opentracks.settings;

import android.os.Bundle;

import androidx.annotation.Nullable;
import androidx.preference.PreferenceFragmentCompat;

import de.dennisguse.opentracks.R;

public class SkiProfileSharpeningFragment extends PreferenceFragmentCompat {
    @Override
    public void onCreatePreferences(@Nullable Bundle savedInstanceState, @Nullable String rootKey) {
        addPreferencesFromResource(R.xml.fragment_ski_profile_sharpening_info);
    }
}
