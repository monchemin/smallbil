package com.smallbil.repository.entities;

import androidx.annotation.NonNull;
import androidx.room.Entity;
import androidx.room.ForeignKey;

@Entity(tableName = "orderdetails",
        primaryKeys = {"orderNumber", "productCode"},
        foreignKeys = {
                @ForeignKey(entity = Order.class,
                        parentColumns = "orderNumber",
                        childColumns = "orderNumber"),
                @ForeignKey(entity = Product.class,
                        parentColumns = "code",
                        childColumns = "productCode")
        }
)
public class OrderDetail {
   public @NonNull String orderNumber;
   public @NonNull String productCode;
   public @NonNull int quantityOrdered;
   public @NonNull double price;

   public OrderDetail(String orderNumber, String productCode, int quantityOrdered, double price) {
       this.orderNumber = orderNumber;
       this.productCode = productCode;
       this.quantityOrdered = quantityOrdered;
       this.price = price;
   }
}
