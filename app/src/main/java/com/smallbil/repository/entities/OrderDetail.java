package com.smallbil.repository.entities;

import androidx.room.Entity;

@Entity(tableName = "orderdetails", primaryKeys = {"firstName", "lastName"})
public class OrderDetail {
   public String orderNumber;
   public String productCode;
   public int quantityOrdered;
   public double price;
}
