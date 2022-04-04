package com.asifaliparvez.contacts.fragments

import android.Manifest
import android.content.pm.PackageManager
import android.os.Bundle
import android.provider.ContactsContract
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.SearchView
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContracts
import androidx.core.app.ActivityCompat
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.asifaliparvez.contacts.R
import com.asifaliparvez.contacts.adapter.ContactsAdapter
import com.asifaliparvez.contacts.databinding.FragmentHomeBinding
import com.asifaliparvez.contacts.managers.ContactsManager


class HomeFragment : Fragment() {
    private var _binding:FragmentHomeBinding? = null
    private val binding get() = _binding!!
    private lateinit var contactAdapter:ContactsAdapter
    private val columns: Array<String> = arrayOf(
        ContactsContract.Contacts.DISPLAY_NAME,
        ContactsContract.Contacts.LOOKUP_KEY,
        ContactsContract.Contacts._ID,
        ContactsContract.Contacts.PHOTO_URI )

    private val permissions = arrayOf(
        Manifest.permission.READ_CONTACTS,
        Manifest.permission.WRITE_CONTACTS,
        Manifest.permission.READ_CALL_LOG,
        Manifest.permission.WRITE_CALL_LOG)

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentHomeBinding.inflate(inflater, container,false)
        //Checking Permissions
        val launcher =registerForActivityResult(ActivityResultContracts.RequestMultiplePermissions()){
            if (it[Manifest.permission.READ_CONTACTS] == true){
                setUpRecyclerView()
            }
        }
        if (ActivityCompat.checkSelfPermission(requireContext(), Manifest.permission.READ_CONTACTS) != PackageManager.PERMISSION_GRANTED){
            launcher.launch(permissions)

        }else{
            setUpRecyclerView()
        }
        binding.floatingButton.setOnClickListener {
            findNavController().navigate(R.id.action_homeFragment_to_addContacts)

        }
        return binding.root
    }
    private fun setUpRecyclerView() {
        val contactsArray = ContactsManager(requireContext()).getAllContacts(columns)

        contactAdapter = ContactsAdapter(contactsArray,  requireContext())
        binding.recycleView.apply {
            layoutManager = LinearLayoutManager(requireContext(), LinearLayoutManager.VERTICAL, false)
            adapter = contactAdapter
        }



}


}
