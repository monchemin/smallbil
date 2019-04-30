package com.smallbil.service;


import android.os.AsyncTask;

import com.smallbil.repository.AppDatabase;
import com.smallbil.repository.entities.Product;

import androidx.lifecycle.LiveData;

public class ProductService {

    AppDatabase db;

    public ProductService(AppDatabase db) {

        this.db = db;
    }


    public InsertTask upsert(final String code, String name, int quantity, double amount) {
       final Product product = fillProduct(code, name, quantity, amount);
        boolean isNew = true;
            if (getProductByCode(code).getValue() != null)
                isNew = false;
             return new InsertTask(product, isNew);
    }

    private Product fillProduct(String code, String name, int quantity, double amount) {
        Product product = new Product();
        product.code = code;
        product.name = name;
        product.amount = amount;
        product.quantity = quantity;
        return product;
    }


    public LiveData<Product> getProductByCode(String code){
        return db.productDao().getProductByCode(code);
    }

    public interface ServiceResponse {
        void didFinish(Boolean result);
    }

    public class InsertTask extends AsyncTask<Product, Void, Boolean> {
        public ServiceResponse response = null;

        private Product product;
        private Boolean isNew;

        public InsertTask(Product product, Boolean isNew) {
           this.product = product;
           this.isNew = isNew;
        }

        @Override
        protected Boolean doInBackground(Product... products) {
            if(this.product != null )
            {
                if (isNew)
                    return db.productDao().insertProduct(this.product) > 0;
                else
                    return db.productDao().updateProduct(this.product) > 0;
            }

            else return false;
        }

        @Override
        protected void onPostExecute(Boolean result) {
            super.onPostExecute(result);
            response.didFinish(result);
        }
    }


}
