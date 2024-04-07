package de.dennisguse.opentracks.data.models;

import android.util.Log;

import androidx.annotation.NonNull;
import java.time.Instant;
import java.util.List;
import java.util.ArrayList;

import de.dennisguse.opentracks.io.file.importer.TrackImporter;

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

    public Distance getDistanceBetweenFirstAndLast() {
        if (trackPoints == null) {
            return null;
        }
        TrackPoint first = trackPoints.get(0);
        TrackPoint last = trackPoints.get(trackPoints.size() - 1);
        Distance distance = last.distanceToPrevious(first);
        Log.d(TAG, "Kevin: distance between first and last in meters: " + distance.toM());
        return distance;
    }
}


