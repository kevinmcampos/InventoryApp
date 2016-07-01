package br.com.memorify.inventoryapp.ui.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import java.util.List;

import br.com.memorify.inventoryapp.R;
import br.com.memorify.inventoryapp.model.Product;

public class ProductAdapter extends RecyclerView.Adapter<ProductAdapter.ViewHolder> {

    private Context context;
    private List<Product> products;
    private ItemClickListener listener;

    public interface ItemClickListener {
        void onItemClicked(Product story);
    }

    public ProductAdapter(Context context, List<Product> stories, ItemClickListener listener) {
        this.context = context;
        this.products = stories;
        this.listener = listener;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.list_item_product, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        holder.bind(products.get(position));
    }

    @Override
    public int getItemCount() {
        return products.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {

        private View contentView;
        private ImageView pictureImageView;
        private TextView nameTextView;
        private TextView stockTextView;
        private TextView priceTextView;
        private Button saleButton;

        public ViewHolder(View itemView) {
            super(itemView);
            bindViews(itemView);
        }

        private void bindViews(View itemView) {
            contentView = itemView.findViewById(R.id.content_view);
            pictureImageView = (ImageView) itemView.findViewById(R.id.product_picture);
            nameTextView = (TextView) itemView.findViewById(R.id.product_name);
            stockTextView = (TextView) itemView.findViewById(R.id.product_stock);
            priceTextView = (TextView) itemView.findViewById(R.id.product_price);
            saleButton = (Button) itemView.findViewById(R.id.product_track_sale_button);
        }

        public void bind(final Product product) {
            if (product.pictureAsByteArray == null) {
                pictureImageView.setVisibility(View.GONE);
            } else {
                pictureImageView.setVisibility(View.VISIBLE);
                pictureImageView.setImageBitmap(product.getPicture());
            }
            nameTextView.setText(product.name);
            stockTextView.setText(context.getString(R.string.product_stock_field, product.stock));
            priceTextView.setText(context.getString(R.string.product_price_field, product.price));
            saleButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    try {
                        product.sale(context);
                        bind(product);
                        Toast.makeText(context, "Sold one " + product.name + ".", Toast.LENGTH_SHORT).show();
                    } catch (Product.NotEnoughInStockToSellException e) {
                        Toast.makeText(context, "Not enought in stock in order to sell.", Toast.LENGTH_SHORT).show();
                    }
                }
            });
            contentView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (listener != null) {
                        listener.onItemClicked(product);
                    }
                }
            });
        }
    }
}
