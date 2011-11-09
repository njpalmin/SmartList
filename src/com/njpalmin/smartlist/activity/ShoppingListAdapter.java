package com.njpalmin.smartlist.activity;

import android.content.Context;
import android.database.Cursor;
import android.graphics.Paint;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CursorAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.njpalmin.smartlist.R;
import com.njpalmin.smartlist.record.ActiveRecord.Product;

public class ShoppingListAdapter extends CursorAdapter {
	private Context mContext;
	private LayoutInflater mInflater = null;
	
	public ShoppingListAdapter(Context context, Cursor c, boolean autoRequery) {
        super(context, c, autoRequery);
        mContext = context;
        mInflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
    }

	@Override
	public void bindView(View view, Context context, Cursor cursor) {
		// TODO Auto-generated method stub
		ViewHolder holder = (ViewHolder) view.getTag();
		holder.mDescription.setText(cursor.getString(cursor.getColumnIndex(Product.DESCRIPTION)));
		if(cursor.getInt(cursor.getColumnIndex(Product.DONE)) == 1){
			holder.mDescription.getPaint().setFlags(Paint.STRIKE_THRU_TEXT_FLAG );
		}else {
			holder.mDescription.getPaint().setFlags(Paint.DEV_KERN_TEXT_FLAG );
		}
		
		String quantity = cursor.getString(cursor.getColumnIndex(Product.QUANTITY))+ " @ $" +
						  cursor.getString(cursor.getColumnIndex(Product.PRICE));
		
		holder.mQuantity.setText(quantity);
	}

	@Override
	public View newView(Context context, Cursor cursor, ViewGroup parent) {
		// TODO Auto-generated method stub
		View view = mInflater.inflate(R.layout.list_item, parent, false);
		ViewHolder holder = new ViewHolder();
		holder.mDescription = (TextView)view.findViewById(R.id.description);
		holder.mQuantity = (TextView)view.findViewById(R.id.quantity);
		
		view.setTag(holder);
		return view;
	}

	public static class ViewHolder{
		TextView mDescription;
		TextView mQuantity;
		ImageView mNote;
		ImageView mCoupon;
	}
}
