package br.com.memorify.inventoryapp.ui.dialog;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.app.DialogFragment;
import android.content.ContentValues;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.support.design.widget.TextInputLayout;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;

import java.io.File;

import br.com.memorify.inventoryapp.R;
import br.com.memorify.inventoryapp.model.Product;
import br.com.memorify.inventoryapp.ui.activity.MainActivity;
import br.com.memorify.inventoryapp.util.DatabaseManager;
import br.com.memorify.inventoryapp.util.ImageHelper;

public class AddProductDialog extends DialogFragment {

    private static final int REQUEST_CODE_TAKE_PHOTO = 10;

    private View rootView;
    private View contentView;
    private ImageView pictureImageView;
    private EditText nameEditTextView;
    private EditText stockEditTextView;
    private EditText priceEditTextView;
    private Button takePhotoButton;
    private TextInputLayout nameEditLayoutView;
    private TextInputLayout stockEditLayoutView;
    private TextInputLayout priceEditLayoutView;

    private Bitmap photoTaken;

    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        AlertDialog.Builder builder = getDialogBuilder();
        final AlertDialog dialog = builder.create();
        bindViews();
        setupViews();
        return dialog;
    }

    private AlertDialog.Builder getDialogBuilder() {
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());

        LayoutInflater inflater = LayoutInflater.from(getActivity());
        rootView = inflater.inflate(R.layout.dialog_add_product, null);

        builder.setView(rootView);
        builder.setMessage("Add Product");
        builder.setPositiveButton("Add", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                // do nothing here, I will override it below.
            }
        });
        builder.setNegativeButton("Cancel", null);
        return builder;
    }

    @Override
    public void onStart() {
        super.onStart();
        final AlertDialog dialog = (AlertDialog) getDialog();
        if (dialog != null) {
            dialog.getButton(Dialog.BUTTON_POSITIVE).setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Boolean wantToCloseDialog = addProduct();
                    if (wantToCloseDialog)
                        dialog.dismiss();
                }
            });
        }
    }

    private void bindViews() {
        contentView = rootView.findViewById(R.id.content_view);
        pictureImageView = (ImageView) rootView.findViewById(R.id.product_picture);
        nameEditTextView = (EditText) rootView.findViewById(R.id.product_name);
        stockEditTextView = (EditText) rootView.findViewById(R.id.product_stock);
        priceEditTextView = (EditText) rootView.findViewById(R.id.product_price);
        nameEditLayoutView = (TextInputLayout) rootView.findViewById(R.id.product_name_input_layout);
        stockEditLayoutView = (TextInputLayout) rootView.findViewById(R.id.product_stock_input_layout);
        priceEditLayoutView = (TextInputLayout) rootView.findViewById(R.id.product_price_input_layout);
        takePhotoButton = (Button) rootView.findViewById(R.id.take_photo_button);
    }

    private void setupViews() {
        takePhotoButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startDefaultCamera();
            }
        });
        pictureImageView.setImageResource(R.drawable.no_image);
    }

    private boolean addProduct() {
        String name = nameEditTextView.getText().toString();
        String price = priceEditTextView.getText().toString();
        String stock = stockEditTextView.getText().toString();

        boolean shouldCancel = false;
        View focusView = null;
        if (stock.trim().isEmpty()) {
            focusView = stockEditTextView;
            shouldCancel = true;
            stockEditLayoutView.setErrorEnabled(true);
            stockEditLayoutView.setError(getString(R.string.error_required_field));
        } else {
            stockEditLayoutView.setError(null);
        }
        if (price.trim().isEmpty()) {
            focusView = priceEditTextView;
            shouldCancel = true;
            priceEditLayoutView.setErrorEnabled(true);
            priceEditLayoutView.setError(getString(R.string.error_required_field));
        } else {
            priceEditLayoutView.setError(null);
        }
        if (name.trim().isEmpty()) {
            focusView = nameEditTextView;
            shouldCancel = true;
            nameEditLayoutView.setErrorEnabled(true);
            nameEditLayoutView.setError(getString(R.string.error_required_field));
        } else {
            nameEditLayoutView.setError(null);
        }

        if (shouldCancel) {
            focusView.requestFocus();
        } else {
            Product product = new Product();
            product.name = name;
            product.price = Double.parseDouble(price);
            product.stock = Long.parseLong(stock);
            product.pictureAsByteArray = ImageHelper.getBitmapAsByteArray(photoTaken);
            product.supplierContact = Product.getRandomSupplier();
            DatabaseManager.getInstance(getActivity()).insertProduct(product);
        }
        return !shouldCancel;
    }

    @Override
    public void onDismiss(DialogInterface dialog) {
        super.onDismiss(dialog);
        final Activity activity = getActivity();
        if (activity instanceof DialogInterface.OnDismissListener) {
            ((DialogInterface.OnDismissListener) activity).onDismiss(dialog);
        }
    }

    private void startDefaultCamera() {
        ContentValues values = new ContentValues();
        values.put(MediaStore.Images.Media.TITLE, "tmpimagefile.jpg");
        Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        intent.putExtra(MediaStore.EXTRA_OUTPUT, Uri.fromFile(new File(Environment.getExternalStorageDirectory() + "/tmpimagefile.jpg")));
        startActivityForResult(intent, AddProductDialog.REQUEST_CODE_TAKE_PHOTO);
    }

    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == REQUEST_CODE_TAKE_PHOTO) {
            if (resultCode == MainActivity.RESULT_OK) {
                handlePhotoTakenFromDefaultCamera();
            }
        }
    }

    private void handlePhotoTakenFromDefaultCamera() {
        photoTaken = ImageHelper.getPhotoTakenFromAndroid(Environment.getExternalStorageDirectory() + "/tmpimagefile.jpg");
        pictureImageView.setImageBitmap(photoTaken);
    }
}
