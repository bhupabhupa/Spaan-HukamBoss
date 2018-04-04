package com.securityapp.hukamboss.securityapp.utils;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.Hashtable;
import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.DatabaseUtils;
import android.database.sqlite.SQLiteOpenHelper;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;

import org.json.JSONObject;

/**
 * Created by PTSPL on 15-01-2018.
 */

public class DBHelper extends SQLiteOpenHelper {
    public static final String DATABASE_NAME = "HBSecApp.db";
    public static final String SETTINGS_TABLE_NAME = "SettingsTbl";
    public static final String SETTINGS_KEY = "Key";
    public static final String SETTINGS_VALUE = "Value";
    private static final int DB_VERSION = 1;
    private static final String TOKEN = "Token";
    private static final String ROLES = "Roles";
    private static final String FUNCTIONS_JSON = "FunctionsJSON";

    private static  final  String CREATE_SETTINGS_TABLE= " ("
            + SETTINGS_KEY +" text, "
            + SETTINGS_VALUE +" text "
            + ")";

    public DBHelper(Context context) {
        super(context, DATABASE_NAME, null, DB_VERSION);
        getWritableDatabase();
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        // TODO Auto-generated method stub
        db.execSQL("create table " + SETTINGS_TABLE_NAME + CREATE_SETTINGS_TABLE);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        // TODO Auto-generated method stub
        // TODO Nothing here For Now ... revisit later
    }


    public boolean isUserLoginInfoPresentInSettingsTbl()
    {
        boolean retVal = false;
        String keyStr = "";
        try {
            SQLiteDatabase db = this.getWritableDatabase();
            Cursor res = db.rawQuery("select * from " + SETTINGS_TABLE_NAME,null);
            if (!res.moveToFirst())
            {
                return retVal;
            }
            do
            {
                keyStr = res.getString(0);
                if (0== keyStr.compareToIgnoreCase(TOKEN)) {
                    retVal = true;
                    break;
                }

            } while (res.moveToNext());
        } catch(Exception e){
            Log.d("insert",e.getMessage());
        }
        return retVal;
    }

    public boolean insertCurrentLoginDetails(String itokenVal) {
        try {
            SQLiteDatabase db = this.getWritableDatabase();
            ContentValues contentValues = new ContentValues();
            contentValues.put(SETTINGS_KEY,TOKEN);
            contentValues.put(SETTINGS_VALUE,itokenVal);
            long result = db.insert(SETTINGS_TABLE_NAME,null ,contentValues);
            } catch(Exception e){
            Log.d("insert",e.getMessage());
            return false;
        }

        return true;
    }

    public boolean insertIntoSettingsTbl(String ikey, String ival)
    {
        try {
            SQLiteDatabase db = this.getWritableDatabase();
            ContentValues contentValues = new ContentValues();
            contentValues.put(SETTINGS_KEY,ikey);
            contentValues.put(SETTINGS_VALUE,ival);
            //contentValues.put(ikey,ival);
            long result = db.insert(SETTINGS_TABLE_NAME,null ,contentValues);
        } catch(Exception e){
            Log.d("insert",e.getMessage());
            return false;
        }

        return true;
    }
    public String getFromSettingsTbl(String ikey)
    {
        String keyStr, valStr = null;
        try {
            SQLiteDatabase db = this.getWritableDatabase();
            Cursor res = db.rawQuery("select * from " + SETTINGS_TABLE_NAME,null);
            if (!res.moveToFirst())
            {
                return null;
            }
            do
            {
                keyStr = res.getString(0);
                if (0== keyStr.compareToIgnoreCase(ikey)) {
                    valStr = res.getString(1);;
                    break;
                }

            } while (res.moveToNext());
        } catch(Exception e){
            Log.d("getquery",e.getMessage());
            return null;
        }
        return valStr;
    }
    public boolean removeFromSettingsTbl(String ikey)
    {
        boolean retVal = false;
        try {
            SQLiteDatabase db = this.getWritableDatabase();
            db.delete(SETTINGS_TABLE_NAME, SETTINGS_KEY + "='" + ikey + "'", null);
        } catch(Exception e){
            Log.d("insert",e.getMessage());
            return retVal;
        }

        return retVal;
    }
}
