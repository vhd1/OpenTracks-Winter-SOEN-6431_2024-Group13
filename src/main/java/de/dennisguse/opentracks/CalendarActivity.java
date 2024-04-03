package de.dennisguse.opentracks;
import android.os.Bundle;
import android.widget.Button;

import androidx.appcompat.app.AppCompatActivity;

import com.google.android.material.appbar.MaterialToolbar;


public class CalendarActivity extends AppCompatActivity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_calendar);
        MaterialToolbar toolbar = findViewById(R.id.calendar_toolbar);
        toolbar.setNavigationOnClickListener((view) -> finish());

        Button dateSelectButton = findViewById(R.id.calendar_date_selected_activity);
        dateSelectButton.setOnClickListener((view)->{
            // add selected date logic.
        });
    }

}
