package de.dennisguse.opentracks.ui.aggregatedStatistics.daystatistics;

import android.os.Bundle;
import android.view.View;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Locale;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

import de.dennisguse.opentracks.AbstractActivity;
import de.dennisguse.opentracks.databinding.StatisticsPerDayBinding;

public class DayStatisticsActivity extends AbstractActivity {

    private StatisticsPerDayBinding viewBinding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        viewBinding = StatisticsPerDayBinding.inflate(getLayoutInflater());

        setCurrentDate();

        // Assuming you have waitTimes data available here
        List<Integer> waitTimes = new ArrayList<>();
        waitTimes.add(300); // 5 minutes in seconds
        waitTimes.add(600); // 10 minutes in seconds
        waitTimes.add(120); // 2 minutes in seconds
        waitTimes.add(480); // 8 minutes in seconds
        setShortestWaitLabel(waitTimes);
        setTotalWaitLabel(waitTimes);

        List<Integer> speedDetails = new ArrayList<>();
        speedDetails.add(15);
        speedDetails.add(25);
        speedDetails.add(30);
        speedDetails.add(20);
        setMaxSpeedLabel(speedDetails);

        List<Double> skiiedKms = new ArrayList<>();
        skiiedKms.add(10.5);
        skiiedKms.add(8.2);
        skiiedKms.add(15.7);
        skiiedKms.add(12.3);
        setSkiiedKmsLabel(skiiedKms);
        setContentView(viewBinding.getRoot());
    }

    @Override
    protected View getRootView() {
        viewBinding = StatisticsPerDayBinding.inflate(getLayoutInflater());
        return viewBinding.getRoot();
    }

    // the following code snippet is the corresponding code in the activity for back button
    @Override
    protected void onStart() {
        super.onStart();
        viewBinding.statsBackButton.setOnClickListener(v -> finish());
    }


    //code to calculate the shortest wait time and set the label in the layout
    private String calculateShortestWaitTime(List<Integer> waitTimes) {
        if (waitTimes == null || waitTimes.isEmpty()) {
            return "00:00:00"; // return default value if no wait times available
        }

        int shortestWaitTime = Collections.min(waitTimes); // find the shortest wait time

        // convert the shortest wait time to a string in the format "HH:MM:SS"
        int hours = shortestWaitTime / 3600;
        int minutes = (shortestWaitTime % 3600) / 60;
        int seconds = shortestWaitTime % 60;

        return String.format(Locale.getDefault(), "%02d:%02d:%02d", hours, minutes, seconds);
    }

    // binding the calculated shortest wait time to the layout
    public void setShortestWaitLabel(List<Integer> waitTimes) {
        String shortestWaitTime = calculateShortestWaitTime(waitTimes);
        viewBinding.statsShortestWaitValue.setText(shortestWaitTime);
    }

    // calculate the longest wait time and label in the layout 
    private String calculateLongestWaitTime(List<Integer> waitTimes) {
        if (waitTimes == null || waitTimes.isEmpty()) {
            return "00:00:00";
        }

        int longestWaitTime = Collections.max(waitTimes);

        int hours = longestWaitTime / 3600;
        int minutes = (longestWaitTime % 3600) / 60;
        int seconds = longestWaitTime % 60;

        return String.format(Locale.getDefault(), "%02d:%02d:%02d", hours, minutes, seconds);
    }


    public void setLongestWaitLabel(List<Integer> waitTimes) {
        String longestWaitTime = calculateLongestWaitTime(waitTimes);
        viewBinding.statsLongestWaitValue.setText(longestWaitTime);
    }
    //end longest wait time
    //Calculate Max speed in the layout
    private String calculateMaxSpeed(List<Integer> speedDetails) {
        if (speedDetails == null || speedDetails.isEmpty()) {
            return "0";
        }

        int maxSpeed = Collections.max(speedDetails);

        return maxSpeed + "";
    }


    public void setMaxSpeedLabel(List<Integer> speedDetails) {
        String maxSpeed = calculateMaxSpeed(speedDetails);
        viewBinding.statsMaxSpeedValue.setText(maxSpeed);
    }
    //end of max speed
    // calculate the total wait time and set the label in the layout
    private String calculateTotalWaitTime(List<Integer> waitTimes) {
        if (waitTimes == null || waitTimes.isEmpty()) {
            return "00:00:00"; // return default value if no wait times available
        }

        int totalWaitTime = waitTimes.stream().mapToInt(Integer::intValue).sum();

        // convert the total wait time to a string in the format "HH:MM:SS"
        int hours = totalWaitTime / 3600;
        int minutes = (totalWaitTime % 3600) / 60;
        int seconds = totalWaitTime % 60;

        return String.format(Locale.getDefault(), "%02d:%02d:%02d", hours, minutes, seconds);
    }

    public void setTotalWaitLabel(List<Integer> waitTimes) {
        String totalWaitTime = calculateTotalWaitTime(waitTimes);
        viewBinding.statsAverageWaitTimeValue.setText(totalWaitTime);
    }

    public void setCurrentDate() {
        LocalDateTime dateTime = LocalDateTime.now();
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("EEEE d'\"' MMMM', 'HH:mm");
        String formattedDateTime = dateTime.format(formatter);
        viewBinding.statsStartDatetimeValue.setText(formattedDateTime);
    }

    //Total skieed km
    private String calculateSkiiedKms(List<Double> distance) {
        if (distance == null || distance.isEmpty()) {
            return "0.0";
        }

        Double skiiedkms = distance.stream().mapToDouble(Double::doubleValue).sum();
        return skiiedkms + "";
    }


    public void setSkiiedKmsLabel(List<Double> distance) {
        String skiiedKms = calculateSkiiedKms(distance);
        viewBinding.statsDistanceValue.setText(skiiedKms);
    }
    //Total skiied km ended 
    
    private String calculateAvgSpeed(List<Integer> speedDetails) {
        if (speedDetails == null || speedDetails.isEmpty()) {
            return "0";
        }

        int avgSpeed = speedDetails.stream().mapToInt(Integer::intValue).sum() / speedDetails.size();
        return avgSpeed + "";
    }


    public void setAvgSpeedLabel(List<Integer> speedDetails) {
        String avgSpeed = calculateAvgSpeed(speedDetails);
        viewBinding.statsAverageSpeedValue.setText(avgSpeed);
    }

}
