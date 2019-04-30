package com.smallbil.adapters;


import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.smallbil.R;
import com.smallbil.repository.entities.Product;

import java.util.ArrayList;
import java.util.List;

import androidx.annotation.NonNull;
import androidx.appcompat.widget.AppCompatTextView;
import androidx.recyclerview.widget.RecyclerView;

public class ProductListAdapter extends RecyclerView.Adapter<ProductListAdapter.ProductListViewHolder> {

    private List<Product> productList = new ArrayList<>();
    private View.OnClickListener mOnItemClickListener;

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


    public Product getItem(int position) {
        return productList != null ? productList.get(position) : null;

    }

    public void setOnItemClickListener(View.OnClickListener itemClickListener) {
        mOnItemClickListener = itemClickListener;
    }

    class ProductListViewHolder extends RecyclerView.ViewHolder  {
        AppCompatTextView name, quantity, amount, total;


        public ProductListViewHolder(@NonNull View itemView) {
            super(itemView);
            name = itemView.findViewById(R.id.product_list_name);
            quantity = itemView.findViewById(R.id.product_list_quantity);
            amount = itemView.findViewById(R.id.product_list_amount);
            total = itemView.findViewById(R.id.product_list_total);
            amount.setEnabled(false);
            total.setEnabled(false);
            name.setEnabled(false);
            itemView.setTag(this);
            itemView.setOnClickListener(mOnItemClickListener);

        }

        public void displayItem(Product product) {
            name.setText(product.name);
            amount.setText(String.valueOf(product.amount));
            quantity.setText(String.valueOf(product.saleQuantity));
            total.setText(String.valueOf(product.amount * product.saleQuantity));
        }

    }
}
