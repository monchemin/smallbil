package com.smallbil.repository.entities;

import androidx.annotation.NonNull;
import androidx.room.Entity;
import androidx.room.Ignore;
import androidx.room.PrimaryKey;

@Entity(tableName = "products")
public class Product {
    @PrimaryKey
    public @NonNull String code;

    public String name;

    public int quantity;

    public double amount;

    @Ignore
    public int saleQuantity;

}
