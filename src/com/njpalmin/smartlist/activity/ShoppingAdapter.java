package com.njpalmin.smartlist.activity;

import java.util.List;

import android.content.Context;
import android.database.Cursor;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.CursorAdapter;
import android.widget.TextView;

import com.njpalmin.smartlist.R;

//public class ShoppingAdapter extends CursorAdapter {
public class ShoppingAdapter extends ArrayAdapter<String>	{
	Context mContext;
	public ShoppingAdapter(Context context, List<String> objects) {
		super(context, 0, objects);
		
		mContext = context;
	}
	
	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		View view = convertView;
		if (view == null) {
			view = LayoutInflater.from(mContext)
						.inflate(R.layout.list_item, null);
		}
		
		//TextView textView = (TextView)view.findViewById(R.id.drag_list_item_text);
		//textView.setText(getItem(position));
		
		return view;
	}
	
//	public ShoppingAdapter(Context context, Cursor c, boolean autoRequery) {
//        super(context, c, autoRequery);
//    }
//
//	@Override
//	public void bindView(View arg0, Context arg1, Cursor arg2) {
//		// TODO Auto-generated method stub
//
//	}
//
//	@Override
//	public View newView(Context context, Cursor cursor, ViewGroup parent) {
//		// TODO Auto-generated method stub
//		LayoutInflater mInflater = LayoutInflater.from(context);
//		return mInflater.inflate(R.layout.list_item, null);
//	}

}
