package com.example.berr.inventory.data;

import android.content.ContentProvider;
import android.content.ContentUris;
import android.content.ContentValues;
import android.content.UriMatcher;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.net.Uri;
import android.util.Log;

import com.example.berr.inventory.data.ProductContract.ProductEntry;

public class ProductProvider extends ContentProvider {

    public static final String LOG_TAG = ProductProvider.class.getSimpleName();
    private static final int PRODUCT = 100;
    private static final int PRODUCT_ID = 101;
    private ProductDbHelper productDbHelper;

    private static final UriMatcher uriMatcher = new UriMatcher(UriMatcher.NO_MATCH);

    static {
        uriMatcher.addURI(ProductContract.CONTENT_AUTHORITY, ProductContract.PATH_PRODUCT, PRODUCT);
        uriMatcher.addURI(ProductContract.CONTENT_AUTHORITY, ProductContract.PATH_PRODUCT + "/#", PRODUCT_ID);
    }

    @Override
    public boolean onCreate() {
        productDbHelper = new ProductDbHelper(getContext());
        return false;
    }

    @Override
    public Cursor query(Uri uri, String[] projection, String selection, String[] selectionArgs, String sortOrder) {
        SQLiteDatabase db = productDbHelper.getReadableDatabase();
        Cursor cursor;
        int match = uriMatcher.match(uri);
        switch (match) {
            case PRODUCT:
                cursor = db.query(ProductEntry.TABLE_NAME, projection, selection, selectionArgs, null, null, sortOrder);
                break;
            case PRODUCT_ID:
                selection = ProductEntry._ID + "=?";
                selectionArgs = new String[]{uri.getLastPathSegment()};
                cursor = db.query(ProductEntry.TABLE_NAME, projection, selection, selectionArgs, null, null, sortOrder);
                break;
            default:
                throw new IllegalArgumentException("Can't query unknown URI" + uri);
        }
        cursor.setNotificationUri(getContext().getContentResolver(), uri);
        return cursor;
    }

    @Override
    public Uri insert( Uri uri, ContentValues values) {
        int match = uriMatcher.match(uri);
        switch (match) {
            case PRODUCT:
                return insertProduct(uri, values);
            default:
                throw new IllegalArgumentException("Insertion is not supported for " + uri);
        }
    }

    private Uri insertProduct(Uri uri, ContentValues values) {
        String name = values.getAsString(ProductEntry.COLUMN_PRODUCT_NAME);
        if (name == null) {
            throw new IllegalArgumentException("Insertion is not supported for " + uri);
        }

        String imageUri = values.getAsString(ProductEntry.COLUMN_PRODUCT_IMAGE_URI);
        if (imageUri == null) {
            throw new IllegalArgumentException("Insertion is not supported for " + uri);
        }

        Integer quality = values.getAsInteger(ProductEntry.COLUMN_PRODUCT_QUALITY);
        if (quality == null || quality < 0) {
            throw new IllegalArgumentException("Insertion is not supported for " + uri);
        }

        Integer price = values.getAsInteger(ProductEntry.COLUMN_PRODUCT_PRICE);
        if (price == null || price < 0) {
            throw new IllegalArgumentException("Insertion is not supported for " + uri);
        }

        String phone = values.getAsString(ProductEntry.COLUMN_SUPPLIER_EMAIL);
        if (phone == null) {
            throw new IllegalArgumentException("Insertion is not supported for " + uri);
        }

        SQLiteDatabase db = productDbHelper.getWritableDatabase();
        long id = db.insert(ProductEntry.TABLE_NAME, null, values);
        if (id == -1) {
            Log.e(LOG_TAG, "Failed to insert row for " + uri);
        }
        getContext().getContentResolver().notifyChange(uri, null);
        return ContentUris.withAppendedId(uri, id);
    }


    @Override
    public int update(Uri uri,  ContentValues values, String selection, String[] selectionArgs) {
        int match = uriMatcher.match(uri);
        switch (match) {
            case PRODUCT:
                return updateProduct(uri, values, selection, selectionArgs);
            case PRODUCT_ID:
                selection = ProductEntry._ID + "=?";
                selectionArgs = new String[]{uri.getLastPathSegment()};
                return updateProduct(uri, values, selection, selectionArgs);
            default:
                throw new IllegalArgumentException("Update is not supported for " + uri);
        }
    }

    private int updateProduct(Uri uri, ContentValues values, String selection, String[] selectionArgs) {
        if (values.containsKey(ProductEntry.COLUMN_PRODUCT_NAME)) {
            String name = values.getAsString(ProductEntry.COLUMN_PRODUCT_NAME);
            if (name == null) {
                throw new IllegalArgumentException("Insertion is not supported for " + uri);
            }
        }

        if (values.containsKey(ProductEntry.COLUMN_PRODUCT_IMAGE_URI)) {
            String imageUri = values.getAsString(ProductEntry.COLUMN_PRODUCT_IMAGE_URI);
            if (imageUri == null) {
                throw new IllegalArgumentException("Insertion is not supported for " + uri);
            }
        }

        if (values.containsKey(ProductEntry.COLUMN_PRODUCT_QUALITY)) {
            Integer quality = values.getAsInteger(ProductEntry.COLUMN_PRODUCT_QUALITY);
            if (quality == null || quality < 0) {
                throw new IllegalArgumentException("Insertion is not supported for " + uri);
            }
        }

        if (values.containsKey(ProductEntry.COLUMN_PRODUCT_PRICE)) {
            Integer price = values.getAsInteger(ProductEntry.COLUMN_PRODUCT_PRICE);
            if (price == null || price < 0) {
                throw new IllegalArgumentException("Insertion is not supported for " + uri);
            }
        }

        if (values.containsKey(ProductEntry.COLUMN_SUPPLIER_EMAIL)) {
            String phone = values.getAsString(ProductEntry.COLUMN_SUPPLIER_EMAIL);
            if (phone == null) {
                throw new IllegalArgumentException("Insertion is not supported for " + uri);
            }
        }

        SQLiteDatabase db = productDbHelper.getWritableDatabase();
        int rowsUpdated = db.update(ProductEntry.TABLE_NAME, values, selection, selectionArgs);
        if (rowsUpdated != 0) {
            getContext().getContentResolver().notifyChange(uri, null);
        }
        return rowsUpdated;
    }


    @Override
    public int delete(Uri uri, String selection, String[] selectionArgs) {
        SQLiteDatabase db = productDbHelper.getWritableDatabase();
        int match = uriMatcher.match(uri);
        int rowsDeleted;

        switch (match) {
            case PRODUCT:
                rowsDeleted = db.delete(ProductEntry.TABLE_NAME, selection, selectionArgs);
                break;
            case PRODUCT_ID:
                selection = ProductEntry._ID + "=?";
                selectionArgs = new String[]{uri.getLastPathSegment()};
                rowsDeleted = db.delete(ProductEntry.TABLE_NAME, selection, selectionArgs);
                break;
            default:
                throw new IllegalArgumentException("Deletion is not supported for" + uri);
        }

        if (rowsDeleted != 0) {
            getContext().getContentResolver().notifyChange(uri, null);
        }
        return rowsDeleted;
    }

    @Override
    public String getType(Uri uri) {
        final int match = uriMatcher.match(uri);
        switch (match) {
            case PRODUCT:
                return ProductEntry.CONTENT_LIST_TYPE;
            case PRODUCT_ID:
                return ProductEntry.CONTENT_ITEM_TYPE;
            default:
                throw new IllegalArgumentException("Unknown URI " + uri + " with match " + match);
        }
    }
}
