package com.example.sripadmanaban.listviewdynamic;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import java.sql.SQLException;

/**
 * SQLite Database to store the data
 * Created by Sripadmanaban on 12/12/2014.
 */
public class GamesDatabaseAdapter
{
    private static final String KEY_ROWID = "GameId";
    private static final String KEY_GAMENAME = "GameName";
    private static final String KEY_DEVELOPERNAME = "DeveloperName";
    private static final String TAG = "GamesDatabaseAdapter";

    private static final String columns[] = {KEY_ROWID, KEY_GAMENAME, KEY_DEVELOPERNAME};

    private static final String DATABASE_NAME = "MyGamesDB.db";
    private static final String DATABASE_TABLE = "Games";
    private static final int DATABASE_VERSION = 1;

    private static final String DATABASE_CREATE = "CREATE TABLE Games(GameId INTEGER PRIMARY KEY AUTOINCREMENT, "
            + "GameName TEXT NOT NULL, DeveloperName TEXT NOT NULL)";

    private DatabaseHelper DBHelper;
    private SQLiteDatabase db;
    private static GamesDatabaseAdapter gamesDatabaseAdapter;

    public static GamesDatabaseAdapter getInstance(Context context)
    {
        if(gamesDatabaseAdapter == null)
        {
            gamesDatabaseAdapter = new GamesDatabaseAdapter(context);
        }

        return gamesDatabaseAdapter;
    }

    private GamesDatabaseAdapter(Context context)
    {
        DBHelper = new DatabaseHelper(context);
    }

    private static class DatabaseHelper extends SQLiteOpenHelper
    {

        DatabaseHelper(Context context)
        {
            super(context, DATABASE_NAME, null, DATABASE_VERSION);
        }

        @Override
        public void onCreate(SQLiteDatabase db)
        {
            try
            {
                db.execSQL(DATABASE_CREATE);
            }
            catch (Exception e)
            {
                e.printStackTrace();
            }
        }

        @Override
        public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion)
        {
            Log.w(TAG, "Upgrading database from version " + oldVersion + " to " + newVersion + ", which will destroy all old data");
            db.execSQL("DROP TABLE IF EXISTS Games");
            onCreate(db);
        }

    }

    //Opens the database
    public GamesDatabaseAdapter open() throws SQLException
    {
        db = DBHelper.getWritableDatabase();
        return this;
    }

    //Close the database
    public void close()
    {
        DBHelper.close();
    }

    //Insert a Entry into the database
    public long insertGame(String gameName, String developerName)
    {
        ContentValues initialValues = new ContentValues();
        initialValues.put(KEY_GAMENAME, gameName);
        initialValues.put(KEY_DEVELOPERNAME, developerName);
        return db.insert(DATABASE_TABLE, null, initialValues);
    }

    //Delete a particular Game
    public boolean deleteGame(long rowId)
    {
        return db.delete(DATABASE_TABLE, KEY_ROWID + "=" + rowId, null) > 0;
    }

    //Retrieves a particular Game
    public Cursor getGame(long rowId) throws SQLException
    {
        Cursor mCursor = db.query(true, DATABASE_TABLE, columns, KEY_ROWID + "=" + rowId, null, null, null, null, null);

        if(mCursor != null)
        {
            mCursor.moveToFirst();
        }

        return mCursor;
    }

    //Update Game Data
    public boolean updateGame(long rowId, String game, String developer)
    {
        ContentValues args = new ContentValues();
        args.put(KEY_GAMENAME, game);
        args.put(KEY_DEVELOPERNAME, developer);
        return db.update(DATABASE_TABLE, args, KEY_ROWID + "=" + rowId, null) > 0;
    }

    public Cursor getAllGames(String type)
    {
        String orderBy = KEY_GAMENAME + type;
        return db.query(DATABASE_TABLE, columns, null, null, null, null, orderBy);
    }
}