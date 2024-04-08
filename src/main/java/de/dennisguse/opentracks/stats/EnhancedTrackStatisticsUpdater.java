package de.dennisguse.opentracks.stats;

import java.time.Duration;
import java.time.Instant;
import java.util.List;

import de.dennisguse.opentracks.data.models.Speed;
import de.dennisguse.opentracks.data.models.TrackPoint;
import de.dennisguse.opentracks.stats.TrackStatistics;
import de.dennisguse.opentracks.stats.TrackStatisticsUpdater;

public class EnhancedTrackStatisticsUpdater extends TrackStatisticsUpdater {
    private static final double CHAIRLIFT_SPEED_THRESHOLD = 1.5; // meters/second
    private static final double ALTITUDE_CHANGE_FOR_CHAIRLIFT = 10.0; // meters

    public EnhancedTrackStatisticsUpdater() {
        super();
    }

}
