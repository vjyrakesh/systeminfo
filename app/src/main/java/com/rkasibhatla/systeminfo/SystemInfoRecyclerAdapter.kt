package com.rkasibhatla.systeminfo

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
open class ListItem(val itemType:String, val itemTitle: String, val itemSubtitle: String)
class HeaderListItem(itemTitle: String): ListItem("header", itemTitle, "")
class ChildListItem(itemTitle: String, itemSubtitle: String): ListItem("child", itemTitle, itemSubtitle)
class SystemInfoRecyclerAdapter(val itemsList: List<ListItem>) : RecyclerView.Adapter<RecyclerView.ViewHolder>() {

    inner class HeaderItemViewHolder(itemView: View): RecyclerView.ViewHolder(itemView) {

        val headerNameView: TextView

        init {
            headerNameView = itemView.findViewById(R.id.list_item_header)
        }
    }

    inner class ChildItemViewHolder(itemView: View): RecyclerView.ViewHolder(itemView) {
        val itemTitleView: TextView
        val itemSubtitleView: TextView

        init {
            itemTitleView = itemView.findViewById(R.id.list_item_title)
            itemSubtitleView = itemView.findViewById(R.id.list_item_subtitle)
        }
    }

    override fun getItemViewType(position: Int): Int {
        val item = itemsList[position]
        when(item.itemType) {
            "header" -> return 0
            "child" -> return 1
        }
        return 0
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        lateinit var viewHolder: RecyclerView.ViewHolder
        when(viewType) {
            0 -> {
                val view = LayoutInflater.from(parent.context).inflate(R.layout.list_header, parent, false)
                viewHolder = HeaderItemViewHolder(view)
            }
            1 -> {
                val view = LayoutInflater.from(parent.context).inflate(R.layout.list_item, parent, false)
                viewHolder = ChildItemViewHolder(view)
            }
        }
        return viewHolder
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        val item = itemsList[position]
        when(item.itemType) {
            "header" -> {
                (holder as HeaderItemViewHolder).headerNameView.text = item.itemTitle
            }
            "child" -> {
                (holder as ChildItemViewHolder).itemTitleView.text = item.itemTitle
                holder.itemSubtitleView.text = item.itemSubtitle
            }
        }
    }

    override fun getItemCount(): Int {
        return itemsList.size
    }

}