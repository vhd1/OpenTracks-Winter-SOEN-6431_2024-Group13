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
import de.dennisguse.opentracks.data.models.Distance;
import de.dennisguse.opentracks.data.models.Track;
import de.dennisguse.opentracks.data.models.TrackPoint;
import de.dennisguse.opentracks.data.models.TrackSegment;
import de.dennisguse.opentracks.databinding.DaySpecificActivityBinding;

import java.time.format.DateTimeFormatter;
import java.time.LocalDate;
import java.util.Date;
import java.time.ZoneId;
import java.util.List;
import java.util.ArrayList;
import androidx.recyclerview.widget.LinearLayoutManager;

public class DaySpecificActivity extends AbstractTrackDeleteActivity {

    private DaySpecificActivityBinding viewBinding;
    private static final String TAG = DaySpecificActivity.class.getSimpleName();
    public static final String EXTRA_TRACK_ID = "track_id";
    private Date activityDate;
    private ContentProviderUtils contentProviderUtils;
    private TrackDataHub trackDataHub;
    private Track.Id trackId;
    private List<TrackSegment> trackSegments;
    private DaySpecificAdapter dataAdapter;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        trackSegments = new ArrayList<>();
        contentProviderUtils = new ContentProviderUtils(this);
        handleIntent(getIntent());
        updateTrackSegments();
        trackDataHub = new TrackDataHub(this);
        setSupportActionBar(viewBinding.bottomAppBarLayout.bottomAppBar);

        viewBinding = DaySpecificActivityBinding.inflate(getLayoutInflater());
        dataAdapter = new DaySpecificAdapter(this, viewBinding.segmentList);
        dataAdapter.swapData(trackSegments);
        viewBinding.segmentList.setAdapter(dataAdapter);
    }

    @Override
    protected void onStart() {
        super.onStart();
    }

    @Override
    protected void onResume() {
        super.onResume();
        updateTrackSegments();
        dataAdapter.swapData(trackSegments);
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

            for(int i=0; i<trackSegments.size(); i++){
                Log.d("RUSHI", "TrackPoint No.: " + i);
                Log.d("RUSHI", "Distance for segment " + trackSegments.get(i).getDistanceBetweenFirstAndLast().toM());
                Log.d("RUSHI", "Time for segment " + trackSegments.get(i).getTotalTime() + "Minutes");
                Log.d("RUSHI", "Speed for segment " + trackSegments.get(i).getSpeed(trackSegments.get(i).getDistanceBetweenFirstAndLast(), trackSegments.get(i).getTotalTime()) + "M/Sec");
            }
        }
    }
    private Date getDummyDate() {
        String dateString = "2024-03-02";
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
        LocalDate localDate = LocalDate.parse(dateString, formatter);
        return Date.from(localDate.atStartOfDay(ZoneId.systemDefault()).toInstant());
    }

    private void handleIntent(Intent intent) {
        trackId = intent.getParcelableExtra(EXTRA_TRACK_ID);
        if (trackId == null) {
            Log.e(TAG, DaySpecificActivity.class.getSimpleName() + " needs EXTRA_TRACK_ID.");

            // None provided, we will assume a specific date on our own
            activityDate = getDummyDate();
            List<Track> tracks = contentProviderUtils.getTracks();
            for (Track track: tracks) {
                System.out.println("Track date = " + track.getStartTime().toString());
            }
            Track track = contentProviderUtils.getTrack(activityDate);
            if (track == null) {
                finish();
            } else {
                trackId = track.getId();
            }
        }
    }

    @Override
    protected View getRootView() {
        viewBinding = DaySpecificActivityBinding.inflate(getLayoutInflater());
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