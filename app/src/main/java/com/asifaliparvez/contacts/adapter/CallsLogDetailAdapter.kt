package com.asifaliparvez.contacts.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.RecyclerView
import com.asifaliparvez.contacts.databinding.CallLogsDetailsItemsBinding
import com.asifaliparvez.contacts.model.CallLogsModel

class CallsLogDetailAdapter(var fragment: Fragment, var arrayList:ArrayList<CallLogsModel>):RecyclerView.Adapter<CallsLogDetailAdapter.ViewHolder>() {
    inner class ViewHolder(val binding:CallLogsDetailsItemsBinding):RecyclerView.ViewHolder(binding.root){

    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = CallLogsDetailsItemsBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {

    }

    override fun getItemCount(): Int {
      return arrayList.size
    }

}