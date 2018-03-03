package lt.to_do_list.model;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.provider.BaseColumns;

import java.util.ArrayList;

/**
 * Created by Vita on 3/3/2018.
 */

public class DBActions extends SQLiteOpenHelper {
    public static final String DB_NAME = "lt.kaunascoding.todo.db";
    public static final int DB_VERSION = 1;

    private class TasksTable implements BaseColumns {
        public static final String TABLE = "tasks";
        public static final String COL_TASK_DONE = "done";
        public static final String COL_TASK_TITLE = "title";
    }


    public DBActions(Context context) {
        super(context, DB_NAME, null, DB_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        String createTable = "CREATE TABLE " + TasksTable.TABLE + " ( " +
                TasksTable._ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
                TasksTable.COL_TASK_DONE + " INTEGER DEFAULT 0," +
                TasksTable.COL_TASK_TITLE + " TEXT NOT NULL" +
                ");";

        db.execSQL(createTable);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS " + TasksTable.TABLE);
        onCreate(db);
    }

    public void addItem(String task){
        SQLiteDatabase db = getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(TasksTable.COL_TASK_TITLE, task);
        db.insertWithOnConflict(TasksTable.TABLE,
                null,
                values,
                SQLiteDatabase.CONFLICT_REPLACE);
        db.close();
    }

    public void deleteItem(ItemVO itemVO){
        SQLiteDatabase db = this.getWritableDatabase();
        db.delete(TasksTable.TABLE,
                TasksTable._ID + " = ?",
                new String[]{Integer.toString(itemVO.id)});
        db.close();

    }

    public void updateItem(ItemVO itemVO){
        ContentValues contentValues = new ContentValues();
        contentValues.put(TasksTable.COL_TASK_DONE, itemVO.done);
        SQLiteDatabase db = this.getWritableDatabase();
        db.updateWithOnConflict(TasksTable.TABLE,
                contentValues,
                TasksTable._ID + " = ?",
                new String[]{Integer.toString(itemVO.id)},
                SQLiteDatabase.CONFLICT_REPLACE
        );
        db.close();
    }

    public ArrayList<ItemVO> getAllItems(){
        ArrayList<ItemVO> taskList = new ArrayList<ItemVO>();
        SQLiteDatabase db = getReadableDatabase();
        Cursor cursor = db.query(
                TasksTable.TABLE,
                new String[]{
                        TasksTable._ID,
                        TasksTable.COL_TASK_DONE,
                        TasksTable.COL_TASK_TITLE
                },
                null,
                null,
                null,
                null,
                null
        );

        while (cursor.moveToNext()) {
            int idIndex = cursor.getColumnIndex(TasksTable._ID);
            int doneIndex = cursor.getColumnIndex(TasksTable.COL_TASK_DONE);
            int titleIndex = cursor.getColumnIndex(TasksTable.COL_TASK_TITLE);

            ItemVO itemVO = new ItemVO();
            itemVO.id = cursor.getInt(idIndex);
            itemVO.done = cursor.getInt(doneIndex);
            itemVO.title = cursor.getString(titleIndex);

            taskList.add(itemVO);
        }
        cursor.close();
        db.close();
        return taskList;
    }

}
