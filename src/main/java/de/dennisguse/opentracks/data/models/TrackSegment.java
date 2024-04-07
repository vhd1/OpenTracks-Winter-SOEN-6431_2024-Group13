package de.dennisguse.opentracks.data.models;

import androidx.annotation.NonNull;
import java.time.Instant;
import java.util.List;
import java.util.ArrayList;

public class TrackSegment {

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
}


