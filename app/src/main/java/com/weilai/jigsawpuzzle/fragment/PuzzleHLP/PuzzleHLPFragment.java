package com.weilai.jigsawpuzzle.fragment.PuzzleHLP;

import android.content.Intent;
import android.content.res.TypedArray;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.drawable.ColorDrawable;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.util.LruCache;
import android.view.View;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.widget.AppCompatTextView;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.luck.picture.lib.basic.PictureSelector;
import com.luck.picture.lib.config.PictureConfig;
import com.luck.picture.lib.config.SelectMimeType;
import com.luck.picture.lib.config.SelectModeConfig;
import com.luck.picture.lib.entity.LocalMedia;
import com.weilai.jigsawpuzzle.R;
import com.weilai.jigsawpuzzle.adapter.puzzleHLP.HLongPicItemAdapter;
import com.weilai.jigsawpuzzle.adapter.puzzleLP.LongPicItemAdapter;
import com.weilai.jigsawpuzzle.base.BaseFragment;
import com.weilai.jigsawpuzzle.bean.PicInfo;
import com.weilai.jigsawpuzzle.bean.TabEntity;
import com.weilai.jigsawpuzzle.dialog.puzzleLP.PuzzleLpColorPopUp;
import com.weilai.jigsawpuzzle.dialog.puzzleLP.PuzzleLpPopUp;
import com.weilai.jigsawpuzzle.event.LpSortEvent;
import com.weilai.jigsawpuzzle.event.LpSplitEvent;
import com.weilai.jigsawpuzzle.fragment.main.SaveFragment;
import com.weilai.jigsawpuzzle.fragment.puzzleLPic.PuzzleLPSortFragment;
import com.weilai.jigsawpuzzle.fragment.puzzleLPic.PuzzleLongPicFragment;
import com.weilai.jigsawpuzzle.fragment.puzzleLPic.PuzzleLpSplitFragment;
import com.weilai.jigsawpuzzle.util.BitmapUtils;
import com.weilai.jigsawpuzzle.util.DimenUtil;
import com.weilai.jigsawpuzzle.util.FileUtil;
import com.weilai.jigsawpuzzle.util.GlideEngine;
import com.weilai.jigsawpuzzle.util.L;
import com.weilai.jigsawpuzzle.util.ToastUtil;
import com.weilai.jigsawpuzzle.weight.main.FlyTabLayout;
import com.weilai.jigsawpuzzle.weight.puzzleHLP.PaddingHirItemDecoration;
import com.weilai.jigsawpuzzle.weight.puzzleLP.PaddingItemDecoration;
import com.weilai.library.listener.CustomTabEntity;
import com.weilai.library.listener.OnTabSelectListener;
import com.xinlan.imageeditlibrary.editimage.EditImageActivity;

import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import java.util.ArrayList;

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
 * * DATE: 2022/10/13
 * * Author:tangerine
 * * Description:
 **/
public class PuzzleHLPFragment extends BaseFragment implements OnTabSelectListener, PuzzleLpPopUp.OnPopUpDismiss, PuzzleLpColorPopUp.OnColorChangedListener {
    private final String[] titles = {"边框", "添加", "排序"};
    private final int[] titlesIcon = new int[]{R.mipmap.icon_lp_frame, R.mipmap.icon_add, R.mipmap.icon_sort};
    private RecyclerView mRvLP;

    private ArrayList<PicInfo> picInfos;
    private static final int FILTER_PUZZLE_LP_CODE = 1;
    private static final int FILTER_PUZZLE_LP_SINGLE = 2;
    private static final int FILTER_PUZZLE_LP_EDIT = 3;
    private HLongPicItemAdapter hLongPicItemAdapter;
    private PuzzleLpPopUp mPuzzleLpPopUp;
    private FlyTabLayout mFlyTabLayout;
    private PuzzleLpColorPopUp mPuzzleLpColor;
    private final int[] integers = new int[]{R.mipmap.split_left, R.mipmap.split_right, R.mipmap.icon_edit, R.mipmap.icon_replace, R.mipmap.icon_delete};

    private PuzzleHLPFragment() {

    }

    public static PuzzleHLPFragment getInstance(ArrayList<String> path) {
        Bundle bundle = new Bundle();
        bundle.putStringArrayList("data", path);
        PuzzleHLPFragment puzzleHLPFragment = new PuzzleHLPFragment();
        puzzleHLPFragment.setArguments(bundle);
        return puzzleHLPFragment;
    }

    @Override
    protected Object setLayout() {
        return R.layout.fragment_puzzle_hlp;
    }

    private PaddingHirItemDecoration paddingItemDecoration;

    @Override
    protected void initView(View view) {
        String[] tipsTitle = new String[]{getString(R.string.split_left), getString(R.string.split_right), getString(R.string.edit), getString(R.string.replace), getString(R.string.delete)};
        mPuzzleLpPopUp = new PuzzleLpPopUp(_mActivity, this, tipsTitle, integers);
        mPuzzleLpColor = new PuzzleLpColorPopUp(_mActivity, this);
        mPuzzleLpPopUp.setOutSideDismiss(true);
        mPuzzleLpPopUp.setOutSideTouchable(true);
        view.findViewById(R.id.tv_save).setVisibility(View.VISIBLE);
        mRvLP = view.findViewById(R.id.rv_lp);
        LinearLayoutManager picHriManager = new LinearLayoutManager(_mActivity);
        picHriManager.setOrientation(RecyclerView.HORIZONTAL);
        mRvLP.setLayoutManager(picHriManager);
        AppCompatTextView tvTitle = view.findViewById(R.id.tv_title);
        tvTitle.setText("拼横图");
        ArrayList<CustomTabEntity> entities = new ArrayList<>();
        for (int i = 0; i < titles.length; i++) {
            TabEntity tabEntity = new TabEntity(titles[i], titlesIcon[i], titlesIcon[i]);
            entities.add(tabEntity);
        }
        mFlyTabLayout = view.findViewById(R.id.tabLayout);
        mFlyTabLayout.setTabData(entities);
        mFlyTabLayout.setCurrentTab(0);
        mFlyTabLayout.setOnTabSelectListener(this);
        //遍历所有的Bitmap
        assert getArguments() != null;
        ArrayList<String> bitmaps = getArguments().getStringArrayList("data");
        initData(bitmaps);
        hLongPicItemAdapter = new HLongPicItemAdapter(_mActivity, picInfos);
        mRvLP.setAdapter(hLongPicItemAdapter);
        paddingItemDecoration = new PaddingHirItemDecoration(mRvLP);
        mRvLP.addItemDecoration(paddingItemDecoration);
        TypedArray actionbarSizeTypedArray = _mActivity.obtainStyledAttributes(new int[]{
                android.R.attr.actionBarSize
        });
        actionbarSizeTypedArray.recycle();
    }

    private void initData(ArrayList<String> data) {
        picInfos = new ArrayList<>();
        for (String path : data) {
            PicInfo picInfo = new PicInfo();
            picInfo.shouldLoad = BitmapUtils.shouldLoadBitmap(path, true);
            picInfo.path = path;
            picInfos.add(picInfo);
        }
    }

    @Override
    protected void initListener(View view) {
        mRvLP.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrolled(@NonNull RecyclerView recyclerView, int dx, int dy) {
                mPuzzleLpPopUp.setVisibility();
                if (hLongPicItemAdapter != null) {
                    hLongPicItemAdapter.resetItem();
                }
                super.onScrolled(recyclerView, dx, dy);
            }
        });
        view.findViewById(R.id.layout_back).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                _mActivity.finish();
            }
        });
        hLongPicItemAdapter.setOnItemClickedListener((itemView, position) -> {
            if (mPuzzleLpPopUp.isShowing()) {
                mPuzzleLpPopUp.dismiss();
            }
            mPuzzleLpPopUp.show(mRvLP, false, position);
        });
        view.findViewById(R.id.tv_save).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                doOnBackGround();
            }
        });
    }

    private void shotScrollView(RecyclerView view) {
        Observable.create(new ObservableOnSubscribe<ArrayList<RecyclerView.ViewHolder>>() {
            @Override
            public void subscribe(ObservableEmitter<ArrayList<RecyclerView.ViewHolder>> emitter) throws Exception {
                showProcessDialog();
                RecyclerView.Adapter adapter = view.getAdapter();
                ArrayList<RecyclerView.ViewHolder> arrayList = new ArrayList<>();
                if (adapter != null) {
                    int size = adapter.getItemCount();
                    // Use 1/8th of the available memory for this memory cache.
                    for (int i = 0; i < size; i++) {
                        RecyclerView.ViewHolder holder = adapter.createViewHolder(view, adapter.getItemViewType(i));
                        adapter.onBindViewHolder(holder, i);
                        holder.itemView.measure(
                                View.MeasureSpec.makeMeasureSpec(0, View.MeasureSpec.UNSPECIFIED),
                                View.MeasureSpec.makeMeasureSpec(DimenUtil.getScreenHeight() / 2, View.MeasureSpec.EXACTLY)
                        );
                        holder.itemView.layout(0, 0, holder.itemView.getMeasuredWidth(),
                                DimenUtil.getScreenHeight() / 2);
                        int padding = paddingItemDecoration.getProcess();
                        if (i == 0) {
                            holder.itemView.setPadding(padding, padding, 0, padding);
                        } else if (i == size - 1) {
                            holder.itemView.setPadding(padding, padding, padding, padding);
                        } else {
                            holder.itemView.setPadding(padding, padding, 0, padding);
                        }
                        holder.itemView.setBackgroundColor(Color.parseColor(paddingItemDecoration.getBackgroundColor()));
                        arrayList.add(holder);
                    }

                }
                emitter.onNext(arrayList);
            }
        }).observeOn(Schedulers.newThread()).flatMap(new Function<ArrayList<RecyclerView.ViewHolder>, ObservableSource<String>>() {
            @Override
            public ObservableSource<String> apply(ArrayList<RecyclerView.ViewHolder> viewHolders) throws Exception {
                Bitmap bigBitmap = null;
                int width = 0;
                Paint paint = new Paint();
                int iWidth = 0;
                final int maxMemory = (int) (Runtime.getRuntime().maxMemory() / 1024);
                // Use 1/8th of the available memory for this memory cache.
                final int cacheSize = maxMemory / 8;
                LruCache<String, Bitmap> bitmaCache = new LruCache<>(cacheSize);
                for (int i = 0; i < viewHolders.size(); i++) {
                    RecyclerView.ViewHolder holder = viewHolders.get(i);
                    holder.itemView.setDrawingCacheEnabled(true);
                    holder.itemView.buildDrawingCache();
                    try {
                        Thread.sleep(500);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                    Bitmap drawingCache = holder.itemView.getDrawingCache();
                    if (drawingCache != null) {
                        bitmaCache.put(String.valueOf(i), drawingCache);
                    }
                    width += holder.itemView.getMeasuredWidth();
                }
                bigBitmap = Bitmap.createBitmap(width, DimenUtil.getScreenHeight() / 2, Bitmap.Config.ARGB_8888);
                Canvas bigCanvas = new Canvas(bigBitmap);
                Drawable lBackground = view.getBackground();
                if (lBackground instanceof ColorDrawable) {
                    ColorDrawable lColorDrawable = (ColorDrawable) lBackground;
                    int lColor = lColorDrawable.getColor();
                    bigCanvas.drawColor(lColor);
                }
                for (int i = 0; i < viewHolders.size(); i++) {
                    Bitmap bitmap = bitmaCache.get(String.valueOf(i));
                    bigCanvas.drawBitmap(bitmap, iWidth, 0, paint);
                    iWidth += bitmap.getWidth();
                    bitmap.recycle();
                }
                String filePath = FileUtil.saveScreenShot(bigBitmap, System.currentTimeMillis() + "");
                return Observable.just(filePath);
            }
        }).subscribeOn(AndroidSchedulers.mainThread()).observeOn(AndroidSchedulers.mainThread()).subscribe(new Observer<String>() {
            @Override
            public void onSubscribe(Disposable d) {
                mDisposable.add(d);
            }

            @Override
            public void onNext(String s) {
                hideProcessDialog();
                SaveFragment puzzleLpAdjustFragment = SaveFragment.getInstance(s,"横拼");
                start(puzzleLpAdjustFragment);
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

    private void doOnBackGround() {
        shotScrollView(mRvLP);
    }

    @Override
    public void onTabSelect(int position) {
        switch (position) {
            case 0:
                //show
                mPuzzleLpColor.show(mFlyTabLayout, false);
                break;
            case 1:
                PictureSelector.create(this).openGallery(SelectMimeType.ofImage())
                        .isDisplayCamera(true)
                        .setMaxSelectNum(9)
                        .setSelectionMode(SelectModeConfig.MULTIPLE)
                        .setImageEngine(GlideEngine.createGlideEngine())
                        .forResult(FILTER_PUZZLE_LP_CODE);

                break;
            case 2:
                puzzleLPSortFragment = PuzzleLPSortFragment.getInstance(picInfos);
                start(puzzleLPSortFragment);
                break;
            default:
                break;
        }
    }

    private PuzzleLPSortFragment puzzleLPSortFragment = null;
    private PuzzleLpSplitFragment puzzleLpSplitFragment = null;

    @Override
    public void onTabReselect(int position) {
        onTabSelect(position);
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onDataSortChange(LpSortEvent event) {
        picInfos = (ArrayList<PicInfo>) event.data;
        hLongPicItemAdapter.notifyDataSetChanged();
        puzzleLPSortFragment.pop();
        puzzleLPSortFragment = null;
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onDataSplitChange(LpSplitEvent event) {
        ArrayList<String> data = (ArrayList<String>) event.data;
        int type = event.type;
        if (type == 1) {
            PicInfo picInfo = picInfos.get(selectedPosition);
            picInfo.path = data.get(0);
            picInfos.remove(selectedPosition);
            picInfos.add(selectedPosition, picInfo);
        } else if (type == 2) {
            PicInfo picInfo = picInfos.get(selectedPosition);
            picInfo.path = data.get(0);
            picInfos.remove(selectedPosition);
            picInfos.add(selectedPosition, picInfo);
            if (selectedPosition + 1 < picInfos.size()) {
                PicInfo picInfo1 = picInfos.get(selectedPosition + 1);
                picInfo1.path = data.get(1);
                picInfos.remove(selectedPosition + 1);
                picInfos.add(selectedPosition + 1, picInfo1);
            }
        }
        hLongPicItemAdapter.notifyDataSetChanged();
        puzzleLpSplitFragment.pop();
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (data != null) {
            ArrayList<LocalMedia> list = data.getParcelableArrayListExtra(PictureConfig.EXTRA_RESULT_SELECTION);
            if (requestCode == FILTER_PUZZLE_LP_CODE) {
                if (list != null) {
                    int size = list.size();
                    if (size > 0) {
                        for (LocalMedia localMedia : list) {
                            if (localMedia.getSize() > 10368000) {
                                ToastUtil.showToast(localMedia.getFileName() + ",这个文件太大了");
                                return;
                            }
                            String path = localMedia.getAvailablePath();
                            PicInfo picInfo = new PicInfo();
                            picInfo.path = path;
                            picInfo.shouldLoad = BitmapUtils.shouldLoadBitmap(path, true);
                            picInfos.add(picInfo);
                            hLongPicItemAdapter.notifyItemInserted(picInfos.size());
                        }
                    }
                }
            } else if (requestCode == FILTER_PUZZLE_LP_SINGLE) {
                if (list != null) {
                    int size = list.size();
                    if (size > 0) {
                        String path = list.get(0).getAvailablePath();
                        PicInfo picInfo = new PicInfo();
                        picInfo.path = path;
                        picInfo.shouldLoad = BitmapUtils.shouldLoadBitmap(path, true);
                        picInfos.remove(selectedPosition);
                        picInfos.add(selectedPosition, picInfo);
                        hLongPicItemAdapter.notifyItemChanged(selectedPosition);
                    }
                }
            } else if (requestCode == FILTER_PUZZLE_LP_EDIT) {
                String filePath = data.getStringExtra("extra_output");
                if (selectedPosition != -1) {
                    boolean isEdit = data.getBooleanExtra("image_is_edit", false);
                    if (isEdit) {
                        PicInfo picInfo = new PicInfo();
                        picInfo.path = filePath;
                        picInfo.shouldLoad = BitmapUtils.shouldLoadBitmap(filePath, true);
                        picInfos.remove(selectedPosition);
                        picInfos.add(selectedPosition, picInfo);
                        hLongPicItemAdapter.notifyItemChanged(selectedPosition);
                    }
                }
            }
        }

    }

    @Override
    public void dismiss() {
        hLongPicItemAdapter.resetItem();
    }

    @Override
    public void onHiddenChanged(boolean hidden) {
        if (hidden) {
            mPuzzleLpPopUp.dismiss();
        }
        super.onHiddenChanged(hidden);
    }

    private int selectedPosition = -1;

    @Override
    public void clicked(View view, int position) {
        selectedPosition = position;
        hLongPicItemAdapter.resetItem();
        mPuzzleLpPopUp.dismiss();
        ArrayList<PicInfo> bitmapsInfo = new ArrayList<>();
        switch (view.getId()) {
            case 0:
                //裁顶
                if (selectedPosition == 0) {
                    bitmapsInfo.add(picInfos.get(selectedPosition));
                    puzzleLpSplitFragment = PuzzleLpSplitFragment.getInstance(bitmapsInfo, 1, 2);
                    start(puzzleLpSplitFragment);
                }
                break;
            case 1:
                bitmapsInfo.add(picInfos.get(selectedPosition));
                if (selectedPosition + 1 < picInfos.size()) {
                    bitmapsInfo.add(picInfos.get(selectedPosition + 1));
                }
                puzzleLpSplitFragment = PuzzleLpSplitFragment.getInstance(bitmapsInfo, 2, 1);
                start(puzzleLpSplitFragment);
                //裁底
                break;
            case 2:
                //编辑
                Intent it = new Intent(getContext(), EditImageActivity.class);
                it.putExtra(EditImageActivity.FILE_PATH, picInfos.get(selectedPosition));
                it.putExtra(EditImageActivity.EXTRA_OUTPUT, FileUtil.getAnPicPath(System.currentTimeMillis() + "_editor"));
                startActivityForResult(it, FILTER_PUZZLE_LP_EDIT);
                break;
            case 3:
                //替换

                PictureSelector.create(this).openGallery(SelectMimeType.ofImage())
                        .isDisplayCamera(true)
                        .setSelectionMode(SelectModeConfig.SINGLE)
                        .setImageEngine(GlideEngine.createGlideEngine())
                        .forResult(FILTER_PUZZLE_LP_SINGLE);

                break;
            case 4:
                picInfos.remove(position);
                hLongPicItemAdapter.notifyDataSetChanged();
                //删除
                break;
        }

    }


    @Override
    public void onProcessed(int position) {
        paddingItemDecoration.setProcess(position);
    }

    @Override
    public void onColorChanged(String color) {
        paddingItemDecoration.setBackground(color);

    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        mPuzzleLpPopUp.onDestroy();
        mPuzzleLpColor.onDestroy();
    }
}
