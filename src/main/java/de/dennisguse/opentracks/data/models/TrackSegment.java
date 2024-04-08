package de.dennisguse.opentracks.data.models;

import androidx.annotation.NonNull;

import java.time.Duration;
import java.time.Instant;
import java.util.List;
import java.util.ArrayList;
import java.util.Optional;

public class TrackSegment {

    private static final String TAG = TrackSegment.class.getSimpleName();
    private TrackPoint.Id id;
    @NonNull
    private final Instant time;

    private List<TrackPoint> trackPoints;

    public TrackSegment(@NonNull Instant time) {
        this.time = time;
        trackPoints = new ArrayList<>();
    }

    public void addTrackPoint(TrackPoint point) {
        this.trackPoints.add(point);
    }

    public int getTrackPointCount() {
        return  trackPoints.size();
    }

    public Boolean hasTrackPoints() {
        return  !trackPoints.isEmpty();
    }

    public double getInitialElevation() {
        Optional<TrackPoint> firstPoint = trackPoints.stream().findFirst();
        if (firstPoint.isPresent()) {
            TrackPoint point = firstPoint.get();
            return point.getAltitude().toM();
        }
        return 0;
    }

    public long getDisplacement() {
        long displacement = 0;
        for (TrackPoint point: trackPoints) {
            if (point.hasAltitudeGain()) {
                displacement += point.getAltitudeGain();
            }

            if (point.hasAltitudeLoss()) {
                displacement += point.getAltitudeLoss();
            }
        }

        return displacement;
    }
    public Distance getDistance() {
        if (trackPoints == null) {
            return null;
        }
        TrackPoint first = trackPoints.get(0);
        TrackPoint last = trackPoints.get(trackPoints.size() - 1);
        return last.distanceToPrevious(first);
    }

    public Duration getTotalTime(){

        if(trackPoints == null){
            return null;
        }
        TrackPoint startTime = trackPoints.get(0);
        TrackPoint endTime = trackPoints.get(trackPoints.size() - 1);

        return Duration.between(startTime.getTime(), endTime.getTime());
    }

    public double getSpeed(){
        // in m/s
        double totalDistance = getDistance().toM();
        long totalTime = getTotalTime().toSeconds();
        return totalDistance/totalTime;
    }

    /**
     * Calculates the average distance between consecutive track points in the segment.
     *
     * @return The average distance as a Distance object.
     */
    public Distance getAvgDistance() {
        if (trackPoints.isEmpty()) {
            return Distance.of(0);
        }


        double totalDistance = 0;


        for (int i = 1; i < trackPoints.size(); i++) {
            TrackPoint previousPoint = trackPoints.get(i - 1);
            TrackPoint currentPoint = trackPoints.get(i);


            Distance distanceBetweenPoints = currentPoint.distanceToPrevious(previousPoint);


            totalDistance += distanceBetweenPoints.toM();
        }

        // Calculate the average distance by dividing the total distance by the number of segments
        double averageDistance = totalDistance / (trackPoints.size() - 1);


        return Distance.of(averageDistance);
    }
}