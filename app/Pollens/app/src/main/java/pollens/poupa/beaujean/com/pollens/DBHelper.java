package pollens.poupa.beaujean.com.pollens;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Hashtable;
import android.content.ContentValues;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.database.DatabaseUtils;
import android.database.sqlite.SQLiteOpenHelper;
import android.database.sqlite.SQLiteDatabase;

public class DBHelper extends SQLiteOpenHelper {

    private HashMap hp;

    public DBHelper(Context context) {
        super(context, "departments" , null, 1);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL("CREATE TABLE departments (id integer primary key, name text, number text, risk integer, color text);");
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS departments");
        onCreate(db);
    }

    public boolean insertDepartment (String name, String number, int risk, String color) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put("name", name);
        contentValues.put("number", number);
        contentValues.put("risk", risk);
        contentValues.put("color", color);
        db.insert("departments", null, contentValues);
        return true;
    }

    public Cursor getDepartment(String id) {
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor res =  db.rawQuery("SELECT * FROM departments WHERE number = ?", new String[] { id });
        return res;
    }

    public int numberOfRows(){
        SQLiteDatabase db = this.getReadableDatabase();
        int numRows = (int) DatabaseUtils.queryNumEntries(db, "departments");
        return numRows;
    }

    public boolean updateDepartment (int id, String name, String number, int risk, String color) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put("name", name);
        contentValues.put("number", number);
        contentValues.put("risk", risk);
        contentValues.put("color", color);
        db.update("departments", contentValues, "id = ? ", new String[] { Integer.toString(id) } );
        return true;
    }

    public Integer deleteDepartment(Integer id) {
        SQLiteDatabase db = this.getWritableDatabase();
        return db.delete("departments",
                "id = ? ",
                new String[] { Integer.toString(id) });
    }

    /**
     * Delete all data in DB
     * @return
     */
    public Integer delete() {
        SQLiteDatabase db = this.getWritableDatabase();
        return db.delete("departments", null, null);
    }

    public ArrayList<String> getAllDepartments() {
        ArrayList<String> array_list = new ArrayList<String>();

        SQLiteDatabase db = this.getReadableDatabase();
        Cursor res =  db.rawQuery( "SELECT * FROM departments", null );
        res.moveToFirst();

        while(res.isAfterLast() == false){
            array_list.add(res.getString(res.getColumnIndex("name")));
            res.moveToNext();
        }
        return array_list;
    }
}