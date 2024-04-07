package de.dennisguse.opentracks.ui.aggregatedStatistics.daySpecificStats;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;

import de.dennisguse.opentracks.AbstractTrackDeleteActivity;
import de.dennisguse.opentracks.R;
import de.dennisguse.opentracks.data.ContentProviderUtils;
import de.dennisguse.opentracks.data.TrackDataHub;
import de.dennisguse.opentracks.data.TrackPointIterator;
import de.dennisguse.opentracks.data.models.Track;
import de.dennisguse.opentracks.data.models.TrackPoint;
import de.dennisguse.opentracks.data.models.TrackSegment;
import de.dennisguse.opentracks.databinding.ActivityDaySpecificBinding;

import java.time.format.DateTimeFormatter;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Date;
import java.time.ZoneId;
import java.util.List;

public class DaySpecificActivity extends AbstractTrackDeleteActivity {

    private ActivityDaySpecificBinding viewBinding;
    private static final String TAG = DaySpecificActivity.class.getSimpleName();
    public static final String EXTRA_TRACK_ID = "track_id";
    private Date activityDate;
    private ContentProviderUtils contentProviderUtils;
    private TrackDataHub trackDataHub;
    private Track.Id trackId;
    private List<TrackSegment> trackSegments;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_day_specific);
        contentProviderUtils = new ContentProviderUtils(this);
        handleIntent(getIntent());
        trackDataHub = new TrackDataHub(this);
        trackSegments = new ArrayList<>();
        setSupportActionBar(viewBinding.bottomAppBarLayout.bottomAppBar);
    }

    @Override
    protected void onStart() {
        super.onStart();
//        trackDataHub.start();
    }

    @Override
    protected void onResume() {
        super.onResume();
        updateTrackSegments();
    }

    @Override
    protected void onPause() {
        super.onPause();
    }

    @Override
    protected void onStop() {
        super.onStop();
    }

    public void updateTrackSegments() {
        try (TrackPointIterator trackPointIterator = contentProviderUtils.getTrackPointLocationIterator(trackId, null)) {
            TrackSegment currentSegment = null;
            while (trackPointIterator.hasNext()) {
                TrackPoint nextPoint = trackPointIterator.next();

                switch (nextPoint.getType()) {
                    case SEGMENT_START_AUTOMATIC:
                    case SEGMENT_START_MANUAL:
                        if (currentSegment != null) {
                            trackSegments.add(currentSegment);
                        }
                        currentSegment = new TrackSegment(nextPoint.getTime());
                        break;

                    case SEGMENT_END_MANUAL:
                        trackSegments.add(currentSegment);
                        currentSegment = null;

                    case TRACKPOINT:
                        if (currentSegment != null) {
                            currentSegment.addTrackPoint(nextPoint);
                        }
                        break;

                    default:
                        Log.d(TAG, "No Action for TrackPoint IDLE/SENSORPOINT while recording segments");
                }
            }
            System.out.println("Segments count: " + trackSegments.size());
        }
    }

    private Date getDummyDate() {
        String dateString = "2024-03-02";
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
        LocalDate localDate = LocalDate.parse(dateString, formatter);
        Date date = Date.from(localDate.atStartOfDay(ZoneId.systemDefault()).toInstant());
        return date;
    }

    private void handleIntent(Intent intent) {
        trackId = intent.getParcelableExtra(EXTRA_TRACK_ID);
        if (trackId == null) {
            Log.e(TAG, DaySpecificActivity.class.getSimpleName() + " needs EXTRA_TRACK_ID.");
            // None provided, we will assume a specific date on our own
            activityDate = getDummyDate();
            Track track = contentProviderUtils.getTrack(activityDate);
            trackId = track.getId();
        }
    }

    @Override
    protected View getRootView() {
        viewBinding = ActivityDaySpecificBinding.inflate(getLayoutInflater());
        return viewBinding.getRoot();
    }

    @Override
    protected void onDeleteConfirmed() {

    }

    @Override
    protected Track.Id getRecordingTrackId() {
        return trackId;
    }

    @Override
    public void onDeleteFinished() {

    }
}