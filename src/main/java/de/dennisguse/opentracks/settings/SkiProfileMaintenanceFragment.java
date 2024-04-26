package de.dennisguse.opentracks.settings;

import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.content.ContentResolver;
import android.content.Context;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;

import androidx.annotation.NonNull;
import androidx.core.app.NotificationCompat;
import androidx.preference.*;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.Instant;
import java.util.Date;

import de.dennisguse.opentracks.BuildConfig;
import de.dennisguse.opentracks.R;
import de.dennisguse.opentracks.data.tables.TracksColumns;

public class SkiProfileMaintenanceFragment extends PreferenceFragmentCompat {

    private Preference sharpeningKmSkiedPreference;
    private Preference sharpeningIntervalPreference;
    private Preference lastSharpeningDatePreference;
    private Preference waxingKmSkiedPreference;
    private Preference waxingIntervalPreference;
    private Preference lastWaxingDatePreference;
    private static final int SHARPENING_THRESHOLD = 100; // Notify when reaching 80% of interval
    private static final int MONTHS_THRESHOLD = 10; // Notify after 10 months since sharpening
    private static final String CHANNEL_ID = "sharpening_channel"; // Replace with your desired channel ID
    private static final int NOTIFICATION_ID = 1; // You can choose a different ID if needed
    private static final String W_CHANNEL_ID = "waxing_channel"; // Replace with your desired channel ID
    private static final int W_NOTIFICATION_ID = 2; // You can choose a different ID if needed



    @Override
    public void onCreatePreferences(Bundle savedInstanceState, String rootKey) {
        addPreferencesFromResource(R.xml.fragment_ski_profile_ski_maintenance);

        sharpeningKmSkiedPreference = findPreference("km_skied_since_sharpening");
        sharpeningIntervalPreference = findPreference("sharpening_interval_percent");
        lastSharpeningDatePreference = findPreference("last_sharpening_date");

        waxingKmSkiedPreference = findPreference("km_skied_since_waxing");
        waxingIntervalPreference = findPreference("waxing_interval_percent");
        lastWaxingDatePreference = findPreference("last_waxing_date");

        updateSharpeningInfo();
        updateWaxingInfo();
    }

    private void updateSharpeningInfo() {
        SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(requireContext());

        // Access sharpening data from preferences
        double sharpeningInterval = Double.parseDouble(sharedPreferences.getString("sharpening_interval", "30"));
        long lastSharpeningTime = getSharpeningTimeFromSharedPrefs(sharedPreferences, "last_sharpening_date");
        double totalDistanceSum = new DatabaseHelper(getContext()).getTotalDistanceSum(lastSharpeningTime);

        // Calculate km skied, % of interval reached, months since last sharpening
        double kmSkied = totalDistanceSum;
        int monthsSinceSharpening = calculateMonthsSince(lastSharpeningTime);
        double intervalPercent = (double) kmSkied / sharpeningInterval * 100;

        // Set summary for preferences
        sharpeningKmSkiedPreference.setSummary(getString(R.string.km_skied_format, kmSkied));
        sharpeningIntervalPreference.setSummary(String.format("%.1f%%", intervalPercent));
        lastSharpeningDatePreference.setSummary(formatSharpeningDate(lastSharpeningTime));

        // Schedule notifications if needed
        if (intervalPercent >= SHARPENING_THRESHOLD || monthsSinceSharpening >= MONTHS_THRESHOLD) {
            showSharpeningNotification(intervalPercent, monthsSinceSharpening);
        }
    }

    private void showSharpeningNotification(double intervalPercent, int monthsSinceSharpening) {

        String notificationTitle;
        String notificationText;
        if (intervalPercent >= SHARPENING_THRESHOLD) {
            notificationTitle = getString(R.string.sharpening_notification_title_interval);
            notificationText = String.format(getString(R.string.sharpening_notification_text_interval), intervalPercent);
        } else {
            notificationTitle = getString(R.string.sharpening_notification_title_months);
            notificationText = String.format(getString(R.string.sharpening_notification_text_months), monthsSinceSharpening);
        }

        // Get NotificationManager instance
        NotificationManager notificationManager = (NotificationManager) getContext().getSystemService(Context.NOTIFICATION_SERVICE);

        // Create a NotificationCompat.Builder object
        NotificationCompat.Builder notificationBuilder = new NotificationCompat.Builder(getContext(), CHANNEL_ID)
                .setSmallIcon(R.drawable.ic_launcher) // Replace with your notification icon resource
                .setContentTitle(notificationTitle)
                .setContentText(notificationText)
                .setStyle(new NotificationCompat.BigTextStyle().bigText(notificationText))
                .setPriority(NotificationCompat.PRIORITY_DEFAULT);

        // Create a NotificationChannel object (required for Android O and above)
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            NotificationChannel notificationChannel = new NotificationChannel(CHANNEL_ID, "Sharpening Reminder", NotificationManager.IMPORTANCE_DEFAULT);
            notificationManager.createNotificationChannel(notificationChannel);
        }

        // Build and display the notification
        notificationManager.notify(NOTIFICATION_ID, notificationBuilder.build());
    }

    private void updateWaxingInfo() {
        SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(requireContext());

        // Access sharpening data from preferences
        double waxingInterval = Double.parseDouble(sharedPreferences.getString("waxing_interval", "30"));
        long lastWaxingTime = getSharpeningTimeFromSharedPrefs(sharedPreferences, "last_waxing_date");
        double totalDistanceSum = new DatabaseHelper(getContext()).getTotalDistanceSum(lastWaxingTime);

        // Calculate km skied, % of interval reached, months since last sharpening
        double kmSkied = totalDistanceSum;
        int monthsSinceSharpening = calculateMonthsSince(lastWaxingTime);
        double intervalPercent = (double) kmSkied / waxingInterval * 100;

        // Set summary for preferences
        waxingKmSkiedPreference.setSummary(getString(R.string.km_skied_format, kmSkied));
        waxingIntervalPreference.setSummary(String.format("%.1f%%", intervalPercent));
        lastWaxingDatePreference.setSummary(formatSharpeningDate(lastWaxingTime));

        // Schedule notifications if needed
        if (intervalPercent >= SHARPENING_THRESHOLD || monthsSinceSharpening >= MONTHS_THRESHOLD) {
            showWaxingNotification(intervalPercent, monthsSinceSharpening);
        }
    }

    private void showWaxingNotification(double intervalPercent, int monthsSinceSharpening) {

        String notificationTitle;
        String notificationText;
        if (intervalPercent >= SHARPENING_THRESHOLD) {
            notificationTitle = getString(R.string.waxing_notification_title_interval);
            notificationText = String.format(getString(R.string.waxing_notification_text_interval), intervalPercent);
        } else {
            notificationTitle = getString(R.string.waxing_notification_title_months);
            notificationText = String.format(getString(R.string.waxing_notification_text_months), monthsSinceSharpening);
        }

        // Get NotificationManager instance
        NotificationManager notificationManager = (NotificationManager) getContext().getSystemService(Context.NOTIFICATION_SERVICE);

        // Create a NotificationCompat.Builder object
        NotificationCompat.Builder notificationBuilder = new NotificationCompat.Builder(getContext(), W_CHANNEL_ID)
                .setSmallIcon(R.drawable.ic_launcher) // Replace with your notification icon resource
                .setContentTitle(notificationTitle)
                .setContentText(notificationText)
                .setStyle(new NotificationCompat.BigTextStyle().bigText(notificationText))
                .setPriority(NotificationCompat.PRIORITY_DEFAULT);

        // Create a NotificationChannel object (required for Android O and above)
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            NotificationChannel notificationChannel = new NotificationChannel(W_CHANNEL_ID, "Sharpening Reminder", NotificationManager.IMPORTANCE_DEFAULT);
            notificationManager.createNotificationChannel(notificationChannel);
        }

        // Build and display the notification
        notificationManager.notify(W_NOTIFICATION_ID, notificationBuilder.build());
    }

    private long getSharpeningTimeFromSharedPrefs(SharedPreferences sharedPreferences, String last_date) {
        String lastSharpeningDate = sharedPreferences.getString(last_date, "");
        // Implement logic to convert the date string to a timestamp (milliseconds since epoch)
        // You can use SimpleDateFormat or a custom parsing method based on your date format
        SimpleDateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyy");
        try {
            Date date = dateFormat.parse(lastSharpeningDate);
            return date.getTime();
        } catch (ParseException e) {
            e.printStackTrace();
            return 0; // Handle parsing exception (you might want to use a default value or log an error)
        }
    }

    private int calculateMonthsSince(long timestamp) {
        long currentTime = System.currentTimeMillis();
        long diffInMs = currentTime - timestamp;
        return (int) (diffInMs / (1000L * 60 * 60 * 24 * 30));
    }

    private String formatSharpeningDate(long timestamp) {
        // Use SimpleDateFormat or a custom formatting method to format the date based on your preference
        SimpleDateFormat dateFormat = new SimpleDateFormat("dd MMM yyyy");
        return dateFormat.format(new Date(timestamp));
    }
}

class DatabaseHelper extends SQLiteOpenHelper {

    private static final String DATABASE_NAME = "database.db";
    private static final String TABLE_NAME = "tracks";
    private static final String COLUMN_TOTAL_DISTANCE = "totaldistance";
    private static final String COLUMN_DATE = "starttime";

    public DatabaseHelper(Context context) {
        super(context, DATABASE_NAME, null, 1);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
    }

    @Override
    public void onDowngrade(SQLiteDatabase db, int oldVersion, int newVersion) {
    }

    public double getTotalDistanceSum(long sharpeningLastDateMillis) {
        SQLiteDatabase db = this.getReadableDatabase();
        double sum = 0;

        // Construct the SQL query with a WHERE clause to filter rows based on the date
        String query = "SELECT " + COLUMN_TOTAL_DISTANCE + " FROM " + TABLE_NAME +
                " WHERE " + COLUMN_DATE + " > ?";

        Cursor cursor = db.rawQuery(query, new String[] { String.valueOf(sharpeningLastDateMillis) });
        if (cursor != null) {
            try {
                int totalDistanceIndex = cursor.getColumnIndexOrThrow(COLUMN_TOTAL_DISTANCE);
                if (cursor.moveToFirst()) {
                    do {
                        sum += cursor.getDouble(totalDistanceIndex);
                    } while (cursor.moveToNext());
                }
            } catch (IllegalArgumentException e) {
                // Handle the case where the column doesn't exist
                e.printStackTrace();
            } finally {
                cursor.close();
            }
        }
        db.close();
        return sum;
    }

}