package com.smallbil.service;

import com.smallbil.repository.AppDatabase;

public class OrderService {
    AppDatabase db;

    public OrderService(AppDatabase db) {
        this.db = db;
    }

}
