package com.smallbil.adapters;


import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.google.android.material.textfield.TextInputEditText;
import com.smallbil.R;
import com.smallbil.repository.Product;

import java.util.ArrayList;
import java.util.List;

import androidx.annotation.NonNull;
import androidx.appcompat.widget.AppCompatTextView;
import androidx.recyclerview.widget.RecyclerView;

public class ProductListAdapter extends RecyclerView.Adapter<ProductListAdapter.ProductListViewHolder> {

    private List<Product> productList = new ArrayList<>();

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

        return productList !=null ? productList.size() : 0;
    }

    public void setProductList(List<Product> mList) {
        productList = mList;
        notifyDataSetChanged();
    }

    public void addProduct(Product product) {
        productList.add(product);
        notifyDataSetChanged();
    }

    public void updateList(int position, String quantity) {
       try {
           productList.get(position).quantity = Integer.parseInt(quantity);
       }
       catch (NumberFormatException ex) {
           productList.remove(position);
       }
       finally {
           notifyDataSetChanged();
       }
    }

    class ProductListViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        AppCompatTextView name, quantity, amount, total;
        int position;

        public ProductListViewHolder(@NonNull View itemView) {
            super(itemView);
            name = itemView.findViewById(R.id.product_list_name);
            quantity = itemView.findViewById(R.id.product_list_quantity);
            amount = itemView.findViewById(R.id.product_list_amount);
            total = itemView.findViewById(R.id.product_list_total);
            amount.setEnabled(false);
            total.setEnabled(false);
            name.setEnabled(false);
            quantity.setOnClickListener(this);

        }

        public void displayItem(Product product) {
            name.setText(product.name);
            amount.setText(String.valueOf(product.amount));
            quantity.setText("1");
            total.setText(String.valueOf(product.amount * Integer.parseInt(quantity.getText().toString())));
        }

        @Override
        public void onClick(View v) {
            this.position = getAdapterPosition();
        }
    }
}
