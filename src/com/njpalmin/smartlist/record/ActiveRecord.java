package com.njpalmin.smartlist.record;

import android.net.Uri;
import android.provider.BaseColumns;

public final class ActiveRecord {
	public static final String AUTHORITY = "com.njpalmin.smartlist.provider";
	
	private ActiveRecord(){}
	
	public static final class Product implements BaseColumns{
		private Product(){}

		public static final Uri CONTENT_URI = Uri.parse("content://" + AUTHORITY + "/products");
		
        public static final String CONTENT_TYPE = "vnd.android.cursor.dir/product";
        public static final String CONTENT_ITEM_TYPE = "vnd.android.cursor.item/product";
        
        public static final String GUID = "guid";
		public static final String LIST_ID = "list_id";
		public static final String DESCRIPTION = "description";
		public static final String DESCRIPTION_SHORT = "description_short";
		public static final String UPC = "upc";
		public static final String QUANTITY = "quantity";
		public static final String UNIT = "unit";
		public static final String PRICE = "price";
		public static final String TAX_FREE = "tax_free";
		public static final String DONE = "done";
		public static final String ORDINAL = "ordinal";
		public static final String NOTE = "note";
		public static final String HAS_COUPON = "has_coupon";
		public static final String COUPON_AMOUNT = "coupon_amount";
		public static final String COUPON_TYPE = "coupon_type";
		public static final String COUPON_NOTE = "coupon_note";
		public static final String CREATED = "created";
		public static final String MODIFIED = "modified";
		
	}
	
	public static final class Lists implements BaseColumns{
		private Lists(){}

		public static final Uri CONTENT_URI = Uri.parse("content://" + AUTHORITY + "/lists");
		
		public static final String GUID = "guid";
		public static final String DESCRIPTION = "description";
		public static final String TYPE = "type";
		public static final String SUB_TYPE = "subtype";
		public static final String OWNER_NICKNAME = "owner_nickname";
		public static final String SORT_BY_DONE = "sort_by_done";
		public static final String SORT_TYPE= "sort_type";
		public static final String SORT_DIRECTION= "sort_direction";
		public static final String CREATED = "created";
		public static final String MODIFIED = "modified";
	}
	
	public static final class ToDo implements BaseColumns{
		private ToDo(){}

		public static final Uri CONTENT_URI = Uri.parse("content://" + AUTHORITY + "/todos");
		
		public static final String GUID = "guid";
		public static final String LIST_ID = "list_id";
		public static final String DESCRIPTION = "description";
		public static final String PRIORITY = "priority";
		public static final String REMINDER = "reminder";
		public static final String DONE = "done";
		public static final String ORDINAL = "ordinal";
		public static final String NOTE = "note";
		public static final String CREATED = "created";
		public static final String MODIFIED = "modified";
	}
}
