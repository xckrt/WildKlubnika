package com.example.wildklubnika

import android.os.Bundle
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import com.google.android.material.bottomnavigation.BottomNavigationView
import androidx.navigation.NavController
import androidx.navigation.fragment.NavHostFragment
import androidx.navigation.ui.NavigationUI
import com.example.wildklubnika.dataclasses.user


class MainActivity : AppCompatActivity() {

    private lateinit var navController: NavController
    private lateinit var bottomNavigationView: BottomNavigationView

    // Эмуляция текущего пользователя
    private val currentUser = user(id = 1, email = "example@example.com", password = "123456", isSeller = true,isLoggedIn=false)

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        // Находим элементы
        bottomNavigationView = findViewById(R.id.nav_view)

        // Получаем NavController для управления навигацией
        val navHostFragment = supportFragmentManager.findFragmentById(R.id.nav_host_fragment_activity_main) as NavHostFragment
        navController = navHostFragment.navController

        // Настроим навигацию
        NavigationUI.setupWithNavController(bottomNavigationView, navController)

        // Проверяем роль пользователя и скрываем/показываем элементы меню
        val menu = bottomNavigationView.menu
        if (currentUser.isSeller) {
            // Если пользователь продавец, показываем пункт меню для продавца
            menu.findItem(R.id.nav_seller_dashboard)?.isVisible = true
        } else {
            // Если не продавец, скрываем пункт меню для продавца
            menu.findItem(R.id.nav_seller_dashboard)?.isVisible = false
        }

        // Обработка выбранных пунктов меню (переключение фрагментов)
        bottomNavigationView.setOnNavigationItemSelectedListener { item ->
            when (item.itemId) {
                R.id.navigation_home -> {
                    navController.navigate(R.id.navigation_home)
                }
                R.id.navigation_dashboard -> {
                    navController.navigate(R.id.navigation_dashboard)
                }
                R.id.navigation_notifications -> {
                    navController.navigate(R.id.navigation_notifications)
                }
                R.id.navigation_lk -> {
                    navController.navigate(R.id.navigation_lk)
                }
                R.id.nav_seller_dashboard -> {
                    navController.navigate(R.id.nav_seller_dashboard)
                }
            }
            true
        }
    }
    fun setBottomNavigationVisibility(visible: Boolean) {
        val bottomNavigationView = findViewById<BottomNavigationView>(R.id.nav_view)
        bottomNavigationView.visibility = if (visible) View.VISIBLE else View.GONE
    }
}
