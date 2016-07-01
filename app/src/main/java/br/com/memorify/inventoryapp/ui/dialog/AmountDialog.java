package br.com.memorify.inventoryapp.ui.dialog;

import android.app.DialogFragment;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

public class AmountDialog extends DialogFragment {

    TextView editTextAmount;
    View addView;
    View removeView;

    private void changeIntervalValue(int action) {
        int quant = (int) Double.parseDouble(editTextAmount.getText().toString());

        switch (action) {
            case 0:
                quant--;
                break;
            case 1:
                quant++;
                break;
        }

        if (quant <= 0) {
            quant = 1;
        }

        editTextAmount.setText("" + quant);
    }

}
