package com.developer.grebnev.to_do_list.database;

import android.content.ContentValues;
import android.content.Context;
import android.database.DatabaseErrorHandler;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.provider.BaseColumns;

import com.developer.grebnev.to_do_list.model.ModelTask;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by Grebnev on 01.12.2015.
 */
public class DBHelper extends SQLiteOpenHelper {

    private DatabaseReference myRef;
    private FirebaseUser user;
    private FirebaseAuth mAuth;

    public static final int DATABASE_VERSION = 1;

    public static final String DATABASE_NAME = "to_do_list_database";

    public static final String TASKS_TABLE = "tasks_table";

    public static final String TASKS_TITLE_COLUMN = "task_title";
    public static final String TASKS_DATE_COLUMN = "task_date";
    public static final String TASKS_PRIORITY_COLUMN = "task_priority";
    public static final String TASKS_STATUS_COLUMN = "task_status";
    public static final String TASKS_TIME_STAMP_COLUMN = "task_time_stamp";

    private static final String TASK_TABLE_CREATE_SCRIPT = "CREATE TABLE "
            + TASKS_TABLE + " (" + BaseColumns._ID
            + " INTEGER PRIMARY KEY AUTOINCREMENT, " + TASKS_TITLE_COLUMN + " TEXT NOT NULL, "
            + TASKS_DATE_COLUMN + " LONG, " + TASKS_PRIORITY_COLUMN + " INTEGER, "
            + TASKS_STATUS_COLUMN + " INTEGER, " + TASKS_TIME_STAMP_COLUMN + " LONG);";

    public static final String SELECTION_STATUS = DBHelper.TASKS_STATUS_COLUMN + " = ?";
    public static final String SELECTION_TIME_STAMP = TASKS_TIME_STAMP_COLUMN + " = ?";
    public static final String SELECTION_LIKE_TITLE = TASKS_TITLE_COLUMN + " LIKE ?";

    private DBQueryManager dbQueryManager;
    private DBUpdateManger dbUpdateManger;

    public DBHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);

        dbQueryManager = new DBQueryManager(getReadableDatabase());
        dbUpdateManger = new DBUpdateManger(getWritableDatabase());
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL(TASK_TABLE_CREATE_SCRIPT);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
                db.execSQL("DROP TABLE " + TASKS_TABLE);
        onCreate(db);
    }

    public void saveTask(ModelTask task) {
        ContentValues newValues = new ContentValues();

        newValues.put(TASKS_TITLE_COLUMN, task.getTitle());
        newValues.put(TASKS_DATE_COLUMN, task.getDate());
        newValues.put(TASKS_PRIORITY_COLUMN, task.getPriority());
        newValues.put(TASKS_STATUS_COLUMN, task.getStatus());
        newValues.put(TASKS_TIME_STAMP_COLUMN, task.getTimeStamp());

        getWritableDatabase().insert(TASKS_TABLE, null, newValues);

        user = mAuth.getInstance().getCurrentUser();
        myRef = FirebaseDatabase.getInstance().getReference(user.getUid());
        myRef.child("tasks").child(Long.toString(task.getTimeStamp())).setValue(task);
    }

    public DBQueryManager query() {
        return dbQueryManager;
    }

    public DBUpdateManger update() {
        return dbUpdateManger;
    }

    public void removeTask(long timeStamp) {
        getWritableDatabase().delete(TASKS_TABLE, SELECTION_TIME_STAMP, new String[]{Long.toString(timeStamp)});
    }
}
