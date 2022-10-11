package com.xinlan.imageeditlibrary.editimage.fragment;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Matrix;
import android.os.Bundle;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.ImageView;
import com.xinlan.imageeditlibrary.R;
import com.xinlan.imageeditlibrary.editimage.EditImageActivity;
import com.xinlan.imageeditlibrary.editimage.ModuleConfig;
import com.xinlan.imageeditlibrary.editimage.adapter.StickerAdapter;
import com.xinlan.imageeditlibrary.editimage.task.StickerTask;
import com.xinlan.imageeditlibrary.editimage.view.StickerItem;
import com.xinlan.imageeditlibrary.editimage.view.StickerView;
import java.util.LinkedHashMap;

/**
 * 贴图分类fragment
 *
 * @author panyi
 */
public class StickerFragment extends BaseEditFragment implements StickerAdapter.OnImageViewClicked {
    private final int[] iconIds = new int[]{
            R.mipmap.icon1,R.mipmap.icon2,R.mipmap.icon3,
            R.mipmap.icon4,R.mipmap.icon5,R.mipmap.icon6,
            R.mipmap.icon7,R.mipmap.icon8,R.mipmap.icon9,
            R.mipmap.icon10,R.mipmap.icon11,R.mipmap.icon12,
            R.mipmap.icon13,R.mipmap.icon14,R.mipmap.icon15,
            R.mipmap.icon16,R.mipmap.icon17,R.mipmap.icon18,R.mipmap.icon19};
    private View mainView;
    public static final int INDEX = ModuleConfig.INDEX_STICKER;
    private StickerView mStickerView;// 贴图显示控件
    private SaveStickersTask mSaveTask;

    public static StickerFragment newInstance() {
        return new StickerFragment();
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        super.onCreateView(inflater,container,savedInstanceState);
        mainView = inflater.inflate(R.layout.fragment_edit_image_sticker_type,
                null);
        //loadStickersData();

        return mainView;
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        this.mStickerView = activity.mStickerView;
        //
        // 返回主菜单
        View backToMenu = mainView.findViewById(R.id.back_to_main);
        // 贴图素材列表
        RecyclerView stickerList = mainView.findViewById(R.id.sticker_list);
        LinearLayoutManager mLayoutManager = new LinearLayoutManager(activity);
        mLayoutManager.setOrientation(LinearLayoutManager.HORIZONTAL);
        ImageView ivSave = mainView.findViewById(R.id.iv_save);
        GridLayoutManager gridLayoutManager = new GridLayoutManager(activity,7);
        stickerList.setLayoutManager(gridLayoutManager);
        // 贴图列表适配器
        StickerAdapter mStickerAdapter = new StickerAdapter(getContext(), iconIds, this);
        stickerList.setAdapter(mStickerAdapter);
        backToMenu.setOnClickListener(new BackToMenuClick());// 返回主菜单
        ivSave.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                applyStickers();
            }
        });
    }

    @Override
    public void onShow() {
        activity.mode = EditImageActivity.MODE_STICKERS;
        activity.mStickerFragment.getmStickerView().setVisibility(
                View.VISIBLE);
    }
    @Override
    public void imageViewClicked(int id) {
        mStickerView.addBitImage(BitmapFactory.decodeResource(getResources(),id));
    }




    @Override
    public void onDestroy() {
        super.onDestroy();

    }
    public StickerView getmStickerView() {
        return mStickerView;
    }
    /**
     * 返回主菜单页面
     *
     * @author panyi
     */
    private final class BackToMenuClick implements OnClickListener {
        @Override
        public void onClick(View v) {
            backToMain();
        }
    }// end inner class

    @Override
    public void backToMain() {
        activity.mode = EditImageActivity.MODE_NONE;
        activity.bottomGallery.setCurrentItem(0);
        mStickerView.setVisibility(View.GONE);
    }
    /**
     * 保存贴图任务
     *
     * @author panyi
     */
    private final class SaveStickersTask extends StickerTask {
        public SaveStickersTask(EditImageActivity activity) {
            super(activity);
        }

        @Override
        public void handleImage(Canvas canvas, Matrix m) {
            LinkedHashMap<Integer, StickerItem> addItems = mStickerView.getBank();
            for (Integer id : addItems.keySet()) {
                StickerItem item = addItems.get(id);
                item.matrix.postConcat(m);// 乘以底部图片变化矩阵
                canvas.drawBitmap(item.bitmap, item.matrix, null);
            }// end for
        }

        @Override
        public void onPostResult(Bitmap result) {
            mStickerView.clear();
            activity.changeMainBitmap(result,true);
            backToMain();
        }
    }// end inner class

    /**
     * 保存贴图层 合成一张图片
     */
    public void applyStickers() {
        // System.out.println("保存 合成图片");
        if (mSaveTask != null) {
            mSaveTask.cancel(true);
        }
        mSaveTask = new SaveStickersTask((EditImageActivity) getActivity());
        mSaveTask.execute(activity.getMainBit());
    }
}// end class
