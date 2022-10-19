package com.weilai.jigsawpuzzle.fragment.portrait;

import android.graphics.Color;
import android.os.Bundle;
import android.view.View;

import androidx.annotation.Nullable;
import androidx.appcompat.widget.AppCompatTextView;

import com.weilai.jigsawpuzzle.R;
import com.weilai.jigsawpuzzle.base.BaseFragment;
import com.weilai.jigsawpuzzle.bean.BackGroundBean;

import java.util.ArrayList;
import java.util.Arrays;

/**
 * * DATE: 2022/10/19
 * * Author:tangerine
 * * Description:
 **/
public class PortraitFragment extends BaseFragment {
    private ArrayList<BackGroundBean> groupList;
    private ArrayList<BackGroundBean> naturalList;
    private ArrayList<BackGroundBean> carList;
    private ArrayList<BackGroundBean> beautyList;
    private ArrayList<BackGroundBean> starList;
    private ArrayList<BackGroundBean> gameList;
    private ArrayList<BackGroundBean> sportList;
    private ArrayList<BackGroundBean> animalList;
    private ArrayList<BackGroundBean> moveList;
    private ArrayList<BackGroundBean> artisticList;
    private ArrayList<BackGroundBean> animeList;
    private ArrayList<BackGroundBean> textList;
    private ArrayList<BackGroundBean> couplesList;
    private ArrayList<BackGroundBean> contractedList;
    private ArrayList<Integer> colorList;

    @Override
    protected Object setLayout() {
        return R.layout.fragment_portrait;
    }

    private PortraitFragment() {

    }

    public static PortraitFragment getInstance(String path) {
        Bundle bundle = new Bundle();
        bundle.putString("data", path);
        PortraitFragment portraitFragment = new PortraitFragment();
        portraitFragment.setArguments(bundle);
        return portraitFragment;
    }

    @Override
    protected void initView(View view) {
        AppCompatTextView tvTitle = view.findViewById(R.id.tv_title);
        tvTitle.setText("智能抠图");
    }

    @Override
    public void onLazyInitView(@Nullable Bundle savedInstanceState) {

        initData();
    }

    private void initData() {
        groupList = (ArrayList<BackGroundBean>) Arrays.asList(new BackGroundBean("album", "相册", "beijing/group/album.png", R.drawable.ic_avatar, false),
                new BackGroundBean("natural", "自然", "beijing/group/natural.png", R.drawable.ic_avatar, false),
                new BackGroundBean("car", "汽车", "beijing/group/car.png", R.drawable.ic_avatar, false),
                new BackGroundBean("game", "游戏", "beijing/group/game.png", R.drawable.ic_avatar, false),
                new BackGroundBean("beauty", "美女", "beijing/group/beauty.png", R.drawable.ic_avatar, false),
                new BackGroundBean("star", "明星", "beijing/group/star.png", R.drawable.ic_avatar, true),
                new BackGroundBean("sports", "体育", "beijing/group/sport.png", R.drawable.ic_avatar, true),
                new BackGroundBean("animal", "动物萌宠", "beijing/group/anima.png", R.drawable.ic_avatar, true),
                new BackGroundBean("mov", "影视", "beijing/group/mov.png", R.drawable.ic_avatar, true),
                new BackGroundBean("contracted", "简约抽象", "beijing/group/contracted.png", R.drawable.ic_avatar, true),
                new BackGroundBean("anime", "动漫", "beijing/group/anime.png", R.drawable.ic_avatar, true),
                new BackGroundBean("text", "文字", "beijing/group/text.png", R.drawable.ic_avatar, true),
                new BackGroundBean("couples", "情侣", "beijing/group/couples.png", R.drawable.ic_avatar, true),
                new BackGroundBean("artistic", "意境", "beijing/group/artistic.png", R.drawable.ic_avatar, true));
        naturalList = (ArrayList<BackGroundBean>) Arrays.asList(
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
        carList = (ArrayList<BackGroundBean>) Arrays.asList(
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
        beautyList = (ArrayList<BackGroundBean>) Arrays.asList(
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
        starList = (ArrayList<BackGroundBean>) Arrays.asList(
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
        sportList = (ArrayList<BackGroundBean>) Arrays.asList(
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
        gameList = (ArrayList<BackGroundBean>) Arrays.asList(
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
        animalList = (ArrayList<BackGroundBean>) Arrays.asList(
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
        moveList = (ArrayList<BackGroundBean>) Arrays.asList(
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
        artisticList = (ArrayList<BackGroundBean>) Arrays.asList(
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
        animeList = (ArrayList<BackGroundBean>) Arrays.asList(
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
        textList = (ArrayList<BackGroundBean>) Arrays.asList(
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
        couplesList = (ArrayList<BackGroundBean>) Arrays.asList(
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
        contractedList = (ArrayList<BackGroundBean>) Arrays.asList(
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
        colorList = (ArrayList<Integer>) Arrays.asList(Color.parseColor("#ff0000"),
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


    }


    @Override
    protected void initListener(View view) {
        view.findViewById(R.id.layout_back).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                _mActivity.finish();
            }
        });
    }
}
