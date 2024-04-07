package de.dennisguse.opentracks.ui.aggregatedStatistics.SeasonStats;

import android.content.Intent;
import android.os.Bundle;
import android.renderscript.ScriptGroup;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListAdapter;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import java.util.ArrayList;

import de.dennisguse.opentracks.R;
import de.dennisguse.opentracks.databinding.ActivitySeasonStatBinding;

public class SeasonStatActivity extends AppCompatActivity {

    ActivitySeasonStatBinding binding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivitySeasonStatBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        EdgeToEdge.enable(this);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });
        String[] seasonNames = {"Season 2020 - 2021", "Season 2021 - 2022", "Season 2022 - 2023", "Season 2023 - 2024"};
        ArrayList<DummySeason> seasonArrayList = new ArrayList<>();
        for (int i = 0; i < seasonNames.length ; i++){
            seasonArrayList.add(new DummySeason(seasonNames[i],i,i,i,i,i,"10:10:10", i,i,i,i));
        }

        SeasonListAdapter listAdapter = new SeasonListAdapter(SeasonStatActivity.this, seasonArrayList);
        binding.seasonListView.setAdapter(listAdapter);
        binding.seasonListView.setClickable(true);
        binding.seasonListView.setOnItemClickListener(new AdapterView.OnItemClickListener(){

            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Intent intent = new Intent(SeasonStatActivity.this, RunAndChairStatActivity.class);
                intent.putExtra("seasonName",seasonArrayList.get(position).seasonName);
                intent.putExtra("days",seasonArrayList.get(position).days);
                intent.putExtra("runs",seasonArrayList.get(position).runs);
                intent.putExtra("resort",seasonArrayList.get(position).resort);
                intent.putExtra("vertical_m",seasonArrayList.get(position).vertical_m);
                intent.putExtra("distance",seasonArrayList.get(position).distance);
                intent.putExtra("active",seasonArrayList.get(position).active);
                intent.putExtra("max_Speed",seasonArrayList.get(position).max_Speed);
                intent.putExtra("max_alt",seasonArrayList.get(position).max_alt);
                intent.putExtra("tallestRun",seasonArrayList.get(position).tallestRun);
                intent.putExtra("longestRun",seasonArrayList.get(position).longestRun);
                startActivity(intent);
            }
        });
    }
}

//This class is each item in the list class which in our case is seasons. this is a dummy class just to have items You should use seasons in the code not ListItem class.
class DummySeason {
    String seasonName;
    int days;
    int runs;
    int resort;
    int vertical_m;
    double distance;
    String active;
    double max_Speed;
    int max_alt;
    int tallestRun;
    double longestRun;

    public DummySeason(String seasonName, int days, int runs, int resort, int vertical_m, double distance, String active, double max_Speed, int max_alt, int tallestRun, double longestRun) {
        this.seasonName = seasonName;
        this.days = days;
        this.runs = runs;
        this.resort = resort;
        this.vertical_m = vertical_m;
        this.distance = distance;
        this.active = active;
        this.max_Speed = max_Speed;
        this.max_alt = max_alt;
        this.tallestRun = tallestRun;
        this.longestRun = longestRun;
    }
}