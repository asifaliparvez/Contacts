package com.asifaliparvez.contacts.adapter

import android.Manifest
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.net.Uri
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContracts
import androidx.core.app.ActivityCompat
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.RecyclerView
import com.asifaliparvez.contacts.databinding.NumberListItemsBinding
import com.asifaliparvez.contacts.fragments.ContactDetailFragment
import org.w3c.dom.Text

class NumberListAdapter(private val array: ArrayList<String>, private val context: Context):RecyclerView.Adapter<NumberListAdapter.ViewHolder>() {
    inner class ViewHolder( binding:NumberListItemsBinding):RecyclerView.ViewHolder(binding.root){
        val textNumber:TextView = binding.textViewNumber
        val callImageBtn = binding.callImageBtn
        val messageImageBtn = binding.messageImageBtn


    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = NumberListItemsBinding.inflate(LayoutInflater.from(parent.context),parent, false)
        return ViewHolder(view)

    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val item = array[position]
        holder.textNumber.text = item

        holder.callImageBtn.setOnClickListener {
            if (ActivityCompat.checkSelfPermission(context, Manifest.permission.CALL_PHONE) == PackageManager.PERMISSION_GRANTED){
                val intent = Intent(Intent.ACTION_CALL).apply {
                    data = Uri.parse("tel:${holder.textNumber.text}")}
                context.startActivity(intent)
            }else{
                Toast.makeText(context, "Call Permission Required", Toast.LENGTH_SHORT).show()
            }
        }
        holder.messageImageBtn.setOnClickListener {
            val intent = Intent(Intent.ACTION_VIEW)
            intent.data = Uri.parse("sms:${holder.textNumber.text}")
            context.startActivity(intent)

        }
    }

    override fun getItemCount(): Int {
       return array.size
    }
}