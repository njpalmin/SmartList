package com.njpalmin.smartlist;

import com.google.zxing.client.android.CaptureActivity;
import com.njpalmin.smartlist.activity.ShoppingListActivity;

import android.app.Activity;
import android.content.ContentResolver;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageButton;

public class MainActivity extends Activity {
    /** Called when the activity is first created. */
	private ImageButton mShoppingList;
	private ContentResolver mContentResolver;
	
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main_activity);
        mContentResolver = getContentResolver();
        
        initView();
    }
    
    private void initView(){
    	mShoppingList = (ImageButton)findViewById(R.id.shopping_list);
    	mShoppingList.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				//Intent intent = new Intent(MainActivity.this, CaptureActivity.class);
				Intent intent = new Intent(MainActivity.this, ShoppingListActivity.class);
				startActivity(intent);
			}
    		
    	});
    }
}