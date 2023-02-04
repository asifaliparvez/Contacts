package com.asifaliparvez.contacts.adapter

import android.Manifest
import android.content.ClipData
import android.content.ClipboardManager
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.net.Uri
import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.content.res.AppCompatResources
import androidx.core.app.ActivityCompat
import androidx.recyclerview.widget.RecyclerView
import com.asifaliparvez.contacts.R
import com.asifaliparvez.contacts.databinding.NumberListItemsBinding

class NumberListAdapter( val array: ArrayList<String>, private val context: Context):RecyclerView.Adapter<NumberListAdapter.ViewHolder>() {
    inner class ViewHolder( binding:NumberListItemsBinding):RecyclerView.ViewHolder(binding.root){
        val textNumber:TextView = binding.textViewNumber
        val callImageView = binding.callImageView
        val messageImageView = binding.messageImageView
        val copyImageView = binding.copyImageView


    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = NumberListItemsBinding.inflate(LayoutInflater.from(parent.context),parent, false)
        return ViewHolder(view)

    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val item = array[position]
        holder.textNumber.text = item
        holder.callImageView.setImageDrawable(AppCompatResources.getDrawable(context, R.drawable.baseline_call_24))

        holder.messageImageView.setImageDrawable(AppCompatResources.getDrawable(context, R.drawable.baseline_message_24))

        holder.callImageView.setOnClickListener {
            if (ActivityCompat.checkSelfPermission(context, Manifest.permission.CALL_PHONE) == PackageManager.PERMISSION_GRANTED){
                val intent = Intent(Intent.ACTION_CALL).apply {
                    data = Uri.parse("tel:${holder.textNumber.text}")}
                context.startActivity(intent)
            }else{
                Toast.makeText(context, "Call Permission Required", Toast.LENGTH_SHORT).show()
            }
        }
        holder.messageImageView.setOnClickListener {
            val intent = Intent(Intent.ACTION_VIEW)
            intent.data = Uri.parse("sms:${holder.textNumber.text}")
            context.startActivity(intent)

        }
        holder.copyImageView.setOnClickListener {
            val clipboardManager = context.getSystemService(Context.CLIPBOARD_SERVICE) as ClipboardManager
            val clipData = ClipData.newPlainText(item, item)
            clipboardManager.setPrimaryClip(clipData)
            Toast.makeText(context, "Number Copied", Toast.LENGTH_SHORT).show()

        }
    }

    override fun getItemCount(): Int {
       return array.size
    }
}