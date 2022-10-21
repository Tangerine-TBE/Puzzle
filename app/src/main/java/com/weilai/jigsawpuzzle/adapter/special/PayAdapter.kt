package com.weilai.jigsawpuzzle.adapter.special

import android.annotation.SuppressLint
import android.graphics.Color
import android.graphics.Paint
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.RelativeLayout
import android.widget.TextView
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.recyclerview.widget.RecyclerView
import com.weilai.jigsawpuzzle.R
import com.weilai.jigsawpuzzle.adapter.RecyclerViewHolder
import com.weilai.jigsawpuzzle.bean.PayBean
import com.weilai.jigsawpuzzle.util.SizeUtils
import com.weilai.jigsawpuzzle.util.Utils

class PayAdapter:RecyclerView.Adapter<RecyclerViewHolder>() {

    private var mList: Array<PayBean> = arrayOf()
    private var selectItem = 0
    private var callback: PriceCallback? = null

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerViewHolder {
        return RecyclerViewHolder(LayoutInflater.from(parent.context).inflate(R.layout.item_vip_price,parent,false))
    }

    @SuppressLint("SetTextI18n")
    override fun onBindViewHolder(holder: RecyclerViewHolder, position: Int) {
        holder.getView<TextView>(R.id.tv_bottom).apply {
            this.paint.flags = Paint.STRIKE_THRU_TEXT_FLAG
            this.paint.isAntiAlias = true
            this.text = "￥${Utils.moneyFormat(mList[position].oldMoney)}"
        }
        if (position == selectItem){
            holder.getView<TextView>(R.id.tv_center).setTextColor(Color.parseColor("#ffffff"))
            holder.getView<TextView>(R.id.tv_bottom).setTextColor(Color.parseColor("#ffffff"))
            holder.getView<TextView>(R.id.tv_center_tip).setTextColor(Color.parseColor("#ffffff"))
            holder.getView<ConstraintLayout>(R.id.rootView).apply {
                this.setBackgroundResource(R.drawable.shape_item_vip_price_y)
            }
        }else{
            holder.getView<TextView>(R.id.tv_center).setTextColor(Color.parseColor("#16181A"))
            holder.getView<TextView>(R.id.tv_bottom).setTextColor(Color.parseColor("#16181A"))
            holder.getView<TextView>(R.id.tv_center_tip).setTextColor(Color.parseColor("#16181A"))
            holder.getView<ConstraintLayout>(R.id.rootView).apply {
                this.setBackgroundResource(R.drawable.shape_item_vip_price_n)
            }
        }
        when(mList[position].vipType){
            "VIP1" ->{
                holder.getView<TextView>(R.id.tv_top).text = "${SizeUtils.numFormat(mList[position].money/30,1,3,2,2)}/天"
                holder.setText(R.id.tv_center_tip,"一个月")
                holder.setText(R.id.tv_center,"￥${Utils.moneyFormat(mList[position].money)}")
            }
            "VIP6" ->{
                holder.getView<TextView>(R.id.tv_top).text = "${SizeUtils.numFormat(mList[position].money/180,1,3,2,2)}/天"
                holder.setText(R.id.tv_center_tip,"半年")
                holder.setText(R.id.tv_center,"￥${Utils.moneyFormat(mList[position].money)}")
            }
            "VIP12" ->{
                holder.getView<TextView>(R.id.tv_top).text = "${SizeUtils.numFormat(mList[position].money/365,1,3,2,2)}/天"
                holder.setText(R.id.tv_center_tip,"一年")
                holder.setText(R.id.tv_center,"￥${Utils.moneyFormat(mList[position].money)}")
            }
            "VIP13" ->{
                holder.getView<TextView>(R.id.tv_top).text = "限时特惠"
                holder.setText(R.id.tv_center_tip,"永久")
                holder.setText(R.id.tv_center,"￥${Utils.moneyFormat(mList[position].money)}")
            }
        }
        holder.itemView.setOnClickListener {
            selectItem = position
            callback?.clickPriceItem(position)
            notifyDataSetChanged()
        }
    }

    override fun getItemCount(): Int {
        return mList.size
    }

    fun setData(list: Array<PayBean>){
        this.mList = list
        notifyDataSetChanged()
    }

    fun setCallback(callback: PriceCallback){
        this.callback = callback
    }

    interface PriceCallback{
        fun clickPriceItem(position: Int)
    }
}