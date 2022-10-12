package com.xinlan.imageeditlibrary.editimage.fragment;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import android.annotation.TargetApi;
import android.app.Dialog;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.graphics.Matrix;
import android.graphics.RectF;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.xinlan.imageeditlibrary.BaseActivity;
import com.xinlan.imageeditlibrary.R;
import com.xinlan.imageeditlibrary.editimage.EditImageActivity;
import com.xinlan.imageeditlibrary.editimage.ModuleConfig;
import com.xinlan.imageeditlibrary.editimage.model.RatioItem;
import com.xinlan.imageeditlibrary.editimage.utils.Matrix3;
import com.xinlan.imageeditlibrary.editimage.view.CropImageView;
import com.xinlan.imageeditlibrary.editimage.view.imagezoom.ImageViewTouchBase;


/**
 * 图片剪裁Fragment
 * 
 * @author panyi
 * 
 */
public class CropFragment extends BaseEditFragment {
    public static final int INDEX = ModuleConfig.INDEX_CROP;
	public static final String TAG = CropFragment.class.getName();
	private View mainView;
	public CropImageView mCropPanel;// 剪裁操作面板
	private LinearLayout ratioList;
	private static final List<RatioItem> dataList = new ArrayList<>();

	private View mSelectedView;
	private final CropRationClick mCropRationClick = new CropRationClick();

	public static CropFragment newInstance() {
		return new CropFragment();
	}

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		dataList.add(new RatioItem("自由", -1f,R.mipmap.icon_free_unselected,R.mipmap.icon_free_selected));
		dataList.add(new RatioItem("1:1", 1f,R.mipmap.icon_11_unselected,R.mipmap.icon_11_selected));
		dataList.add(new RatioItem("3:2", 3 / 2f,R.mipmap.icon_32_unselected,R.mipmap.icon_32_selected));
		dataList.add(new RatioItem("4:3", 4 / 3f,R.mipmap.icon_43_unselected,R.mipmap.icon_43_selected));
		dataList.add(new RatioItem("2:3", 2 / 3f,R.mipmap.icon_23_unselected,R.mipmap.icon_23_selected));
		dataList.add(new RatioItem("16:9",16/9f,R.mipmap.icon_169_unselected,R.mipmap.icon_169_selected));
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		mainView = inflater.inflate(R.layout.fragment_edit_image_crop, null);
		return mainView;
	}

	private void setUpRatioList() {
		// init UI
		ratioList.removeAllViews();
		LinearLayout.LayoutParams imageViewParams = new LinearLayout.LayoutParams(150,150);
		LinearLayout.LayoutParams parentViewParams = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.WRAP_CONTENT, LinearLayout.LayoutParams.WRAP_CONTENT,1);
		LinearLayout.LayoutParams textViewParams = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);
		textViewParams.gravity = Gravity.CENTER;
		imageViewParams.gravity = Gravity.CENTER;
		parentViewParams.leftMargin = 10;
		parentViewParams.rightMargin = 10;
		parentViewParams.gravity =Gravity.CENTER;
		for (int i = 0, len = dataList.size(); i < len; i++) {
			TextView text = new TextView(activity);
			text.setTextColor(Color.BLACK);
			text.setTextSize(20);
			text.setText(dataList.get(i).getText());
			ImageView imageView = new ImageView(activity);
			imageView.setImageResource(dataList.get(i).getIconUnSelected());
			imageView.setLayoutParams(imageViewParams);
			imageView.setId(0);
			LinearLayout linearLayout = new LinearLayout(activity);
			linearLayout.setOrientation(LinearLayout.VERTICAL);
			linearLayout.addView(imageView);
			linearLayout.addView(text,textViewParams);
			linearLayout.setLayoutParams(parentViewParams);
			ratioList.addView(linearLayout);
			dataList.get(i).setIndex(i);
			linearLayout.setTag(dataList.get(i));
			linearLayout.setOnClickListener(mCropRationClick);
			if (i == 0) {
				mSelectedView = linearLayout;
				imageView.setImageResource(dataList.get(i).getIconSelected());
			}

		}// end for i
	}

	/**
	 * 选择剪裁比率
	 * 
	 * @author
	 * 
	 */
	private final class CropRationClick implements OnClickListener {
		@Override
		public void onClick(View v) {
			RatioItem dataItem = (RatioItem) v.getTag();
			ImageView imageView = v.findViewById(0);
			imageView.setImageResource(dataItem.getIconSelected());
			ImageView imageView1 = mSelectedView.findViewById(0);
			RatioItem dataItem1 = (RatioItem) mSelectedView.getTag();
			imageView1.setImageResource(dataItem1.getIconUnSelected());
			mCropPanel.setRatioCropRect(activity.mainImage.getBitmapRect(),
					dataItem.getRatio());
			mSelectedView = v;
			// System.out.println("dataItem   " + dataItem.getText());
		}
	}// end inner class

	@Override
	public void onActivityCreated(Bundle savedInstanceState) {
		super.onActivityCreated(savedInstanceState);

		// 返回主菜单
		View backToMenu = mainView.findViewById(R.id.back_to_main);
        ratioList = mainView.findViewById(R.id.ratio_list_group);
        setUpRatioList();
        this.mCropPanel = ensureEditActivity().mCropPanel;
		backToMenu.setOnClickListener(new BackToMenuClick());// 返回主菜单
		mainView.findViewById(R.id.iv_save).setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				applyCropImage();
			}
		});
	}

    @Override
    public void onShow() {
        activity.mode = EditImageActivity.MODE_CROP;

        activity.mCropPanel.setVisibility(View.VISIBLE);
        activity.mainImage.setImageBitmap(activity.getMainBit());
        activity.mainImage.setDisplayType(ImageViewTouchBase.DisplayType.FIT_TO_SCREEN);
        activity.mainImage.setScaleEnabled(false);// 禁用缩放
		activity.findViewById(R.id.tv_save).setVisibility(View.INVISIBLE);
		// System.out.println(r.left + "    " + r.top);
		//  bug  fixed  https://github.com/siwangqishiq/ImageEditor-Android/issues/59
		// 设置完与屏幕匹配的尺寸  确保变换矩阵设置生效后才设置裁剪区域
		activity.mainImage.post(new Runnable() {
			@Override
			public void run() {
				final RectF r = activity.mainImage.getBitmapRect();
				activity.mCropPanel.setCropRect(r);
			}
		});
    }

    /**
	 * 返回按钮逻辑
	 * 
	 * @author panyi
	 * 
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
		mCropPanel.setVisibility(View.GONE);
		activity.mainImage.setScaleEnabled(true);// 恢复缩放功能
		activity.bottomGallery.setCurrentItem(0);
		activity.findViewById(R.id.tv_save).setVisibility(View.VISIBLE);
		mCropPanel.setRatioCropRect(activity.mainImage.getBitmapRect(), -1);
	}

	/**
	 * 保存剪切图片
	 */
	public void applyCropImage() {
		// System.out.println("保存剪切图片");
		CropImageTask task = new CropImageTask();
		task.execute(activity.getMainBit());
	}

	/**
	 * 图片剪裁生成 异步任务
	 * 
	 * @author panyi
	 * 
	 */
	private  final class CropImageTask extends AsyncTask<Bitmap, Void, Bitmap> {
		private Dialog dialog;

		@Override
		protected void onCancelled() {
			super.onCancelled();
			dialog.dismiss();
		}

		@TargetApi(Build.VERSION_CODES.HONEYCOMB)
		@Override
		protected void onCancelled(Bitmap result) {
			super.onCancelled(result);
			dialog.dismiss();
		}

		@Override
		protected void onPreExecute() {
			super.onPreExecute();
			dialog = BaseActivity.getLoadingDialog(getActivity(), R.string.saving_image,
					false);
			dialog.show();
		}

		@SuppressWarnings("WrongThread")
        @Override
		protected Bitmap doInBackground(Bitmap... params) {
			RectF cropRect = mCropPanel.getCropRect();// 剪切区域矩形
			Matrix touchMatrix = activity.mainImage.getImageViewMatrix();
			// Canvas canvas = new Canvas(resultBit);
			float[] data = new float[9];
			touchMatrix.getValues(data);// 底部图片变化记录矩阵原始数据
			Matrix3 cal = new Matrix3(data);// 辅助矩阵计算类
			Matrix3 inverseMatrix = cal.inverseMatrix();// 计算逆矩阵
			Matrix m = new Matrix();
			m.setValues(inverseMatrix.getValues());
			m.mapRect(cropRect);// 变化剪切矩形

			// Paint paint = new Paint();
			// paint.setColor(Color.RED);
			// paint.setStrokeWidth(10);
			// canvas.drawRect(cropRect, paint);
			// Bitmap resultBit = Bitmap.createBitmap(params[0]).copy(
			// Bitmap.Config.ARGB_8888, true);
			Bitmap resultBit = Bitmap.createBitmap(params[0],
					(int) cropRect.left, (int) cropRect.top,
					(int) cropRect.width(), (int) cropRect.height());

			//saveBitmap(resultBit, activity.saveFilePath);
			return resultBit;
		}

		@Override
		protected void onPostExecute(Bitmap result) {
			super.onPostExecute(result);
			dialog.dismiss();
			if (result == null)
				return;

            activity.changeMainBitmap(result,true);
			activity.mCropPanel.setCropRect(activity.mainImage.getBitmapRect());
			backToMain();
		}
	}// end inner class

	/**
	 * 保存Bitmap图片到指定文件
	 */
	public static void saveBitmap(Bitmap bm, String filePath) {
		File f = new File(filePath);
		if (f.exists()) {
			f.delete();
		}
		try {
			FileOutputStream out = new FileOutputStream(f);
			bm.compress(Bitmap.CompressFormat.PNG, 90, out);
			out.flush();
			out.close();
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		// System.out.println("保存文件--->" + f.getAbsolutePath());
	}
}// end class
