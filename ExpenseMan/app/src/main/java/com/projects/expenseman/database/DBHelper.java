package com.projects.expenseman.database;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.DatabaseUtils;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import com.projects.expenseman.models.Expense;

import java.util.ArrayList;
import java.util.Date;

/**
 * Created by ullasjoseph on 18/07/14.
 */
public class DBHelper extends SQLiteOpenHelper {

    public static final String DATABASE_NAME = "MyDBName.db";
    public static final String EXPENSE_TABLE_NAME = "expense";
    public static final String EXPENSE_COLUMN_AMOUNT = "amount";
    public static final String EXPENSE_COLUMN_CATEGORY = "category";
    public static final String EXPENSE_COLUMN_TIME = "time";
    public static final String EXPENSE_COLUMN_PAYEE = "payee";
    public static final String EXPENSE_COLUMN_NOTES = "notes";


    public DBHelper(Context context)
    {
        super(context, DATABASE_NAME , null, 1);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        // TODO Auto-generated method stub
        db.execSQL(
                "create table expense " +
                        "(id integer primary key, category text,time date,payee text, notes text, amount int)"
        );
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        // TODO Auto-generated method stub
        db.execSQL("DROP TABLE IF EXISTS expense");
        onCreate(db);
    }

    public void resetDB() {
        SQLiteDatabase db = this.getWritableDatabase();
        db.execSQL("DROP TABLE IF EXISTS expense");
        onCreate(db);
    }
    public boolean insertExpense  (Expense expense)
    {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues contentValues = new ContentValues();

        contentValues.put("category", expense.getCategory());
        contentValues.put("time", expense.getTime().getTime());
        contentValues.put(EXPENSE_COLUMN_AMOUNT, expense.getAmount());
        contentValues.put("payee", expense.getPayee());
        contentValues.put("notes", expense.getNotes());

        db.insert(EXPENSE_TABLE_NAME, null, contentValues);
        return true;
    }
    public Cursor getData(int id){
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor res =  db.rawQuery( "select * from expense where id="+id+"", null );
        return res;
    }
    public int numberOfRows(){
        SQLiteDatabase db = this.getReadableDatabase();
        int numRows = (int) DatabaseUtils.queryNumEntries(db, EXPENSE_TABLE_NAME);
        return numRows;
    }
    public boolean updateExpense (Expense expense)
    {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put("category", expense.getCategory());
        contentValues.put("time", expense.getTime().getTime());
        contentValues.put(EXPENSE_COLUMN_AMOUNT, expense.getAmount());
        contentValues.put("payee", expense.getPayee());
        contentValues.put("notes", expense.getNotes());
//        db.update("expense", contentValues, "id = ? ", new String[] { Integer.toString(id) } );
        return true;
    }

    public Integer deleteExpense (Integer id)
    {
        SQLiteDatabase db = this.getWritableDatabase();
        return db.delete("expense",
                "id = ? ",
                new String[] { Integer.toString(id) });
    }
    public ArrayList<Expense> getAllExpense()
    {
        ArrayList<Expense> array_list = new ArrayList<Expense>();
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor res =  db.rawQuery( "select * from expense", null );
        res.moveToFirst();
        while(res.isAfterLast() == false){
            Expense ex = new Expense();
            ex.setCategory(res.getString(res.getColumnIndex(EXPENSE_COLUMN_CATEGORY)));
            ex.setTime(new Date(res.getLong(res.getColumnIndex(EXPENSE_COLUMN_TIME))));
            ex.setPayee(res.getString(res.getColumnIndex(EXPENSE_COLUMN_PAYEE)));
            ex.setNotes(res.getString(res.getColumnIndex(EXPENSE_COLUMN_NOTES)));
            ex.setAmount(res.getDouble(res.getColumnIndex(EXPENSE_COLUMN_AMOUNT)));
            array_list.add(ex);
            res.moveToNext();
        }
        return array_list;
    }

}
