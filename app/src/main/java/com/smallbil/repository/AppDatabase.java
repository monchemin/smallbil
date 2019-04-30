package com.smallbil.repository;

import com.smallbil.repository.daos.OrderDao;
import com.smallbil.repository.daos.OrderDetailDao;
import com.smallbil.repository.daos.ProductDao;
import com.smallbil.repository.entities.Order;
import com.smallbil.repository.entities.OrderDetail;
import com.smallbil.repository.entities.Product;

import androidx.room.Database;
import androidx.room.RoomDatabase;

@Database(entities = {
        Product.class,
        Order.class,
        OrderDetail.class
    },
        version=1, exportSchema = false)
public abstract class AppDatabase extends RoomDatabase {
    public abstract ProductDao productDao();
    public abstract OrderDao orderDao();
    public abstract OrderDetailDao orderDetailDao();
}
