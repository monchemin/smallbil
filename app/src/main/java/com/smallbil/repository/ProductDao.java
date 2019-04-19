package com.smallbil.repository;

import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;
import androidx.room.Update;

@Dao
public interface ProductDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    Long insertProduct(Product product);

    @Update
    int updateProduct(Product product);

    @Delete
    void deleteProduct(Product product);

    @Query("Select * From products Where code = :mcode")
    LiveData<Product> getProductByCode(String mcode);

}
