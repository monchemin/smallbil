package com.smallbil.repository.daos;

import com.smallbil.repository.entities.Order;
import com.smallbil.repository.entities.Product;

import androidx.lifecycle.LiveData;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;
import androidx.room.Update;

public interface OrderDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    Long insertOrder(Order... order);

    @Update
    int updateOrder(Order order);

    @Delete
    void deleteOrder(Order... orders);

    @Query("Select * From orders Where orderNumber = :mcode")
    LiveData<Order> getOrderByNumber(String mcode);
}
