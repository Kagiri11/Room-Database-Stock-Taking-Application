package com.example.myshop.data

import androidx.lifecycle.LiveData
import androidx.room.*
import com.example.myshop.model.Item
import com.example.myshop.model.SellEntry
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.distinctUntilChanged

@Dao
interface ShopDatabaseDao {

    @Delete
    suspend fun deleteItem(item: Item)

    @Query("SELECT * FROM item_table ORDER BY id  DESC")
    suspend fun getItems(): List<Item>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun addItem(item: Item)

    @Query("SELECT * FROM item_table WHERE name LIKE :itemName")
    fun searchItemsByName(itemName : String):Flow<List<Item>>

    @Query("SELECT * FROM sells_table ORDER BY id DESC")
    suspend fun getSellEntries():List<SellEntry>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun addSellEntry(entry:SellEntry)

}