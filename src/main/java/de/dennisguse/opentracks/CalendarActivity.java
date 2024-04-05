package de.dennisguse.opentracks;
import android.content.Intent;
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
                    setSelectedDate(new Date(year-1900, month, dayOfMonth));
                }
            });
            dateSelectButton.setOnClickListener((view)->{
//                Intent intent = new Intent(getApplicationContext(), classname.class);
                //startActivity(intent);
//                intent.putExtra("Year", selectedDate.getYear());
//                intent.putExtra("Month", selectedDate.getMonth());
//                intent.putExtra("Day", selectedDate.getDay());

                // To extract
//                Bundle extras = getIntent().getExtras();
//                if (extras != null) {
//                    String value = extras.getString("key");
//                    //The key argument here must match that used in the other activity
//                }
            });
    }


    public void setSelectedDate(Date date){
        this.selectedDate = date;
    }

    public Date getSelectedDate(){
        return selectedDate;
    }
}
