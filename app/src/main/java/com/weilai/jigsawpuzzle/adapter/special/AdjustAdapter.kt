package com.weilai.jigsawpuzzle.adapter.special

import android.graphics.Color
import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.weilai.jigsawpuzzle.R
import com.weilai.jigsawpuzzle.adapter.RecyclerViewHolder
import com.weilai.jigsawpuzzle.bean.AdjustBean

class AdjustAdapter : RecyclerView.Adapter<RecyclerViewHolder>() {

    private var mList: Array<AdjustBean> = arrayOf()
    private var selectItem = 0
    private var callback: AdjustAdapterCallback? = null

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerViewHolder {
        return RecyclerViewHolder(LayoutInflater.from(parent.context).inflate(R.layout.item_adjust,parent,false))
    }

    override fun onBindViewHolder(holder: RecyclerViewHolder, position: Int) {
        holder.setText(R.id.name,mList[position].name)
        if (position == selectItem){
            holder.setImage(R.id.image, mList[position].img_y)
            holder.getView<TextView>(R.id.name).setTextColor(Color.parseColor("#5B92FF"))
        }
        else{
            holder.setImage(R.id.image, mList[position].img_n)
            holder.getView<TextView>(R.id.name).setTextColor(Color.parseColor("#16181A"))
        }
        holder.itemView.setOnClickListener {
            val old = selectItem
            selectItem = position
            notifyItemChanged(old)
            notifyItemChanged(selectItem)
            callback?.clickAdjustItem(mList[position].name)
        }
    }

    override fun getItemCount(): Int {
        return mList.size
    }

    fun setData(list: Array<AdjustBean>){
        this.mList = list
        notifyDataSetChanged()
    }

    fun setCallback(callback: AdjustAdapterCallback){
        this.callback = callback
    }

    interface AdjustAdapterCallback{
        fun clickAdjustItem(type: String)
    }
}