package com.weilai.jigsawpuzzle.adapter.special

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.weilai.jigsawpuzzle.R
import com.weilai.jigsawpuzzle.Resources
import com.weilai.jigsawpuzzle.adapter.RecyclerViewHolder

/**
 * @description:
 * @author: luo
 * @date: 2022/8/16
 */
class VipTabAdapt: RecyclerView.Adapter<RecyclerViewHolder>() {

    companion object{
        fun loadVipTxAdapt(rv:RecyclerView){
            rv.adapter=VipTabAdapt()
            rv.layoutManager=LinearLayoutManager(rv.context,
                LinearLayoutManager.VERTICAL,false)
        }
    }

    val list= Resources.vipTabList
    val headCode=444

    class VipViewHolder(view:View): RecyclerViewHolder(view)

    class VipHeadViewHolder(view:View): RecyclerViewHolder(view)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerViewHolder {
        return if (viewType==headCode){
           VipHeadViewHolder( LayoutInflater.from(parent.context)
               .inflate(R.layout.item_pay_tx_head,parent,false))
        }else{
            VipViewHolder(LayoutInflater.from(parent.context)
                .inflate(R.layout.item_pay_tx,parent,false))
        }
    }

    override fun onBindViewHolder(holder: RecyclerViewHolder, position: Int) {
        if (holder is VipViewHolder){
            val data = list[position]
            holder.setText(R.id.item1,data.name)
            holder.setText(R.id.item2,data.vipUser)
            holder.setText(R.id.item3,data.ptUser)
        }
    }

    override fun getItemCount(): Int {
      return list.size
    }

    override fun getItemViewType(position: Int): Int {
        if (position==0)return headCode
        return super.getItemViewType(position)
    }
}