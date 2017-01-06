package com.rydohg.assignmenttracker;

import java.text.SimpleDateFormat;

public class Assignment {
    private String name;
    private String importance;
    private long dueDate;
    private String extraInfo;
    private boolean repeats;

    public Assignment(String name, String importance, long dueDate, String extraInfo, boolean repeats){
        this.name = name;
        this.importance = importance;
        this.dueDate = dueDate;
        this.extraInfo = extraInfo;
        this.repeats = repeats;
    }

    public String getImportance() {
        return importance;
    }

    public long getDueDate() {
        return dueDate;
    }

    public String getFormattedDueDate(){
        SimpleDateFormat formatter = new SimpleDateFormat("MM/dd/yyyy");
        return formatter.format(dueDate);
    }

    public String getName() {
        return name;
    }

    public String getExtraInfo() {
        return extraInfo;
    }

    public boolean isRepeats() {
        return repeats;
    }

    public void setRepeats(boolean repeats) {
        this.repeats = repeats;
    }
}
