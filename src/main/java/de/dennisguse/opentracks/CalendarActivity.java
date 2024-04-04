package de.dennisguse.opentracks;
import android.os.Bundle;
import android.widget.Button;
import android.widget.CalendarView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.material.appbar.MaterialToolbar;
import java.util.Date;


public class CalendarActivity extends AppCompatActivity {

    private Date selectedDate;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_calendar);
        MaterialToolbar toolbar = findViewById(R.id.calendar_toolbar);
        toolbar.setNavigationOnClickListener((view) -> finish());

        Button dateSelectButton = findViewById(R.id.calendar_date_selected_activity);
        CalendarView calendarView = (CalendarView) findViewById(R.id.calendarView);
            calendarView.setOnDateChangeListener(new CalendarView.OnDateChangeListener() {
                @Override
                public void onSelectedDayChange(@NonNull CalendarView view, int year, int month, int dayOfMonth) {
                    // Display the selected date
                    setSelectedDate(new Date(year-1900, month, dayOfMonth));
                }
            });
            dateSelectButton.setOnClickListener((view)->{
                // Navigate to a new screen with passing the date;
            });
    }


    public void setSelectedDate(Date date){
        this.selectedDate = date;
    }

    public Date getSelectedDate(){
        return selectedDate;
    }
}
