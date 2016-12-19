package com.example.acer.testtodocamdb;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Acer on 12/5/2016.
 */

public class ToDoDBHelper extends SQLiteOpenHelper {
    //TASK 1: DEFINE THE DATABASE AND TABLE
    private static final int DATABASE_VERSION = 1;
    private static final String DATABASE_NAME = "toDo_List";
    private static final String DATABASE_TABLE = "toDo_Items";


    //TASK 2: DEFINE THE COLUMN NAMES FOR THE TABLE
    private static final String KEY_TASK_ID = "_id";
    private static final String KEY_DESCRIPTION = "description";
    private static final String KEY_PICTURE = "picture";
    private static final String KEY_IS_DONE = "is_done";

    private int taskCount;

    public ToDoDBHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }


    @Override
    public void onCreate(SQLiteDatabase db) {

        String table = "CREATE TABLE " + DATABASE_TABLE + "("
                + KEY_TASK_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, "
                + KEY_DESCRIPTION + " TEXT, "
                + KEY_PICTURE + " TEXT, "
                + KEY_IS_DONE + " INTEGER" + ")";

        db.execSQL(table);
    }

    @Override
    public void onUpgrade(SQLiteDatabase database, int oldVersion, int newVersion) {
        // DROP OLDER TABLE IF EXISTS
        database.execSQL("DROP TABLE IF EXISTS " + DATABASE_TABLE);

        // CREATE TABLE AGAIN
        onCreate(database);
    }


    //********** DATABASE OPERATIONS:  ADD, EDIT, DELETE
    // Adding new task
    public void addToDoItem(ToDoItem task) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();

        //ADD KEY-VALUE PAIR INFORMATION FOR THE TASK DESCRIPTION
        values.put(KEY_DESCRIPTION, task.getDescription()); // task name
        values.put(KEY_PICTURE, task.getImageFile());

        //ADD KEY-VALUE PAIR INFORMATION FOR
        //IS_DONE VALUE: 0- NOT DONE, 1 - IS DONE
        values.put(KEY_IS_DONE, task.getIs_done());

        // INSERT THE ROW IN THE TABLE
        db.insert(DATABASE_TABLE, null, values);
        taskCount++;

        System.out.println("Created task: " + taskCount + ": " + task.toString());

        // CLOSE THE DATABASE CONNECTION
        db.close();
    }

    public void deleteToDoItem() {
        List<ToDoItem> todoList = getAllTasks();

        SQLiteDatabase database = this.getReadableDatabase();

        for (int i = 0; i < todoList.size(); i++) {
            ToDoItem task = todoList.get(i);
            System.out.println(task.toString());
            if (task.getIs_done() == 1) {
                database.delete(DATABASE_TABLE, KEY_TASK_ID + " = ?",
                        new String[]{String.valueOf(task.get_id())});
                System.out.println("Deleted task: " + task.toString());
            }
        }
        database.close();
    }

    public List<ToDoItem> getAllTasks() {

        //GET ALL THE TASK ITEMS ON THE LIST
        List<ToDoItem> todoList = new ArrayList<ToDoItem>();

        //SELECT ALL QUERY FROM THE TABLE
        String selectQuery = "SELECT  * FROM " + DATABASE_TABLE;

        SQLiteDatabase db = this.getWritableDatabase();
        Cursor cursor = db.rawQuery(selectQuery, null);

        // LOOP THROUGH THE TODO TASKS
        if (cursor.moveToFirst()) {
            do {
                ToDoItem task = new ToDoItem();
                task.set_id(cursor.getInt(0));
                task.setDescription(cursor.getString(1));
                task.setImageFile(cursor.getString(2));
                task.setIs_done(cursor.getInt(3));
                todoList.add(task);
            } while (cursor.moveToNext());
        }

        // RETURN THE LIST OF TASKS FROM THE TABLE
        return todoList;
    }

    public void clearAll() {
        //GET ALL THE LIST TASK ITEMS AND CLEAR THEM

        SQLiteDatabase db = this.getWritableDatabase();
        db.delete(DATABASE_TABLE, null, new String[]{});
        db.close();
    }


    public void updateTask(ToDoItem task) {
        // updating row
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(KEY_DESCRIPTION, task.getDescription());
        values.put(KEY_PICTURE, task.getImageFile());
        values.put(KEY_IS_DONE, task.getIs_done());
        db.update(DATABASE_TABLE, values, KEY_TASK_ID + " = ?", new String[]{String.valueOf(task.get_id())});
        db.close();
    }


}
