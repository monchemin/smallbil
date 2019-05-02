package com.smallbil.repository.daos;

import com.smallbil.repository.entities.Order;
import com.smallbil.repository.entities.OrderDetail;

import java.util.List;

import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;
import androidx.room.Update;

@Dao
public interface OrderDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    Long[] insertOrder(Order... order);

    @Update
    int updateOrder(Order order);

    @Delete
    void deleteOrder(Order... orders);

    @Query("Select * From orders Where orderNumber = :mcode")
    LiveData<Order> getOrderByNumber(String mcode);

    @Insert
    Long[] insertOrderDetail(OrderDetail... orderDetails);

    @Insert
    Long[] insertOrderDetail(List<OrderDetail> orderDetails);

    @Query("Select * from orderdetails where orderNumber = :orderNumber")
    LiveData<OrderDetail> getOrderDetails(String orderNumber);

    @Query("SELECT * FROM orders")
    LiveData<List<Order>> getOrders();

    @Query("SELECT * FROM orderdetails")
    LiveData<List<OrderDetail>> getOrderDetails();

}
