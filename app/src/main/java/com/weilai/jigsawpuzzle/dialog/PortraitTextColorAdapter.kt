package com.weilai.jigsawpuzzle.dialog

import android.graphics.Color
import android.graphics.drawable.GradientDrawable
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.weilai.jigsawpuzzle.R
import com.weilai.jigsawpuzzle.adapter.RecyclerViewHolder
import com.weilai.jigsawpuzzle.util.DensityUtil

class PortraitTextColorAdapter: RecyclerView.Adapter<RecyclerViewHolder>() {

    private var mList:Array<Int> = arrayOf()
    private var selectItem = 0
    private var callback: PortraitTextColorCallback? = null

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerViewHolder {
        return RecyclerViewHolder(LayoutInflater.from(parent.context).inflate(R.layout.item_portrait_text_color,parent,false))
    }

    override fun onBindViewHolder(holder: RecyclerViewHolder, position: Int) {
        holder.getView<View>(R.id.colorBg).background = getBg(mList[position],position == selectItem)
//        holder.getView<View>(R.id.colorBg).setBackgroundColor(mList[position])
//        if (position == selectItem){
//            holder.getView<View>(R.id.colorSelect).visibility = View.VISIBLE
//        }else{
//            holder.getView<View>(R.id.colorSelect).visibility = View.GONE
//        }
        holder.itemView.setOnClickListener {
            val old = selectItem
            selectItem = position
            callback?.selectTextColor(position)
            notifyDataSetChanged()
        }
    }

    override fun getItemCount(): Int {
        return mList.size
    }

    fun setData(list: Array<Int>){
        this.mList = list
        notifyDataSetChanged()
    }

    fun setCallback(callback: PortraitTextColorCallback){
        this.callback = callback
    }

    private fun getBg(color: Int,boolean: Boolean): GradientDrawable{
        val drawable = GradientDrawable()
        drawable.setColor(color)
        drawable.cornerRadius = DensityUtil.dipTopx(4f)
        if (boolean){
            drawable.setStroke(DensityUtil.dipTopxAsInt(1f), Color.parseColor("#ffffff"))
        }
        drawable.shape = GradientDrawable.RECTANGLE
        return drawable
    }

    interface PortraitTextColorCallback{
        fun selectTextColor(position: Int)
    }
}