package com.njpalmin.smartlist.activity;


import java.util.List;

import javax.xml.parsers.SAXParser;
import javax.xml.parsers.SAXParserFactory;

import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.DefaultHttpClient;
import org.xml.sax.InputSource;
import org.xml.sax.XMLReader;

import android.app.ListActivity;
import android.content.ContentResolver;
import android.content.ContentUris;
import android.content.ContentValues;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Color;
import android.graphics.Typeface;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.google.zxing.client.android.CaptureActivity;
import com.njpalmin.smartlist.R;
import com.njpalmin.smartlist.record.ActiveRecord.Lists;
import com.njpalmin.smartlist.record.ActiveRecord.Product;
import com.njpalmin.smartlist.ui.ActionItem;
import com.njpalmin.smartlist.ui.FooterView;
import com.njpalmin.smartlist.ui.HeaderView;
import com.njpalmin.smartlist.ui.QuickAction;
import com.njpalmin.smartlist.ui.SortableListView;
import com.njpalmin.smartlist.utils.SyncDataHandler;
import com.njpalmin.smartlist.utils.Utils;


public class ShoppingListActivity extends ListActivity{
	final private static String TAG="ShoppingListActivity";
	private HeaderView headerView;
	private FooterView footerView;
	private SortableListView mShoppingListView;
	private List<String> list;
	private ContentResolver mContentResolver;
	private ShoppingListAdapter mShoppingListAdapter = null;
	private QuickAction mQuickAction ;
	private Cursor mCursor;
	private Uri mUri;
	private int mDone;
	
	private static final String[] LISTS_PROJECTION = new String[] {
		Lists._ID,
		Lists.GUID,
		Lists.DESCRIPTION, 
		Lists.TYPE,
		Lists.SUB_TYPE,
		Lists.OWNER_NICKNAME,
		Lists.SORT_BY_DONE,
		Lists.SORT_TYPE,
		Lists.SORT_DIRECTION,
		Lists.CREATED,
		Lists.MODIFIED,
	};
	
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
        setContentView(R.layout.shopping_list_activity);
        
        mContentResolver = getContentResolver();
        Intent intent = getIntent();
        if (intent.getData() == null) {
            intent.setData(Product.CONTENT_URI);
            Log.d(TAG,"run here");
        }
        initView();
    }
	
	private void initView() {	
		headerView = (HeaderView)findViewById(R.id.header);
		headerView.setBackgroundResource(R.drawable.bg_header);
		headerView.setPadding(6, 12, 6, 12);
		
		LinearLayout titleLayout = headerView.getTitleLayout();
        headerView.getTitleBtn().setText("Shopping list >");
        Button button = headerView.getTitleBtn();
        button.setTextColor(Color.WHITE);
        button.setTextSize(20);
        button.setTypeface(null, Typeface.BOLD);
        ImageButton addBtn = headerView.getAddBtn();
        addBtn.setBackgroundResource(R.drawable.btn_add);
		addBtn.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				headerView.getInputView().setVisibility(0);
				headerView.getTitleLayout().setVisibility(8);
			}
		});
		ImageButton scanBtn = headerView.getScanBtn();
        scanBtn.setBackgroundResource(R.drawable.btn_scan);
        scanBtn.setOnClickListener(new View.OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				Intent intent = new Intent(ShoppingListActivity.this, CaptureActivity.class);
				startActivityForResult(intent, 0);
			}
		});
        headerView.getInputView().getOptionBtn().setOnClickListener(new View.OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				Intent intent = new Intent(ShoppingListActivity.this, ProductDetailsActivity.class);
				startActivityForResult(intent, 0);
			}
		});
        
        
        footerView = (FooterView)findViewById(R.id.footer);
        footerView.setBackgroundResource(R.drawable.bg_footer);
        TextView text1 = footerView.getTextOne();
        text1.setText( getResources().getString(R.string.remaining) +" $0.00");
        text1.setTextColor(Color.WHITE);
        text1.setTextSize(18);
        text1.setTypeface(null, Typeface.BOLD);
        TextView text2 = footerView.getTextTwo();
        text2.setTextColor(Color.WHITE);
        text2.setTextSize(18);
        text2.setTypeface(null, Typeface.BOLD);
        footerView.useTextView();

        mShoppingListView = (SortableListView)getListView();
        mShoppingListView.setBackgroundResource(R.drawable.bg_list);
        /*
        initData();
        listView.setAdapter(new ShoppingAdapter(this, list));*/
        fillData();
        
    	//Add action item
        ActionItem markAction = new ActionItem();
		
        markAction.setTitle( getResources().getString(R.string.done));
        markAction.setIcon(getResources().getDrawable(R.drawable.ic_quickaction_mark));

		//Accept action item
		ActionItem deleteAction = new ActionItem();
		
		deleteAction.setTitle(getResources().getString(R.string.delete));
		deleteAction.setIcon(getResources().getDrawable(R.drawable.ic_quickaction_delete));
		
		//Upload action item
		ActionItem editAction = new ActionItem();
		
		editAction.setTitle(getResources().getString(R.string.edit));
		editAction.setIcon(getResources().getDrawable(R.drawable.ic_quickaction_edit));
		
		mQuickAction = new QuickAction(this);
		
		mQuickAction.addActionItem(markAction);
		mQuickAction.addActionItem(deleteAction);
		mQuickAction.addActionItem(editAction);
		
		mQuickAction.setOnActionItemClickListener(new QuickAction.OnActionItemClickListener() {	
			
			@Override
			public void onItemClick(int pos) {
				if (pos == 0) { //Mark item selected
					//int productId = mCursor.getInt(mCursor.getColumnIndex(Product._ID));
					ContentValues values = new ContentValues();
					if(mDone == 1){
						values.put(Product.DONE,"0");
						mDone = 0;
					}else{
						values.put(Product.DONE,"1");
						mDone = 1;
					}
					mContentResolver.update(Product.CONTENT_URI,values,null,null);	
				} else if (pos == 1) { //Delete item selected
					mContentResolver.delete(mUri,null,null);
				} else if (pos == 2) { //Edit item selected
					//Toast.makeText(ShoppingListActivity.this, mUri.toString(), Toast.LENGTH_SHORT).show();
					Log.d(TAG,"mUri: Pos "+mUri);
					startActivity(new Intent(Utils.INTENT_ACTION_EDIT, mUri));
					//startActivity(new Intent(Utils.INTENT_ACTION_EDIT));
					/*
					int productId = mCursor.getInt(mCursor.getColumnIndex(Product._ID));
					Intent intent = new Intent(ShoppingListActivity.this, ProductDetailsActivity.class);
					intent.setAction("EditProduct");
					intent.putExtra("ProductId",productId);*/
				}	
			}
		});
		
        mShoppingListView.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
				// TODO Auto-generated method stub
				
		        mUri = ContentUris.withAppendedId(getIntent().getData(), id);
		        Log.d(TAG,"mUri:"+mUri);
				mQuickAction.show(view);
			}
        });
	}

	public boolean onKeyDown(int keyCode, KeyEvent event) {
		if (keyCode == KeyEvent.KEYCODE_BACK 
				 && headerView.getTitleLayout().getVisibility() == 8) {
			headerView.getInputView().setVisibility(8);
			headerView.getTitleLayout().setVisibility(0);	
			return true;
		}
		
		return super.onKeyDown(keyCode, event);
	}
	
	@Override
	public void onActivityResult(int requestCode, int resultCode, Intent data) {
		if (resultCode != 0) {
			return;
		}
		
		HttpResponse rp = null;
		String result = null;
		
		if (data == null){
			return ;
		}
		
		Log.d(TAG,"SCAN RESULT = "+ data.getStringExtra("scanresult"));
		
		try {
			HttpClient httpClient = new DefaultHttpClient();
			String httpUrl = "http://www.luciabistro.cl/web_service.php?b=" 
								+ data.getStringExtra("scanresult");
			HttpGet get = new HttpGet(httpUrl);
//			HttpGet get = new HttpGet("http://www.luciabistro.cl/web_service.php?b=0075678164125");
			rp = httpClient.execute(get);
		
			if (rp != null) {
				SAXParserFactory saxParserFactory = SAXParserFactory.newInstance();
				SAXParser saxParser = saxParserFactory.newSAXParser();
		
				/* Get the XMLReader of the SAXParser we created. */
				XMLReader syncReader = saxParser.getXMLReader();
				/* Create a new ContentHandler and apply it to the XML-Reader */
				SyncDataHandler syncHandler = new SyncDataHandler();
				syncReader.setContentHandler(syncHandler);
		
				/* Parse the xml-data from our URL. */
				// xr.parse(new InputSource(getAssets().open("inbox.xml")));
				syncReader.parse(new InputSource(rp.getEntity().getContent()));
				result = syncHandler.getProductName();
				/* Parsing has finished. */
			}
	
			if(result != null){
				Intent intent = new Intent(ShoppingListActivity.this, ProductDetailsActivity.class);
				//intent.putExtra("description", builder.toString());
				intent.putExtra("description", result);
				//startActivityForResult(intent, 1);
				startActivity(intent);
			}
		} catch (Exception e) {
			
		}
	}

	
	private void fillData(){
		String where = Product.LIST_ID + "=?";
		//Cursor cursor = mContentResolver.query(Lists.CONTENT_URI,LISTS_PROJECTION,where,new String[]{"SHOPPINGLIST"},null);
		Cursor cursor = mContentResolver.query(Product.CONTENT_URI,PRODUCT_PROJECTION,where,new String[]{"1"},null);
		mShoppingListAdapter = new ShoppingListAdapter(this,cursor, true);
		mShoppingListView.setAdapter(mShoppingListAdapter);
	}
	
	private Uri getUri(){
		return mUri;
	}
}
