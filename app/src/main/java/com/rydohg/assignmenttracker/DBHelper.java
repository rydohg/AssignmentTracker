package com.rydohg.assignmenttracker;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.DatabaseUtils;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import java.util.ArrayList;

/**
 * Class used to interact with the database
 */

public class DBHelper extends SQLiteOpenHelper {

    private static final int DATABASE_VERSION = 0;
    private static final String TAG = "DBHelper";
    private static final String DATABASE_NAME = "assignmentTracker.db";
    private static final String ASSIGNMENT_TABLE_NAME = "assignment";
    private static final String ASSIGNMENT_COLUMN_ID = "id";
    private static final String ASSIGNMENT_COLUMN_NAME = "name";
    private static final String ASSIGNMENT_COLUMN_IMPORTANCE = "importance";
    private static final String ASSIGNMENT_COLUMN_DUE_DATE = "due_date";
    private static final String ASSIGNMENT_COLUMN_EXTRA_INFO = "extra_info";

    private static final String ASSIGNMENT_TABLE_CREATE =
            "CREATE TABLE " + ASSIGNMENT_TABLE_NAME + " ( " +
                    ASSIGNMENT_COLUMN_ID + " INTEGER PRIMARY KEY, " +
                    ASSIGNMENT_COLUMN_NAME + " TEXT, " +
                    ASSIGNMENT_COLUMN_IMPORTANCE + " TEXT, " +
                    ASSIGNMENT_COLUMN_DUE_DATE+ " TEXT, " +
                    ASSIGNMENT_COLUMN_EXTRA_INFO  + " TEXT );";

    public DBHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL(ASSIGNMENT_TABLE_CREATE);
        Log.d(TAG, "Database created");
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE " + ASSIGNMENT_TABLE_NAME);
        Log.d(TAG, "Database dropped");
        onCreate(db);
    }

    static boolean insertAssignment(Context context, String name, long dueDate, String importance, String extraInfo){
        SQLiteDatabase db = new DBHelper(context).getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put(ASSIGNMENT_COLUMN_NAME, name);
        contentValues.put(ASSIGNMENT_COLUMN_DUE_DATE, dueDate);
        contentValues.put(ASSIGNMENT_COLUMN_IMPORTANCE, importance);
        contentValues.put(ASSIGNMENT_COLUMN_EXTRA_INFO, extraInfo);

        db.insert(ASSIGNMENT_TABLE_NAME, null, contentValues);
        Log.d(TAG, "Assignment \"" + name + "\" was inserted");
        return true;
    }

    static boolean insertAssignment(Context context, Assignment assignment){
        SQLiteDatabase db = new DBHelper(context).getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put(ASSIGNMENT_COLUMN_NAME, assignment.getName());
        contentValues.put(ASSIGNMENT_COLUMN_DUE_DATE, assignment.getDueDate());
        contentValues.put(ASSIGNMENT_COLUMN_IMPORTANCE, assignment.getImportance());
        contentValues.put(ASSIGNMENT_COLUMN_EXTRA_INFO, assignment.getExtraInfo());

        db.insert(ASSIGNMENT_TABLE_NAME, null, contentValues);
        Log.d(TAG, "Assignment \"" + assignment.getName() + "\" was inserted");
        return true;
    }

    public Cursor getData(int id){
        SQLiteDatabase db = this.getReadableDatabase();
        return db.rawQuery( "SELECT * FROM " + ASSIGNMENT_TABLE_NAME + " WHERE id=" + id, null );
    }

    public int numberOfRows(){
        SQLiteDatabase db = this.getReadableDatabase();
        return (int) DatabaseUtils.queryNumEntries(db, ASSIGNMENT_TABLE_NAME);
    }

    public static ArrayList<Assignment> getAssignments(Context context) {
        ArrayList<Assignment> assignments = new ArrayList<>();
        SQLiteDatabase db = new DBHelper(context).getReadableDatabase();
        Cursor res =  db.rawQuery("SELECT * FROM " + ASSIGNMENT_TABLE_NAME, null );
        res.moveToFirst();

        while(!res.isAfterLast()){
            String name = res.getString(res.getColumnIndex(ASSIGNMENT_COLUMN_NAME));
            long dueDate = res.getLong(res.getColumnIndex(ASSIGNMENT_COLUMN_DUE_DATE));
            String importance = res.getString(res.getColumnIndex(ASSIGNMENT_COLUMN_IMPORTANCE));
            String extraInfo = res.getString(res.getColumnIndex(ASSIGNMENT_COLUMN_EXTRA_INFO));

            Assignment assignment = new Assignment(name, importance, dueDate, extraInfo, false);
            assignments.add(assignment);
            res.moveToNext();
        }
        res.close();
        return assignments;
    }
}
