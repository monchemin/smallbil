package com.smallbil.repository.entities;

import com.smallbil.utils.DateUtils;

import java.util.Date;

import androidx.annotation.NonNull;
import androidx.room.Entity;
import androidx.room.PrimaryKey;

@Entity(tableName = "orders")
public class Order {
    @PrimaryKey @NonNull
    public String orderNumber;
    public String orderDate;

    public Order(String orderNumber) {
        this.orderNumber = orderNumber;
        this.orderDate = DateUtils.toISO8601UTC(new Date());
    }
}
