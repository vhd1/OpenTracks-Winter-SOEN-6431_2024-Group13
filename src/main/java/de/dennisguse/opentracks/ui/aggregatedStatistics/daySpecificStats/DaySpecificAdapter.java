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

import de.dennisguse.opentracks.R;
import de.dennisguse.opentracks.data.models.Track;
import de.dennisguse.opentracks.databinding.DaySpecificActivityItemBinding;
import de.dennisguse.opentracks.ui.TrackListAdapter;
import de.dennisguse.opentracks.ui.util.ActivityUtils;

public class DaySpecificAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> implements ActionMode.Callback {

    private final AppCompatActivity context;
    private final RecyclerView recyclerView;

    private final SparseBooleanArray selection = new SparseBooleanArray();

    private Cursor cursor;

    private boolean selectionMode = false;
    private ActivityUtils.ContextualActionModeCallback actionModeCallback;

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
        TrackListAdapter.ViewHolder viewHolder = (TrackListAdapter.ViewHolder) holder;

        cursor.moveToPosition(position);
        viewHolder.bind(cursor);
    }

    @Override
    public int getItemCount() {
        return 0;
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

        public void bind(Cursor cursor){
            viewBinding.daySpecificActivity.setText("Run");
            viewBinding.daySpecificActivityDisplacement.setText("0 m");
            viewBinding.daySpecificActivityDistance.setText("0.14 km");
            viewBinding.daySpecificActivitySpeed.setText("36.3 km/h");
            viewBinding.daySpecificActivityTime.setText("0.50");
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
