package com.example.locationtracker.fragment

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.provider.ContactsContract.CommonDataKinds.Email
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import com.example.locationtracker.LTApplicationClass
import com.example.locationtracker.R
import com.example.locationtracker.databinding.LtFragmentLoginBinding
import com.example.locationtracker.viewmodel.LTLoginViewModel
import java.util.regex.Pattern

class LTLoginFragment : Fragment() {
    private lateinit var binding: LtFragmentLoginBinding
    private lateinit var viewModel: LTLoginViewModel

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = LtFragmentLoginBinding.inflate(inflater, container, false)
        // Inflate the layout for this fragment
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        viewModel = ViewModelProvider(this)[LTLoginViewModel::class.java]
        binding.llTvSignup.setOnClickListener {
            parentFragmentManager.beginTransaction()
                .replace(R.id.fl_authentication, LTSignupFragment())
                .addToBackStack(null).commit()
        }


        binding.tvLogin.setOnClickListener {
            binding.apply {
                val mail = etMail.text.toString()
                val password = etPassword.text.toString()
                if (mail.isEmpty()) {
                    etMail.error = "Please Enter the mail id"
                    etMail.requestFocus()
                } else if (!isValidEmail(mail)) {
                    etMail.error = "Please enter the valid mail id"
                } else if (password.isEmpty()) {
                    etPassword.error = getString(R.string.please_enter_the_password)
                    etPassword.requestFocus()
                } else {
                    viewModel.checkLoginCredentials(mail, password)
                }
            }
        }
        viewModel.loginStatus.observe(viewLifecycleOwner, Observer {
            if (it){
                Toast.makeText(requireContext(), "Successfully logged in", Toast.LENGTH_SHORT).show()
            }
            else Toast.makeText(requireContext(), "Please check your mail and password", Toast.LENGTH_SHORT).show()
        })
    }


    private fun navigateActivity(activity: Activity) {
        LTApplicationClass.sharedPreference.setLoginStatus(true)
        startActivity(Intent(requireActivity(), activity::class.java))
        requireActivity().finish()
    }

    fun isValidEmail(email: String): Boolean {
        val emailRegex = "^[A-Za-z](.*)([@]{1})(.{1,})(\\.)(.{1,})"
        return email.matches(emailRegex.toRegex())
    }

}

