package com.smallbil.adapters;


import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Switch;

import com.smallbil.R;
import com.smallbil.repository.entities.Product;

import java.util.ArrayList;
import java.util.List;

import androidx.annotation.NonNull;
import androidx.appcompat.widget.AppCompatTextView;
import androidx.recyclerview.widget.RecyclerView;

public class ProductListAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    private List<Product> productList = new ArrayList<>();
    private View.OnClickListener mOnItemClickListener;

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(parent.getContext());
        View itemView = null;

        switch(viewType) {
            case 0:
                 itemView = inflater.inflate(R.layout.recyclerview_product_list_item, parent, false);
                return  new ProductListViewHolder(itemView);
            case 1:
                 itemView = inflater.inflate(R.layout.recyclerview_threshold_list_item, parent, false);
                 return new TresholdListViewHolder(itemView);
        }
        return null;

    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {
        if(productList != null) {
            Product prod = productList.get(position);
            HolderDisplay holder1 = (HolderDisplay) holder;
            holder1.displayItem(prod);
        }
    }


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

    class ProductListViewHolder extends RecyclerView.ViewHolder implements HolderDisplay  {
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
            System.out.println("SMB  sale" );
            name.setText(product.name);
            amount.setText(String.valueOf(product.amount));
            quantity.setText(String.valueOf(product.saleQuantity));
            total.setText(String.valueOf(product.amount * product.saleQuantity));
        }

    }

    class TresholdListViewHolder extends RecyclerView.ViewHolder implements HolderDisplay  {
        AppCompatTextView name, quantity;


        public TresholdListViewHolder(@NonNull View itemView) {
            super(itemView);
            name = itemView.findViewById(R.id.product_list_name);
            quantity = itemView.findViewById(R.id.product_list_quantity);
            name.setEnabled(false);

        }

        public void displayItem(Product product) {
            System.out.println("SMB  dash" );
            name.setText(product.name);
            quantity.setText(String.valueOf(product.quantity));

        }

    }

    public interface HolderDisplay {
        void displayItem(Product product);
    }
}
