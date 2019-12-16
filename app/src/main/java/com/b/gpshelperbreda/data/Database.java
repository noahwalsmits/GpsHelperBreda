package com.b.gpshelperbreda.data;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import java.util.ArrayList;

public class Database extends SQLiteOpenHelper {

    private static final String TAG = "MetingManager";

    private static final int DATABASE_VERSION = 1;
    private static final String DATABASE_NAME = "DATABASE";
    private static final String TABLE_NAME = "waypoints";
    private static final String COLUMN_SEQUENCEID = "sequenceID";
    private static final String COLUMN_NAME = "name";
    private static final String COLUMN_DESCRIPTION = "description";
    private static final String COLUMN_LATITUDE = "latitude";
    private static final String COLUMN_LONGITUDE = "longitude";
    private static final String COLUMN_LATLNG = "latlng";
    private static final String COLUMN_ROUTEID = "routeID";
    private static final String COLUMN_PHOTOIDS = "photoIDs";
    private static final String COLUMN_SEEN = "seen";

    private static final String CREATE_TABLE =
            "CREATE TABLE " + TABLE_NAME + " (" +
                    COLUMN_SEQUENCEID + " INTEGER PRIMARY KEY," +
                    COLUMN_NAME + " TEXT," +
                    COLUMN_DESCRIPTION + " TEXT," +
                    COLUMN_LATITUDE + " REAL," +
                    COLUMN_LONGITUDE + " REAL," +
                    COLUMN_LATLNG + " BLOB," +
                    COLUMN_ROUTEID + " INTEGER," +
                    COLUMN_PHOTOIDS + " TEXT," +
                    COLUMN_SEEN + " INTEGER" +
                    ");";


    private static final String DROP_TABLE = "DROP TABLE IF EXISTS " + TABLE_NAME;

    private static final String CHECK_TABLE = "SELECT count(*) FROM " + TABLE_NAME;

    public Database(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL(CREATE_TABLE);
        Log.i(TAG, "onCreate() called, creating table");
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL(DROP_TABLE);
        Log.i(TAG, "onUpgrade() called, dropping table");
        onCreate(db);
    }

    public void insertValue(Waypoint waypoint) {
        getWritableDatabase().delete(TABLE_NAME, "sequenceID=?", new String[]{waypoint.getSequenceID() + ""});

        ContentValues values = new ContentValues();

        values.put(COLUMN_SEQUENCEID, waypoint.getSequenceID());
        values.put(COLUMN_NAME, waypoint.getName());
        values.put(COLUMN_DESCRIPTION, waypoint.getDescription());
        values.put(COLUMN_LATITUDE, waypoint.getLatitude());
        values.put(COLUMN_LONGITUDE, waypoint.getLongitude());
//        values.put(COLUMN_LATLNG,null);
        values.put(COLUMN_ROUTEID, waypoint.getRouteID());
        values.put(COLUMN_PHOTOIDS, waypoint.getPhotoIDsToString());
        values.put(COLUMN_SEEN, (waypoint.isSeen()) ? 1 : 0);

        SQLiteDatabase db = getWritableDatabase();

        db.insert(TABLE_NAME, null, values);
        Log.i(TAG, "insertValue() called, inserted into " + TABLE_NAME);
    }

    public boolean isTableFilled() {
        SQLiteDatabase db = getReadableDatabase();
        Cursor cursor = db.rawQuery(CHECK_TABLE, null);
        cursor.moveToFirst();
        int icount = cursor.getInt(0);
        return icount != 0;
    }

    public void resetTable() {
        SQLiteDatabase db = getWritableDatabase();
        db.execSQL(DROP_TABLE);
        db.execSQL(CREATE_TABLE);
    }

    public ArrayList<Waypoint> readValues() {
        String query = "SELECT * FROM " + TABLE_NAME + ";";
        String[] args = new String[10];
        SQLiteDatabase db = getReadableDatabase();
        Cursor cursor = db.rawQuery(query, null);
        ArrayList<Waypoint> waypoints = new ArrayList<>();

        if (cursor.moveToFirst()) {
            do {
                int routeID = cursor.getInt(cursor.getColumnIndex(COLUMN_ROUTEID));
                int sequenceID = cursor.getInt(cursor.getColumnIndex(COLUMN_SEQUENCEID));
                boolean seen = cursor.getInt(cursor.getColumnIndex(COLUMN_SEEN)) == 1;
                String name = cursor.getString(cursor.getColumnIndex(COLUMN_NAME));
                String description = cursor.getString(cursor.getColumnIndex(COLUMN_DESCRIPTION));
                double latitude = cursor.getDouble(cursor.getColumnIndex(COLUMN_LATITUDE));
                double longitude = cursor.getDouble(cursor.getColumnIndex(COLUMN_LONGITUDE));
                String photoIDs = cursor.getString(cursor.getColumnIndex(COLUMN_PHOTOIDS));

                waypoints.add(new Waypoint(photoIDs, routeID, sequenceID, latitude, longitude, name, description, seen));
            } while (cursor.moveToNext());
        }
        return waypoints;
    }


}
