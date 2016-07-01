package br.com.memorify.inventoryapp.ui.activity;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import br.com.memorify.inventoryapp.R;
import br.com.memorify.inventoryapp.model.Product;
import br.com.memorify.inventoryapp.util.DatabaseManager;

public class DetailActivity extends AppCompatActivity {

    public static final int REQUEST_DETAIL = 11;
    private static final String PRODUCT_ID_KEY = "PRODUCT_ID_KEY";
    public static final int RESULT_DELETED_PRODUCT = 3;

    private ImageView pictureImageView;
    private TextView nameTextView;
    private TextView stockTextView;
    private TextView priceTextView;
    private TextView supplierTextView;
    private Button contactSupplierButton;

    private Product selectedProduct;

    public static void startDetail(Activity activity, long productId) {
        Intent intent = new Intent(activity, DetailActivity.class);
        intent.putExtra(PRODUCT_ID_KEY, productId);
        activity.startActivityForResult(intent, REQUEST_DETAIL);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail);

        if (getIntent() == null || getIntent().getLongExtra(PRODUCT_ID_KEY, 0) == 0) {
            throw new IllegalArgumentException();
        } else {
            long productId = getIntent().getLongExtra(PRODUCT_ID_KEY, 0);
            selectedProduct = DatabaseManager.getInstance(getBaseContext()).getProductById(productId);
            if (selectedProduct == null) {
                throw new IllegalArgumentException();
            }
        }

        bindViews();
        setupViews();
    }

    private void bindViews() {
        pictureImageView = (ImageView) findViewById(R.id.product_picture);
        nameTextView = (TextView) findViewById(R.id.product_name);
        stockTextView = (TextView) findViewById(R.id.product_stock);
        priceTextView = (TextView) findViewById(R.id.product_price);
        supplierTextView = (TextView) findViewById(R.id.product_supplier);
        contactSupplierButton = (Button) findViewById(R.id.contact_supplier_button);
    }

    private void setupViews() {
        nameTextView.setText(selectedProduct.name);
        stockTextView.setText(getString(R.string.product_stock_field, selectedProduct.stock));
        priceTextView.setText(getString(R.string.product_price_field, selectedProduct.price));
        supplierTextView.setText(getString(R.string.product_supplier_field, selectedProduct.supplierContact));
        contactSupplierButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                sendEmailToSupplier();
            }
        });
    }

    private void sendEmailToSupplier() {
        Intent i = new Intent(Intent.ACTION_SEND);
        i.setType("message/rfc822");
        i.putExtra(Intent.EXTRA_EMAIL  , new String[]{selectedProduct.supplierContact});
        i.putExtra(Intent.EXTRA_SUBJECT, "Supply request");
        i.putExtra(Intent.EXTRA_TEXT, "Greetings. I need more " + selectedProduct.name + ". Please, send me fast. Thanks. Best regards.");
        try {
            startActivity(Intent.createChooser(i, "Send mail..."));
        } catch (android.content.ActivityNotFoundException ex) {
            Toast.makeText(this, "There are no email clients installed.", Toast.LENGTH_SHORT).show();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.detail_menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        if (id == R.id.action_delete) {
            DatabaseManager.getInstance(getBaseContext()).deleteProduct(selectedProduct._id);
            Toast.makeText(getBaseContext(), "Product was deleted successfully.", Toast.LENGTH_SHORT).show();
            setResult(RESULT_DELETED_PRODUCT);
            finish();
            return true;
        }
        return super.onOptionsItemSelected(item);
    }
}
