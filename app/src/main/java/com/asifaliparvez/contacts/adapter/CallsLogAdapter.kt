package com.asifaliparvez.contacts.adapter

import android.Manifest
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.net.Uri
import android.provider.CallLog.Calls
import android.util.Log
import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.Toast
import androidx.core.app.ActivityCompat
import androidx.core.os.bundleOf
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.RecyclerView
import com.asifaliparvez.contacts.R
import com.asifaliparvez.contacts.databinding.CallLogsItemsBinding
import com.asifaliparvez.contacts.model.CallLogsModel
import com.asifaliparvez.contacts.repo.ContactRepository
import java.text.SimpleDateFormat
import kotlin.collections.ArrayList

class CallsLogAdapter(val fragment: Fragment, var array: ArrayList<CallLogsModel>):RecyclerView.Adapter<CallsLogAdapter.ViewHolder>() {
    inner class ViewHolder(val binding: CallLogsItemsBinding):RecyclerView.ViewHolder(binding.root){

    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val binding = CallLogsItemsBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return ViewHolder(binding)
    }

    override fun getItemCount(): Int {
      return  array.size
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val item = array[position]
        val profileTextView = holder.binding.profileTextView
        val numberTextView = holder.binding.numberTextView
        val callImageBtn = holder.binding.callImageBtn
        val dateTextView = holder.binding.dateTextView
        val callTypeImageView = holder.binding.callTypeImageView
        val simpleDateFormat = SimpleDateFormat("h:m a, E")
        val date = simpleDateFormat.format(item.date.toLong())
        dateTextView.text =date.toString()


        profileTextView.setOnClickListener {
            //TODO: Implement Call Logs Detail Fragment
//           val array =  ContactRepository(fragment.requireContext()).getCallLogsOfSpecificNumber(item.number)
//            array.forEach {
//                Log.d("Hello", it.number)
//            }
//            val bundle = bundleOf("id" to item.id, "name" to item.name, "number" to item.number)
//            fragment.findNavController().navigate(R.id.action_recentCallsFragment_to_callLogsDetailFragment, bundle)
        }

        if (item.name !=""){
            numberTextView.text = item.name
            profileTextView.text = item.name[0].toString()
        }else{
            profileTextView.text = item.number[item.number.length -1].toString()
            numberTextView.text = item.number

        }
        callImageBtn.setOnClickListener {
            if (ActivityCompat.checkSelfPermission(fragment.requireContext(), Manifest.permission.CALL_PHONE) == PackageManager.PERMISSION_GRANTED){
                val intent = Intent(Intent.ACTION_CALL).apply {
                    data = Uri.parse("tel:${item.number}")}
                fragment.startActivity(intent)
            }else{
                Toast.makeText(fragment.requireContext(), "Call Permission Required", Toast.LENGTH_SHORT).show()
            }
        }



        when(item.type.toInt()){
            Calls.OUTGOING_TYPE ->{
                callTypeImageView.setImageResource(R.drawable.baseline_call_made_24)

            }
            Calls.INCOMING_TYPE ->{
                callTypeImageView.setImageResource(R.drawable.baseline_call_received_24)
            }
            Calls.MISSED_TYPE ->{
                callTypeImageView.setImageResource(R.drawable.baseline_call_missed_24)
            }

        }

    }
}