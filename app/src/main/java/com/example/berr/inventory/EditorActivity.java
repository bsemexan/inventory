package com.example.berr.inventory;

import android.content.ContentValues;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.LoaderManager;
import android.support.v4.content.CursorLoader;
import android.support.v4.content.Loader;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;

import com.example.berr.inventory.data.ProductContract;

public class EditorActivity extends AppCompatActivity implements LoaderManager.LoaderCallbacks<Cursor> {


    private static final int EXISTING_PRODUCT_LOADER = 0;
    private static final int RESULT_LOAD_IMAGE = 1;

    private Uri mProductUri;

    private ImageView mImageUri;

    private EditText mNameEditText;

    private EditText mPriceEditText;

    private EditText mQuantityEditText;

    private EditText mEmailEditText;

    private boolean mProductHasChanged = false;

    private View.OnTouchListener mTouchListener = new View.OnTouchListener() {
        @Override
        public boolean onTouch(View view, MotionEvent motionEvent) {
            mProductHasChanged = true;
            return false;
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit);


        Intent intent = getIntent();
        mProductUri = intent.getData();

        if (mProductUri == null) {
            setTitle(getString(R.string.add_label));
            invalidateOptionsMenu();
        }else{
            setTitle(getString(R.string.edit_label));
            getSupportLoaderManager().initLoader(EXISTING_PRODUCT_LOADER, null, this);
        }

        mImageUri = (ImageView) findViewById(R.id.detail_image_view);
        mNameEditText = (EditText) findViewById(R.id.detail_name_tv);
        mPriceEditText = (EditText) findViewById(R.id.detail_price_et);
        mQuantityEditText = (EditText) findViewById(R.id.detail_quality_et);
        mEmailEditText = (EditText) findViewById(R.id.detail_phone_et);

        mImageUri.setOnTouchListener(mTouchListener);
        mNameEditText.setOnTouchListener(mTouchListener);
        mPriceEditText.setOnTouchListener(mTouchListener);
        mQuantityEditText.setOnTouchListener(mTouchListener);
        mEmailEditText.setOnTouchListener(mTouchListener);
    }

    private void saveProduct() {

        String image = mImageUri.toString();
        String nameString = mNameEditText.getText().toString().trim();
        String priceString = mPriceEditText.getText().toString().trim();
        String quantityString = mQuantityEditText.getText().toString().trim();
        String emailString = mEmailEditText.getText().toString().trim();

        ContentValues values = new ContentValues();
        values.put(ProductContract.ProductEntry.COLUMN_PRODUCT_IMAGE_URI, image);
        values.put(ProductContract.ProductEntry.COLUMN_PRODUCT_NAME,nameString );
        values.put(ProductContract.ProductEntry.COLUMN_PRODUCT_PRICE,priceString);
        values.put(ProductContract.ProductEntry.COLUMN_PRODUCT_QUALITY, quantityString);
        values.put(ProductContract.ProductEntry.COLUMN_SUPPLIER_EMAIL, emailString);

        if (mProductUri == null) {
            getContentResolver().insert(ProductContract.ProductEntry.CONTENT_URI, values);
        } else {
            getContentResolver().update(mProductUri, values, null, null);
        }
        finish();
    }


    private void confirmDelete() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setPositiveButton(R.string.delete, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int id) {
                deleteProduct();
            }
        });
    }
    public void deleteProduct() {
                getContentResolver().delete(mProductUri, null, null);
                finish();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_editor, menu);
        return true;
    }

    @Override
    public boolean onPrepareOptionsMenu(Menu menu) {
        super.onPrepareOptionsMenu(menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.edit_save:
                saveProduct();
                return true;
            case R.id.detail_delete_bt:
                deleteProduct();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    @Override
    public Loader<Cursor> onCreateLoader(int id, Bundle args) {

        String[] projection ={
                ProductContract.ProductEntry._ID,
                ProductContract.ProductEntry.COLUMN_PRODUCT_IMAGE_URI,
                ProductContract.ProductEntry.COLUMN_PRODUCT_NAME,
                ProductContract.ProductEntry.COLUMN_PRODUCT_PRICE,
                ProductContract.ProductEntry.COLUMN_PRODUCT_QUALITY,
                ProductContract.ProductEntry.COLUMN_SUPPLIER_EMAIL};

        return new CursorLoader(this,
                mProductUri,
                projection,
                null,
                null,
                null);
    }

    @Override
    public void onLoadFinished(Loader<Cursor> loader, Cursor data) {
        if(data == null || data.getCount() < 1){
            return;
        }

        if(data.moveToFirst()){
            int nameColumnIndex = data.getColumnIndex(ProductContract.ProductEntry.COLUMN_PRODUCT_NAME);
            int priceColumnIndex = data.getColumnIndex(ProductContract.ProductEntry.COLUMN_PRODUCT_PRICE);
            int quantityColumnIndex = data.getColumnIndex(ProductContract.ProductEntry.COLUMN_PRODUCT_QUALITY);
            int emailColumnIndex = data.getColumnIndex(ProductContract.ProductEntry.COLUMN_SUPPLIER_EMAIL);

            String name = data.getString(nameColumnIndex);
            String price = data.getString(priceColumnIndex);
            String quantity = data.getString(quantityColumnIndex);
            String email = data.getString(emailColumnIndex);

            mNameEditText.setText(name);
            mPriceEditText.setText(price);
            mQuantityEditText.setText(quantity);
            mEmailEditText.setText(email);
        }

    }

    @Override
    public void onLoaderReset(Loader<Cursor> loader) {
        mNameEditText.setText("");
        mPriceEditText.setText("");
        mQuantityEditText.setText("");
        mEmailEditText.setText("");

    }
}