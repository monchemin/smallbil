package com.smallbil.service;

import android.os.AsyncTask;
import android.util.Log;

import com.smallbil.repository.AppDatabase;
import com.smallbil.repository.entities.Order;
import com.smallbil.repository.entities.OrderDetail;
import com.smallbil.repository.entities.Product;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import androidx.lifecycle.LiveData;

public class OrderService {
    AppDatabase db;

    public OrderService(AppDatabase db) {
        this.db = db;
    }

    public WriteOrder write(List<Product> products) {
        Log.d("SMB", "dans write");
        return new WriteOrder(products);

    }

    public LiveData<List<Order>> getOrders() {
        return db.orderDao().getOrders();
    }

    public LiveData<List<OrderDetail>> getDetails() {
        return db.orderDao().getOrderDetails();
    }

public class WriteOrder extends AsyncTask<Product, Void, Boolean> {

    private ServiceResponse response = null;
    List<Product> productList;

    public void setCallback(ServiceResponse response) {
        this.response = response;
    }


    WriteOrder(List<Product> productList) {
        this.productList = productList;
    }

    @Override
    protected Boolean doInBackground(final Product... products) {
        if (productList.size() == 0) return false;
        Log.d("SMB", "dans doing");
       // Order order1 = new Order(String.valueOf(System.currentTimeMillis()), new Date());
       // db.orderDao().insertOrder(order1);
        final String orderNumber = String.valueOf(System.currentTimeMillis());
        final Order order = new Order(orderNumber, new Date());
        final List<OrderDetail> orderDetails = new ArrayList<>();
        for(Product prod : productList) {
            prod.quantity -= prod.saleQuantity;
            orderDetails.add(new OrderDetail(orderNumber, prod.code, prod.saleQuantity, prod.amount));
        }
        db.beginTransaction();
        try {
            Runnable rn = new Runnable() {
                @Override
                public void run() {
                    Log.d("SMB", order.orderNumber + " : "+ orderDetails.size() + " : " + productList.size());
                    Long[] od  = db.orderDao().insertOrder(order);
                    for(OrderDetail or: orderDetails){
                        Log.d("SMB", or.orderNumber + " : "+ or.productCode + " : " + or.quantityOrdered);
                        db.orderDao().insertOrderDetail(or);
                    }
                    for (Product p: productList) {
                        db.productDao().updateProduct(p);
                    }
                    /*  Long[] od = db.orderDao().insertOrder(order);
                      db.orderDao().insertOrderDetail(orderDetails);
                      db.productDao().updateProduct(productList);
                    Log.d("SMB", orderNumber + " : "+ od[0]); */
                }
            };
            db.runInTransaction(rn);
            rn.run();
        } catch (Exception ex) {
            Log.d("SMB", ex.getMessage());
            return false;
        }
        finally {
            db.endTransaction();
        }

        return true;
    }

    @Override
    protected void onPostExecute(Boolean aBoolean) {
        super.onPostExecute(aBoolean);
        response.didFinish(aBoolean);
    }
}

}
