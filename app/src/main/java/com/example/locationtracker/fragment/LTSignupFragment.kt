package com.example.locationtracker.fragment

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import com.example.locationtracker.LTApplicationClass
import com.example.locationtracker.R
import com.example.locationtracker.common.AlertDialogHelper
import com.example.locationtracker.databinding.LtFragmentSignupBinding
import com.example.locationtracker.model.UserRealmModel
import com.example.locationtracker.viewmodel.LTSignupViewModel


class LTSignupFragment : Fragment() {
    private lateinit var binding: LtFragmentSignupBinding
    private lateinit var viewModel: LTSignupViewModel
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        binding = LtFragmentSignupBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        viewModel = ViewModelProvider(this)[LTSignupViewModel::class.java]
        binding.ivBack.setOnClickListener {
            parentFragmentManager.beginTransaction()
                .replace(R.id.fl_authentication, LTLoginFragment()).commit()
        }
        binding.llLogin.setOnClickListener {
            parentFragmentManager.beginTransaction()
                .replace(R.id.fl_authentication, LTLoginFragment()).commit()
        }
        binding.tvSignUp.setOnClickListener {
            binding.apply {
                val name = etUserName.text.toString()
                val mail = etMail.text.toString()
                val password = etPassword.text.toString()
                val confirmPassword = etConfirmPassword.text.toString()
                if (name.isEmpty()) {
                    etUserName.error = getString(R.string.please_enter_the_name)
                    etUserName.requestFocus()
                } else if (mail.isEmpty()) {
                    etMail.error = "Please enter the email id"
                    etMail.requestFocus()
                } else if (!isValidEmail(mail)) {
                    etMail.error = "Please enter valid mail id"
                    etMail.requestFocus()
                } else if (password.isEmpty()) {
                    etPassword.error = getString(R.string.please_enter_the_password)
                    etPassword.requestFocus()
                } else if (!isValidPassword(password)) {
                    etPassword.error = getString(R.string.password_format_error)
                    etConfirmPassword.requestFocus()
                } else if (confirmPassword.isEmpty()) {
                    etConfirmPassword.error = getString(R.string.please_enter_the_confirm_password)
                    etConfirmPassword.requestFocus()
                } else if (!isPasswordsMatch(password, confirmPassword)) {
                    etConfirmPassword.error = getString(R.string.password_should_be_match)
                    etConfirmPassword.requestFocus()
                } else {
                    viewModel.saveUserDetails(UserRealmModel(name, mail, password))
                }
            }
        }

        viewModel.signupStatus.observe(viewLifecycleOwner, Observer {
            if (it == "Success") {
                AlertDialogHelper.showAlertDialog(
                    requireContext(),
                    "Successfully registered please login"
                )
            } else if (it == "Fail") {
                AlertDialogHelper.showAlertDialog(
                    requireContext(),
                    "Account is already exist please login"
                )
            }
        })
    }

    private fun isValidIndianMobileNumber(number: String): Boolean {
        val regex = Regex("^[6789]\\d{9}$")
        return regex.matches(number) && number.length == 10
    }

    private fun isValidPassword(password: String): Boolean {
        val passwordPattern = "^(?=.*[0-9])(?=.*[a-z])(?=.*[A-Z])(?=.*[@#$%^&+=!])(?=\\S+$).{8,}$"
        return password.matches(passwordPattern.toRegex())
    }

    private fun isPasswordsMatch(password: String, confirmPassword: String): Boolean {
        return password == confirmPassword
    }

    fun isValidEmail(email: String): Boolean {
        val emailRegex = "^[A-Za-z](.*)([@]{1})(.{1,})(\\.)(.{1,})"
        return email.matches(emailRegex.toRegex())
    }
}
