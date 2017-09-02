package com.example.berr.inventory;

import android.content.ContentUris;
import android.content.ContentValues;
import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;

import com.example.berr.inventory.data.ProductContract;

public class MainActivity extends AppCompatActivity implements android.app.LoaderManager.LoaderCallbacks<Cursor> {

    private static final int PRODUCT_LOADER = 0;

    ProductCursorAdapter mCursorAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.floating_add);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(MainActivity.this, EditorActivity.class);
                startActivity(intent);
            }
        });

        ListView petListView = (ListView) findViewById(R.id.list_view);

        View emptyView = findViewById(R.id.empty_list_view);
        petListView.setEmptyView(emptyView);

        mCursorAdapter = new ProductCursorAdapter(this, null);
        petListView.setAdapter(mCursorAdapter);

        petListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int position, long id) {

                Intent intent = new Intent(MainActivity.this, EditorActivity.class);
                Uri currentPetUri = ContentUris.withAppendedId(ProductContract.ProductEntry.CONTENT_URI, id);
                intent.setData(currentPetUri);
                startActivity(intent);
            }
        });
        getLoaderManager().initLoader(PRODUCT_LOADER, null, this);
    }

    private void insertProduct(){
        ContentValues values = new ContentValues();
        values.put(ProductContract.ProductEntry.COLUMN_PRODUCT_IMAGE_URI," ");
        values.put(ProductContract.ProductEntry.COLUMN_PRODUCT_NAME,"iPhone");
        values.put(ProductContract.ProductEntry.COLUMN_PRODUCT_PRICE, 399);
        values.put(ProductContract.ProductEntry.COLUMN_PRODUCT_QUALITY, 100);
        values.put(ProductContract.ProductEntry.COLUMN_SUPPLIER_EMAIL,"b@yahoo.com");

        Uri newUri = getContentResolver().insert(ProductContract.ProductEntry.CONTENT_URI,values);
    }

    private void deleteAllProducts(){
        int rowsDeleted = getContentResolver().delete(ProductContract.ProductEntry.CONTENT_URI, null, null);
        Log.v("MainActivity", rowsDeleted + " rows deleted from pet database");
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_main_activity, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.insert_product:
                insertProduct();
                return true;
            case R.id.delete_all:
                deleteAllProducts();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    @Override
    public android.content.Loader<Cursor> onCreateLoader(int id, Bundle args) {
        String[] projection = new String[]{ProductContract.ProductEntry._ID,
                ProductContract.ProductEntry.COLUMN_PRODUCT_NAME,
                ProductContract.ProductEntry.COLUMN_PRODUCT_PRICE,
                ProductContract.ProductEntry.COLUMN_PRODUCT_QUALITY};

        return new android.content.CursorLoader(this,
                ProductContract.ProductEntry.CONTENT_URI,
                projection,
                null,
                null,
                null);

    }

    @Override
    public void onLoadFinished(android.content.Loader<Cursor> loader, Cursor cursor) {
        mCursorAdapter.swapCursor(cursor);
    }

    @Override
    public void onLoaderReset(android.content.Loader<Cursor> loader) {
        mCursorAdapter.swapCursor(null);
    }

}
