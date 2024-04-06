package de.dennisguse.opentracks.ui.aggregatedStatistics.SeasonStats;

import android.content.Intent;
import android.os.Bundle;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import de.dennisguse.opentracks.R;
import de.dennisguse.opentracks.databinding.ActivityRunAndChairStatBinding;

public class RunAndChairStatActivity extends AppCompatActivity {

    ActivityRunAndChairStatBinding binding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityRunAndChairStatBinding.inflate(getLayoutInflater());

        EdgeToEdge.enable(this);
        setContentView(binding.getRoot());
        binding.bottomNavigationViewSeasons.setOnItemSelectedListener(item -> {
            if (item.getItemId() == R.id.runs_frag) {
                replaceFragment(new RunsStatFragment());
            } else if (item.getItemId() == R.id.chairs_frag) {
                replaceFragment(new ChairsStatFragment());
            }
            return true;
        });

        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });
    }

    private void replaceFragment(Fragment fragment){
        FragmentManager fragmentManager = getSupportFragmentManager();
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
        if (fragment instanceof RunsStatFragment) {
            Intent intent = this.getIntent();
            String seasonName = "123";
            String days = "123";
            String runs = "123";
            String resort = "123";
            String vertical_m = "123";
            String distance = "123";
            String active= "123";
            String max_Speed="123";
            String max_alt="123";
            String tallestRun="123";
            String longestRun="123";
            if (intent != null){

                seasonName = intent.getStringExtra("seasonName");
                active = intent.getStringExtra("active");
                days = Integer.toString(intent.getIntExtra("days",-1));
                runs = Integer.toString(intent.getIntExtra("runs", -1));
                resort = Integer.toString(intent.getIntExtra("resort", -1));
                vertical_m = Integer.toString(intent.getIntExtra("vertical_m", -1));
                max_alt = Integer.toString(intent.getIntExtra("max_alt", -1));
                tallestRun = Integer.toString(intent.getIntExtra("tallestRun", -1));
                distance = Integer.toString((int)intent.getDoubleExtra("distance", -1.0));
                max_Speed = Integer.toString((int)intent.getDoubleExtra("max_Speed", -1.0));
                longestRun = Integer.toString((int)intent.getDoubleExtra("longestRun", -1.0));

            }
            // Pass the string as an argument if the fragment is RunsStatFragment
            fragmentTransaction.replace(R.id.frame_nav_season, ((RunsStatFragment) fragment).newInstance(seasonName, days, runs, resort, vertical_m, distance, active, max_Speed, max_alt, tallestRun, longestRun));
        } else {
            fragmentTransaction.replace(R.id.frame_nav_season, fragment);
        }
        fragmentTransaction.commit();
    }
}