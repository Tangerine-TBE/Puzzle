package com.xinlan.imageeditlibrary.editimage.fragment;

import android.app.Dialog;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Matrix;
import android.os.Bundle;
import android.text.Editable;
import android.text.Layout;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.SeekBar;

import androidx.annotation.NonNull;
import androidx.appcompat.widget.AppCompatSeekBar;
import androidx.appcompat.widget.AppCompatTextView;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.alibaba.fastjson.JSONArray;
import com.xiaopo.flying.sticker.BitmapStickerIcon;
import com.xiaopo.flying.sticker.DeleteIconEvent;
import com.xiaopo.flying.sticker.RotateIconEvent;
import com.xiaopo.flying.sticker.SpecialZoomIconEvent;
import com.xiaopo.flying.sticker.Sticker;
import com.xiaopo.flying.sticker.StickerView;
import com.xiaopo.flying.sticker.TextSticker;
import com.xiaopo.flying.sticker.ZoomIconEvent;
import com.xinlan.imageeditlibrary.R;
import com.xinlan.imageeditlibrary.editimage.EditImageActivity;
import com.xinlan.imageeditlibrary.editimage.EditTextDialog;
import com.xinlan.imageeditlibrary.editimage.ModuleConfig;
import com.xinlan.imageeditlibrary.editimage.adapter.ColorItemAdapter;
import com.xinlan.imageeditlibrary.editimage.adapter.TextStyleItemAdapter;
import com.xinlan.imageeditlibrary.editimage.task.StickerTask;
import com.xinlan.imageeditlibrary.editimage.ui.ColorPicker;
import com.xinlan.imageeditlibrary.editimage.utils.AssetsUtil;
import com.xinlan.imageeditlibrary.editimage.view.TextStickerView;

import java.util.Arrays;


/**
 * 添加文本贴图
 *
 * @author 潘易
 */
public class AddTextFragment extends BaseEditFragment implements ColorItemAdapter.OnColorPickedListener, StickerView.OnStickerOperationListener, EditTextDialog.EditTextCallback, TextStyleItemAdapter.OnClickTextStyle {
    public static final int INDEX = ModuleConfig.INDEX_ADDTEXT;
    public static final String TAG = AddTextFragment.class.getName();

    private AppCompatTextView mTvPaintColor;
    private AppCompatSeekBar mSeekBar;
    private View mainView;
    private StickerView mTextStickerView;// 文字贴图显示控件
    private int mTextColor = Color.WHITE;
    private Sticker mSelectSticker;
    private EditTextDialog editTextDialog;
    private AppCompatTextView tvAlpha;
    private AppCompatTextView mTvModeView;
    private AppCompatTextView mTvModeStyle;
    private AppCompatTextView mTvModeTypeFace;
    private RecyclerView mRvTextStyle;
    private int drawables[] ;
    public static AddTextFragment newInstance() {
        return new AddTextFragment();
    }


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        mainView = inflater.inflate(R.layout.fragment_edit_image_add_text, null);
        return mainView;
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        drawables = new int[]{R.drawable.text1,R.drawable.text2,R.drawable.text3,R.drawable.text4,
        R.drawable.text5,R.drawable.text6};
        editTextDialog = new EditTextDialog(getActivity(),this);
        mTextStickerView = getActivity().findViewById(R.id.text_sticker_panel);
        tvAlpha = mainView.findViewById(R.id.tv_alpha);
        mainView.findViewById(R.id.iv_save).setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                applyTextImage();
            }
        });
        // 返回主菜单
        View backToMenu = mainView.findViewById(R.id.back_to_main);
        //颜色列表View
        RecyclerView mColorListView = mainView.findViewById(R.id.paint_color_list);
        mTvPaintColor = mainView.findViewById(R.id.tv_selected_color);
        mSeekBar = mainView.findViewById(R.id.seekbar);
        mTvModeView = mainView.findViewById(R.id.tv_mode_view);
        mTvModeStyle = mainView.findViewById(R.id.tv_mode_style);
        mTvModeTypeFace = mainView.findViewById(R.id.tv_mode_text);
        mRvTextStyle = mainView.findViewById(R.id.rv_text_style);
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getActivity());
        linearLayoutManager.setOrientation(RecyclerView.HORIZONTAL);
        initRvTextStyle(linearLayoutManager);
        mainView.findViewById(R.id.tv_default).setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                addTextSticker();
            }
        });
        BitmapStickerIcon bitmapStickerIconDelete = new BitmapStickerIcon(ContextCompat.getDrawable(getActivity(),R.drawable.icon_sticket_delete),BitmapStickerIcon.LEFT_TOP);
        BitmapStickerIcon bitmapStickerIconRotate = new BitmapStickerIcon(ContextCompat.getDrawable(getActivity(),R.drawable.icon_sticket_move),BitmapStickerIcon.RIGHT_BOTOM);
        BitmapStickerIcon bitmapStickerIconZoom = new BitmapStickerIcon(ContextCompat.getDrawable(getActivity(),R.drawable.icon_sticket_zoom),BitmapStickerIcon.LEFT_BOTTOM);
        bitmapStickerIconRotate.setIconEvent(new RotateIconEvent());
        bitmapStickerIconDelete.setIconEvent(new DeleteIconEvent());
        bitmapStickerIconZoom.setIconEvent(new SpecialZoomIconEvent());
        mTextStickerView.setIcons(Arrays.asList(bitmapStickerIconDelete, bitmapStickerIconZoom,bitmapStickerIconRotate));
        mTextStickerView.setLocked(false);
        mTextStickerView.setConstrained(true);
        mTextStickerView.setOnStickerOperationListener(this);
        backToMenu.setOnClickListener(new BackToMenuClick());// 返回主菜单

        //统一颜色设置
        LinearLayoutManager stickerListLayoutManager = new LinearLayoutManager(activity);
        stickerListLayoutManager.setOrientation(LinearLayoutManager.HORIZONTAL);
        mColorListView.setLayoutManager(stickerListLayoutManager);
        String colorStr = AssetsUtil.getAssertString(getContext(), "color.json");
        JSONArray jsonArray = JSONArray.parseArray(colorStr);
        ColorItemAdapter mColorAdapter = new ColorItemAdapter(getContext(), jsonArray, this);
        mColorListView.setAdapter(mColorAdapter);
        mSeekBar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                if (fromUser){
                    if (mSelectSticker != null){
                        mSelectSticker.setAlpha(progress);
                        mTextStickerView.invalidate();
                    }
                }
                tvAlpha.setText(String.valueOf(progress));

            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {

            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {

            }
        });
        mTvModeView.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                mTvModeView.setTextColor(Color.BLACK);
                mTvModeTypeFace.setTextColor(Color.parseColor("#C4C4C4"));
                mTvModeStyle.setTextColor(Color.parseColor("#C4C4C4"));
                mainView.findViewById(R.id.layout_text_style).setVisibility(View.VISIBLE);
                mainView.findViewById(R.id.layout_style).setVisibility(View.GONE);
                mainView.findViewById(R.id.layout_type_face).setVisibility(View.GONE);
            }
        });
        mTvModeTypeFace.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                mTvModeView.setTextColor(Color.parseColor("#C4C4C4"));
                mTvModeTypeFace.setTextColor(Color.BLACK);
                mTvModeStyle.setTextColor(Color.parseColor("#C4C4C4"));
                mainView.findViewById(R.id.layout_text_style).setVisibility(View.GONE);
                mainView.findViewById(R.id.layout_style).setVisibility(View.GONE);
                mainView.findViewById(R.id.layout_type_face).setVisibility(View.VISIBLE);

            }
        });
        mTvModeStyle.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                mTvModeView.setTextColor(Color.parseColor("#C4C4C4"));
                mTvModeTypeFace.setTextColor(Color.parseColor("#C4C4C4"));
                mTvModeStyle.setTextColor(Color.BLACK);
                mainView.findViewById(R.id.layout_style).setVisibility(View.VISIBLE);
                mainView.findViewById(R.id.layout_text_style).setVisibility(View.GONE);
                mainView.findViewById(R.id.layout_type_face).setVisibility(View.GONE);

            }
        });

    }
    private void initRvTextStyle(LinearLayoutManager linearLayoutManager){
        TextStyleItemAdapter textStyleItemAdapter = new TextStyleItemAdapter(drawables,this);
        mRvTextStyle.setAdapter(textStyleItemAdapter);
        mRvTextStyle.setLayoutManager(linearLayoutManager);
    }
    private void addTextSticker(){
        TextSticker textSticker = new TextSticker(getContext());
        textSticker.setText("双击输入文字")  ;
        textSticker.setTextColor(mTextColor ==0?Color.BLACK:mTextColor);
        textSticker.setTextAlign(Layout.Alignment.ALIGN_CENTER);
        textSticker.resizeText();
        textSticker.setAlpha(255);
        mSeekBar.setProgress(255);
        mTextStickerView.addSticker(textSticker);
    }
    private void addDrawableSticker(int position){
        TextSticker textSticker = new TextSticker(getContext());
        textSticker.setText("双击输入文字")  ;
        textSticker.setTextColor(mTextColor ==0?Color.BLACK:mTextColor);
        textSticker.setTextAlign(Layout.Alignment.ALIGN_CENTER);
        textSticker.resizeText();
        textSticker.setAlpha(255);
        textSticker.setDrawable(ContextCompat.getDrawable(getActivity(),drawables[position]));
        mSeekBar.setProgress(255);
        mTextStickerView.addSticker(textSticker);
    }


    @Override
    public void onColorPicked(String color) {
        this.mTextColor = Color.parseColor(color);
        mTvPaintColor.setBackgroundColor(Color.parseColor(color));
        if (mSelectSticker != null){
            ((TextSticker) mSelectSticker).setTextColor(Color.parseColor(color));
            mTextStickerView.invalidate();
        }
    }

    @Override
    public void onStickerAdded(@NonNull Sticker sticker) {
        mSelectSticker = sticker;
    }

    @Override
    public void onStickerClicked(@NonNull Sticker sticker) {
        mSelectSticker = sticker;
        mSeekBar.setProgress(((TextSticker)sticker).getAlpha());
    }

    @Override
    public void onStickerDeleted(@NonNull Sticker sticker) {

    }

    @Override
    public void onStickerDragFinished(@NonNull Sticker sticker) {

    }

    @Override
    public void onStickerTouchedDown(@NonNull Sticker sticker) {

    }

    @Override
    public void onStickerZoomFinished(@NonNull Sticker sticker) {

    }

    @Override
    public void onStickerFlipped(@NonNull Sticker sticker) {

    }

    @Override
    public void onStickerDoubleTapped(@NonNull Sticker sticker) {
        editTextDialog.setSticker(sticker).show();
    }

    @Override
    public void editText(String str, Sticker sticker) {
        if (sticker != null){
            if (sticker instanceof  TextSticker){
                if (!TextUtils.isEmpty(str)){
                    ((TextSticker) sticker).setText(str);
                }else{
                    ((TextSticker) sticker).setText("");
                }
                ((TextSticker) sticker).resizeText();
                mTextStickerView.invalidate();

            }
        }
    }

    @Override
    public void textStyleClicked(int position) {
        addDrawableSticker(position);
    }


    /**
     * 返回按钮逻辑
     *
     * @author panyi
     */
    private final class BackToMenuClick implements OnClickListener {
        @Override
        public void onClick(View v) {
            backToMain();
        }
    }// end class

    /**
     * 返回主菜单
     */
    @Override
    public void backToMain() {
        activity.mode = EditImageActivity.MODE_NONE;
        activity.bottomGallery.setCurrentItem(MainMenuFragment.INDEX);
        activity.mainImage.setVisibility(View.VISIBLE);
        mTextStickerView.setVisibility(View.GONE);
        mTextColor = Color.WHITE;
        mTextStickerView.removeAllStickers();
        activity.mainImage.setScaleEnabled(true);
        activity.findViewById(R.id.tv_save).setVisibility(View.VISIBLE);
    }

    @Override
    public void onShow() {
        activity.mode = EditImageActivity.MODE_TEXT;
        activity.mainImage.setImageBitmap(activity.getMainBit());
        activity.findViewById(R.id.tv_save).setVisibility(View.INVISIBLE);
        mTextStickerView.setVisibility(View.VISIBLE);
        activity.findViewById(R.id.tv_save).setVisibility(View.INVISIBLE);
        activity.mainImage.setScaleEnabled(false);

    }

    /**
     * 保存贴图图片
     */
    public void applyTextImage() {
        activity.changeMainBitmap(mTextStickerView.createBitmap(),true) ;
        backToMain();
    }

    /**
     * 文字合成任务
     * 合成最终图片
     */

    @Override
    public void onDestroy() {
        super.onDestroy();

    }
}// end class
