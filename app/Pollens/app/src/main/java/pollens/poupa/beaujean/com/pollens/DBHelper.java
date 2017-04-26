package pollens.poupa.beaujean.com.pollens;

import java.util.ArrayList;
import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.DatabaseUtils;
import android.database.sqlite.SQLiteOpenHelper;
import android.database.sqlite.SQLiteDatabase;

public class DBHelper extends SQLiteOpenHelper {

    public DBHelper(Context context) {
        super(context, "departments" , null, 1);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL("CREATE TABLE departments (id INTEGER PRIMARY KEY, name TEXT, number TEXT, risk INTEGER, color TEXT);");
        db.execSQL("CREATE TABLE risks (id INTEGER PRIMARY KEY, name TEXT, number TEXT, risk INTEGER);");
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS departments");
        db.execSQL("DROP TABLE IF EXISTS risks");
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

    public boolean insertRisk (String name, String number, int risk) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put("name", name);
        contentValues.put("number", number);
        contentValues.put("risk", risk);
        db.insert("risks", null, contentValues);
        return true;
    }

    public Cursor getDepartment(String id) {
        SQLiteDatabase db = this.getReadableDatabase();
        return db.rawQuery("SELECT * FROM departments WHERE number = ?", new String[] { id });
    }

    public Cursor getRisk(String id) {
        SQLiteDatabase db = this.getReadableDatabase();
        return db.rawQuery("SELECT * FROM risks WHERE number = ?", new String[] { id });
    }

    public int numberOfRowsDepartments(){
        SQLiteDatabase db = this.getReadableDatabase();
        return (int) DatabaseUtils.queryNumEntries(db, "departments");
    }

    public int numberOfRowsRisks(){
        SQLiteDatabase db = this.getReadableDatabase();
        return (int) DatabaseUtils.queryNumEntries(db, "risks");
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

    public boolean updateRisk (int id, String name, String number, int risk) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put("name", name);
        contentValues.put("number", number);
        contentValues.put("risk", risk);
        db.update("risks", contentValues, "id = ? ", new String[] { Integer.toString(id) } );
        return true;
    }

    public Integer deleteDepartment(Integer id) {
        SQLiteDatabase db = this.getWritableDatabase();
        return db.delete("departments",
                "id = ? ",
                new String[] { Integer.toString(id) });
    }

    public Integer deleteRisk(Integer id) {
        SQLiteDatabase db = this.getWritableDatabase();
        return db.delete("risks",
                "id = ? ",
                new String[] { Integer.toString(id) });
    }

    /**
     * Delete all data in DB
     * @return void
     */
    public void delete() {
        SQLiteDatabase db = this.getWritableDatabase();
        db.delete("departments", null, null);
        db.delete("risks", null, null);
    }

    public ArrayList<String> getAllDepartments() {
        ArrayList<String> array_list = new ArrayList<String>();

        SQLiteDatabase db = this.getReadableDatabase();
        Cursor res =  db.rawQuery( "SELECT * FROM departments", null );
        res.moveToFirst();
        res.close();

        while(!res.isAfterLast()){
            array_list.add(res.getString(res.getColumnIndex("name")));
            res.moveToNext();
        }
        return array_list;
    }

    public ArrayList<String> getAllRisks() {
        ArrayList<String> array_list = new ArrayList<String>();

        SQLiteDatabase db = this.getReadableDatabase();
        Cursor res =  db.rawQuery( "SELECT * FROM risks", null );
        res.moveToFirst();
        res.close();

        while(!res.isAfterLast()){
            array_list.add(res.getString(res.getColumnIndex("number")));
            res.moveToNext();
        }
        return array_list;
    }
}