package com.example.wildklubnika


import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import android.content.Context
import androidx.room.TypeConverters
import com.example.wildklubnika.dataclasses.CartItem
import com.example.wildklubnika.dataclasses.Category
import com.example.wildklubnika.dataclasses.Finance
import com.example.wildklubnika.dataclasses.Order
import com.example.wildklubnika.dataclasses.Product
import com.example.wildklubnika.dataclasses.Seller
import com.example.wildklubnika.dataclasses.Sklad
import com.example.wildklubnika.interfaces.FinanceDao
import com.example.wildklubnika.interfaces.OrderDao
import com.example.wildklubnika.interfaces.ProductDao
import com.example.wildklubnika.interfaces.UserDao
import com.example.wildklubnika.interfaces.WarehouseDao
import com.example.wildklubnika.dataclasses.Transaction
import com.example.wildklubnika.dataclasses.user
import com.example.wildklubnika.interfaces.CartDao
import com.example.wildklubnika.interfaces.CategoryDao

@Database(entities = [Product::class, Order::class, Finance::class, Seller::class, Sklad::class,user::class,Transaction::class, CartItem::class,Category::class], version = 5)
@TypeConverters(Converters::class)
abstract class AppDatabase : RoomDatabase() {
    abstract fun categoryDao():CategoryDao
    abstract fun cartDao():CartDao
    abstract fun userDao():UserDao
    abstract fun productDao(): ProductDao
    abstract fun orderDao(): OrderDao
    abstract fun warehouseDao(): WarehouseDao
    abstract fun financeDao(): FinanceDao

    companion object {
        @Volatile
        private var INSTANCE: AppDatabase? = null

        fun getDatabase(context: Context): AppDatabase {
            return INSTANCE ?: synchronized(this) {
                val instance = Room.databaseBuilder(
                    context.applicationContext,
                    AppDatabase::class.java,
                    "app_database"
                ).fallbackToDestructiveMigration().build()
                INSTANCE = instance
                instance
            }
        }
    }
}

