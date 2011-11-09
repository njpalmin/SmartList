package com.njpalmin.smartlist.provider;

import java.util.HashMap;

import android.content.ContentProvider;
import android.content.ContentUris;
import android.content.ContentValues;
import android.content.UriMatcher;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteQueryBuilder;
import android.net.Uri;
import android.text.TextUtils;

import com.njpalmin.smartlist.record.ActiveRecord;
import com.njpalmin.smartlist.record.ActiveRecord.Lists;
import com.njpalmin.smartlist.record.ActiveRecord.Product;
import com.njpalmin.smartlist.record.ActiveRecord.ToDo;

public class SmartListProvider extends ContentProvider{
	private static final String TAG = "SmartListProvider";

	private static final int PRODUCT = 1;
	private static final int PRODUCT_ID = 2;
	private static final int LIST = 3;
	private static final int LIST_ID = 4;
	private static final int TODO = 5;
	private static final int TODO_ID = 6;
	
	public static final String TABLE_PRODUCT = "products";
	public static final String TABLE_LIST = "lists";
	public static final String TABLE_TODO = "todos";
	
	private static final HashMap<String, String> sProductProjectionMap;
	private static final HashMap<String, String> sListProjectionMap;
	private static final HashMap<String, String> sToDoProjectionMap;
	
	private static final UriMatcher sURLMatcher = new UriMatcher(UriMatcher.NO_MATCH);
	static{
        sURLMatcher.addURI(ActiveRecord.AUTHORITY, "products", PRODUCT);
        sURLMatcher.addURI(ActiveRecord.AUTHORITY, "products/#", PRODUCT_ID);
        
        sURLMatcher.addURI(ActiveRecord.AUTHORITY, "lists", LIST);
        sURLMatcher.addURI(ActiveRecord.AUTHORITY, "lists/#", LIST_ID);

        sURLMatcher.addURI(ActiveRecord.AUTHORITY, "todos", TODO);
        sURLMatcher.addURI(ActiveRecord.AUTHORITY, "todos/#", TODO_ID);

        
        sProductProjectionMap = new HashMap<String,String>();
        sProductProjectionMap.put(Product._ID, Product._ID);
        sProductProjectionMap.put(Product.GUID, Product.GUID);
        sProductProjectionMap.put(Product.LIST_ID, Product.LIST_ID);
        sProductProjectionMap.put(Product.DESCRIPTION, Product.DESCRIPTION);
        sProductProjectionMap.put(Product.DESCRIPTION_SHORT, Product.DESCRIPTION_SHORT);
        sProductProjectionMap.put(Product.UPC, Product.UPC);
        sProductProjectionMap.put(Product.QUANTITY, Product.QUANTITY);
        sProductProjectionMap.put(Product.UNIT, Product.UNIT);
        sProductProjectionMap.put(Product.PRICE, Product.PRICE);
        sProductProjectionMap.put(Product.TAX_FREE, Product.TAX_FREE);
        sProductProjectionMap.put(Product.DONE, Product.DONE);
        sProductProjectionMap.put(Product.ORDINAL, Product.ORDINAL);
        sProductProjectionMap.put(Product.NOTE, Product.NOTE);
        sProductProjectionMap.put(Product.HAS_COUPON, Product.HAS_COUPON);
        sProductProjectionMap.put(Product.COUPON_AMOUNT, Product.COUPON_AMOUNT);
        sProductProjectionMap.put(Product.COUPON_TYPE, Product.COUPON_TYPE);
        sProductProjectionMap.put(Product.COUPON_NOTE, Product.COUPON_NOTE);
        sProductProjectionMap.put(Product.CREATED, Product.CREATED);
        sProductProjectionMap.put(Product.MODIFIED, Product.MODIFIED);
        
        sListProjectionMap = new HashMap<String,String>();
        sListProjectionMap.put(Lists._ID, Lists._ID);
        sListProjectionMap.put(Lists.GUID, Lists.GUID);
        sListProjectionMap.put(Lists.DESCRIPTION, Lists.DESCRIPTION);
        sListProjectionMap.put(Lists.TYPE, Lists.TYPE);
        sListProjectionMap.put(Lists.SUB_TYPE, Lists.SUB_TYPE);
        sListProjectionMap.put(Lists.OWNER_NICKNAME, Lists.OWNER_NICKNAME);
        sListProjectionMap.put(Lists.SORT_BY_DONE, Lists.SORT_BY_DONE);
        sListProjectionMap.put(Lists.SORT_TYPE, Lists.SORT_TYPE);
        sListProjectionMap.put(Lists.SORT_DIRECTION, Lists.SORT_DIRECTION);
        sListProjectionMap.put(Lists.CREATED, Lists.CREATED);
        sListProjectionMap.put(Lists.MODIFIED, Lists.MODIFIED);
        
        sToDoProjectionMap = new HashMap<String,String>();
        sToDoProjectionMap.put(ToDo._ID, ToDo._ID);
        sToDoProjectionMap.put(ToDo.GUID, ToDo.GUID);
        sToDoProjectionMap.put(ToDo.DESCRIPTION, ToDo.DESCRIPTION);
        sToDoProjectionMap.put(ToDo.PRIORITY, ToDo.PRIORITY);
        sToDoProjectionMap.put(ToDo.REMINDER, ToDo.REMINDER);
        sToDoProjectionMap.put(ToDo.DONE, ToDo.DONE);
        sToDoProjectionMap.put(ToDo.ORDINAL, ToDo.ORDINAL);
        sToDoProjectionMap.put(ToDo.NOTE, ToDo.NOTE);
        sToDoProjectionMap.put(ToDo.CREATED, ToDo.CREATED);
        sToDoProjectionMap.put(ToDo.MODIFIED, ToDo.MODIFIED);
	}
	private SmartListDatabaseHelper mDbHelper;
	
	@Override
	public int delete(Uri uri, String where, String[] whereArgs) {
		// TODO Auto-generated method stub
		final SQLiteDatabase db = mDbHelper.getWritableDatabase();
		String table;
		Uri contentUri;
		int count;
		
		int match = sURLMatcher.match(uri);
		switch (match){
		case PRODUCT:
			table = TABLE_PRODUCT;
			contentUri = Product.CONTENT_URI;
			count = db.delete(table, where, whereArgs);
			break;
		case LIST:
			table = TABLE_LIST;
			contentUri = Lists.CONTENT_URI;
			count = db.delete(table, where, whereArgs);
			break;
        case PRODUCT_ID:
        	table = TABLE_PRODUCT;
            String produtId = uri.getPathSegments().get(1);
            count = db.delete(table, Product._ID + "=" + produtId
                    + (!TextUtils.isEmpty(where) ? " AND (" + where + ')' : ""), whereArgs);
            break;
        default:
		    throw new IllegalArgumentException("Unknown URI " + uri);		
		}
		
        getContext().getContentResolver().notifyChange(uri, null);
        
        return count;
	}

	@Override
	public String getType(Uri uri) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Uri insert(Uri uri, ContentValues initialValues) {
		// TODO Auto-generated method stub
		final int match = sURLMatcher.match(uri);
		String table;
		Uri contentUri;
		
        ContentValues values;
        if (initialValues != null) {
            values = new ContentValues(initialValues);
        } else {
            values = new ContentValues();
        }
        
        switch (match){
		case PRODUCT:
			table = TABLE_PRODUCT;
			contentUri = Product.CONTENT_URI;
			break;
		case LIST:
			table = TABLE_LIST;
			contentUri = Lists.CONTENT_URI;
			break;	
        default:
		    throw new IllegalArgumentException("Unknown URI " + uri);
        }
        
		final SQLiteDatabase db = mDbHelper.getWritableDatabase();
        
		long rowId = db.insert(table, null, values);
        
        if (rowId > 0) {
            Uri appendUri = ContentUris.withAppendedId(contentUri, rowId);
            getContext().getContentResolver().notifyChange(appendUri, null);
            return appendUri;
        }		
		
        throw new SQLException("Failed to insert row into " + uri);
		//return null;
	}

	@Override
	public boolean onCreate() {
		// TODO Auto-generated method stub
		mDbHelper = SmartListDatabaseHelper.getInstance(getContext());
		return true;
	}

	@Override
	public Cursor query(Uri uri, String[] projection, String selection,
			String[] selectionArgs, String sortOrder) {
		// TODO Auto-generated method stub
		SQLiteQueryBuilder qb = new SQLiteQueryBuilder();
		int match = sURLMatcher.match(uri);
		switch (match){
		case PRODUCT:
			qb.setTables(TABLE_PRODUCT);
			qb.setProjectionMap(sProductProjectionMap);
			break;
        case PRODUCT_ID:
        	qb.setTables(TABLE_PRODUCT);
            qb.setProjectionMap(sProductProjectionMap);
            qb.appendWhere(Product._ID + "=" + uri.getPathSegments().get(1));
            break;			
		case LIST:
			qb.setTables(TABLE_LIST);
			qb.setProjectionMap(sListProjectionMap);
			break;
		case TODO:
			qb.setTables(TABLE_TODO);
			qb.setProjectionMap(sToDoProjectionMap);
			break;		
		default:
		    throw new IllegalArgumentException("Unknown URI " + uri);
		}
        SQLiteDatabase db = mDbHelper.getReadableDatabase();
        Cursor c = qb.query(db, projection, selection, selectionArgs, null, null, sortOrder);
        if (c != null) {
        	c.setNotificationUri(getContext().getContentResolver(), uri);
        }
        
		return c;
	}

	@Override
	public int update(Uri uri, ContentValues values, String where,
			String[] whereArgs) {
		// TODO Auto-generated method stub
		String table;
		SQLiteDatabase db = mDbHelper.getWritableDatabase();
		int count;
		
		switch (sURLMatcher.match(uri)){
		case PRODUCT:
			table = TABLE_PRODUCT;
			count = db.update(table,values,where, whereArgs);
			break;
        case PRODUCT_ID:
            String productId = uri.getPathSegments().get(1);
            table = TABLE_PRODUCT;
            count = db.update(table, values, Product._ID + "=" + productId
                    + (!TextUtils.isEmpty(where) ? " AND (" + where + ')' : ""), whereArgs);
            break;
		case LIST:
			table = TABLE_LIST;
			count = db.update(table,values,where, whereArgs);
			break;		
		default:
		    throw new IllegalArgumentException("Unknown URI " + uri);
		}
		
		getContext().getContentResolver().notifyChange(uri, null);
        
        return count;
	}

}
