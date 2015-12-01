package com.developer.grebnev.to_do_list.database;

import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import com.developer.grebnev.to_do_list.model.ModelTask;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Grebnev on 01.12.2015.
 */
public class DBQueryManager {

    private SQLiteDatabase database;

    DBQueryManager(SQLiteDatabase database) {
        this.database = database;
    }

    public List<ModelTask> getTasks(String selection, String[]selectionArgs, String oerderBy) {
        List<ModelTask> tasks = new ArrayList<>();

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
