package com.smallbil.adapters;


import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.google.android.material.textfield.TextInputEditText;
import com.smallbil.R;
import com.smallbil.repository.Product;

import java.util.List;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

public class ProductListAdapter extends RecyclerView.Adapter<ProductListAdapter.ProductListViewHolder> {

    private List<Product> productList;

    @NonNull
    @Override
    public ProductListViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(parent.getContext());
        View itemView = inflater.inflate(R.layout.recyclerview_product_list_item, parent, false);
        return  new ProductListViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(@NonNull ProductListViewHolder holder, int position) {
        if(productList != null) {
            Product cProduit = productList.get(position);
            holder.displayItem(cProduit);
        }

    }

    @Override
    public int getItemCount() {
        return productList.size();
    }

    public void setProductList(List<Product> mList) {
        productList = mList;
        notifyDataSetChanged();
    }

    class ProductListViewHolder extends RecyclerView.ViewHolder {
        TextInputEditText name, quantity, amount, total;

        public ProductListViewHolder(@NonNull View itemView) {
            super(itemView);
            name = itemView.findViewById(R.id.product_list_name);
            quantity = itemView.findViewById(R.id.product_list_quantity);
            amount = itemView.findViewById(R.id.product_list_amount);
            total = itemView.findViewById(R.id.product_list_total);
            amount.setEnabled(false);
            total.setEnabled(false);
        }

        public void displayItem(Product product) {
            name.setText(product.name);
            amount.setText(String.valueOf(product.amount));
            quantity.setText("1");
            total.setText(String.valueOf(product.amount * Integer.parseInt(quantity.getText().toString())));
        }
    }
}
