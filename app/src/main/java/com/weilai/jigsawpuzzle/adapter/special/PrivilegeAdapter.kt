package com.weilai.jigsawpuzzle.adapter.special

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.weilai.jigsawpuzzle.R
import com.weilai.jigsawpuzzle.adapter.RecyclerViewHolder
import com.weilai.jigsawpuzzle.bean.PrivilegeBean

class PrivilegeAdapter: RecyclerView.Adapter<RecyclerViewHolder>() {

    private var mList: Array<PrivilegeBean> = arrayOf()

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerViewHolder {
        return RecyclerViewHolder(LayoutInflater.from(parent.context)
            .inflate(R.layout.item_vip_privilege,parent,false))
    }

    override fun onBindViewHolder(holder: RecyclerViewHolder, position: Int) {
        holder.setImage(R.id.img,mList[position].img)
        holder.setText(R.id.title_text,mList[position].title)
    }

    override fun getItemCount(): Int {
        return mList.size
    }

    fun setData(list: Array<PrivilegeBean>){
        this.mList = list
        notifyDataSetChanged()
    }
}