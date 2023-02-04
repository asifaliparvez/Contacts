package com.asifaliparvez.contacts.fragments

import android.Manifest
import android.content.ActivityNotFoundException
import android.content.Intent
import android.content.pm.PackageManager
import android.net.Uri
import android.os.Bundle
import android.provider.ContactsContract.Contacts
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AlertDialog
import androidx.core.app.ActivityCompat
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import com.asifaliparvez.contacts.R
import com.asifaliparvez.contacts.adapter.NumberListAdapter
import com.asifaliparvez.contacts.databinding.FragmentContactDetailBinding
import com.asifaliparvez.contacts.repo.ContactRepository


class ContactDetailFragment : Fragment() {
    private var _binding: FragmentContactDetailBinding? = null
    private val binding get() = _binding!!
    private  var lookUpKey: String? = null
    private  var name: String? = null
    private var id:String = ""
    private lateinit var contactsManager: ContactRepository
    private lateinit var numberListAdapter: NumberListAdapter
    private var selectedContactUri:Uri? = null
    private lateinit var launcher:ActivityResultLauncher<Array<String>>
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
        id = arguments?.getString("id", "")!!
        selectedContactUri = Contacts.getLookupUri(id.toLong(),lookUpKey )

        setUpToolBar()
        setUpRecyclerView()
        setUpViews()
        setUpOnClicks()

        contactsManager = ContactRepository(requireContext())

        launcher =registerForActivityResult(ActivityResultContracts.RequestMultiplePermissions()){
            if (it[Manifest.permission.CALL_PHONE] == true){
                setUpRecyclerView()
            }else{
                launcher.launch(arrayOf(Manifest.permission.CALL_PHONE))

            }
        }

        return  binding.root
    }
    private fun setUpViews(){
        binding.textViewName.text = name
        binding.textViewProfile.text = name?.get(0).toString()
    }
    private fun setUpOnClicks(){
        val number = numberListAdapter.array[0]
        binding.imageViewCall.setOnClickListener {
            if (ActivityCompat.checkSelfPermission(requireContext(), Manifest.permission.CALL_PHONE) != PackageManager.PERMISSION_GRANTED){
                launcher.launch(arrayOf(Manifest.permission.CALL_PHONE) )
            }else{
               val intent = Intent(Intent.ACTION_CALL)

                intent.data = Uri.parse("tel:$number")
            }

        }
        binding.imageViewVideo.setOnClickListener {
            val data = Uri.parse("tel:$number")
            val intent = Intent("videocall")
            intent.putExtra("videoCall", true)
            intent.data = data
            try {
                startActivity(intent)
            }catch (exeption:ActivityNotFoundException){
                Toast.makeText(requireContext(), "Cannot make video call! ", Toast.LENGTH_SHORT).show()
            }
        }
        binding.imageViewMessage.setOnClickListener{
            startActivity(Intent(Intent.ACTION_VIEW, Uri.fromParts("sms", number, null)))
        }
    }



    private fun setUpToolBar() {
        binding.toolbar.setOnMenuItemClickListener {
            when(it.itemId){
                R.id.menu_edit ->{
                    val intent = Intent(Intent.ACTION_EDIT).apply {
                        setDataAndType(selectedContactUri, Contacts.CONTENT_ITEM_TYPE)

                    }
                    startActivity(intent)

                }
                R.id.menu_delete ->{
                    deleteContactDialog()

                }
            }
            false
        }
    }


    private fun setUpRecyclerView(){
        val numberList = ContactRepository(requireContext()).contactDetail(lookUpKey!!,name!!)
        numberListAdapter = NumberListAdapter(numberList,requireContext())
        binding.recycleView.apply {
            layoutManager = LinearLayoutManager(requireContext(), LinearLayoutManager.VERTICAL, false)
            adapter = numberListAdapter
        }
    }

    private fun deleteContactDialog(){
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








}