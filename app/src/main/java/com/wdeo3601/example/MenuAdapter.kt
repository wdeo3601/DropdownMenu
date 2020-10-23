package com.wdeo3601.example

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView

/**
 * Created on 2020/10/23.
 * @author wdeo3601
 * @description
 */
class MenuAdapter(private val dataList: List<String>) :
    RecyclerView.Adapter<MenuAdapter.MenuViewHolder>() {

    var onItemClick: ((menuText: String) -> Unit)? = null

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MenuViewHolder {
        val inflate = LayoutInflater.from(parent.context).inflate(R.layout.item_menu, parent, false)
        return MenuViewHolder(inflate)
    }

    override fun onBindViewHolder(holder: MenuViewHolder, position: Int) {
        holder.setMenuText(dataList[position])
        holder.itemView.setOnClickListener {
            onItemClick?.invoke(dataList[position])
        }
    }

    override fun getItemCount(): Int {
        return dataList.size
    }

    class MenuViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        private val tvMenu by lazy {
            itemView.findViewById<TextView>(R.id.tv_menu)
        }

        fun setMenuText(menuText: String) {
            tvMenu.text = menuText
        }
    }
}