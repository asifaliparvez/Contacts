package com.asifaliparvez.contacts.fragments

import android.Manifest
import android.app.Dialog
import android.content.Context
import android.content.DialogInterface
import android.content.pm.PackageManager
import android.net.Uri
import android.os.Bundle
import android.provider.CallLog
import android.telecom.Call
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.DatePicker
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AlertDialog
import androidx.core.app.ActivityCompat
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import com.asifaliparvez.contacts.adapter.NumberListAdapter
import com.asifaliparvez.contacts.databinding.FragmentContactDetailBinding
import com.asifaliparvez.contacts.managers.ContactsManager
import com.asifaliparvez.contacts.model.CallLogsModel


class ContactDetailFragment : Fragment() {
    private var _binding: FragmentContactDetailBinding? = null
    private val binding get() = _binding!!
    private  var lookUpKey: String? = null
    private  var name: String? = null
    private lateinit var contactsManager: ContactsManager
    private lateinit var numberListAdapter: NumberListAdapter


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        //   val columns = arrayOf(ContactsContract.CommonDataKinds.Phone.)
        _binding = FragmentContactDetailBinding.inflate(inflater, container, false)
        binding.toolbar.setNavigationOnClickListener {
            activity?.onBackPressed()
        }
        name = arguments?.getString("name")
        lookUpKey = arguments?.getString("lookUpKey")
        setUpRecyclerView()
        val launcher = registerForActivityResult(ActivityResultContracts.RequestPermission()){}
        if (ActivityCompat.checkSelfPermission(requireContext(), Manifest.permission.CALL_PHONE) != PackageManager.PERMISSION_GRANTED){
            launcher.launch(Manifest.permission.CALL_PHONE)
        }




        contactsManager = ContactsManager(requireContext())
        binding.textViewName.text = name
        binding.imageBtnDelete.setOnClickListener {
            val builder = AlertDialog.Builder(requireContext())
            builder.setTitle("Delete This Contact?")
            builder.setMessage("Do you want to delete $name info?")
            builder.setNegativeButton("Cancel"
            ) { _, _ ->
                // User cancelled the dialog
            }
            builder.setPositiveButton("Delete"){ _, _ ->
                val  rows = contactsManager.deleteContact(lookUpKey!!)
                if (rows > 0){
                    Toast.makeText(requireContext(),"$name Deleted Successfully", Toast.LENGTH_SHORT).show()
                    activity?.onBackPressed()
                }

            }
            builder.show()



        }




        return  binding.root
    }



    private fun setUpRecyclerView(){
        val numberList = ContactsManager(requireContext()).contactDetail(lookUpKey!!,name!!)
        numberListAdapter = NumberListAdapter(numberList,requireContext())
        binding.recycleView.apply {
            layoutManager = LinearLayoutManager(requireContext(), LinearLayoutManager.VERTICAL, false)
            adapter = numberListAdapter
        }
    }








}