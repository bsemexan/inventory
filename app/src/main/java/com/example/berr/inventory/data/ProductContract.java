package com.example.berr.inventory.data;

import android.content.ContentResolver;
import android.net.Uri;
import android.provider.BaseColumns;


public final class ProductContract {

    public static final String CONTENT_AUTHORITY = "com.example.berr.inventory";
    public static final Uri BASE_CONTENT_URI = Uri.parse("content://" + CONTENT_AUTHORITY);
    public static final String PATH_PRODUCT = "products";

    public static class ProductEntry implements BaseColumns {

        public static final Uri CONTENT_URI = Uri.withAppendedPath(BASE_CONTENT_URI, PATH_PRODUCT);

        public static final String CONTENT_LIST_TYPE = ContentResolver.CURSOR_DIR_BASE_TYPE + "/"
                + CONTENT_AUTHORITY + "/" + PATH_PRODUCT;
        public static final String CONTENT_ITEM_TYPE = ContentResolver.CURSOR_ITEM_BASE_TYPE + "/"
                + CONTENT_AUTHORITY + "/" + PATH_PRODUCT;

        public static final String TABLE_NAME = "products";
        public static final String _ID = BaseColumns._ID;
        public static final String COLUMN_PRODUCT_IMAGE_URI = "image";
        public static final String COLUMN_PRODUCT_NAME = "name";
        public static final String COLUMN_PRODUCT_QUALITY = "quality";
        public static final String COLUMN_PRODUCT_PRICE = "price";
        public static final String COLUMN_SUPPLIER_EMAIL = "email";

    }

}
