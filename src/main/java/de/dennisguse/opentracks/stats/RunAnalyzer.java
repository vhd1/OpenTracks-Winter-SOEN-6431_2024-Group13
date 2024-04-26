package de.dennisguse.opentracks.stats;

import java.util.ArrayList;
import java.util.List;

import de.dennisguse.opentracks.data.models.Speed;
import de.dennisguse.opentracks.data.models.TrackPoint;
import de.dennisguse.opentracks.data.models.Run;

public class RunAnalyzer {
    private RunAnalyzer() {}

    private static final double ELEVATION_THRESHOLD = 5; // Meters
    private static final double SPEED_THRESHOLD = 2;   // Meters per second
    private static final long TIME_THRESHOLD = 10;    // Seconds

    public static List<Run> identifyRuns(String sessionId, List<TrackPoint> trackPoints) {
        List<Run> runs = new ArrayList<>();
        Run currentRun = null;

        for (int i = 1; i < trackPoints.size(); i++) {
            TrackPoint currentPoint = trackPoints.get(i);
            TrackPoint previousPoint = trackPoints.get(i - 1);

            double elevationChange = Math.abs(currentPoint.getAltitude().toM() - previousPoint.getAltitude().toM());
            double speedDifference = Math.abs(currentPoint.getSpeed().toMPS() - previousPoint.getSpeed().toMPS());

            if (currentRun == null && elevationChange > ELEVATION_THRESHOLD && speedDifference > SPEED_THRESHOLD) {
                // Start a new run
                currentRun = new Run(sessionId); // Assuming a constructor exists
                currentRun.setStartTime(currentPoint.getTime().toEpochMilli()); // Convert Instant to long
                currentRun.addTrackPoint(currentPoint); // Add starting point
                runs.add(currentRun);
            } else if (currentRun != null && elevationChange <= ELEVATION_THRESHOLD && speedDifference <= SPEED_THRESHOLD) {
                long timeSinceLastChange = currentPoint.getTime().getEpochSecond() - currentRun.getEndTime();
                if (timeSinceLastChange > TIME_THRESHOLD) {
                    // End the current run
                    currentRun.setEndTime(previousPoint.getTime().toEpochMilli()); // Convert Instant to long
                    currentRun = null;
                } else {
                    // Update current run's track points
                    currentRun.addTrackPoint(currentPoint);
                }
            } else if (currentRun != null) {
                // Update the current run's distance, elevation, and other metrics
                // (code for updating other metrics omitted for brevity)
                currentRun.addTrackPoint(currentPoint);
            }
        }

        if (currentRun != null) {
            // Handle the last run if it doesn't have an explicit end point
            currentRun.setEndTime(trackPoints.get(trackPoints.size() - 1).getTime().toEpochMilli()); // Convert Instant to long
            currentRun.addTrackPoint(trackPoints.get(trackPoints.size() - 1)); // Add ending point
        }

        return runs;
    }


    public static void calculateMaxSpeedPerRun(List<Run> runs) {
        for (Run run : runs) {
            float maxSpeed = 0;

            List<TrackPoint> trackPoints = run.getTrackPoints();
            for (TrackPoint trackPoint : trackPoints) {
                double speed = trackPoint.getSpeed().toMPS();
                maxSpeed = (float) Math.max(maxSpeed, speed);
            }

            run.setMaxSpeed(Speed.of(maxSpeed));
        }
    }

    public static void calculateAvgSpeedStatistics(List<Run> runs) {
        double totalSpeed = 0;
        double averageSpeed;
        ArrayList<Speed> speedList = new ArrayList<>();
        for (Run run : runs) {
            List<TrackPoint> trackPoints = run.getTrackPoints();
            for (TrackPoint tp : trackPoints) {
                Speed speed = tp.getSpeed();
                speedList.add(speed);
                totalSpeed += speed.toMPS(); // convert speed to m/s
            }

            // Set ski run average speed
            averageSpeed = totalSpeed / speedList.size();
            run.setAverageSpeed(Speed.of(averageSpeed));
        }

    }
}


