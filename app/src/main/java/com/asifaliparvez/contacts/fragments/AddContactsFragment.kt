package com.asifaliparvez.contacts.fragments

import android.Manifest
import android.content.Intent
import android.content.pm.PackageManager
import android.os.Bundle
import android.provider.ContactsContract
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.activity.result.contract.ActivityResultContracts
import androidx.core.app.ActivityCompat
import com.asifaliparvez.contacts.R
import com.asifaliparvez.contacts.databinding.FragmentAddContactsBinding
import com.asifaliparvez.contacts.managers.ContactsManager


class AddContactsFragment : Fragment() {
    private  var _binding: FragmentAddContactsBinding? = null
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentAddContactsBinding.inflate(layoutInflater, container, false)
        binding.toolbar.setNavigationOnClickListener {
            activity?.onBackPressed()
        }

        binding.addBtn.setOnClickListener {
                when{
                binding.editTextName.text.isEmpty() ->{

                    binding.editTextName.error = "Enter Name"
                }
                binding.editTextNumber.text.toString().isEmpty() ->{
                    binding.editTextNumber.error = "Enter Phone Number"
                }
                else ->{
                    ContactsManager(requireContext()).addContact(binding.editTextNumber.text.trim().toString(),
                        binding.editTextName.text.trim().toString())
                }


            }

        }
        return  binding.root
    }



}