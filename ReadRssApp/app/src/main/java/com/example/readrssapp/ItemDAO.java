package com.example.readrssapp;

import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.Query;
import androidx.room.Update;

import java.util.List;
@Dao
public interface ItemDAO {
    @Insert
    public void insert(News... items);

    @Update
    public void update(News... items);

    @Delete
    public void delete(News item);

    @Query("SELECT * FROM items")
    public List<News> getItems();

    @Query("DELETE FROM items")
    public void deleteAll();
}
