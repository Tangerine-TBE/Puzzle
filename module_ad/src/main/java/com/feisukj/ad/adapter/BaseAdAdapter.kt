package com.feisukj.ad.adapter

import androidx.recyclerview.widget.RecyclerView

abstract class BaseAdAdapter : RecyclerView.Adapter<RecyclerView.ViewHolder>() {
    abstract fun addAdList(list: List<Any>?)
}