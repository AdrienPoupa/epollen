package pollens.poupa.beaujean.com.pollens;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.DatabaseUtils;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import java.util.ArrayList;

public class DatabaseHelper extends SQLiteOpenHelper {

    private static DatabaseHelper mInstance = null;

    public static DatabaseHelper getInstance(Context context) {

        // Use the application context, which will ensure that you
        // don't accidentally leak an Activity's context.
        // See this article for more information: http://bit.ly/6LRzfx
        if (mInstance == null) {
            mInstance = new DatabaseHelper(context);
        }
        return mInstance;
    }

    /**
     * Constructor should be private to prevent direct instantiation.
     * make call to static factory method "getInstance()" instead.
     */
    private DatabaseHelper(Context context) {
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
        db.close();
        return true;
    }

    public boolean insertRisk (String name, String number, int risk) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put("name", name);
        contentValues.put("number", number);
        contentValues.put("risk", risk);
        db.insert("risks", null, contentValues);
        db.close();
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
        db.close();
        return true;
    }

    public boolean updateRisk (int id, String name, String number, int risk) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put("name", name);
        contentValues.put("number", number);
        contentValues.put("risk", risk);
        db.update("risks", contentValues, "id = ? ", new String[] { Integer.toString(id) } );
        db.close();
        return true;
    }

    public Integer deleteDepartment(Integer id) {
        SQLiteDatabase db = this.getWritableDatabase();
        return db.delete("departments",
                "id = ? ",
                new String[] { Integer.toString(id) });
    }

    public Integer deleteRisk(String number) {
        SQLiteDatabase db = this.getWritableDatabase();
        return db.delete("risks",
                "number = ? ",
                new String[] { number });
    }

    /**
     * Delete all data in DB
     * @return void
     */
    public void deleteDepartments() {
        SQLiteDatabase db = this.getWritableDatabase();
        db.delete("departments", null, null);
        db.close();
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

    @Override
    protected void finalize() throws Throwable {
        this.close();
        super.finalize();
    }
}