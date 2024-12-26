package com.example.wildklubnika

import android.os.Bundle
import android.text.InputType
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.animation.AnimationUtils
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import com.example.wildklubnika.databinding.FragmentRegistrationBinding
import com.example.wildklubnika.dataclasses.user
import com.example.wildklubnika.interfaces.UserDao
import kotlinx.coroutines.runBlocking


class Registration : Fragment() {
    private lateinit var binding: FragmentRegistrationBinding

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentRegistrationBinding.inflate(inflater, container, false)
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


        binding.registerButton.setOnClickListener {
            val scaleDown = AnimationUtils.loadAnimation(requireContext(), R.anim.scale_down)
            binding.registerButton.startAnimation(scaleDown)

            val email = binding.email.text.toString()
            val password = binding.pass.text.toString()
            val confirmPassword = binding.confirmPass.text.toString()
            val isSeller = binding.type.isChecked

            if (email.isEmpty()) {
                binding.email.error = "Поле не может быть пустым"
                return@setOnClickListener
            }
            if(password.isEmpty())
            {
                binding.pass.error = "Поле не может быть пустым"
                return@setOnClickListener
            }
            if(confirmPassword.isEmpty())
            {
                binding.confirmPass.error = "Поле не может быть пустым"
                return@setOnClickListener
            }

            val emailPattern = "[a-zA-Z0-9._-]+@[a-z]+\\.+[a-z]+"
            if (!email.matches(Regex(emailPattern))) {
                binding.email.error = "Введите действующий email"
                return@setOnClickListener
            }
            if (password != confirmPassword) {
                binding.pass.error = "Пароли должны совпадать"
                binding.confirmPass.error = "Пароли должны совпадать"
                return@setOnClickListener
            }
            if (password.length < 8) {
                binding.pass.error = "Пароль должен быть не менее 8 символов"
                return@setOnClickListener
            }

            if (password.contains(" ")) {
                binding.pass.error = "Пароль не должен содержать пробелов"
                return@setOnClickListener
            }

            val hasUppercase = password.any { it.isUpperCase() }
            val hasLowercase = password.any { it.isLowerCase() }
            val hasDigit = password.any { it.isDigit() }
            val hasSpecialChar = password.any { "!»№;@%;?:".contains(it) }
            if (!hasUppercase || !hasLowercase || !hasDigit || !hasSpecialChar) {
                binding.pass.error = "Пароль должен содержать цифры, буквы разного регистра и спецсимвол (!»№;@%;?:)"
                return@setOnClickListener
            }
            runBlocking {
                val userDao = AppDatabase.getDatabase(requireContext()).userDao()
                val result = registerUser(userDao, email, password, isSeller)

                // Показ результата регистрации
                requireActivity().runOnUiThread {
                    if (result == "Регистрация успешна!") {
                        // Навигация на экран авторизации
                        val scaleUp = AnimationUtils.loadAnimation(requireContext(), R.anim.scale_up)
                        binding.registerButton.startAnimation(scaleUp)

                        findNavController().navigate(R.id.reg_to_auth)
                    }
                }
            }
        }

        return binding.root
    }

    private suspend fun registerUser(
        userDao: UserDao,
        email: String,
        password: String,
        isSeller: Boolean
    ): String {

        val isRegistered = userDao.isEmailRegistered(email) > 0

        return if (isRegistered) {
            "Пользователь с таким email уже зарегистрирован"
        } else {
            val user = user(email = email, password = password, isSeller = isSeller,isLoggedIn=false)
            userDao.insertUser(user)
            "Регистрация успешна!"
        }
    }
    override fun onResume() {
        super.onResume()
        (activity as MainActivity).setBottomNavigationVisibility(false)
    }
}
