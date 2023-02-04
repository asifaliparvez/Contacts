package com.asifaliparvez.contacts.fragments

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.asifaliparvez.contacts.databinding.FragmentAddContactsBinding
import com.asifaliparvez.contacts.repo.ContactRepository


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
                    ContactRepository(requireContext()).addContact(binding.editTextNumber.text.trim().toString(),
                        binding.editTextName.text.trim().toString())
                }


            }

        }
        return  binding.root
    }



}