package com.rydohg.assignmenttracker;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.AdapterView;
import android.widget.CalendarView;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;

public class AddAssignmentActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_assignment);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setTitle("");
        initOptionsList();
        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(
                new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        ListView listView = (ListView) findViewById(R.id.options_list_view);

                        String title = ((EditText) findViewById(R.id.title_edit_text)).getText().toString();
                        String desc = ((EditText) findViewById(R.id.desc_edit_text)).getText().toString();
                        String importance = ((TextView) listView.getChildAt(1).findViewById(R.id.selectedOption)).getText().toString();
                        SimpleDateFormat formatter = new SimpleDateFormat("MM/dd/yyyy");
                        String dateString = ((TextView) listView.getChildAt(0).findViewById(R.id.selectedOption)).getText().toString();

                        Assignment assignment = null;
                        try {
                            if (dateString.contains("Repeats Weekly")) {
                                dateString = dateString.substring(0, 10);
                                assignment = new Assignment(title, importance, formatter.parse(dateString).getTime(), desc, true);
                            }
                            else {
                                assignment = new Assignment(title, importance, formatter.parse(dateString).getTime(), desc, false);
                            }
                        }
                        catch (ParseException e) {
                            e.printStackTrace();
                        }

                        DBHelper.insertAssignment(AddAssignmentActivity.this.getApplicationContext(), assignment);

                        AlarmManager alarmManager = (AlarmManager) getApplicationContext().getSystemService(Context.ALARM_SERVICE);
                        Intent intent = new Intent(getApplicationContext(), NotificationAlarmReceiver.class);
                        PendingIntent alarmIntent = PendingIntent.getBroadcast(getApplicationContext(), 0, intent, 0);

                        // Set the alarm to start at 4pm
                        Calendar calendar = Calendar.getInstance();
                        calendar.setTimeInMillis(System.currentTimeMillis());
                        calendar.set(Calendar.HOUR_OF_DAY, 16);

                        alarmManager.setRepeating(AlarmManager.RTC_WAKEUP, calendar.getTimeInMillis(),
                                1000 * 60 * 60 * 24, alarmIntent);

                        Intent i = new Intent(AddAssignmentActivity.this.getApplicationContext(), MainActivity.class);
                        AddAssignmentActivity.this.startActivity(i);
                    }
                }
        );
    }

    void initOptionsList(){
        ListView listView = (ListView) findViewById(R.id.options_list_view);
        listView.setOnItemClickListener(
            (AdapterView<?> parent, View v, int pos, long id) -> {
                String optionName = ((TextView) v.findViewById(R.id.option_name)).getText().toString();
                ArrayList<String> options = new ArrayList<>();
                switch (optionName){
                    case "Importance":
                        options.add("Not so important");
                        options.add("Important");
                        options.add("Very important");
                        break;
                }
                if (!optionName.equals("Due Date")){
                    OptionsDialog.newInstance(optionName, options, pos).show(getFragmentManager(), "OptionsDialogFragment");
                } else {
                    LayoutInflater inflater = (LayoutInflater)getApplicationContext().getSystemService
                            (Context.LAYOUT_INFLATER_SERVICE);
                    LinearLayout layout = (LinearLayout)inflater.inflate(R.layout.calender_dialog_layout, null, false);
                    CheckBox checkBox = (CheckBox) layout.getChildAt(0);
                    CalendarView cv = (CalendarView) layout.getChildAt(1);
                    TextView textView = (TextView) listView.getChildAt(0).findViewById(R.id.selectedOption);
                    cv.setOnDateChangeListener(new CalendarView.OnDateChangeListener() {
                        @Override
                        public void onSelectedDayChange(CalendarView view, int year, int month, int dayOfMonth) {
                            textView.setText(String.valueOf((month + 1) + "/" + dayOfMonth + "/" + year));
                        }
                    });
                    new AlertDialog.Builder(AddAssignmentActivity.this)
                            .setTitle("Due Date")
                            .setView(layout)
                            .setPositiveButton("Ok", (dialog, whichButton) -> {
                                if (checkBox.isChecked()){
                                    String text = textView.getText().toString() + " Repeats Weekly";
                                    textView.setText(text);
                                }
                            }).setNegativeButton("Cancel", (dialog, whichButton) -> dialog.dismiss()
                    ).show();
                }
                Log.w("OnClickLambda", optionName);
            }
        );
        ArrayList<OptionListItem> optionListItems = new ArrayList<>();
        optionListItems.add(new OptionListItem("Due Date", R.drawable.ic_clock_grey600_48dp, R.id.label));
        optionListItems.add(new OptionListItem("Importance", R.drawable.ic_exclamation_grey600_48dp, R.id.label));
        listView.setAdapter(new OptionsAdapter(this, R.layout.list_item_option, optionListItems));
    }

}
