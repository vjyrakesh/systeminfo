package com.rkasibhatla.systeminfo

import android.content.Context
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.activity.viewModels
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModel
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.appbar.MaterialToolbar

class MainActivity : AppCompatActivity() {
    val systemInfoViewModel: SystemInfoViewModel by viewModels()
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)



        val recyclerView = findViewById<RecyclerView>(R.id.recycler_view)
        systemInfoViewModel.setContext(this)
        systemInfoViewModel.refreshSystemInfo()
        val systemInfoRecyclerAdapter = systemInfoViewModel.getItemsList().value?.let {itemsList ->
            SystemInfoRecyclerAdapter(
                itemsList
            )
        }
        val linearLayoutManager = LinearLayoutManager(this)
        recyclerView.layoutManager = linearLayoutManager
        recyclerView.adapter = systemInfoRecyclerAdapter

        val appBar = findViewById<MaterialToolbar>(R.id.app_bar)
        appBar.setOnMenuItemClickListener { menuItem ->
            when(menuItem.itemId) {
                R.id.menu_refresh -> {
                    systemInfoViewModel.refreshSystemInfo()
                    true
                }
                else -> true
            }
        }

        systemInfoViewModel.getItemsList().observe(this, Observer {
            systemInfoRecyclerAdapter?.notifyDataSetChanged()
        })
    }
}

class SystemInfoViewModel: ViewModel() {
    private val itemsList: MutableLiveData<List<ListItem>> = MutableLiveData()
    private lateinit var context: Context

    init {
       // itemsList.value = SystemInfo.getSystemInfo()
    }

    fun getItemsList(): MutableLiveData<List<ListItem>> {
        return itemsList
    }

    fun setContext(ctx: Context) {
        context = ctx
    }

    fun refreshSystemInfo() {
        itemsList.value = SystemInfo.getSystemInfo(context)
    }
}