package com.weilai.jigsawpuzzle.adapter.data;

import android.content.Context;
import android.security.identity.EphemeralPublicKeyNotFoundException;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.weilai.jigsawpuzzle.R;
import com.weilai.jigsawpuzzle.db.RecordInfo;
import com.weilai.jigsawpuzzle.util.BitmapUtils;
import com.weilai.jigsawpuzzle.util.DateUtil;
import com.weilai.jigsawpuzzle.util.dao.DaoTool;

import java.util.List;

public class RecordAdapter extends RecyclerView.Adapter<RecordAdapter.ViewHolder> {
    private final Context mContext;

    public RecordAdapter(List<RecordInfo> list, Context context,OnAllCleanListener onAllCleanListener) {
        this.list = list;
        this.mContext = context;
        this.onAllCleanListener = onAllCleanListener;
    }

    private List<RecordInfo> list;

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        final View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_record, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        RecordInfo recordInfo = list.get(position);
        holder.tvDate.setText(DateUtil.unixTimeToDateTimeString(recordInfo.getTime() / 1000));
        holder.tvTitle.setText(recordInfo.getFileName());
        Glide.with(mContext).load(BitmapUtils.pathToUri(recordInfo.getFilePath())).placeholder(R.mipmap.icon_replace).into(holder.ivRecord);
        holder.cbEdit.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                recordInfo.setSelected(isChecked);
            }
        });
        if (mEditMode) {
            holder.cbEdit.setVisibility(View.VISIBLE);
            holder.cbEdit.setChecked(recordInfo.getIsSelected());
        } else {
            holder.cbEdit.setVisibility(View.INVISIBLE);
            holder.cbEdit.setChecked(false);
        }
    }

    private boolean mEditMode;

    public final void setMode(boolean mEdit) {
        if (list.isEmpty()){
            Toast.makeText(mContext,"没有更多可编辑的数据了!",Toast.LENGTH_SHORT).show();
            return;
        }
        this.mEditMode = mEdit;
        if (!mEdit){
            for (RecordInfo recordInfo : list){
                recordInfo.setIsSelected(false);
            }
        }
        notifyItemRangeChanged(0, list.size());
    }

    public final void setSelectAll() {
        for (RecordInfo recordInfo : list) {
            recordInfo.setSelected(true);
        }
        notifyItemRangeChanged(0, list.size());
    }
    public final void setUnSelectAll(){
        for (RecordInfo recordInfo : list){
            recordInfo.setSelected(false);
        }
        notifyItemRangeChanged(0,list.size());
    }

    public final void deleteSelected() {
        for (int i = list.size()-1 ; i>=0 ; i --){
            if (list.get(i).isSelected()) {
                DaoTool.deleteRecord(list.get(i));
                list.remove(i);
            }
        }
        if (list.size() == 0){
            onAllCleanListener.allClean();
        }
        notifyDataSetChanged();

    }
    private OnAllCleanListener onAllCleanListener;
    public interface OnAllCleanListener{
        void allClean();
    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        ImageView ivRecord;
        TextView tvTitle;
        TextView tvDate;
        CheckBox cbEdit;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            ivRecord = itemView.findViewById(R.id.iv_record);
            tvTitle = itemView.findViewById(R.id.tv_title);
            tvDate = itemView.findViewById(R.id.tv_date);
            cbEdit = itemView.findViewById(R.id.delete_selected);
        }
    }
}
