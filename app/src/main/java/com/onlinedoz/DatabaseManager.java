package com.onlinedoz;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;


public class DatabaseManager extends SQLiteOpenHelper {

    private static final String DatabaseName = "drugstoredatabase.db";
    private static final int Version = 1;
    private static final String FRIENDS_TBL = "friends_tbl";
    private static final String ID = "id";
    private static final String NAME = "name";
    private static final String USERNAME = "username";
    private static final String IMAGE_FR = "image_fr";


    public DatabaseManager(Context cnt) {

        super(cnt, DatabaseName, null, Version);
    }

    @Override
    public void onCreate(SQLiteDatabase cdb) {

        String frQuery = "CREATE TABLE IF NOT EXISTS " + FRIENDS_TBL + " ( " + ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " + NAME + " TEXT, " + USERNAME + " TEXT, " + IMAGE_FR + " TEXT );";
        cdb.execSQL(frQuery);


    }

    @Override
    public void onUpgrade(SQLiteDatabase sqLiteDatabase, int i, int i1) {

    }
    //ezafe kardan dost
    public void insertFr(String name,String username,String image) {

        SQLiteDatabase insertdb = this.getWritableDatabase();
        ContentValues contentV = new ContentValues();
        //contentV.put(ID, data.pID);
        contentV.put(NAME, name);
        contentV.put(USERNAME, username);
        contentV.put(IMAGE_FR,image);

        insertdb.insert(FRIENDS_TBL, null, contentV);
        insertdb.close();

    }

    public Items_list DisplayName(int row){

        Items_list items = new Items_list("","","","","","","","","","");
        SQLiteDatabase getdb = this.getReadableDatabase();
        Cursor cu = getdb.rawQuery("select * from "+FRIENDS_TBL, null);
        cu.moveToPosition(row);
        items.setName_list(cu.getString(1));
        items.setUsername_list(cu.getString(2));
        items.setImage_list(cu.getString(3));
        return items;
    }

    public int Count() {

        String gQuery = "SELECT * FROM " + FRIENDS_TBL;
        SQLiteDatabase countdb = this.getReadableDatabase();
        Cursor gCur = countdb.rawQuery(gQuery, null);
        int cResult = gCur.getCount();
        return cResult;

    }

    public boolean check(String username) {

        SQLiteDatabase mydb = this.getReadableDatabase();
        Cursor c = mydb.rawQuery("SELECT * FROM " + FRIENDS_TBL + " WHERE " + USERNAME + "='" + username + "'", null);

       int tedad = c.getCount();

        boolean b;
        if (tedad>0){

             b = true;
        }else {
            b = false;
        }
        c.close();
        mydb.close();
        return b;
    }

    public boolean deleteFr(String username) {

        SQLiteDatabase deletedb = this.getWritableDatabase();
        long Result = deletedb.delete(FRIENDS_TBL, USERNAME + "=?", new String[] {username});
        if (Result == 0)
            return false;
        else
            return true;
    }



}