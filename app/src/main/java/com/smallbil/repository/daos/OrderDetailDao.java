package com.smallbil.repository.daos;

import com.smallbil.repository.entities.Order;
import com.smallbil.repository.entities.OrderDetail;

import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.Query;

@Dao
public interface OrderDetailDao {
    @Insert
    Long insertOrderDetail(OrderDetail... orderDetails);

    @Query("Select * from orderdetails where orderNumber = :order")
    LiveData<OrderDetail> getOrderDetails(Order order);

}
