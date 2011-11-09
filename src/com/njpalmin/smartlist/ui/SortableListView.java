package com.njpalmin.smartlist.ui;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.PixelFormat;
import android.util.AttributeSet;
import android.view.Gravity;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewConfiguration;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.ListView;

import com.njpalmin.smartlist.R;
import com.njpalmin.smartlist.activity.ShoppingAdapter;

public class SortableListView extends ListView
{
	private ImageView dragImageView;
	private int dragSrcPosition;
	private int dragPosition;
	
	private int dragPoint;
	private int dragOffset;
	
	private WindowManager windowManager;
	private WindowManager.LayoutParams windowParams;
	
	private int scaledTouchSlop;
	private int upScrollBounce;
	private int downScrollBounce;
	
	public SortableListView(Context context, AttributeSet paramAttributeSet)
	{
		super(context, paramAttributeSet);
		scaledTouchSlop = ViewConfiguration.get(context).getScaledTouchSlop();
	}
	
	@Override
	public boolean onInterceptTouchEvent(MotionEvent ev) {
		if (ev.getAction() == MotionEvent.ACTION_DOWN) {
			int x = (int)ev.getX();
			int y = (int)ev.getY();
			dragSrcPosition = dragPosition = pointToPosition(x, y);
			if (dragPosition == AdapterView.INVALID_POSITION) {
				return super.onInterceptTouchEvent(ev);
			}
			
			ViewGroup itemView = 
				(ViewGroup)getChildAt(dragPosition - getFirstVisiblePosition());
			dragPoint = y - itemView.getTop();
			dragOffset = (int)(ev.getRawY() - y);
			/*View dragger = itemView.findViewById(R.id.drag_list_item_image);
			if (dragger != null && (x > dragger.getLeft() - 20)) {
				upScrollBounce = Math.min(y - scaledTouchSlop, getHeight()/3);
				downScrollBounce = Math.max(y +  scaledTouchSlop, getHeight() * 2 /3);
				
				itemView.setDrawingCacheEnabled(true);
				Bitmap bm = Bitmap.createBitmap(itemView.getDrawingCache());
				
				startDrag(bm, y);
			}*/
			return false;
		}
		
		return super.onInterceptTouchEvent(ev);
	}
	
	@Override
	public boolean onTouchEvent(MotionEvent ev) {
		if (dragImageView != null && dragPosition != INVALID_POSITION) {
			int action = ev.getAction();
			switch (action) {
			case MotionEvent.ACTION_UP:
				int upY = (int)ev.getY();
				stopDrag();
				onDrop(upY);
				break;
			case MotionEvent.ACTION_MOVE:
				int movY = (int)ev.getY();
				onDrag(movY);
				break;
			default:
				break;
			}
			return true;
		}
		
		return super.onTouchEvent(ev);
	}
	
	public void onDrag(int y) {
		if (dragImageView != null) {
			//windowParams.alpha = 0.6f;
			windowParams.y = y - dragPoint + dragOffset;
			windowManager.updateViewLayout(dragImageView, windowParams);
		}
		
		int tempPosition = pointToPosition(0, y);
		if (tempPosition != INVALID_POSITION) {
			dragPosition = tempPosition;
		}
		
		int scrollHeight = 0;
		if (y < upScrollBounce) {
			scrollHeight = 8;
		} else if (y > downScrollBounce) {
			scrollHeight = -8;
		}
		
		if (scrollHeight != 0) {
			setSelectionFromTop(dragPosition,
					getChildAt(dragPosition - getFirstVisiblePosition()).getTop() 
						+ scrollHeight);
		}
	}
	
	public void onDrop(int y) {
		int tempPosition = pointToPosition(0, y);
		if (tempPosition != INVALID_POSITION) {
			dragPosition = tempPosition;
		}
		
		if (y < getChildAt(0).getTop()) {
			dragPosition = 0;
		} else if (y > getChildAt(getChildCount() - 1).getBottom()) {
			//dragPosition = -1; // discard drop if out of bottom range 
			dragPosition =  getAdapter().getCount() - 1;
		}
		
		if (dragPosition >= 0 && dragPosition < getAdapter().getCount()) {
			ShoppingAdapter adapter = (ShoppingAdapter)getAdapter();
			String dragItem = adapter.getItem(dragSrcPosition);
			adapter.remove(dragItem);
			adapter.insert(dragItem, dragPosition);
		}
	}
	
	public void startDrag(Bitmap bm, int y) {
		stopDrag();
		
		windowParams = new WindowManager.LayoutParams();
		windowParams.gravity = Gravity.TOP;
		windowParams.x = 0;
		windowParams.y = y - dragPoint + dragOffset;
		windowParams.width = WindowManager.LayoutParams.WRAP_CONTENT;
		windowParams.height = WindowManager.LayoutParams.WRAP_CONTENT;
		windowParams.flags = WindowManager.LayoutParams.FLAG_NOT_FOCUSABLE |
							WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE |
							WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON |
							WindowManager.LayoutParams.FLAG_LAYOUT_IN_SCREEN;
		windowParams.format = PixelFormat.TRANSLUCENT;
		windowParams.windowAnimations = 0;
		
		ImageView imageView = new ImageView(getContext());
		imageView.setImageBitmap(bm);
		windowManager = (WindowManager)getContext().getSystemService("window");
		windowManager.addView(imageView, windowParams);
		
		dragImageView = imageView;
	}
	
	public void stopDrag() {
		if (dragImageView != null) {
			windowManager.removeView(dragImageView);
			dragImageView = null;
		}
	}
}

