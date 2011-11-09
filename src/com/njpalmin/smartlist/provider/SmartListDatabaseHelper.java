package com.njpalmin.smartlist.provider;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteDatabase.CursorFactory;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

public class SmartListDatabaseHelper extends SQLiteOpenHelper{
	
	private static final String TAG="SmartListDatabaseHelper";
    private static final String DATABASE_NAME = "smartlist.db";
    private static final int DATABASE_VERSION = 1;
	private static SmartListDatabaseHelper sSingleton = null;
	private final Context mContext;
	
	private static final String CREATE_PRODUCTS_TABLE ="CREATE TABLE products (_id INTEGER PRIMARY KEY AUTOINCREMENT," +
																			  "guid TEXT NULL,list_id INTEGER NULL," +
																			  "description TEXT NOT NULL," +
																			  "description_short TEXT NULL," +
																			  "upc TEXT NULL,quantity INTEGER NOT NULL," +
																			  "unit TEXT NULL," +
																			  "price REAL NULL," +
																			  "tax_free INTEGER NULL," +
																			  "done INTEGER NOT NULL," +
																			  "ordinal INTEGER CONSTRAINT df_ordinal DEFAULT(0)," +
																			  "note TEXT NULL," +
																			  "has_coupon INT NULL," +
																			  "coupon_amount REAL NULL," +
																			  "coupon_type TEXT NULL," +
																			  "coupon_note TEXT NULL," +
																			  "created TEXT NULL," +
																			  "modified TEXT NULL)";
	
	private static final String CREATE_LISTS_TABLE = "CREATE TABLE lists (_id INTEGER PRIMARY KEY AUTOINCREMENT," +
																		 "guid TEXT NULL," +
																		 "description TEXT NOT NULL," +
																		 "type TEXT NULL," +
																		 "subtype TEXT NULL," +
																		 "is_owner INTEGER NULL," +
																		 "owner_nickname TEXT NULL," +
																		 "sort_by_done INTEGER NULL," +
																		 "sort_type TEXT NULL," +
																		 "sort_direction TEXT NULL," +
																		 "created TEXT NULL," +
																		 "modified TEXT NULL)";
	
	private static final String CREATE_TODOS_TABLE = "CREATE TABLE todos (_id INTEGER PRIMARY KEY AUTOINCREMENT," +
																		 "guid TEXT NULL," +
																		 "list_id INTEGER NULL," +
																		 "description TEXT NOT NULL," +
																		 "priority INTEGER NOT NULL," +
																		 "reminder TEXT NULL," +
																		 "done INTEGER NOT NULL," +
																		 "ordinal INTEGER CONSTRAINT df_todos_ordinal DEFAULT(0)," +
																		 "note TEXT NULL," +
																		 "created TEXT NULL," +
																		 "modified TEXT NULL)";

	
    public static synchronized SmartListDatabaseHelper getInstance(Context context) {
    	if (sSingleton == null) {
    		sSingleton = new SmartListDatabaseHelper(context);
    		sSingleton.getWritableDatabase();
    	}
        return sSingleton;
    }

    public SmartListDatabaseHelper(Context context){
		super(context, DATABASE_NAME, null, DATABASE_VERSION);
    	mContext = context;
    }

	@Override
	public void onCreate(SQLiteDatabase db) {
		// TODO Auto-generated method stub
		db.execSQL(CREATE_PRODUCTS_TABLE);
		db.execSQL(CREATE_LISTS_TABLE);
		db.execSQL(CREATE_TODOS_TABLE);
	}

	@Override
	public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
		// TODO Auto-generated method stub
        Log.w(TAG, "Upgrading database from version " + oldVersion + " to "
                + newVersion + ", which will destroy all old data");
        db.execSQL("DROP TABLE IF EXISTS products, lists, todos");
        onCreate(db);
	}

}
