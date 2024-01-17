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
import com.example.locationtracker.activity.LTHomeActivity
import com.example.locationtracker.common.AlertDialogHelper
import com.example.locationtracker.common.LTSharedPreferences
import com.example.locationtracker.databinding.LtFragmentLoginBinding
import com.example.locationtracker.viewmodel.LTLoginViewModel
import java.util.regex.Pattern

class LTLoginFragment : Fragment() {
    private lateinit var binding: LtFragmentLoginBinding
    private lateinit var viewModel: LTLoginViewModel
    private var mail: String = ""

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
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
                mail = etMail.text.toString()
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
            if (it) {
                startActivity(Intent(requireContext(), LTHomeActivity::class.java))
                LTApplicationClass.sharedPreference.setMail(mail)
                requireActivity().finish()
                LTApplicationClass.sharedPreference.setLoginStatus(true)
            } else AlertDialogHelper.showAlertDialog(
                requireContext(),
                "please check the username or password"
            )
        })
    }


//    private fun navigateActivity(activity: Activity) {
//        LTApplicationClass.sharedPreference.setLoginStatus(true)
//        startActivity(Intent(requireActivity(), activity::class.java))
//        requireActivity().finish()
//    }

    private fun isValidEmail(email: String): Boolean {
        val emailRegex = "^[A-Za-z](.*)([@]{1})(.{1,})(\\.)(.{1,})"
        return email.matches(emailRegex.toRegex())
    }

}

