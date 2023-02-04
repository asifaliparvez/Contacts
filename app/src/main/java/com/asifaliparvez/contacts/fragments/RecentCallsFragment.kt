package com.asifaliparvez.contacts.fragments

import android.content.pm.PackageManager
import android.os.Build
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.contract.ActivityResultContracts
import androidx.annotation.RequiresApi
import androidx.core.app.ActivityCompat
import androidx.recyclerview.widget.LinearLayoutManager
import com.asifaliparvez.contacts.adapter.CallsLogAdapter
import com.asifaliparvez.contacts.databinding.FragmentRecentCallsBinding
import com.asifaliparvez.contacts.repo.ContactRepository


class RecentCallsFragment : Fragment() {
    private var _binding:FragmentRecentCallsBinding? = null
    private val binding get() = _binding!!
    private lateinit var launcher: ActivityResultLauncher<Array<String>>
    private val permission = arrayOf(android.Manifest.permission.READ_CALL_LOG)

    @RequiresApi(Build.VERSION_CODES.O)
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentRecentCallsBinding.inflate(inflater, container, false)
        launcher  = registerForActivityResult(ActivityResultContracts.RequestMultiplePermissions()){
            if (it[android.Manifest.permission.READ_CALL_LOG] == true){
                setUpRecyclerView()
            }
        }
        if (ActivityCompat.checkSelfPermission(requireContext(), android.Manifest.permission.READ_CALL_LOG) != PackageManager.PERMISSION_GRANTED){
            launcher.launch(permission)
        }else{
            setUpRecyclerView()
        }


        return binding.root
    }

    @RequiresApi(Build.VERSION_CODES.O)
    private fun setUpRecyclerView(){
        val layoutManager = LinearLayoutManager(requireContext(), LinearLayoutManager.VERTICAL, false)
        val array = ContactRepository(requireContext()).getCallLogs()
        val adapter = CallsLogAdapter(this, array)
        binding.recyclerView.apply {
            this.layoutManager = layoutManager
            this.adapter = adapter
        }

    }

}