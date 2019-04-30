package com.smallbil.repository.entities;

import java.util.Date;

import androidx.annotation.NonNull;
import androidx.room.Entity;
import androidx.room.PrimaryKey;

@Entity(tableName = "orders")
public class Order {
    @PrimaryKey @NonNull
    public String orderNumber;
    public Date orderDate;
}
