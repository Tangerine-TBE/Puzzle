package com.weilai.jigsawpuzzle.adapter.special

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.weilai.jigsawpuzzle.R
import com.weilai.jigsawpuzzle.adapter.RecyclerViewHolder
import com.weilai.jigsawpuzzle.bean.FilterBean
import com.weilai.jigsawpuzzle.util.BitmapUtils

class MagicCameraAdapter : RecyclerView.Adapter<RecyclerViewHolder>() {

    private var mList: Array<FilterBean> = arrayOf()
    private var selectItem = 0
    private var callback: MagicCameraCallback? = null
    private lateinit var mContext: Context

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerViewHolder {
        mContext = parent.context
        return RecyclerViewHolder(LayoutInflater.from(parent.context).inflate(R.layout.item_magic_camera,parent,false))
    }

    override fun onBindViewHolder(holder: RecyclerViewHolder, position: Int) {
        holder.setRadiusImage(R.id.img,8f, BitmapUtils.getBitmapFromAssetsFile(mList[position].img))
        holder.setText(R.id.name,mList[position].filterName)
        if (position == selectItem){
            holder.getView<View>(R.id.selectBorder).visibility = View.VISIBLE
        }else{
            holder.getView<View>(R.id.selectBorder).visibility = View.GONE
        }
        if (mList[position].needVip){
            holder.getView<TextView>(R.id.vip_tip).visibility = View.VISIBLE
        }else{
            holder.getView<TextView>(R.id.vip_tip).visibility = View.GONE
        }
        holder.itemView.setOnClickListener {
            if (!(mList[position].needVip)){
                val old = selectItem
                selectItem = position
                notifyItemChanged(old)
                notifyItemChanged(selectItem)
            }
            callback?.clickFilterItem(position)
        }
    }

    override fun getItemCount(): Int {
        return mList.size
    }

    fun setData(mList: Array<FilterBean>){
        this.mList = mList
        notifyDataSetChanged()
    }

    fun setCallBack(callback: MagicCameraCallback){
        this.callback = callback
    }

    interface MagicCameraCallback{
        fun clickFilterItem(position: Int)
    }
}