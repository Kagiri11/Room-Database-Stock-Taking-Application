package com.example.myshop.ui.sell

import android.os.Build
import androidx.annotation.RequiresApi
import androidx.hilt.lifecycle.ViewModelInject
import androidx.lifecycle.*
import com.example.myshop.model.Item
import com.example.myshop.model.SellEntry
import com.example.myshop.repositories.ShopRepository
import kotlinx.coroutines.launch
import java.time.LocalDateTime
import java.time.LocalTime
import java.time.format.DateTimeFormatter
import java.time.format.FormatStyle

class SellViewModel
@ViewModelInject constructor(private val shopRepository: ShopRepository) : ViewModel() {

    val items = liveData {
        emit(shopRepository.getItems())
    }
    private val _itemsByName = MutableLiveData<List<Item>>()
    val itemsByName: LiveData<List<Item>> = _itemsByName
    val cartList = mutableListOf<Item>()
    var goodList2 = mutableListOf<Item>()


    val _sellCart2 = MutableLiveData<List<Item>>()
    val sellCart2: LiveData<List<Item>> = _sellCart2

    fun addToCart(item: Item) {
        cartList.add(item)
        _sellCart2.value = cartList.toList()
    }

    fun removeFromCart(item: Item) {
        cartList.remove(item)
        _sellCart2.value = cartList.toList()
    }

    fun deleteItem(item: Item) = viewModelScope.launch {
        shopRepository.deleteItem(item)
    }


    val soldItemName = cartList.map { it.name }
    val totalProfit = cartList.sumOf { it.profit }
    @RequiresApi(Build.VERSION_CODES.O)
    val timeSold = LocalDateTime.now().format(DateTimeFormatter.ofLocalizedTime(FormatStyle.SHORT))

    val entry = SellEntry(soldItems = soldItemName, timeSold = timeSold, totalProfit = totalProfit)

    fun sellCart() {

        println(cartList)
        val totalProfit = cartList.sumOf { it.profit }
        val soldItemName = cartList.map { it.name }
        val entry = SellEntry(soldItems = soldItemName, timeSold = timeSold, totalProfit = totalProfit)
        println(totalProfit)
        println(soldItemName)
        println(entry)

        viewModelScope.launch {
            shopRepository.addEntry(entry)
        }
//        cartList.forEach { itemInCart ->
//            val sameItemInDb = items.value?.first { it.name == itemInCart.name }
//            deleteItem(sameItemInDb!!)
//        }

//        println(timeSold)
//        println(totalProfit)
//        cartList.clear()


    }

    fun searchItemByName(itemName: String) = viewModelScope.launch {
        val items = shopRepository.searchItemsByName(itemName)
        _itemsByName.value = items.toList().take(1)
    }
}