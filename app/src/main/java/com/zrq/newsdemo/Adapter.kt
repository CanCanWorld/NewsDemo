package com.zrq.newsdemo

import android.annotation.SuppressLint
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import androidx.viewbinding.ViewBinding

open class Adapter<T : Any, V : ViewBinding>(
    var call: (binding: V, p: Int, t: T) -> Unit,
    var binding: (inflater: LayoutInflater, parent: ViewGroup) -> V,
    var data: MutableList<T> = mutableListOf()
) : RecyclerView.Adapter<ViewHolder<V>>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder<V> {
        return ViewHolder(binding(LayoutInflater.from(parent.context),parent))
    }

    override fun onBindViewHolder(holder: ViewHolder<V>, position: Int) {
        call(holder.binding, position, data[position])
    }

    override fun getItemCount(): Int {
        return data.size
    }

    @SuppressLint("NotifyDataSetChanged")
    fun addNews(list: MutableList<T>){
        data.addAll(list)
        notifyDataSetChanged()
    }
}

class ViewHolder<T : ViewBinding>(var binding: T) : RecyclerView.ViewHolder(binding.root)
