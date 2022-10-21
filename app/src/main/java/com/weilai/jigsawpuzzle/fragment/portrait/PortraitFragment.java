package com.weilai.jigsawpuzzle.fragment.portrait;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.graphics.drawable.BitmapDrawable;
import android.os.Bundle;
import android.text.Layout;
import android.text.TextUtils;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.widget.AppCompatTextView;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.alibaba.fastjson.JSONObject;
import com.luck.picture.lib.basic.PictureSelector;
import com.luck.picture.lib.config.PictureConfig;
import com.luck.picture.lib.config.SelectMimeType;
import com.luck.picture.lib.config.SelectModeConfig;
import com.luck.picture.lib.entity.LocalMedia;
import com.weilai.jigsawpuzzle.R;
import com.weilai.jigsawpuzzle.adapter.portrait.PortraitBackgroundAdapter;
import com.weilai.jigsawpuzzle.adapter.portrait.PortraitBackgroundGroupAdapter;
import com.weilai.jigsawpuzzle.adapter.portrait.TextColorAdapter;
import com.weilai.jigsawpuzzle.base.BaseFragment;
import com.weilai.jigsawpuzzle.bean.BackGroundBean;
import com.weilai.jigsawpuzzle.bean.BackGroundGroupBean;
import com.weilai.jigsawpuzzle.dialog.portrait.PortraitNoSaveDialog;
import com.weilai.jigsawpuzzle.fragment.main.SaveFragment;
import com.weilai.jigsawpuzzle.util.Base64Utils;
import com.weilai.jigsawpuzzle.util.BitmapUtils;
import com.weilai.jigsawpuzzle.util.EffectUtils;
import com.weilai.jigsawpuzzle.util.FileUtil;
import com.weilai.jigsawpuzzle.util.GlideEngine;
import com.xiaopo.flying.sticker.BitmapStickerIcon;
import com.xiaopo.flying.sticker.DeleteIconEvent;
import com.xiaopo.flying.sticker.DrawableSticker;
import com.xiaopo.flying.sticker.RotateIconEvent;
import com.xiaopo.flying.sticker.SpecialZoomIconEvent;
import com.xiaopo.flying.sticker.Sticker;
import com.xiaopo.flying.sticker.StickerView;
import com.xiaopo.flying.sticker.TextSticker;
import com.xinlan.imageeditlibrary.editimage.EditTextDialog;
import com.xinlan.imageeditlibrary.editimage.utils.AssetsUtil;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import io.reactivex.Observable;
import io.reactivex.ObservableEmitter;
import io.reactivex.ObservableOnSubscribe;
import io.reactivex.ObservableSource;
import io.reactivex.Observer;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.Disposable;
import io.reactivex.functions.Function;
import io.reactivex.schedulers.Schedulers;

/**
 * * DATE: 2022/10/19
 * * Author:tangerine
 * * Description:
 **/
public class PortraitFragment extends BaseFragment implements StickerView.OnStickerOperationListener {
    private static final int BG_CODE = 1;
    private static final int CUT_OUT_CODE = 2;
    private List<BackGroundGroupBean> groupList;
    private List<BackGroundBean> naturalList;
    private List<BackGroundBean> carList;
    private List<BackGroundBean> beautyList;
    private List<BackGroundBean> starList;
    private List<BackGroundBean> gameList;
    private List<BackGroundBean> sportList;
    private List<BackGroundBean> animalList;
    private List<BackGroundBean> moveList;
    private List<BackGroundBean> artisticList;
    private List<BackGroundBean> animeList;
    private List<BackGroundBean> textList;
    private List<BackGroundBean> couplesList;
    private List<BackGroundBean> contractedList;
    private List<Integer> colorList;

    private PortraitBackgroundGroupAdapter groupAdapter;
    private TextColorAdapter textColorAdapter;
    private PortraitBackgroundAdapter backgroundAdapter;


    private boolean isBack;
    private StickerView stickerView;
    private RecyclerView recyclerView;
    private ImageView ivBig;
    private ImageView ivSmall;
    private TextView tvName;
    private ImageView srcImg;
    private RelativeLayout centerView;
    private Sticker selectSticker = null;
    private LinearLayout frame;
    private RelativeLayout bgRootView;
    private ImageView arrow_background;
    private RecyclerView colorRecycle;
    private ImageView arrow_text;
    private boolean canSetBg = true;


    @Override
    protected Object setLayout() {
        return R.layout.fragment_portrait;
    }

    private PortraitFragment() {

    }

    public static PortraitFragment getInstance(String path, String type) {
        Bundle bundle = new Bundle();
        bundle.putString("data", path);
        bundle.putString("type", type);
        PortraitFragment portraitFragment = new PortraitFragment();
        portraitFragment.setArguments(bundle);
        return portraitFragment;
    }

    private String type;

    @Override
    protected void initView(View view) {
        type = getArguments().getString("type");
        AppCompatTextView tvTitle = view.findViewById(R.id.tv_title);
        if ("portrait".equals(type)) {
            tvTitle.setText("人像抠图");
        } else {
            tvTitle.setText("智能抠图");
        }
        stickerView = view.findViewById(R.id.stickerView);
        recyclerView = view.findViewById(R.id.recycler);
        ivBig = view.findViewById(R.id.image_big);
        ivSmall = view.findViewById(R.id.image_small);
        tvName = view.findViewById(R.id.name);
        srcImg = view.findViewById(R.id.srcImg);
        centerView = view.findViewById(R.id.centerView);
        frame = view.findViewById(R.id.frame);
        bgRootView = view.findViewById(R.id.bgRootView);
        arrow_background = view.findViewById(R.id.arrow_background);
        colorRecycle = view.findViewById(R.id.colorRecycle);
        arrow_text = view.findViewById(R.id.arrow_text);
        view.findViewById(R.id.tv_save).setVisibility(View.VISIBLE);
    }

    @Override
    public void onLazyInitView(@Nullable Bundle savedInstanceState) {

        initData();
        recyclerView.setLayoutManager(new LinearLayoutManager(_mActivity, LinearLayoutManager.HORIZONTAL, false));
        recyclerView.setAdapter(groupAdapter);
        colorRecycle.setLayoutManager(new LinearLayoutManager(_mActivity, LinearLayoutManager.HORIZONTAL, false));
        colorRecycle.setAdapter(textColorAdapter);
        BitmapStickerIcon bitmapStickerIconDelete = new BitmapStickerIcon(ContextCompat.getDrawable(getActivity(), com.xinlan.imageeditlibrary.R.drawable.icon_sticket_delete), BitmapStickerIcon.LEFT_TOP);
        BitmapStickerIcon bitmapStickerIconRotate = new BitmapStickerIcon(ContextCompat.getDrawable(getActivity(), com.xinlan.imageeditlibrary.R.drawable.icon_sticket_move), BitmapStickerIcon.RIGHT_BOTOM);
        BitmapStickerIcon bitmapStickerIconZoom = new BitmapStickerIcon(ContextCompat.getDrawable(getActivity(), com.xinlan.imageeditlibrary.R.drawable.icon_sticket_zoom), BitmapStickerIcon.LEFT_BOTTOM);
        bitmapStickerIconRotate.setIconEvent(new RotateIconEvent());
        bitmapStickerIconDelete.setIconEvent(new DeleteIconEvent());
        bitmapStickerIconZoom.setIconEvent(new SpecialZoomIconEvent());
        stickerView.setIcons(Arrays.asList(bitmapStickerIconDelete, bitmapStickerIconZoom, bitmapStickerIconRotate));
        stickerView.setLocked(false);
        stickerView.setConstrained(true);
        stickerView.setOnStickerOperationListener(this);
        PictureSelector.create(this)
                .openGallery(SelectMimeType.ofImage())
                .setImageEngine(GlideEngine.createGlideEngine())
                .isPreviewImage(false)
                .isDisplayCamera(false)
                .setSelectionMode(SelectModeConfig.SINGLE)
                .forResult(CUT_OUT_CODE);
    }

    private void initData() {
        groupList = Arrays.asList(new BackGroundGroupBean("album", "相册", "beijing/group/album.png", R.drawable.ic_avatar, false),
                new BackGroundGroupBean("natural", "自然", "beijing/group/natural.png", R.drawable.ic_avatar, false),
                new BackGroundGroupBean("car", "汽车", "beijing/group/car.png", R.drawable.ic_avatar, false),
                new BackGroundGroupBean("game", "游戏", "beijing/group/game.png", R.drawable.ic_avatar, false),
                new BackGroundGroupBean("beauty", "美女", "beijing/group/beauty.png", R.drawable.ic_avatar, false),
                new BackGroundGroupBean("star", "明星", "beijing/group/star.png", R.drawable.ic_avatar, true),
                new BackGroundGroupBean("sports", "体育", "beijing/group/sport.png", R.drawable.ic_avatar, true),
                new BackGroundGroupBean("animal", "动物萌宠", "beijing/group/anima.png", R.drawable.ic_avatar, true),
                new BackGroundGroupBean("mov", "影视", "beijing/group/mov.png", R.drawable.ic_avatar, true),
                new BackGroundGroupBean("contracted", "简约抽象", "beijing/group/contracted.png", R.drawable.ic_avatar, true),
                new BackGroundGroupBean("anime", "动漫", "beijing/group/anime.png", R.drawable.ic_avatar, true),
                new BackGroundGroupBean("text", "文字", "beijing/group/text.png", R.drawable.ic_avatar, true),
                new BackGroundGroupBean("couples", "情侣", "beijing/group/couples.png", R.drawable.ic_avatar, true),
                new BackGroundGroupBean("artistic", "意境", "beijing/group/artistic.png", R.drawable.ic_avatar, true));
        naturalList = Arrays.asList(
                new BackGroundBean("natural", "beijing/natural_small/natural_1.jpg", "beijing/natural/natural_1.jpg", false),
                new BackGroundBean("natural", "beijing/natural_small/natural_2.jpg", "beijing/natural/natural_2.jpeg", false),
                new BackGroundBean("natural", "beijing/natural_small/natural_3.jpg", "beijing/natural/natural_3.jpg", false),
                new BackGroundBean("natural", "beijing/natural_small/natural_4.jpg", "beijing/natural/natural_4.jpg", false),
                new BackGroundBean("natural", "beijing/natural_small/natural_5.jpg", "beijing/natural/natural_5.jpg", false),
                new BackGroundBean("natural", "beijing/natural_small/natural_6.jpg", "beijing/natural/natural_6.jpeg", false),
                new BackGroundBean("natural", "beijing/natural_small/natural_7.jpg", "beijing/natural/natural_7.jpg", false),
                new BackGroundBean("natural", "beijing/natural_small/natural_8.jpg", "beijing/natural/natural_8.jpg", false),
                new BackGroundBean("natural", "beijing/natural_small/natural_9.jpg", "beijing/natural/natural_9.jfif", false),
                new BackGroundBean("natural", "beijing/natural_small/natural_10.jpg", "beijing/natural/natural_10.jpeg", false),
                new BackGroundBean("natural", "beijing/natural_small/natural_11.jpg", "beijing/natural/natural_11.jpeg", false),
                new BackGroundBean("natural", "beijing/natural_small/natural_12.jpg", "beijing/natural/natural_12.jpg", false),
                new BackGroundBean("natural", "beijing/natural_small/natural_13.jpg", "beijing/natural/natural_13.jpg", false),
                new BackGroundBean("natural", "beijing/natural_small/natural_14.jpg", "beijing/natural/natural_14.jpeg", false),
                new BackGroundBean("natural", "beijing/natural_small/natural_15.jpg", "beijing/natural/natural_15.jpg", false),
                new BackGroundBean("natural", "beijing/natural_small/natural_16.jpg", "beijing/natural/natural_16.jpg", false),
                new BackGroundBean("natural", "beijing/natural_small/natural_17.jpg", "beijing/natural/natural_17.jpg", false),
                new BackGroundBean("natural", "beijing/natural_small/natural_18.jpg", "beijing/natural/natural_18.jpg", false),
                new BackGroundBean("natural", "beijing/natural_small/natural_19.jpg", "beijing/natural/natural_19.jpg", false),
                new BackGroundBean("natural", "beijing/natural_small/natural_20.jpg", "beijing/natural/natural_20.jpg", false));
        carList = Arrays.asList(
                new BackGroundBean("car", "beijing/car_small/car_1.jpg", "beijing/car/car_1.jpg", false),
                new BackGroundBean("car", "beijing/car_small/car_2.jpg", "beijing/car/car_2.jpg", false),
                new BackGroundBean("car", "beijing/car_small/car_3.jpg", "beijing/car/car_3.jpg", false),
                new BackGroundBean("car", "beijing/car_small/car_4.jpg", "beijing/car/car_4.jpg", false),
                new BackGroundBean("car", "beijing/car_small/car_5.jpg", "beijing/car/car_5.jpg", false),
                new BackGroundBean("car", "beijing/car_small/car_6.jpg", "beijing/car/car_6.jpg", false),
                new BackGroundBean("car", "beijing/car_small/car_7.jpg", "beijing/car/car_7.jpg", false),
                new BackGroundBean("car", "beijing/car_small/car_8.jpg", "beijing/car/car_8.jpg", false),
                new BackGroundBean("car", "beijing/car_small/car_9.jpg", "beijing/car/car_9.jpg", false),
                new BackGroundBean("car", "beijing/car_small/car_10.jpg", "beijing/car/car_10.jpg", false),
                new BackGroundBean("car", "beijing/car_small/car_11.jpg", "beijing/car/car_11.jpg", false),
                new BackGroundBean("car", "beijing/car_small/car_12.jpg", "beijing/car/car_12.jpg", false),
                new BackGroundBean("car", "beijing/car_small/car_13.jpg", "beijing/car/car_13.jpg", false),
                new BackGroundBean("car", "beijing/car_small/car_14.jpg", "beijing/car/car_14.jpg", false),
                new BackGroundBean("car", "beijing/car_small/car_15.jpg", "beijing/car/car_15.jpg", false),
                new BackGroundBean("car", "beijing/car_small/car_16.jpg", "beijing/car/car_16.jpg", false),
                new BackGroundBean("car", "beijing/car_small/car_17.jpg", "beijing/car/car_17.jpg", false),
                new BackGroundBean("car", "beijing/car_small/car_18.jpg", "beijing/car/car_18.jpg", false),
                new BackGroundBean("car", "beijing/car_small/car_19.jpg", "beijing/car/car_19.jpg", false),
                new BackGroundBean("car", "beijing/car_small/car_20.jpg", "beijing/car/car_20.jpg", false),
                new BackGroundBean("car", "beijing/car_small/car_21.jpg", "beijing/car/car_21.jfif", false),
                new BackGroundBean("car", "beijing/car_small/car_22.jpg", "beijing/car/car_22.jpg", false),
                new BackGroundBean("car", "beijing/car_small/car_23.jpg", "beijing/car/car_23.jpg", false),
                new BackGroundBean("car", "beijing/car_small/car_24.jpg", "beijing/car/car_24.jpg", false),
                new BackGroundBean("car", "beijing/car_small/car_25.jpg", "beijing/car/car_25.jpg", false)
        );
        beautyList = Arrays.asList(
                new BackGroundBean("beauty", "beijing/beauty_small/beauty_1.jpg", "beijing/beauty/beauty_1.jpeg", false),
                new BackGroundBean("beauty", "beijing/beauty_small/beauty_2.jpg", "beijing/beauty/beauty_2.jpg", false),
                new BackGroundBean("beauty", "beijing/beauty_small/beauty_3.jpg", "beijing/beauty/beauty_3.jpeg", false),
                new BackGroundBean("beauty", "beijing/beauty_small/beauty_4.jpg", "beijing/beauty/beauty_4.jpg", false),
                new BackGroundBean("beauty", "beijing/beauty_small/beauty_5.jpg", "beijing/beauty/beauty_5.jpg", false),
                new BackGroundBean("beauty", "beijing/beauty_small/beauty_6.jpg", "beijing/beauty/beauty_6.jpeg", false),
                new BackGroundBean("beauty", "beijing/beauty_small/beauty_7.jpg", "beijing/beauty/beauty_7.jpeg", false),
                new BackGroundBean("beauty", "beijing/beauty_small/beauty_8.jpg", "beijing/beauty/beauty_8.jpeg", false),
                new BackGroundBean("beauty", "beijing/beauty_small/beauty_9.jpg", "beijing/beauty/beauty_9.jpeg", false),
                new BackGroundBean("beauty", "beijing/beauty_small/beauty_10.jpg", "beijing/beauty/beauty_10.jpeg", false),
                new BackGroundBean("beauty", "beijing/beauty_small/beauty_11.jpg", "beijing/beauty/beauty_11.jpg", false),
                new BackGroundBean("beauty", "beijing/beauty_small/beauty_12.jpg", "beijing/beauty/beauty_12.jpg", false),
                new BackGroundBean("beauty", "beijing/beauty_small/beauty_13.jpg", "beijing/beauty/beauty_13.jpg", false),
                new BackGroundBean("beauty", "beijing/beauty_small/beauty_14.jpg", "beijing/beauty/beauty_14.jpeg", false),
                new BackGroundBean("beauty", "beijing/beauty_small/beauty_15.jpg", "beijing/beauty/beauty_15.jpeg", false),
                new BackGroundBean("beauty", "beijing/beauty_small/beauty_16.jpg", "beijing/beauty/beauty_16.jpeg", false),
                new BackGroundBean("beauty", "beijing/beauty_small/beauty_17.jpg", "beijing/beauty/beauty_17.jpg", false),
                new BackGroundBean("beauty", "beijing/beauty_small/beauty_18.jpg", "beijing/beauty/beauty_18.jpeg", false),
                new BackGroundBean("beauty", "beijing/beauty_small/beauty_19.jpg", "beijing/beauty/beauty_19.jpeg", false),
                new BackGroundBean("beauty", "beijing/beauty_small/beauty_20.jpg", "beijing/beauty/beauty_20.jpeg", false)
        );
        starList = Arrays.asList(
                new BackGroundBean("star", "beijing/star_small/star_1.jpg", "beijing/star/star_1.jpg", true),
                new BackGroundBean("star", "beijing/star_small/star_2.png", "beijing/star/star_2.png", true),
                new BackGroundBean("star", "beijing/star_small/star_3.jpg", "beijing/star/star_3.jpg", true),
                new BackGroundBean("star", "beijing/star_small/star_4.jpg", "beijing/star/star_4.jpg", true),
                new BackGroundBean("star", "beijing/star_small/star_5.jpg", "beijing/star/star_5.jpg", true),
                new BackGroundBean("star", "beijing/star_small/star_6.jpg", "beijing/star/star_6.jpeg", true),
                new BackGroundBean("star", "beijing/star_small/star_7.jpg", "beijing/star/star_7.jpeg", true),
                new BackGroundBean("star", "beijing/star_small/star_8.jpg", "beijing/star/star_8.jpg", true),
                new BackGroundBean("star", "beijing/star_small/star_9.jpg", "beijing/star/star_9.jpg", true),
                new BackGroundBean("star", "beijing/star_small/star_10.png", "beijing/star/star_10.png", true),
                new BackGroundBean("star", "beijing/star_small/star_11.jpg", "beijing/star/star_11.jpg", true),
                new BackGroundBean("star", "beijing/star_small/star_12.jpg", "beijing/star/star_12.jpeg", true),
                new BackGroundBean("star", "beijing/star_small/star_13.jpg", "beijing/star/star_13.jpg", true),
                new BackGroundBean("star", "beijing/star_small/star_14.jpg", "beijing/star/star_14.jpg", true),
                new BackGroundBean("star", "beijing/star_small/star_15.jpg", "beijing/star/star_15.jpg", true),
                new BackGroundBean("star", "beijing/star_small/star_16.jpg", "beijing/star/star_16.jpg", true),
                new BackGroundBean("star", "beijing/star_small/star_17.jpg", "beijing/star/star_17.jpeg", true),
                new BackGroundBean("star", "beijing/star_small/star_18.jpg", "beijing/star/star_18.jpeg", true),
                new BackGroundBean("star", "beijing/star_small/star_19.jpg", "beijing/star/star_19.jpg", true),
                new BackGroundBean("star", "beijing/star_small/star_20.png", "beijing/star/star_20.jpg", true),
                new BackGroundBean("star", "beijing/star_small/star_21.jpg", "beijing/star/star_21.jpg", true),
                new BackGroundBean("star", "beijing/star_small/star_22.jpg", "beijing/star/star_22.jpg", true),
                new BackGroundBean("star", "beijing/star_small/star_23.jpg", "beijing/star/star_23.jpeg", true),
                new BackGroundBean("star", "beijing/star_small/star_24.jpg", "beijing/star/star_24.jpg", true),
                new BackGroundBean("star", "beijing/star_small/star_25.jpg", "beijing/star/star_25.jpg", true)

        );
        sportList = Arrays.asList(
                new BackGroundBean("sports", "beijing/sports_small/sports_1.jpg", "beijing/sports/sports_1.jpeg", true),
                new BackGroundBean("sports", "beijing/sports_small/sports_2.jpg", "beijing/sports/sports_2.jpg", true),
                new BackGroundBean("sports", "beijing/sports_small/sports_3.jpg", "beijing/sports/sports_3.jpeg", true),
                new BackGroundBean("sports", "beijing/sports_small/sports_4.jpg", "beijing/sports/sports_4.jpg", true),
                new BackGroundBean("sports", "beijing/sports_small/sports_5.jpg", "beijing/sports/sports_5.jpg", true),
                new BackGroundBean("sports", "beijing/sports_small/sports_6.jpg", "beijing/sports/sports_6.jpg", true),
                new BackGroundBean("sports", "beijing/sports_small/sports_7.jpg", "beijing/sports/sports_7.jpg", true),
                new BackGroundBean("sports", "beijing/sports_small/sports_8.jpg", "beijing/sports/sports_8.jpg", true),
                new BackGroundBean("sports", "beijing/sports_small/sports_9.jpg", "beijing/sports/sports_9.jpg", true),
                new BackGroundBean("sports", "beijing/sports_small/sports_10.jpg", "beijing/sports/sports_10.jpg", true),
                new BackGroundBean("sports", "beijing/sports_small/sports_11.jpg", "beijing/sports/sports_11.jpg", true),
                new BackGroundBean("sports", "beijing/sports_small/sports_12.jpg", "beijing/sports/sports_12.jpeg", true),
                new BackGroundBean("sports", "beijing/sports_small/sports_13.jpg", "beijing/sports/sports_13.jpeg", true),
                new BackGroundBean("sports", "beijing/sports_small/sports_14.jpg", "beijing/sports/sports_14.jpeg", true),
                new BackGroundBean("sports", "beijing/sports_small/sports_15.jpg", "beijing/sports/sports_15.jpg", true),
                new BackGroundBean("sports", "beijing/sports_small/sports_16.jpg", "beijing/sports/sports_16.jpg", true),
                new BackGroundBean("sports", "beijing/sports_small/sports_17.jpg", "beijing/sports/sports_17.jpg", true),
                new BackGroundBean("sports", "beijing/sports_small/sports_18.jpg", "beijing/sports/sports_18.jpeg", true),
                new BackGroundBean("sports", "beijing/sports_small/sports_19.jpg", "beijing/sports/sports_19.jpeg", true),
                new BackGroundBean("sports", "beijing/sports_small/sports_20.jpg", "beijing/sports/sports_20.jpg", true),
                new BackGroundBean("sports", "beijing/sports_small/sports_21.jpg", "beijing/sports/sports_21.jpg", true),
                new BackGroundBean("sports", "beijing/sports_small/sports_22.jpg", "beijing/sports/sports_22.jpg", true),
                new BackGroundBean("sports", "beijing/sports_small/sports_23.jpg", "beijing/sports/sports_23.jpg", true),
                new BackGroundBean("sports", "beijing/sports_small/sports_24.jpg", "beijing/sports/sports_24.jpg", true),
                new BackGroundBean("sports", "beijing/sports_small/sports_25.jpg", "beijing/sports/sports_25.jpg", true)
        );
        gameList = Arrays.asList(
                new BackGroundBean("game", "beijing/game_small/game_1.jpg", "beijing/game/game_1.jpg", false),
                new BackGroundBean("game", "beijing/game_small/game_2.jpg", "beijing/game/game_2.jpeg", false),
                new BackGroundBean("game", "beijing/game_small/game_3.jpg", "beijing/game/game_3.jpeg", false),
                new BackGroundBean("game", "beijing/game_small/game_4.jpg", "beijing/game/game_4.jpeg", false),
                new BackGroundBean("game", "beijing/game_small/game_5.jpg", "beijing/game/game_5.jpeg", false),
                new BackGroundBean("game", "beijing/game_small/game_6.jpg", "beijing/game/game_6.jpg", false),
                new BackGroundBean("game", "beijing/game_small/game_7.jpg", "beijing/game/game_7.jpeg", false),
                new BackGroundBean("game", "beijing/game_small/game_8.jpg", "beijing/game/game_8.jpeg", false),
                new BackGroundBean("game", "beijing/game_small/game_9.jpg", "beijing/game/game_9.jpeg", false),
                new BackGroundBean("game", "beijing/game_small/game_10.jpg", "beijing/game/game_10.jpeg", false),
                new BackGroundBean("game", "beijing/game_small/game_11.jpg", "beijing/game/game_11.jpg", false)

        );
        animalList = Arrays.asList(
                new BackGroundBean("animal", "beijing/animal_small/animal_1.jpg", "beijing/animal/animal_1.jpg", true),
                new BackGroundBean("animal", "beijing/animal_small/animal_2.jpg", "beijing/animal/animal_2.jpg", true),
                new BackGroundBean("animal", "beijing/animal_small/animal_3.jpg", "beijing/animal/animal_3.jpg", true),
                new BackGroundBean("animal", "beijing/animal_small/animal_4.jpg", "beijing/animal/animal_4.jpg", true),
                new BackGroundBean("animal", "beijing/animal_small/animal_5.jpg", "beijing/animal/animal_5.jpg", true),
                new BackGroundBean("animal", "beijing/animal_small/animal_6.jpg", "beijing/animal/animal_6.jpg", true),
                new BackGroundBean("animal", "beijing/animal_small/animal_7.jpg", "beijing/animal/animal_7.jpg", true),
                new BackGroundBean("animal", "beijing/animal_small/animal_8.jpg", "beijing/animal/animal_8.jpg", true),
                new BackGroundBean("animal", "beijing/animal_small/animal_9.jpg", "beijing/animal/animal_9.jpg", true),
                new BackGroundBean("animal", "beijing/animal_small/animal_10.jpg", "beijing/animal/animal_10.jpg", true),
                new BackGroundBean("animal", "beijing/animal_small/animal_11.jpg", "beijing/animal/animal_11.jpg", true),
                new BackGroundBean("animal", "beijing/animal_small/animal_12.jpg", "beijing/animal/animal_12.jpg", true),
                new BackGroundBean("animal", "beijing/animal_small/animal_13.jpg", "beijing/animal/animal_13.jpg", true),
                new BackGroundBean("animal", "beijing/animal_small/animal_14.jpg", "beijing/animal/animal_14.jpg", true),
                new BackGroundBean("animal", "beijing/animal_small/animal_15.jpg", "beijing/animal/animal_15.jpg", true),
                new BackGroundBean("animal", "beijing/animal_small/animal_16.jpg", "beijing/animal/animal_16.jpg", true),
                new BackGroundBean("animal", "beijing/animal_small/animal_17.jpg", "beijing/animal/animal_17.jpg", true),
                new BackGroundBean("animal", "beijing/animal_small/animal_18.jpg", "beijing/animal/animal_18.jpg", true),
                new BackGroundBean("animal", "beijing/animal_small/animal_19.jpg", "beijing/animal/animal_19.jpg", true)
        );
        moveList = Arrays.asList(
                new BackGroundBean("mov", "beijing/mov_small/mov_1.jpg", "beijing/mov/mov_1.jpg", true),
                new BackGroundBean("mov", "beijing/mov_small/mov_2.jpg", "beijing/mov/mov_2.jpg", true),
                new BackGroundBean("mov", "beijing/mov_small/mov_3.jpg", "beijing/mov/mov_3.jpg", true),
                new BackGroundBean("mov", "beijing/mov_small/mov_4.jpg", "beijing/mov/mov_4.jpg", true),
                new BackGroundBean("mov", "beijing/mov_small/mov_5.jpg", "beijing/mov/mov_5.jpg", true),
                new BackGroundBean("mov", "beijing/mov_small/mov_6.jpg", "beijing/mov/mov_6.jpg", true),
                new BackGroundBean("mov", "beijing/mov_small/mov_7.jpg", "beijing/mov/mov_7.jpg", true),
                new BackGroundBean("mov", "beijing/mov_small/mov_8.jpg", "beijing/mov/mov_8.jpg", true),
                new BackGroundBean("mov", "beijing/mov_small/mov_9.png", "beijing/mov/mov_9.png", true),
                new BackGroundBean("mov", "beijing/mov_small/mov_10.jpg", "beijing/mov/mov_10.jpg", true),
                new BackGroundBean("mov", "beijing/mov_small/mov_11.jpg", "beijing/mov/mov_11.jpg", true),
                new BackGroundBean("mov", "beijing/mov_small/mov_12.jpg", "beijing/mov/mov_12.jpg", true),
                new BackGroundBean("mov", "beijing/mov_small/mov_13.jpg", "beijing/mov/mov_13.jpg", true),
                new BackGroundBean("mov", "beijing/mov_small/mov_14.jpg", "beijing/mov/mov_14.jpg", true),
                new BackGroundBean("mov", "beijing/mov_small/mov_15.jpg", "beijing/mov/mov_15.jpg", true),
                new BackGroundBean("mov", "beijing/mov_small/mov_16.jpg", "beijing/mov/mov_16.jpg", true),
                new BackGroundBean("mov", "beijing/mov_small/mov_17.jpg", "beijing/mov/mov_17.jpg", true),
                new BackGroundBean("mov", "beijing/mov_small/mov_18.png", "beijing/mov/mov_18.png", true),
                new BackGroundBean("mov", "beijing/mov_small/mov_19.jpg", "beijing/mov/mov_19.jpg", true),
                new BackGroundBean("mov", "beijing/mov_small/mov_20.jpg", "beijing/mov/mov_20.jpg", true),
                new BackGroundBean("mov", "beijing/mov_small/mov_21.jpg", "beijing/mov/mov_21.jpg", true),
                new BackGroundBean("mov", "beijing/mov_small/mov_22.png", "beijing/mov/mov_22.png", true),
                new BackGroundBean("mov", "beijing/mov_small/mov_23.jpg", "beijing/mov/mov_23.jpg", true)
        );
        artisticList = Arrays.asList(
                new BackGroundBean("artistic", "beijing/artistic_small/artistic_1.jpg", "beijing/artistic/artistic_1.jpg", true),
                new BackGroundBean("artistic", "beijing/artistic_small/artistic_2.jpg", "beijing/artistic/artistic_2.jpg", true),
                new BackGroundBean("artistic", "beijing/artistic_small/artistic_3.jpg", "beijing/artistic/artistic_3.jpg", true),
                new BackGroundBean("artistic", "beijing/artistic_small/artistic_4.jpg", "beijing/artistic/artistic_4.jpg", true),
                new BackGroundBean("artistic", "beijing/artistic_small/artistic_5.jpg", "beijing/artistic/artistic_5.jpg", true),
                new BackGroundBean("artistic", "beijing/artistic_small/artistic_6.jpg", "beijing/artistic/artistic_6.jpg", true),
                new BackGroundBean("artistic", "beijing/artistic_small/artistic_7.jpg", "beijing/artistic/artistic_7.jpg", true),
                new BackGroundBean("artistic", "beijing/artistic_small/artistic_8.jpg", "beijing/artistic/artistic_8.jpg", true),
                new BackGroundBean("artistic", "beijing/artistic_small/artistic_9.jpg", "beijing/artistic/artistic_9.jpg", true),
                new BackGroundBean("artistic", "beijing/artistic_small/artistic_10.jpg", "beijing/artistic/artistic_10.jpg", true),
                new BackGroundBean("artistic", "beijing/artistic_small/artistic_11.jpg", "beijing/artistic/artistic_11.jpg", true),
                new BackGroundBean("artistic", "beijing/artistic_small/artistic_12.jpg", "beijing/artistic/artistic_12.jpg", true),
                new BackGroundBean("artistic", "beijing/artistic_small/artistic_13.jpg", "beijing/artistic/artistic_13.jpg", true),
                new BackGroundBean("artistic", "beijing/artistic_small/artistic_14.jpg", "beijing/artistic/artistic_14.jpg", true),
                new BackGroundBean("artistic", "beijing/artistic_small/artistic_15.jpg", "beijing/artistic/artistic_15.jpg", true),
                new BackGroundBean("artistic", "beijing/artistic_small/artistic_16.jpg", "beijing/artistic/artistic_16.jpg", true),
                new BackGroundBean("artistic", "beijing/artistic_small/artistic_17.jpg", "beijing/artistic/artistic_17.jpg", true)
        );
        animeList = Arrays.asList(
                new BackGroundBean("anime", "beijing/anime_small/anime_1.jpg", "beijing/anime/anime_1.jpeg", true),
                new BackGroundBean("anime", "beijing/anime_small/anime_2.jpg", "beijing/anime/anime_2.jpg", true),
                new BackGroundBean("anime", "beijing/anime_small/anime_3.jpg", "beijing/anime/anime_3.jpg", true),
                new BackGroundBean("anime", "beijing/anime_small/anime_4.jpg", "beijing/anime/anime_4.jpg", true),
                new BackGroundBean("anime", "beijing/anime_small/anime_5.jpg", "beijing/anime/anime_5.jpeg", true),
                new BackGroundBean("anime", "beijing/anime_small/anime_6.jpg", "beijing/anime/anime_6.jpeg", true),
                new BackGroundBean("anime", "beijing/anime_small/anime_7.jpg", "beijing/anime/anime_7.jpg", true),
                new BackGroundBean("anime", "beijing/anime_small/anime_8.jpg", "beijing/anime/anime_8.jpeg", true),
                new BackGroundBean("anime", "beijing/anime_small/anime_9.jpg", "beijing/anime/anime_9.jpg", true),
                new BackGroundBean("anime", "beijing/anime_small/anime_10.jpg", "beijing/anime/anime_10.jpeg", true),
                new BackGroundBean("anime", "beijing/anime_small/anime_11.jpg", "beijing/anime/anime_11.jpg", true),
                new BackGroundBean("anime", "beijing/anime_small/anime_12.jpg", "beijing/anime/anime_12.jpg", true),
                new BackGroundBean("anime", "beijing/anime_small/anime_13.jpg", "beijing/anime/anime_13.jpg", true),
                new BackGroundBean("anime", "beijing/anime_small/anime_14.jpg", "beijing/anime/anime_14.jpg", true),
                new BackGroundBean("anime", "beijing/anime_small/anime_15.jpg", "beijing/anime/anime_15.jpg", true),
                new BackGroundBean("anime", "beijing/anime_small/anime_16.jpg", "beijing/anime/anime_16.jpg", true),
                new BackGroundBean("anime", "beijing/anime_small/anime_17.jpg", "beijing/anime/anime_17.jpg", true),
                new BackGroundBean("anime", "beijing/anime_small/anime_18.jpg", "beijing/anime/anime_18.jpg", true),
                new BackGroundBean("anime", "beijing/anime_small/anime_19.jpg", "beijing/anime/anime_19.jpeg", true),
                new BackGroundBean("anime", "beijing/anime_small/anime_20.jpg", "beijing/anime/anime_20.jpg", true),
                new BackGroundBean("anime", "beijing/anime_small/anime_21.jpg", "beijing/anime/anime_21.jpg", true),
                new BackGroundBean("anime", "beijing/anime_small/anime_22.jpg", "beijing/anime/anime_22.jpg", true),
                new BackGroundBean("anime", "beijing/anime_small/anime_23.jpg", "beijing/anime/anime_23.jpg", true),
                new BackGroundBean("anime", "beijing/anime_small/anime_24.jpg", "beijing/anime/anime_24.jpg", true),
                new BackGroundBean("anime", "beijing/anime_small/anime_25.jpg", "beijing/anime/anime_25.jpg", true),
                new BackGroundBean("anime", "beijing/anime_small/anime_26.jpg", "beijing/anime/anime_26.jpg", true),
                new BackGroundBean("anime", "beijing/anime_small/anime_27.jpg", "beijing/anime/anime_27.jpeg", true)
        );
        textList = Arrays.asList(
                new BackGroundBean("text", "beijing/text_small/text_1.jpg", "beijing/text/text_1.jpg", true),
                new BackGroundBean("text", "beijing/text_small/text_2.jpg", "beijing/text/text_2.jpg", true),
                new BackGroundBean("text", "beijing/text_small/text_3.jpg", "beijing/text/text_3.jpg", true),
                new BackGroundBean("text", "beijing/text_small/text_4.jpg", "beijing/text/text_4.jpg", true),
                new BackGroundBean("text", "beijing/text_small/text_5.jpg", "beijing/text/text_5.jpg", true),
                new BackGroundBean("text", "beijing/text_small/text_6.jpg", "beijing/text/text_6.jpeg", true),
                new BackGroundBean("text", "beijing/text_small/text_7.jpg", "beijing/text/text_7.jpg", true),
                new BackGroundBean("text", "beijing/text_small/text_8.jpg", "beijing/text/text_8.jpeg", true),
                new BackGroundBean("text", "beijing/text_small/text_9.jpg", "beijing/text/text_9.jpeg", true),
                new BackGroundBean("text", "beijing/text_small/text_10.jpg", "beijing/text/text_10.jpg", true),
                new BackGroundBean("text", "beijing/text_small/text_11.jpg", "beijing/text/text_11.jpg", true),
                new BackGroundBean("text", "beijing/text_small/text_12.jpg", "beijing/text/text_12.jpg", true),
                new BackGroundBean("text", "beijing/text_small/text_13.jpg", "beijing/text/text_13.jpg", true),
                new BackGroundBean("text", "beijing/text_small/text_14.jpg", "beijing/text/text_14.jpg", true),
                new BackGroundBean("text", "beijing/text_small/text_15.jpg", "beijing/text/text_15.jpg", true),
                new BackGroundBean("text", "beijing/text_small/text_16.jpg", "beijing/text/text_16.jpg", true),
                new BackGroundBean("text", "beijing/text_small/text_17.jpg", "beijing/text/text_17.jpg", true),
                new BackGroundBean("text", "beijing/text_small/text_18.jpg", "beijing/text/text_18.jpg", true),
                new BackGroundBean("text", "beijing/text_small/text_19.jpg", "beijing/text/text_19.jpg", true),
                new BackGroundBean("text", "beijing/text_small/text_20.jpg", "beijing/text/text_20.jpeg", true),
                new BackGroundBean("text", "beijing/text_small/text_21.jpg", "beijing/text/text_21.jpg", true),
                new BackGroundBean("text", "beijing/text_small/text_22.jpg", "beijing/text/text_22.jpg", true),
                new BackGroundBean("text", "beijing/text_small/text_23.jpg", "beijing/text/text_23.jpeg", true),
                new BackGroundBean("text", "beijing/text_small/text_24.jpg", "beijing/text/text_24.jpg", true),
                new BackGroundBean("text", "beijing/text_small/text_25.jpg", "beijing/text/text_25.jpeg", true),
                new BackGroundBean("text", "beijing/text_small/text_26.jpg", "beijing/text/text_26.jpg", true)
        );
        couplesList = Arrays.asList(
                new BackGroundBean("couples", "beijing/couples_small/couples_1.jpg", "beijing/couples/couples_1.jpg", true),
                new BackGroundBean("couples", "beijing/couples_small/couples_2.jpg", "beijing/couples/couples_2.jpg", true),
                new BackGroundBean("couples", "beijing/couples_small/couples_3.jpg", "beijing/couples/couples_3.jpg", true),
                new BackGroundBean("couples", "beijing/couples_small/couples_4.jpg", "beijing/couples/couples_4.jpg", true),
                new BackGroundBean("couples", "beijing/couples_small/couples_5.jpg", "beijing/couples/couples_5.jpg", true),
                new BackGroundBean("couples", "beijing/couples_small/couples_6.jpg", "beijing/couples/couples_6.jpg", true),
                new BackGroundBean("couples", "beijing/couples_small/couples_7.jpg", "beijing/couples/couples_7.jpg", true),
                new BackGroundBean("couples", "beijing/couples_small/couples_8.jpg", "beijing/couples/couples_8.jpg", true),
                new BackGroundBean("couples", "beijing/couples_small/couples_9.jpg", "beijing/couples/couples_9.jpg", true),
                new BackGroundBean("couples", "beijing/couples_small/couples_10.jpg", "beijing/couples/couples_10.jpg", true),
                new BackGroundBean("couples", "beijing/couples_small/couples_11.jpg", "beijing/couples/couples_11.jpg", true),
                new BackGroundBean("couples", "beijing/couples_small/couples_12.jpg", "beijing/couples/couples_12.jpg", true),
                new BackGroundBean("couples", "beijing/couples_small/couples_13.jpg", "beijing/couples/couples_13.jpg", true),
                new BackGroundBean("couples", "beijing/couples_small/couples_14.jpg", "beijing/couples/couples_14.jpg", true),
                new BackGroundBean("couples", "beijing/couples_small/couples_15.jpg", "beijing/couples/couples_15.jpg", true),
                new BackGroundBean("couples", "beijing/couples_small/couples_16.jpg", "beijing/couples/couples_16.jpg", true),
                new BackGroundBean("couples", "beijing/couples_small/couples_17.jpg", "beijing/couples/couples_17.jpg", true),
                new BackGroundBean("couples", "beijing/couples_small/couples_18.jpg", "beijing/couples/couples_18.jpg", true),
                new BackGroundBean("couples", "beijing/couples_small/couples_19.jpg", "beijing/couples/couples_19.jpg", true),
                new BackGroundBean("couples", "beijing/couples_small/couples_20.jpg", "beijing/couples/couples_20.jpg", true),
                new BackGroundBean("couples", "beijing/couples_small/couples_21.jpg", "beijing/couples/couples_21.jpg", true)
        );
        contractedList = Arrays.asList(
                new BackGroundBean("contracted", "beijing/contracted_small/contracted_1.jpg", "beijing/contracted/contracted_1.jpeg", true),
                new BackGroundBean("contracted", "beijing/contracted_small/contracted_2.jpg", "beijing/contracted/contracted_2.jpeg", true),
                new BackGroundBean("contracted", "beijing/contracted_small/contracted_3.jpg", "beijing/contracted/contracted_3.jpeg", true),
                new BackGroundBean("contracted", "beijing/contracted_small/contracted_4.jpg", "beijing/contracted/contracted_4.jpeg", true),
                new BackGroundBean("contracted", "beijing/contracted_small/contracted_5.jpg", "beijing/contracted/contracted_5.jpeg", true),
                new BackGroundBean("contracted", "beijing/contracted_small/contracted_6.jpg", "beijing/contracted/contracted_6.jpeg", true),
                new BackGroundBean("contracted", "beijing/contracted_small/contracted_7.jpg", "beijing/contracted/contracted_7.jpg", true),
                new BackGroundBean("contracted", "beijing/contracted_small/contracted_8.jpg", "beijing/contracted/contracted_8.jpeg", true),
                new BackGroundBean("contracted", "beijing/contracted_small/contracted_9.jpg", "beijing/contracted/contracted_9.jpeg", true),
                new BackGroundBean("contracted", "beijing/contracted_small/contracted_10.jpg", "beijing/contracted/contracted_10.jpeg", true),
                new BackGroundBean("contracted", "beijing/contracted_small/contracted_11.jpg", "beijing/contracted/contracted_11.jpg", true),
                new BackGroundBean("contracted", "beijing/contracted_small/contracted_12.jpg", "beijing/contracted/contracted_12.jpeg", true),
                new BackGroundBean("contracted", "beijing/contracted_small/contracted_13.jpg", "beijing/contracted/contracted_13.jpg", true),
                new BackGroundBean("contracted", "beijing/contracted_small/contracted_14.jpg", "beijing/contracted/contracted_14.jpeg", true),
                new BackGroundBean("contracted", "beijing/contracted_small/contracted_15.jpg", "beijing/contracted/contracted_15.jpeg", true)
        );
        colorList = Arrays.asList(Color.parseColor("#ff0000"),
                Color.parseColor("#ff4000"),
                Color.parseColor("#ff8000"),
                Color.parseColor("#ffbf00"),
                Color.parseColor("#ffff00"),
                Color.parseColor("#bfff00"),
                Color.parseColor("#80ff00"),
                Color.parseColor("#40ff00"),
                Color.parseColor("#00ff00"),
                Color.parseColor("#00ff40"),
                Color.parseColor("#00ff80"),
                Color.parseColor("#00ffbf"),
                Color.parseColor("#00ffff"),
                Color.parseColor("#00bfff"),
                Color.parseColor("#0080ff"),
                Color.parseColor("#0040ff"),
                Color.parseColor("#0000ff"),
                Color.parseColor("#4000ff"),
                Color.parseColor("#8000ff"),
                Color.parseColor("#bf00ff"),
                Color.parseColor("#ff00ff"),
                Color.parseColor("#ff00bf"),
                Color.parseColor("#ff0080"),
                Color.parseColor("#ff0040"));
        groupAdapter = new PortraitBackgroundGroupAdapter(groupList, new PortraitBackgroundGroupAdapter.OnItemClickListener() {
            @Override
            public void onItemClicked(int position) {
                switch (position) {
                    case 0:
                        PictureSelector.create(PortraitFragment.this)
                                .openGallery(SelectMimeType.ofImage())
                                .setImageEngine(GlideEngine.createGlideEngine())
                                .isPreviewImage(true)
                                .isDisplayCamera(false)
                                .setSelectionMode(SelectModeConfig.SINGLE)
                                .forResult(BG_CODE);
                        break;
                    case 1:
                        setList(naturalList);
                        break;
                    case 2:
                        setList(carList);
                        break;
                    case 3:
                        setList(gameList)
                        ;
                        break;
                    case 4:
                        setList(beautyList)
                        ;
                        break;
                    case 5:
                        setList(starList)
                        ;
                        break;
                    case 6:
                        setList(sportList)
                        ;
                        break;
                    case 7:
                        setList(animalList)
                        ;
                        break;
                    case 8:
                        setList(moveList)
                        ;
                        break;
                    case 9:
                        setList(contractedList)
                        ;
                        break;
                    case 10:
                        setList(animeList)
                        ;
                        break;
                    case 11:
                        setList(textList)
                        ;
                        break;
                    case 12:
                        setList(couplesList)
                        ;
                        break;
                    case 13:
                        setList(artisticList)
                        ;
                        break;
                    default:
                        break;

                }
            }
        });
        backgroundAdapter = new PortraitBackgroundAdapter(new PortraitBackgroundAdapter.OnItemClickedListener() {
            @Override
            public void onItemClicked(BackGroundBean backGroundBean) {
                Bitmap bitmap = adjust(AssetsUtil.getAssertFile(_mActivity, backGroundBean.getImgSrc()));
                if (bitmap == null) {
                    Toast.makeText(_mActivity, "出错了,请重试", Toast.LENGTH_SHORT).show();
                }
                srcImg.setImageBitmap(bitmap);
            }
        });
        textColorAdapter = new TextColorAdapter(colorList, new TextColorAdapter.OnItemClickedListener() {
            @Override
            public void onItemClicked(int position) {
                if (selectSticker != null && selectSticker instanceof TextSticker) {
                    ((TextSticker) selectSticker).setTextColor(colorList.get(position));
                    stickerView.invalidate();
                }
            }
        });
    }

    private Bitmap adjust(Bitmap bitmap) {
        float bitW = bitmap.getWidth();
        float bitH = bitmap.getHeight();
        float cenW = centerView.getWidth();
        float cenH = centerView.getHeight();
        float scale = bitW > bitH ? cenW / bitW : cenH / bitH;
        return BitmapUtils.bitMapScale(bitmap, scale);
    }

    private void setList(List<BackGroundBean> list) {
        isBack = true;
        backgroundAdapter.setList(list);
        recyclerView.setAdapter(backgroundAdapter);
        ivBig.setImageResource(R.drawable.shape_white_bg_radius_4dp);
        ivSmall.setImageResource(R.drawable.ic_portrait_item_back);
        tvName.setText("返回");
    }

    @Override
    protected void initListener(View view) {
        view.findViewById(R.id.layout_back).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                _mActivity.finish();
            }
        });
        view.findViewById(R.id.background).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                view.findViewById(R.id.arrow_background).setVisibility(View.VISIBLE);
                view.findViewById(R.id.bgRootView).setVisibility(View.VISIBLE);
                view.findViewById(R.id.arrow_move).setVisibility(View.INVISIBLE);
                view.findViewById(R.id.arrow_eraser).setVisibility(View.INVISIBLE);
                view.findViewById(R.id.arrow_text).setVisibility(View.INVISIBLE);
                view.findViewById(R.id.colorRecycle).setVisibility(View.GONE);
            }
        });

        view.findViewById(R.id.move).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                view.findViewById(R.id.arrow_move).setVisibility(View.VISIBLE);
                view.findViewById(R.id.arrow_background).setVisibility(View.INVISIBLE);
                view.findViewById(R.id.arrow_eraser).setVisibility(View.INVISIBLE);
                view.findViewById(R.id.arrow_text).setVisibility(View.INVISIBLE);
            }
        });
        view.findViewById(R.id.eraser).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                view.findViewById(R.id.arrow_eraser).setVisibility(View.VISIBLE);
                view.findViewById(R.id.arrow_background).setVisibility(View.INVISIBLE);
                view.findViewById(R.id.arrow_move).setVisibility(View.INVISIBLE);
                view.findViewById(R.id.arrow_text).setVisibility(View.INVISIBLE);
            }
        });
        view.findViewById(R.id.text).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                view.findViewById(R.id.arrow_text).setVisibility(View.VISIBLE);
                view.findViewById(R.id.colorRecycle).setVisibility(View.VISIBLE);
                view.findViewById(R.id.arrow_background).setVisibility(View.INVISIBLE);
                view.findViewById(R.id.arrow_move).setVisibility(View.INVISIBLE);
                view.findViewById(R.id.arrow_eraser).setVisibility(View.INVISIBLE);
                view.findViewById(R.id.bgRootView).setVisibility(View.GONE);
                TextSticker textSticker = new TextSticker(_mActivity);
                textSticker.setText("双击输入文字");
                textSticker.setTextColor(Color.BLACK);
                textSticker.setTextAlign(Layout.Alignment.ALIGN_CENTER);
                textSticker.resizeText();
                stickerView.addSticker(textSticker);
            }
        });
        view.findViewById(R.id.noBg).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (isBack) {
                    isBack = false;
                    recyclerView.setAdapter(groupAdapter);
                    ivBig.setImageResource(R.drawable.shape_portrait_item_no_bg);
                    ivSmall.setImageResource(R.drawable.ic_portrait_item_no_bg);
                    tvName.setText("无背景");
                } else {
                    srcImg.setImageBitmap(BitmapUtils.getBitmap(_mActivity, R.drawable.shape_portrait_transparent_bg, centerView.getWidth(), centerView.getHeight()));
                }
            }
        });
        view.findViewById(R.id.tv_save).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Observable.create(new ObservableOnSubscribe<String>() {
                    @Override
                    public void subscribe(ObservableEmitter<String> emitter) throws Exception {
                        Bitmap bitmap = stickerView.createBitmap();
                        String path = FileUtil.saveScreenShot(bitmap, System.currentTimeMillis() + "");
                        emitter.onNext(path);
                    }
                }).observeOn(AndroidSchedulers.mainThread()).subscribeOn(Schedulers.io()).subscribe(new Observer<String>() {
                    @Override
                    public void onSubscribe(Disposable d) {
                        mDisposable.add(d);
                    }

                    @Override
                    public void onNext(String s) {
                        start(SaveFragment.getInstance(s));
                    }

                    @Override
                    public void onError(Throwable e) {
                        e.printStackTrace();
                    }

                    @Override
                    public void onComplete() {

                    }
                });
            }
        });
        view.findViewById(R.id.layout_back).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

            }
        });
    }


    @Override
    public void onStickerAdded(@NonNull Sticker sticker) {
        selectSticker = sticker;

    }

    @Override
    public void onStickerClicked(@NonNull Sticker sticker) {
        selectSticker = sticker;
        visible(frame);
        if (sticker instanceof TextSticker) {
            gone(bgRootView, arrow_background);
            visible(colorRecycle, arrow_text);
        } else {
            visible(bgRootView, arrow_background);
            gone(colorRecycle, arrow_text);
        }
    }

    @Override
    public void onStickerDeleted(@NonNull Sticker sticker) {
        visible(frame);

    }

    @Override
    public void onStickerDragFinished(@NonNull Sticker sticker) {
        visible(frame);
        selectSticker = sticker;
    }

    @Override
    public void onStickerTouchedDown(@NonNull Sticker sticker) {
        gone(frame);
    }

    @Override
    public void onStickerZoomFinished(@NonNull Sticker sticker) {
        visible(frame);
        selectSticker = sticker;
    }

    @Override
    public void onStickerFlipped(@NonNull Sticker sticker) {
        visible(frame);
        selectSticker = sticker;
    }

    @Override
    public void onStickerDoubleTapped(@NonNull Sticker sticker) {
        if (sticker instanceof TextSticker) {
            new EditTextDialog(_mActivity, new EditTextDialog.EditTextCallback() {
                @Override
                public void editText(String str, Sticker sticker) {
                    ((TextSticker) sticker).setText(str);
                    ((TextSticker) sticker).resizeText();
                    stickerView.invalidate();
                }
            }).show();
        }
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        if (requestCode == CUT_OUT_CODE) {
            if (data != null) {
                ArrayList<LocalMedia> list = data.getParcelableArrayListExtra(PictureConfig.EXTRA_RESULT_SELECTION);
                if (list != null) {
                    int size = list.size();
                    if (size > 0) {
                        String path = list.get(0).getAvailablePath();
                        showProcessDialog();
                        BitmapUtils.loadBitmapWithSize(path)
                                .flatMap((Function<Bitmap, ObservableSource<Object[]>>) bitmap -> {
                                            String baseValue = Base64Utils.bitmapToBase64(bitmap);
                                            if (!bitmap.isRecycled()) {
                                                bitmap.recycle();
                                            }
                                            if ("portrait".equals(type)) {
                                                return EffectUtils.analyzeTheFace(baseValue);
                                            } else {
                                                return Observable.create(new ObservableOnSubscribe<Object[]>() {
                                                    @Override
                                                    public void subscribe(ObservableEmitter<Object[]> emitter) throws Exception {
                                                        Object[] objects = new Object[2];
                                                        objects[0] = true;
                                                        objects[1] = baseValue;
                                                        emitter.onNext(objects);
                                                    }
                                                });
                                            }
                                        }
                                )
                                .flatMap((Function<Object[], ObservableSource<String>>) o -> {
                                    boolean isTrue = (boolean) o[0];
                                    String base64 = (String) o[1];
                                    if (isTrue) {
                                        return EffectUtils.portraitCutout(base64);
                                    } else {
                                        throw new RuntimeException("请求裁剪图出现错误!");
                                    }
                                }).observeOn(AndroidSchedulers.mainThread()).subscribeOn(Schedulers.io()).subscribe(new Observer<String>() {
                                    @Override
                                    public void onSubscribe(Disposable d) {
                                        mDisposable.add(d);
                                    }

                                    @Override
                                    public void onNext(String s) {
                                        JSONObject jsonObject = JSONObject.parseObject(s);
                                        String imageBase64 = jsonObject.getString("ResultImage");
                                        if (!TextUtils.isEmpty(imageBase64)) {
                                            BitmapDrawable bitmapDrawable = new BitmapDrawable(getResources(), Base64Utils.base64ToBitmap(imageBase64));
                                            if (canSetBg) {
                                                canSetBg = false;
                                                srcImg.setImageBitmap(BitmapUtils.getBitmap(_mActivity, R.drawable.shape_portrait_transparent_bg, centerView.getWidth(), centerView.getHeight()));
                                            }
                                            stickerView.addSticker(new DrawableSticker(bitmapDrawable));
                                        } else {
                                            _mActivity.finish();
                                        }
                                        hideProcessDialog();
                                    }

                                    @Override
                                    public void onError(Throwable e) {
                                        Toast.makeText(_mActivity, e.getMessage(), Toast.LENGTH_SHORT).show();
                                        _mActivity.finish();
                                        hideProcessDialog();
                                    }

                                    @Override
                                    public void onComplete() {
                                    }
                                });
                    }
                }
            } else {
                _mActivity.finish();
            }
        } else if (requestCode == BG_CODE) {
            if (data != null) {
                ArrayList<LocalMedia> list = data.getParcelableArrayListExtra(PictureConfig.EXTRA_RESULT_SELECTION);
                if (list != null) {
                    int size = list.size();
                    if (size > 0) {
                        String path = list.get(0).getAvailablePath();
                        BitmapUtils.loadBitmapWithSize(path).observeOn(AndroidSchedulers.mainThread()).subscribeOn(Schedulers.io()).subscribe(new Observer<Bitmap>() {
                            @Override
                            public void onSubscribe(Disposable d) {
                                mDisposable.add(d);
                            }

                            @Override
                            public void onNext(Bitmap bitmap) {
                                srcImg.setImageBitmap(adjust(bitmap));
                            }

                            @Override
                            public void onError(Throwable e) {
                                Toast.makeText(_mActivity, e.getMessage(), Toast.LENGTH_SHORT).show();
                            }

                            @Override
                            public void onComplete() {

                            }
                        });
                    }
                }

            }
        }
        super.onActivityResult(requestCode, resultCode, data);
    }

    @Override
    public boolean onBackPressedSupport() {
        new PortraitNoSaveDialog(_mActivity).setConfimText("再想想").setCancelText("退出").setTitleText("抠图尚未保存，是否确定推出编辑").show();
        return true;
    }
}
