package com.asifaliparvez.contacts.adapter

import android.content.Context
import android.net.Uri
import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.content.res.AppCompatResources
import androidx.core.os.bundleOf
import androidx.navigation.findNavController
import androidx.recyclerview.widget.RecyclerView
import com.asifaliparvez.contacts.R
import com.asifaliparvez.contacts.databinding.ContactsItemsBinding
import com.asifaliparvez.contacts.model.ContactsModel

class ContactsAdapter(var arrayList:ArrayList<ContactsModel>, var context:Context):RecyclerView.Adapter<ContactsAdapter.MyViewHolder>() {

    inner class  MyViewHolder(var binding:ContactsItemsBinding):RecyclerView.ViewHolder(binding.root){
        val textViewName:TextView = binding.textViewName
        val imageView:ImageView = binding.profileImageView
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyViewHolder {
        val view = ContactsItemsBinding.inflate(LayoutInflater.from(parent.context), parent,false)
        return MyViewHolder(view)

    }

    override fun onBindViewHolder(holder: MyViewHolder, position: Int) {
        val sorted = arrayList.sortedWith(compareBy {
            it.name
        })
        sorted.size
        val item = arrayList.elementAt(position)
        holder.textViewName.text = item.name

        if(item.photo != "null"){
            holder.imageView.setImageURI(Uri.parse(item.photo))
        }else{
            holder.imageView.setImageDrawable(AppCompatResources.getDrawable(context, R.drawable.user))
        }
        holder.itemView.setOnClickListener {
            val bundle = bundleOf(
                "name" to holder.textViewName.text.toString(),
                "lookUpKey" to item.lookUpKey
            )
            it.findNavController().navigate(R.id.action_homeFragment_to_contactDetail, bundle)
        }

    }

    override fun getItemCount(): Int {
        return  arrayList.size
    }
}