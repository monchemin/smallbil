package com.smallbil.service;

import android.os.AsyncTask;

import com.smallbil.repository.AppDatabase;
import com.smallbil.repository.entities.Order;
import com.smallbil.repository.entities.OrderDetail;
import com.smallbil.repository.entities.Product;

import java.util.ArrayList;
import java.util.List;

public class OrderService {
    AppDatabase db;

    public OrderService(AppDatabase db) {
        this.db = db;
    }

    public WriteOrder write(ArrayList<Product> products) {

        WriteOrder writeOrder = new WriteOrder(products);
        return writeOrder;
    }


public class WriteOrder extends AsyncTask<Product, Void, Boolean> {

    private ServiceResponse response = null;
    ArrayList<Product> productList;

    public WriteOrder(ServiceResponse response) {
        this.response = response;
    }

    public WriteOrder(ArrayList<Product> productList) {
        this.productList = productList;
    }

    @Override
    protected Boolean doInBackground(final Product... products) {
        if (products.length == 0) return false;

        String orderNumber = String.valueOf(System.currentTimeMillis());
        final Order order = new Order(orderNumber);
        final List<OrderDetail> orderDetails = new ArrayList<>();
        for(Product prod : productList) {
            prod.quantity -= prod.saleQuantity;
            orderDetails.add(new OrderDetail());
        }
        try {
            db.runInTransaction(new Runnable() {
                @Override
                public void run() {
                    db.orderDao().insertOrder(order);
                    db.orderDao().insertOrderDetail(orderDetails);
                    db.productDao().updateProduct(products);
                }
            });
        } catch (Exception ex) {
            return false;
        }
        finally {
            return false;
        }
    }

    @Override
    protected void onPostExecute(Boolean aBoolean) {
        super.onPostExecute(aBoolean);
        response.didFinish(aBoolean);
    }
}

}
