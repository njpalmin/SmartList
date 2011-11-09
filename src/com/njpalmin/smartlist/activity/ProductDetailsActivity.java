package com.njpalmin.smartlist.activity;

import android.app.Activity;
import android.content.ContentResolver;
import android.content.ContentValues;
import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.text.format.Time;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;

import com.njpalmin.smartlist.R;
import com.njpalmin.smartlist.record.ActiveRecord.Product;
import com.njpalmin.smartlist.utils.Utils;


public class ProductDetailsActivity extends Activity {
	final private static String TAG="ProductDetailsActivity";
    private static final int STATE_EDIT = 0;
    private static final int STATE_INSERT = 1;

	private ContentResolver mContentResolver;
	private Button mSaveButton;
	private Button mCancelButton;
	private Cursor mCursor;
	private boolean mEdit;

    private int mState;
    private Uri mUri;
	
	private static final String[] PRODUCT_PROJECTION = new String[] {
		Product._ID,           
		Product.GUID,        
		Product.LIST_ID,  
		Product.DESCRIPTION,
		Product.DESCRIPTION_SHORT, 
		Product.UPC,          
		Product.QUANTITY, 
		Product.UNIT,        
		Product.PRICE,       
		Product.TAX_FREE, 
		Product.DONE,         
		Product.ORDINAL,  
		Product.NOTE,         
		Product.HAS_COUPON, 
		Product.COUPON_AMOUNT,
		Product.COUPON_TYPE,
		Product.COUPON_NOTE,
		Product.CREATED,   
		Product.MODIFIED,
	};	
	
	@Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        
        final Intent intent = getIntent();
        final String action = intent.getAction();
        
        Log.d(TAG,"action="+action);
        
        if (Utils.INTENT_ACTION_EDIT.equals(action)) {
            // Requested to edit: set that state, and the data being edited.
            mState = STATE_EDIT;
            mUri = intent.getData();
            Log.d(TAG,"mUri:"+mUri);
        } 

        
        setContentView(R.layout.product_details_activity);
        
        Window window = getWindow();
        WindowManager.LayoutParams layoutParams = window.getAttributes();
        layoutParams.width = WindowManager.LayoutParams.MATCH_PARENT;
        getWindow().setAttributes(layoutParams);
        
        mContentResolver = getContentResolver();
        if(mUri != null){
        	mCursor = mContentResolver.query(mUri, PRODUCT_PROJECTION, null, null, null);
        }

        initView();
        
        handleIntentData(getIntent());
    }
	
	private void handleIntentData(Intent intent) {
		if (intent != null) {
			String description = intent.getStringExtra("description");
			((EditText)findViewById(R.id.description)).setText(description);
		
	/*	
			if(intent.getAction().equals("EditProduct")){
				mEdit = true;
				int productId = intent.getIntExtra("ProductId",0);
				mCursor = getContentResolver().query(Product.CONTENT_URI,PRODUCT_PROJECTION, Product._ID+"=?", 
												   new String[]{String.valueOf(productId)},null);
				
				if(mCursor != null && mCursor.getCount() !=0){
					mCursor.moveToFirst();
					((EditText)findViewById(R.id.description)).setText(mCursor.getString(mCursor.getColumnIndex(Product.DESCRIPTION)));
					((EditText)findViewById(R.id.price)).setText(mCursor.getString(mCursor.getColumnIndex(Product.PRICE)));
					((EditText)findViewById(R.id.coupon_amount)).setText(mCursor.getString(mCursor.getColumnIndex(Product.COUPON_AMOUNT)));
					((EditText)findViewById(R.id.coupon_note)).setText(mCursor.getString(mCursor.getColumnIndex(Product.COUPON_NOTE)));
					((EditText)findViewById(R.id.note)).setText(mCursor.getString(mCursor.getColumnIndex(Product.NOTE)));
					((CheckBox)findViewById(R.id.tax_free)).setChecked((mCursor.getInt(mCursor.getColumnIndex(Product.TAX_FREE))) == 1 ? true:false);
					((CheckBox)findViewById(R.id.has_coupon)).setChecked((mCursor.getInt(mCursor.getColumnIndex(Product.HAS_COUPON))) == 1 ? true:false);
				}
			}*/
		}
	}
	
	private void initView(){
		mSaveButton = (Button)findViewById(R.id.save);
		mSaveButton.setOnClickListener(saveButtonOnClickListener);

		mCancelButton = (Button)findViewById(R.id.cancel);
		mCancelButton.setOnClickListener(new View.OnClickListener(){
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				finish();
			}
			
		});
		
		if(mCursor != null ){
			mCursor.moveToFirst();
			//Log.d(TAG,"Description = "+mCursor.getColumnIndex(Product.DESCRIPTION)));
			((EditText)findViewById(R.id.description)).setText(mCursor.getString(mCursor.getColumnIndex(Product.DESCRIPTION)));
			((EditText)findViewById(R.id.price)).setText(mCursor.getString(mCursor.getColumnIndex(Product.PRICE)));
			((EditText)findViewById(R.id.coupon_amount)).setText(mCursor.getString(mCursor.getColumnIndex(Product.COUPON_AMOUNT)));
			((EditText)findViewById(R.id.coupon_note)).setText(mCursor.getString(mCursor.getColumnIndex(Product.COUPON_NOTE)));
			((EditText)findViewById(R.id.note)).setText(mCursor.getString(mCursor.getColumnIndex(Product.NOTE)));
			((CheckBox)findViewById(R.id.tax_free)).setChecked((mCursor.getInt(mCursor.getColumnIndex(Product.TAX_FREE))) == 1 ? true:false);
			((CheckBox)findViewById(R.id.has_coupon)).setChecked((mCursor.getInt(mCursor.getColumnIndex(Product.HAS_COUPON))) == 1 ? true:false);
		}
	}
	
	private OnClickListener saveButtonOnClickListener = new OnClickListener(){

		@Override
		public void onClick(View view) {
			// TODO Auto-generated method stub
			EditText description = (EditText)findViewById(R.id.description);
			EditText quantity = (EditText)findViewById(R.id.number_text);
			EditText price = (EditText)findViewById(R.id.price);
			CheckBox taxFree = (CheckBox)findViewById(R.id.tax_free);
			CheckBox hasCoupon = (CheckBox)findViewById(R.id.has_coupon);
			EditText couponAmount = (EditText)findViewById(R.id.coupon_amount);
			EditText couponNote = (EditText)findViewById(R.id.coupon_note);
			EditText note = (EditText)findViewById(R.id.note);
			
			ContentValues values = new ContentValues();
			values.put(Product.DESCRIPTION,description.getText().toString());
			values.put(Product.LIST_ID,"1");
			values.put(Product.QUANTITY,quantity.getText().toString());
			values.put(Product.PRICE,price.getText().toString());
			values.put(Product.TAX_FREE, taxFree.isChecked()? "1":"0");
			values.put(Product.HAS_COUPON, hasCoupon.isChecked()? "1":"0");
			values.put(Product.COUPON_AMOUNT,couponAmount.getText().toString());
			values.put(Product.COUPON_NOTE,couponNote.getText().toString());
			values.put(Product.NOTE, note.getText().toString());
			values.put(Product.DONE, "0");
			
			long now = System.currentTimeMillis();
			Time time = new Time();
			time.set(now);
			time.normalize(true);
			values.put(Product.CREATED, time.format("YYMMDD:HH:MM:SS"));
			values.put(Product.MODIFIED, time.format("YYMMDD:HH:MM:SS"));
			
			mContentResolver.insert(Product.CONTENT_URI,values);
			finish();
		}
		
	};
}
