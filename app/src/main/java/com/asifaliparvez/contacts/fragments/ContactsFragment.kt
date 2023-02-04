package com.asifaliparvez.contacts.fragments

import android.Manifest
import android.content.Intent
import android.content.pm.PackageManager
import android.net.Uri
import android.os.Bundle
import android.provider.ContactsContract
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.EditText
import android.widget.ImageView
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContracts
import androidx.core.app.ActivityCompat
import androidx.core.widget.addTextChangedListener
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import com.asifaliparvez.contacts.R
import com.asifaliparvez.contacts.adapter.ContactsAdapter
import com.asifaliparvez.contacts.databinding.FragmentHomeBinding
import com.asifaliparvez.contacts.repo.ContactRepository
import com.asifaliparvez.contacts.model.ContactsModel
import com.google.android.material.bottomsheet.BottomSheetDialog


class ContactsFragment : Fragment() {
    private var _binding:FragmentHomeBinding? = null
    private val binding get() = _binding!!
    private lateinit var contactAdapter:ContactsAdapter
    private lateinit var contactsArray: ArrayList<ContactsModel>
    private val columns: Array<String> = arrayOf(
        ContactsContract.Contacts.DISPLAY_NAME,
        ContactsContract.Contacts.LOOKUP_KEY,
        ContactsContract.Contacts._ID,
        ContactsContract.Contacts.PHOTO_URI )
    private val permissions = arrayOf(
        Manifest.permission.READ_CONTACTS,
        Manifest.permission.WRITE_CONTACTS,
        Manifest.permission.CALL_PHONE)

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
        editTextSearchSetup()
        binding.floatingActionButton.setOnClickListener {
            showBottomSheet()
        }
        binding.profileImageView.setOnClickListener {
            createNewContact()
        }
        binding.textViewName.setOnClickListener {
            createNewContact()

        }
        return binding.root
    }

    private fun createNewContact() {
        val intent = Intent(Intent.ACTION_INSERT)
        intent.type = ContactsContract.RawContacts.CONTENT_TYPE
        startActivity(intent)

    }

    private fun showBottomSheet(){
        val dialog = BottomSheetDialog(requireContext())
        val view  = layoutInflater.inflate(R.layout.fragment_phone_dialer_list_dialog, null)
        val oneBtn = view.findViewById<Button>(R.id.oneBtn)
        val twoBtn = view.findViewById<Button>(R.id.twoBtn)
        val threeBtn = view.findViewById<Button>(R.id.threeBtn)
        val fourBtn = view.findViewById<Button>(R.id.fourBtn)
        val fiveBtn = view.findViewById<Button>(R.id.fiveBtn)
        val sixBtn = view.findViewById<Button>(R.id.sixBtn)
        val sevenBtn = view.findViewById<Button>(R.id.sevenBtn)
        val eightBtn = view.findViewById<Button>(R.id.eightBtn)
        val nineBtn = view.findViewById<Button>(R.id.nineBtn)
        val starBtn = view.findViewById<Button>(R.id.starBtn)
        val hashBtn = view.findViewById<Button>(R.id.hashBtn)
        val zeroBtn = view.findViewById<Button>(R.id.zeroBtn)
        val callImageView = view.findViewById<ImageView>(R.id.call_Image_View)
        val numberEditText = view.findViewById<EditText>(R.id.number_EditText)
        val closeImageVIew = view.findViewById<ImageView>(R.id.closeImageView)
        callImageView.setOnClickListener { 
            if (numberEditText.text.trim().isNotEmpty()){
                if (ActivityCompat.checkSelfPermission(requireContext(), Manifest.permission.CALL_PHONE) == PackageManager.PERMISSION_GRANTED){
                    val intent = Intent(Intent.ACTION_CALL).apply {
                        data = Uri.parse("tel:${numberEditText.text}")
                    }
                    startActivity(intent)
                
            }else{
                    Toast.makeText(requireContext(), "Permission Needed", Toast.LENGTH_SHORT).show()
                }
            }
        }
        oneBtn.setOnClickListener {
            numberEditText.text.append("1")
        }
        twoBtn.setOnClickListener {
            numberEditText.text.append("2")
        }
        threeBtn.setOnClickListener {
            numberEditText.text.append("3")
        }
        fourBtn.setOnClickListener {
            numberEditText.text.append("4")
        }
        fiveBtn.setOnClickListener {
            numberEditText.text.append("5")
        }
        sixBtn.setOnClickListener {
            numberEditText.text.append("6")
        }
        sevenBtn.setOnClickListener {
            numberEditText.text.append("7")
        }
        eightBtn.setOnClickListener {
            numberEditText.text.append("8")
        }
        nineBtn.setOnClickListener {
            numberEditText.text.append("9")
        }
        zeroBtn.setOnClickListener {
            numberEditText.text.append("0")
        }
        starBtn.setOnClickListener {
            numberEditText.text.append("*")
        }
        hashBtn.setOnClickListener {
            numberEditText.append("#")
        }
        closeImageVIew.setOnClickListener {
            if (numberEditText.text.trim().isNotEmpty()){
                val builder = StringBuilder()

                builder.append(numberEditText.text)
                builder.setLength(builder.length -1)
                numberEditText.setText(builder.toString())
            }
        }
        dialog.setContentView(view)
        dialog.show()

    }
    private fun editTextSearchSetup() {
        // below line is to call set on query text listene
        // r method.
        binding.editText.addTextChangedListener {
            updateAdapter(it.toString())
        }

    }
    private fun updateAdapter(text:String){
        val filterList = ArrayList<ContactsModel>()
        for (i in contactsArray){
            if (i.name.lowercase().contains(text.lowercase())){
                filterList.add(i)
            }
        }
        if (filterList.isNotEmpty()){
            contactAdapter.filterList(filterList)
        }




    }
    private fun setUpRecyclerView() {
         contactsArray = ContactRepository(requireContext()).getAllContacts(columns)

        contactAdapter = ContactsAdapter(contactsArray,  requireContext())
        binding.recycleView.apply {
            layoutManager = LinearLayoutManager(requireContext(), LinearLayoutManager.VERTICAL, false)
            adapter = contactAdapter
        }



    }


}
