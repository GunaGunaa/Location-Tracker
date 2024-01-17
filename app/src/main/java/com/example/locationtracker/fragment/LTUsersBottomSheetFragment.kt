package com.example.locationtracker.fragment

import android.annotation.SuppressLint
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.locationtracker.LTApplicationClass
import com.example.locationtracker.adapter.UsersAdapter
import com.example.locationtracker.databinding.LtBottomSheetFragmentUsersBinding
import com.example.locationtracker.listener.SwitchUserItemClickListener
import com.example.locationtracker.listener.UsersItemClickListener
import com.example.locationtracker.model.UserRealmModel
import com.example.locationtracker.viewmodel.LTUsersViewModel
import com.google.android.material.bottomsheet.BottomSheetDialogFragment


class LTUsersBottomSheetFragment(private var listener: SwitchUserItemClickListener) :
    BottomSheetDialogFragment(), UsersItemClickListener {
    private lateinit var binding: LtBottomSheetFragmentUsersBinding
    private lateinit var viewModel: LTUsersViewModel
    private lateinit var adapter: UsersAdapter

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = LtBottomSheetFragmentUsersBinding.inflate(inflater, container, false)
        return binding.root
    }


    @SuppressLint("NotifyDataSetChanged")
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        viewModel = ViewModelProvider(this)[LTUsersViewModel::class.java]
        viewModel.getAllUsers()
        viewModel.usersList.observe(viewLifecycleOwner, Observer {
            binding.rvUsers.layoutManager = LinearLayoutManager(requireContext())
            adapter = UsersAdapter(requireContext(), it, this)
            binding.rvUsers.adapter = adapter
            adapter.notifyDataSetChanged()
        })
    }

    override fun userItemClickListener(user: UserRealmModel) {
        listener.getSwitchUSerItemClickListener(user.mail)
        LTApplicationClass.sharedPreference.setMail(user.mail)
        dismiss()
    }

}