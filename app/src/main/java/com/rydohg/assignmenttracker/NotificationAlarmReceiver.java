package com.rydohg.assignmenttracker;

import android.app.Notification;
import android.app.NotificationManager;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.util.Log;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.GregorianCalendar;

/***
 * This is called everyday at 4pm (when I get home)
 * onReceive() checks to see what is considered a current assignment and adds it to the notification
 */

public class NotificationAlarmReceiver extends BroadcastReceiver {
    ArrayList<Assignment> assignments = new ArrayList<>();
    @Override
    public void onReceive(Context context, Intent intent) {
        assignments = DBHelper.getAssignments(context);
        Log.w("Alarm", "onReceive()");
        for (Assignment assignment: getCurrentAssignments(context)){
            Notification notification = new Notification.Builder(context)
                .setContentTitle("Today's Assignments")
                .setSmallIcon(R.drawable.ic_clock_grey600_48dp)
                .setContentText(assignment.getName())
                .build();
            NotificationManager manager = (NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);
            manager.notify((int) (Math.random() * 1000), notification);
        }
    }

    private ArrayList<Assignment> getCurrentAssignments(Context context){
        final long millisInDay = 1000 * 60 * 60 * 24;
        ArrayList<Assignment> allAssignments = DBHelper.getAssignments(context);
        ArrayList<Assignment> currentAssignments = new ArrayList<>();
        long currentTime = System.currentTimeMillis();

        // Checks the next week for what I consider current assignments
        long dueDate;
        String importance;
        for (Assignment a : assignments){
            dueDate = a.getDueDate();
            importance = a.getImportance();
            Calendar date = Calendar.getInstance();

            date.set(Calendar.HOUR, 0);
            date.set(Calendar.MINUTE, 0);
            date.set(Calendar.SECOND, 0);
            date.set(Calendar.MILLISECOND, 0);

            long dateNow = date.getTimeInMillis();
            if (dueDate >= dateNow) {
                // Add assignment if due in the next two days no matter what

                if (dueDate < (currentTime + millisInDay * 2)) {
                    currentAssignments.add(a);
                }
                // Add assignment if it's important and due in next 4 days
                else if (dueDate < (currentTime + millisInDay * 4) && importance.equals("Important")) {
                    currentAssignments.add(a);
                }
                // Add assignment if it's very important and due in next 7 days
                else if (dueDate < (currentTime + millisInDay * 7) && importance.equals("Very Important")) {
                    currentAssignments.add(a);
                }
            }
        }
        return currentAssignments;
    }
}
