package com.smallbil.service;


import android.annotation.SuppressLint;
import android.os.AsyncTask;

import com.smallbil.repository.AppDatabase;
import com.smallbil.repository.Product;
import com.smallbil.repository.ProductDao;

import java.util.concurrent.ExecutionException;

public class ProductService {

    AppDatabase db;

    public ProductService(AppDatabase db) {

        this.db = db;
    }

    @SuppressLint("StaticFieldLeak")
    public long upsert(final String code, String name, int quantity, double amount) {
       final Product product = fillProduct(code, name, quantity, amount);
       long rt = 0;
        if(code != null) {
             new AsyncTask<Void, Void, Long>() {
                @Override
                protected Long doInBackground(Void... voids) {
                    if (getProductByCode(code).code == null) {
                        return db.productDao().insertProduct(product);
                    } else {
                        return (long) db.productDao().updateProduct(product);
                    }
                }

                @Override
                protected void onPostExecute(Long aLong) {
                    super.onPostExecute(aLong);
                }

            }.execute();
        }
        return rt;
    }

    private Product fillProduct(String code, String name, int quantity, double amount) {
        Product product = new Product();
        product.code = code;
        product.name = name;
        product.amount = amount;
        product.quantity = quantity;
        return product;
    }


    @SuppressLint("StaticFieldLeak")
    public Product getProductByCode(final String code){
        Product rt = null;
        try {
            rt =  new AsyncTask<Void, Void, Product>() {
                   @Override
                   protected Product doInBackground(Void... voids) {
                       return db.productDao().getProductByCode(code);
                   }

                @Override
                protected void onPostExecute(Product product) {
                    super.onPostExecute(product);
                   if (product != null) System.out.println("nyemo : " + product.code);
                }
            }.execute().get();
        } catch (ExecutionException e) {
            System.out.println("nyemo : " + e.getMessage());
        } catch (InterruptedException e) {
            System.out.println("nyemo : " + e.getMessage());
        }
        return rt;
    }

}
