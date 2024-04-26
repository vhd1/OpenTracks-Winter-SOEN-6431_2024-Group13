package de.dennisguse.opentracks;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.CalendarView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.material.appbar.MaterialToolbar;

import de.dennisguse.opentracks.ui.aggregatedStatistics.daySpecificStats.DaySpecificActivity;
import de.dennisguse.opentracks.ui.aggregatedStatistics.daystatistics.DayStatisticsActivity;

import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;
import java.util.Objects;


public class CalendarActivity extends AppCompatActivity {

    private Date selectedDate;
    private Calendar calendar = Calendar.getInstance();
    public static final String EXTRA_TRACK_DATE = "track_date";


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        setSelectedDate(new Date());
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_calendar);
        MaterialToolbar toolbar = findViewById(R.id.calendar_toolbar);
        toolbar.setNavigationOnClickListener((view) -> finish());
        Bundle extras = getIntent().getExtras();

        Button dateSelectButton = findViewById(R.id.calendar_date_selected_activity);
        CalendarView calendarView = (CalendarView) findViewById(R.id.calendarView);
            calendarView.setOnDateChangeListener(new CalendarView.OnDateChangeListener() {
                @Override
                public void onSelectedDayChange(@NonNull CalendarView view, int year, int month, int dayOfMonth) {
                    setSelectedDate(new Date(year-1900, month, dayOfMonth));
                }
            });
            dateSelectButton.setOnClickListener((View v)->{
                Context context = v.getContext();
                Intent intent = new Intent(context, DaySpecificActivity.class);

                if(extras!=null && Objects.equals(extras.getString("Display Fields"), "Elevation and Speed")){
                    intent = new Intent(v.getContext(), ElevationAndSpeedActivity.class);
                }

                calendar.setTime(selectedDate);
                SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd", Locale.getDefault());
                String dateString = dateFormat.format(selectedDate);
                intent.putExtra(EXTRA_TRACK_DATE, dateString);
                v.getContext().startActivity(intent);
            });
    }


    public void setSelectedDate(Date date){
        this.selectedDate = date;
    }

    /**
     * Retrieves the currently selected date.
     *
     * @return The currently selected date.
     */
    public Date getSelectedDate(){
        // Assuming selectedDate is a member variable, return its value
        return selectedDate;
    }
}
