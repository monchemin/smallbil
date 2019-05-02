package com.smallbil.repository;

import com.smallbil.repository.daos.OrderDao;
import com.smallbil.repository.daos.ProductDao;
import com.smallbil.repository.entities.Order;
import com.smallbil.repository.entities.OrderDetail;
import com.smallbil.repository.entities.Product;
import com.smallbil.utils.DateConverter;

import androidx.room.Database;
import androidx.room.RoomDatabase;
import androidx.room.TypeConverters;

@Database(entities = {
        Product.class,
        Order.class,
        OrderDetail.class
    },
        version=2, exportSchema = false)
@TypeConverters({DateConverter.class})
public abstract class AppDatabase extends RoomDatabase {
    public abstract ProductDao productDao();
    public abstract OrderDao orderDao();
}
