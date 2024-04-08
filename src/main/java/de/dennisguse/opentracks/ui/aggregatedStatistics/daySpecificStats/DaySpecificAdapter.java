package de.dennisguse.opentracks.ui.aggregatedStatistics.daySpecificStats;

import android.database.Cursor;
import android.util.SparseBooleanArray;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.view.ActionMode;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

import de.dennisguse.opentracks.R;
import de.dennisguse.opentracks.data.models.Distance;
import de.dennisguse.opentracks.data.models.Track;
import de.dennisguse.opentracks.data.models.TrackSegment;
import de.dennisguse.opentracks.databinding.DaySpecificActivityItemBinding;
import de.dennisguse.opentracks.ui.util.ActivityUtils;

public class DaySpecificAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> implements ActionMode.Callback {

    private static final String TAG = DaySpecificAdapter.class.getSimpleName();
    DaySpecificActivityItemBinding viewBinding;
    private final AppCompatActivity context;
    private final RecyclerView recyclerView;

    private final SparseBooleanArray selection = new SparseBooleanArray();

    private Cursor cursor;

    private ActivityUtils.ContextualActionModeCallback actionModeCallback;
    private List<TrackSegment> trackSegments;

    public DaySpecificAdapter(AppCompatActivity context, RecyclerView recyclerView) {
        this.context = context;
        this.recyclerView = recyclerView;
    }

    @Override
    public boolean onCreateActionMode(ActionMode mode, Menu menu) {
        return false;
    }

    @Override
    public boolean onPrepareActionMode(ActionMode mode, Menu menu) {
        return false;
    }

    @Override
    public boolean onActionItemClicked(ActionMode mode, MenuItem item) {
        return false;
    }

    @Override
    public void onDestroyActionMode(ActionMode mode) {

    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.day_specific_activity_item, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {
        DaySpecificAdapter.ViewHolder viewHolder = (DaySpecificAdapter.ViewHolder) holder;
        TrackSegment segment = trackSegments.get(position);
        viewHolder.bind(segment);
    }

    public void swapData(List<TrackSegment> segments) {
        this.trackSegments = segments;
        this.notifyDataSetChanged();
    }
    @Override
    public int getItemCount() {
        return trackSegments.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener, View.OnLongClickListener {

        private final DaySpecificActivityItemBinding viewBinding;
        private final View view;

        private Track.Id trackId;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);

            viewBinding = DaySpecificActivityItemBinding.bind(itemView);
            view = itemView;

            view.setOnClickListener(this);
            view.setOnLongClickListener(this);
        }

        public void bind(TrackSegment segment) {
            Distance distance = segment.getDistanceBetweenFirstAndLast();
            Long time = segment.getTotalTime();
            double speed = segment.getSpeed(distance, time);
            viewBinding.daySpecificActivity.setText("Run");
            viewBinding.daySpecificActivityDisplacement.setText("0 m");
            viewBinding.daySpecificActivityDistance.setText("0.14 km");
            viewBinding.daySpecificActivitySpeed.setText(speed + " km/h");
            viewBinding.daySpecificActivityTime.setText("" + segment.getTotalTime());
        }

        public void setSelected(boolean isSelected) {
            selection.put((int) getId(), isSelected);
            view.setActivated(isSelected);
        }

        public long getId() {
            return trackId.id();
        }

        @Override
        public void onClick(View view) {

        }

        @Override
        public boolean onLongClick(View view) {
            return false;
        }
    }
}
