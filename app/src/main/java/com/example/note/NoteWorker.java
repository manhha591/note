package com.example.note;

import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.util.Log;

import androidx.annotation.NonNull;
import androidx.core.app.NotificationCompat;
import androidx.work.ExistingPeriodicWorkPolicy;
import androidx.work.PeriodicWorkRequest;
import androidx.work.WorkManager;
import androidx.work.Worker;
import androidx.work.WorkerParameters;

import com.example.note.Activity.MyApplication;
import com.example.note.database.NoteDatabase;
import com.example.note.database.dao.NoteDao;
import com.example.note.model.Note;

import java.util.concurrent.TimeUnit;

import io.reactivex.rxjava3.schedulers.Schedulers;


public class NoteWorker extends Worker {
    public NoteWorker(@NonNull Context context, @NonNull WorkerParameters workerParams) {
        super(context, workerParams);
        createNotificationChannel();
    }

    @NonNull
    @Override
    public Result doWork() {
        NoteDatabase database = NoteDatabase.getInstance(getApplicationContext());
        NoteDao noteDao = database.getNoteDao();

        noteDao.getAllNote()
                .subscribeOn(Schedulers.io())
                .subscribe(notes -> {
                    long currentTime = System.currentTimeMillis();
                    for (Note note : notes) {
                        if (note.getStartDate() != null && note.getStartDate() < currentTime && note.getState() == 0) {
                            note.setState(2); // Quá hạn
                            noteDao.updateNote(note).subscribeOn(Schedulers.io()).subscribe();
                        } else if (note.getStartDate() != null && note.getStartDate() <= currentTime + 60000 && note.getState() == 0) {
                            sendNotification(note);
                            System.out.println("hello");
                        }
                    }
                });

        return Result.success();
    }

    private void sendNotification(Note note) {
        NotificationManager notificationManager = (NotificationManager) getApplicationContext().getSystemService(Context.NOTIFICATION_SERVICE);
        String channelId = "note_channel";

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            NotificationChannel channel = new NotificationChannel(channelId, "Note Notifications", NotificationManager.IMPORTANCE_HIGH);
            notificationManager.createNotificationChannel(channel);
        }

        // Intent for the action button
        Intent actionIntent = new Intent(getApplicationContext(), MainActivity.class);
        actionIntent.putExtra("note_id", note.getId());
        PendingIntent actionPendingIntent = PendingIntent.getActivity(getApplicationContext(), 0, actionIntent, PendingIntent.FLAG_UPDATE_CURRENT | PendingIntent.FLAG_IMMUTABLE);

        NotificationCompat.Builder builder = new NotificationCompat.Builder(getApplicationContext(), channelId)
                .setSmallIcon(R.drawable.ic_notification)
                .setContentTitle("Note Reminder")
                .setContentText("Note \"" + note.getTitle() + "\" is due soon!")
                .setPriority(NotificationCompat.PRIORITY_HIGH)
                .setAutoCancel(true)
                .addAction(R.drawable.outline_check_circle_outline_24, "Open", actionPendingIntent); // Add action button

        notificationManager.notify(note.getId(), builder.build());
    }


    private void createNotificationChannel() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            String channelId = "note_channel";
            String channelName = "Note Notifications";
            NotificationChannel channel = new NotificationChannel(channelId, channelName, NotificationManager.IMPORTANCE_HIGH);
            channel.setDescription("Channel for Note notifications");

            NotificationManager notificationManager = (NotificationManager) getApplicationContext().getSystemService(Context.NOTIFICATION_SERVICE);
            notificationManager.createNotificationChannel(channel);
        }
    }
    public static void scheduleWork(Context context) {
        PeriodicWorkRequest workRequest = new PeriodicWorkRequest.Builder(NoteWorker.class, 1, TimeUnit.HOURS)
                .build();
        WorkManager.getInstance(context).enqueueUniquePeriodicWork("NoteWorker", ExistingPeriodicWorkPolicy.REPLACE, workRequest);
    }
}
