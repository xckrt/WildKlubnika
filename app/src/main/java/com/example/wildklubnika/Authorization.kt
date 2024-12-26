package com.example.wildklubnika

import android.annotation.SuppressLint
import android.os.Bundle
import android.text.InputType
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.animation.AnimationUtils
import androidx.fragment.app.Fragment
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import com.example.wildklubnika.databinding.FragmentAuthorizationBinding
import com.example.wildklubnika.interfaces.UserDao
import com.google.android.material.bottomnavigation.BottomNavigationView
import kotlinx.coroutines.launch

class Authorization : Fragment() {
    private lateinit var binding: FragmentAuthorizationBinding

    @SuppressLint("SuspiciousIndentation")
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentAuthorizationBinding.inflate(inflater, container, false)

        binding.pass.setOnClickListener {

            val editText = binding.pass
            val currentInputType = editText.inputType
            if (currentInputType == (InputType.TYPE_CLASS_TEXT or InputType.TYPE_TEXT_VARIATION_PASSWORD)) {
                // Показать пароль
                editText.inputType = InputType.TYPE_CLASS_TEXT or InputType.TYPE_TEXT_VARIATION_VISIBLE_PASSWORD
            } else {
                // Скрыть пароль
                editText.inputType = InputType.TYPE_CLASS_TEXT or InputType.TYPE_TEXT_VARIATION_PASSWORD
            }
            editText.setSelection(editText.text?.length ?: 0)
        }

        binding.registerLink.setOnClickListener {
            findNavController().navigate(R.id.toreg)
        }

        binding.loginButton.setOnClickListener {
            // Загружаем анимацию
            val scaleDown = AnimationUtils.loadAnimation(requireContext(), R.anim.scale_down)
            binding.loginButton.startAnimation(scaleDown)

            val email = binding.email.text.toString()
            val password = binding.pass.text.toString()

            // Проверка на пустые поля
            if (email.isEmpty()) {
                binding.email.error = "Введите email"
                return@setOnClickListener
            }
            if (password.isEmpty()) {
                binding.pass.error = "Введите пароль"
                return@setOnClickListener
            }

            val emailPattern = "[a-zA-Z0-9._-]+@[a-z]+\\.+[a-z]+"
            if (!email.matches(Regex(emailPattern))) {
                binding.email.error = "Введите действующий email"
                return@setOnClickListener
            }

            // Авторизация пользователя
            lifecycleScope.launch {
                val userDao = AppDatabase.getDatabase(requireContext()).userDao()
                val result = authenticateUser(userDao, email, password)
                val user = userDao.getUserByEmail(email)
                // Показываем результат авторизации
                requireActivity().runOnUiThread {
                    if (result) {
                        // Обновляем статус авторизованного пользователя
                        user?.let {
                            lifecycleScope.launch {
                                userDao.setUserLoggedIn(it.id)
                            }
                        }

                        // Навигация на HomeFragment
                        findNavController().navigate(R.id.action_authorization_to_home)

                        // Получаем ссылку на BottomNavigationView
                        val navView = requireActivity().findViewById<BottomNavigationView>(R.id.nav_view)

                        // Если пользователь продавец, показываем пункт "Режим продавца"
                        if (user?.isSeller == true) {
                            navView.menu.findItem(R.id.nav_seller_dashboard).isVisible = true
                        } else {
                            // Если пользователь покупатель, скрываем этот пункт
                            navView.menu.findItem(R.id.nav_seller_dashboard).isVisible = false
                        }

                        // Восстанавливаем анимацию для кнопки
                        val scaleUp = AnimationUtils.loadAnimation(requireContext(), R.anim.scale_up)
                        binding.loginButton.startAnimation(scaleUp)
                    } else {
                        binding.email.error = "Неверный email или пароль"
                    }
                }
            }
        }
        return binding.root
    }

    suspend fun authenticateUser(userDao: UserDao, email: String, password: String): Boolean {
        val user = userDao.getUserByEmail(email)
        return if (user != null) {
            user.password == password
        } else {
            false
        }
    }

    override fun onResume() {
        super.onResume()
        (activity as MainActivity).setBottomNavigationVisibility(false)
    }
}
