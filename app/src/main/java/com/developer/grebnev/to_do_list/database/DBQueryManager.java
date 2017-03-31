package com.developer.grebnev.to_do_list.database;

import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;

import com.developer.grebnev.to_do_list.model.ModelTask;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.GenericTypeIndicator;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Grebnev on 01.12.2015.
 */
public class DBQueryManager {
    private final String TAG = this.getClass().getSimpleName();

    private SQLiteDatabase database;
    private DatabaseReference myRef;
    private FirebaseUser user;
    private FirebaseAuth mAuth;

    DBQueryManager(SQLiteDatabase database) {
        this.database = database;
    }

    public ModelTask getTask(long timeStamp) {
        ModelTask modelTask = null;
        Cursor cursor = database.query(DBHelper.TASKS_TABLE, null, DBHelper.SELECTION_TIME_STAMP,
                new String[]{Long.toString(timeStamp)}, null, null, null);

        if (cursor.moveToFirst()) {
            String title = cursor.getString(cursor.getColumnIndex(DBHelper.TASKS_TITLE_COLUMN));
            long date = cursor.getLong(cursor.getColumnIndex(DBHelper.TASKS_DATE_COLUMN));
            int priority = cursor.getInt(cursor.getColumnIndex(DBHelper.TASKS_PRIORITY_COLUMN));
            int status = cursor.getInt(cursor.getColumnIndex(DBHelper.TASKS_STATUS_COLUMN));

            modelTask = new ModelTask(title, date, priority, status, timeStamp);
        }
        cursor.close();

        return modelTask;
    }

    public List<ModelTask> getTasks(String selection, String[]selectionArgs, String oerderBy) {
        final List<ModelTask> tasks = new ArrayList<>();

//        user = mAuth.getInstance().getCurrentUser();
//        myRef = FirebaseDatabase.getInstance().getReference(user.getUid());
//        myRef.addValueEventListener(new ValueEventListener() {
//            @Override
//            public void onDataChange(DataSnapshot dataSnapshot) {
//                GenericTypeIndicator<ModelTask> t = new GenericTypeIndicator<ModelTask>() {};
//                ModelTask task = dataSnapshot.child("tasks").getValue(t);
//                tasks.add(task);
//                Log.v(TAG, Integer.toString(tasks.size()) + " " + tasks.get(0).getTitle());
//            }
//
//            @Override
//            public void onCancelled(DatabaseError databaseError) {
//
//            }
//        });

        Cursor c = database.query(DBHelper.TASKS_TABLE, null, selection, selectionArgs, null, null, oerderBy);

        if(c.moveToFirst()) {
            do {
                String title = c.getString(c.getColumnIndex(DBHelper.TASKS_TITLE_COLUMN));
                long date = c.getLong(c.getColumnIndex(DBHelper.TASKS_DATE_COLUMN));
                int priority = c.getInt(c.getColumnIndex(DBHelper.TASKS_PRIORITY_COLUMN));
                int status = c.getInt(c.getColumnIndex(DBHelper.TASKS_STATUS_COLUMN));
                long timeStamp = c.getLong(c.getColumnIndex(DBHelper.TASKS_TIME_STAMP_COLUMN));

                ModelTask modelTask = new ModelTask(title, date, priority, status, timeStamp);
                tasks.add(modelTask);
            } while (c.moveToNext());
        }
        c.close();

        return tasks;
    }
}
