package de.dennisguse.opentracks.ui.aggregatedStatistics.daySpecificStats;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;

import de.dennisguse.opentracks.AbstractTrackDeleteActivity;
import de.dennisguse.opentracks.R;
import de.dennisguse.opentracks.data.ContentProviderUtils;
import de.dennisguse.opentracks.data.TrackDataHub;
import de.dennisguse.opentracks.data.models.Track;
import de.dennisguse.opentracks.databinding.ActivityDaySpecificBinding;

import java.time.format.DateTimeFormatter;
import java.time.LocalDate;
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

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_day_specific);
        contentProviderUtils = new ContentProviderUtils(this);
        handleIntent(getIntent());
        trackDataHub = new TrackDataHub(this);
        setSupportActionBar(viewBinding.bottomAppBarLayout.bottomAppBar);
    }

    @Override
    protected void onStart() {
        super.onStart();
        trackDataHub.start();
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
//            finish();
            // None provided, we will assume a specific date on our own
            activityDate = getDummyDate();
            List<Track> tracks = contentProviderUtils.getTracks();
            for (Track track: tracks) {
                System.out.println("Track date = " + track.getStartTime().toString());
            }
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
        return null;
    }

    @Override
    public void onDeleteFinished() {

    }
}