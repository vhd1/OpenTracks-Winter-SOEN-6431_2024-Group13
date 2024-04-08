package de.dennisguse.opentracks.stats;

import java.time.Duration;
import java.time.Instant;
import java.util.List;

import de.dennisguse.opentracks.data.models.Speed;
import de.dennisguse.opentracks.data.models.TrackPoint;
import de.dennisguse.opentracks.stats.TrackStatistics;
import de.dennisguse.opentracks.stats.TrackStatisticsUpdater;

public class EnhancedTrackStatistics extends TrackStatistics {
    private double averageSlope; // In percentage
    private double topSpeed; // In meters/second
    private Speed averageSpeed; // In meters/second
    private long timeOnChairlift; // In milliseconds
    private long waitingTimeForChairlift; // In milliseconds

    // Constructor
    public EnhancedTrackStatistics() {
        super();
        this.averageSlope = 0.0;
        this.topSpeed = 0.0;
        this.averageSpeed = Speed.of(0);
        this.timeOnChairlift = 0L;
        this.waitingTimeForChairlift = 0L;
    }
}