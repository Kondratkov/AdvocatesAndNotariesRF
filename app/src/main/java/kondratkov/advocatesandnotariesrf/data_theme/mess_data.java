package kondratkov.advocatesandnotariesrf.data_theme;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.DatabaseErrorHandler;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.provider.BaseColumns;
import android.util.Log;

import java.util.ArrayList;
import java.util.List;

import kondratkov.advocatesandnotariesrf.IN;

public class mess_data extends SQLiteOpenHelper implements BaseColumns {

    private static final String DATABASE_NAME = "mythemedata.db";
    private static final int DATABASE_VERSION = 3;
    private static final String DATABASE_TABLE = "mess_table";

    public IN in = new IN();

    public static final String COLUMN_ID = BaseColumns._ID;
    public static final String COLUMN_IDT    = "idt";
    public static final String COLUMN_IDU    = "idu";
    public static final String COLUMN_IDJ    = "idj";
    public static final String COLUMN_DATE   = "date";
    public static final String COLUMN_NAME   = "name";
    public static final String COLUMN_TEXT   = "text";
    public static final String COLUMN_PLACE  = "place";
    public static final String COLUMN_TIPWHO = "tip_who";

    public mess_data(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    public mess_data(Context context, String name, SQLiteDatabase.CursorFactory factory,
                      int version) {
        super(context, name, factory, version);
    }

    public mess_data(Context context, String name, SQLiteDatabase.CursorFactory factory,
                      int version, DatabaseErrorHandler errorHandler) {
        super(context, name, factory, version, errorHandler);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

    }

    public void addQuestion(AllMess ask) {
        SQLiteDatabase db = this.getWritableDatabase();

        ContentValues values = new ContentValues();

        values.put(  COLUMN_IDT,   ask.get_idt());
        values.put(  COLUMN_IDU,   ask.get_idu());
        values.put(  COLUMN_IDJ,   ask.get_idj());
        values.put(  COLUMN_DATE,  ask.get_date());
        values.put(  COLUMN_NAME,  ask.get_name());
        values.put(  COLUMN_TEXT,  ask.get_text());
        values.put(  COLUMN_PLACE, ask.get_place());
        values.put(  COLUMN_TIPWHO, ask.get_tipwho());

        db.insert(DATABASE_TABLE, null, values);
        db.close();
    }

    // ???????? ???????
    public AllMess getMess(int id) {
        SQLiteDatabase db = this.getReadableDatabase();

        Cursor cursor = db.query(DATABASE_TABLE, new String[] {
                        COLUMN_ID,   COLUMN_IDT,   COLUMN_IDU,   COLUMN_IDJ,  COLUMN_DATE,
                        COLUMN_NAME, COLUMN_TEXT,  COLUMN_PLACE, COLUMN_TIPWHO }, COLUMN_ID + "=?",
                new String[] { String.valueOf(id) }, null, null, null, null);
        if (cursor != null)
            cursor.moveToFirst();

        AllMess mess = new AllMess(
                Integer.parseInt(cursor.getString(0)),//id
                Integer.parseInt(cursor.getString(1)), //idt
                Integer.parseInt(cursor.getString(2)),//idu
                Integer.parseInt(cursor.getString(3)),//idj
                cursor.getString(4),//date
                cursor.getString(5), //name
                cursor.getString(6),//text
                Integer.parseInt(cursor.getString(7)),
                Integer.parseInt(cursor.getString(8)));//tipwho
        return mess;
    }

    //vozrat danih o theme
    public String date_mess(int id, int id_table)
    {
        SQLiteDatabase db = this.getReadableDatabase();
        String con = "";

        Cursor cursor = db.query(DATABASE_TABLE, new String[]{
                        COLUMN_ID,   COLUMN_IDT,  COLUMN_IDU,   COLUMN_IDJ,  COLUMN_DATE,
                        COLUMN_NAME, COLUMN_TEXT, COLUMN_PLACE, COLUMN_TIPWHO}, COLUMN_ID + "=?",
                new String[]{String.valueOf(id)}, null, null, null, null);
        if (cursor != null)
            cursor.moveToFirst();
        for(int i =0; i<9; i++)
        {
            if(i==id_table)con = cursor.getString(i);
        }
        return con;
    }

    public String last_date(){
        SQLiteDatabase db = this.getWritableDatabase();
        Log.d("qwerty", "dataLast");
        Cursor cursor = db.query(DATABASE_TABLE,
                null,//new String[]{"name", "date"},
                COLUMN_IDT + " = " + in.get_idt() + " and " + COLUMN_PLACE + " = " + in.get_place(),
                null,
                null,
                null,
                COLUMN_DATE);
        cursor.moveToLast();
        Log.d("qwerty", "dataLast "+ cursor.getPosition());
        String s_date = "0";
        if(cursor.getPosition()>-1){
            s_date = cursor.getString(4);
        }
        return s_date;
    }

    // ???????? ??? ????????
    public List<AllMess> getAllMess() {
        List contactList = new ArrayList<>();
        String selectQuery = "SELECT * FROM " + DATABASE_TABLE;

        SQLiteDatabase db = this.getWritableDatabase();
        Cursor cursor = db.query(DATABASE_TABLE,
                null,//new String[]{"name", "date"},
                COLUMN_IDT + " = " + in.get_idt()+ " and "+ COLUMN_PLACE+ " = " + in.get_place(),
                null,
                null,
                null,
                null);

        if (cursor.moveToFirst()) {
            do {
                AllMess mess = new AllMess();
                mess.setID(Integer.parseInt(cursor.getString(0)));//id
                mess.set_idt(Integer.parseInt(cursor.getString(1)));//idt
                mess.set_idu(Integer.parseInt(cursor.getString(2)));//idu
                mess.set_idj(Integer.parseInt(cursor.getString(3)));//idj
                mess.set_date(cursor.getString(4));//date
                mess.set_name(cursor.getString(5));//name
                mess.set_text(cursor.getString(6));//text
                mess.set_place(Integer.parseInt(cursor.getString(7)));
                mess.set_tipwho(Integer.parseInt(cursor.getString(8)));//tipwho

                contactList.add(mess);

            } while (cursor.moveToNext());
        }
        cursor.close();
        return contactList;
    }

    // ???????? ????? ?????????
    public int getContactsCount() {
        String countQuery = "SELECT * FROM " + DATABASE_TABLE;
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.query(DATABASE_TABLE,
                null,//new String[]{"name", "date"},
                COLUMN_IDT + " = " + in.get_idt() + " and " + COLUMN_PLACE + " = " + in.get_place(),
                null,
                null,
                null,
                COLUMN_DATE);
        int count = cursor.getCount();

        cursor.close();

        return count;
    }

    // ???????? ???????
    public int updateContact(AllMess mess) {
        SQLiteDatabase db = this.getWritableDatabase();

        ContentValues values = new ContentValues();

        values.put(  COLUMN_IDT,   mess.get_idt());
        values.put(  COLUMN_IDU,   mess.get_idu());
        values.put(  COLUMN_IDJ,   mess.get_idj());
        values.put(  COLUMN_DATE,  mess.get_date());
        values.put(  COLUMN_NAME,  mess.get_name());
        values.put(  COLUMN_TEXT,  mess.get_text());
        values.put(  COLUMN_PLACE,  mess.get_place());
        values.put(  COLUMN_TIPWHO,mess.get_tipwho());

        // ????????? ??????
        return db.update(DATABASE_TABLE, values, COLUMN_ID + " = ?",
                new String[] { String.valueOf(mess.getID()) });
    }

    // ??????? ???????
    public void deleteContact(AllMess mess) {
        SQLiteDatabase db = this.getWritableDatabase();
        db.delete(DATABASE_TABLE, COLUMN_ID + " = ?",
                new String[] { String.valueOf(mess.getID()) });
        db.close();
    }
}

