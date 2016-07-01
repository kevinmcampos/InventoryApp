package br.com.memorify.inventoryapp.ui.activity;

import android.content.DialogInterface;
import android.content.Intent;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.TextView;

import java.util.List;

import br.com.memorify.inventoryapp.R;
import br.com.memorify.inventoryapp.model.Product;
import br.com.memorify.inventoryapp.ui.adapter.ProductAdapter;
import br.com.memorify.inventoryapp.ui.dialog.AddProductDialog;
import br.com.memorify.inventoryapp.util.DatabaseManager;

public class MainActivity extends AppCompatActivity implements DialogInterface.OnDismissListener {

    private static final String ADD_PRODUCT_DIALOG_KEY = "ADD_PRODUCT_DIALOG_KEY";

    private AddProductDialog addProductDialog;
    private RecyclerView productListView;
    private TextView emptyView;
    private FloatingActionButton fab;

    private ProductAdapter productAdapter;

    private List<Product> allProducts;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        bindViews();
        setupViews();
    }

    private void bindViews() {
        productListView = (RecyclerView) findViewById(R.id.product_list);
        emptyView = (TextView) findViewById(R.id.empty_view);
        fab = (FloatingActionButton) findViewById(R.id.fab_add_product);
    }

    private void setupViews() {
        setupList();
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                addProductDialog = new AddProductDialog();
                addProductDialog.show(getFragmentManager(), ADD_PRODUCT_DIALOG_KEY);
            }
        });
    }

    private void setupList() {
        allProducts = DatabaseManager.getInstance(getBaseContext()).getAllProducts();
        productListView.setHasFixedSize(true);
        productListView.setLayoutManager(new LinearLayoutManager(getBaseContext()));
        productAdapter = new ProductAdapter(this, allProducts, new ProductAdapter.ItemClickListener() {
            @Override
            public void onItemClicked(Product product) {
                DetailActivity.startDetail(MainActivity.this, product._id);
            }
        });
        productListView.setAdapter(productAdapter);
        if (allProducts.size() == 0) {
            emptyView.setVisibility(View.VISIBLE);
        } else {
            emptyView.setVisibility(View.GONE);
        }
    }

    private void refreshDataFromDB() {
        allProducts.clear();
        allProducts.addAll(DatabaseManager.getInstance(getBaseContext()).getAllProducts());
        productAdapter.notifyDataSetChanged();

        if (allProducts.size() == 0) {
            emptyView.setVisibility(View.VISIBLE);
        } else {
            emptyView.setVisibility(View.GONE);
        }
    }

    @Override
    public void onDismiss(DialogInterface dialog) {
        refreshDataFromDB();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (addProductDialog != null) {
            addProductDialog.onActivityResult(requestCode, resultCode, data);
        }
        if (requestCode == DetailActivity.REQUEST_DETAIL && resultCode == DetailActivity.RESULT_UPDATED_PRODUCT) {
            refreshDataFromDB();
        }
    }
}
