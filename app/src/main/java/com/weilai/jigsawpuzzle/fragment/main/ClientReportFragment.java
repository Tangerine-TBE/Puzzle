package com.weilai.jigsawpuzzle.fragment.main;

import android.text.TextUtils;
import android.view.View;
import android.widget.Toast;

import androidx.appcompat.widget.AppCompatEditText;

import com.alibaba.fastjson.JSONObject;
import com.weilai.jigsawpuzzle.BaseConstant;
import com.weilai.jigsawpuzzle.R;
import com.weilai.jigsawpuzzle.base.BaseFragment;
import com.weilai.jigsawpuzzle.net.base.NetConfig;
import com.weilai.jigsawpuzzle.util.L;
import com.weilai.jigsawpuzzle.util.ToastUtil;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import io.reactivex.Observer;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.Disposable;
import io.reactivex.schedulers.Schedulers;
import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import okhttp3.Response;
import okhttp3.ResponseBody;

/**
 ** DATE: 2022/10/18
 ** Author:tangerine
 ** Description:
 **/
public class ClientReportFragment extends BaseFragment {
    private AppCompatEditText etPros;
    private AppCompatEditText etName;
    private ClientReportFragment (){

    }
    public static ClientReportFragment getInstance(){
        return new ClientReportFragment();
    }
    @Override
    protected Object setLayout() {
        return R.layout.fragment_client_report;
    }

    @Override
    protected void initView(View view) {
        etPros = view.findViewById(R.id.et_report);
        etName = view.findViewById(R.id.et_number);

    }

    @Override
    protected void initListener(View view) {
        view.findViewById(R.id.layout_back).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                _mActivity.finish();
            }
        });
        view.findViewById(R.id.btn_start).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String text = etPros.getText().toString();
                if (TextUtils.isEmpty(text)){
                    Toast.makeText(_mActivity, "请填写具体问题", Toast.LENGTH_SHORT).show();
                    return;
                }
                String name = etName.getText().toString();
                if (TextUtils.isEmpty(name)){
                    Toast.makeText(_mActivity, "请填写联系方式", Toast.LENGTH_SHORT).show();
                    return;
                }
                //发送

                MultipartBody.Builder builder = new MultipartBody.Builder();
                builder.addFormDataPart("user_id","111");
                builder.addFormDataPart("content",text);
                builder.addFormDataPart("user_system","1");
                builder.addFormDataPart("user_package",_mActivity.getPackageName());
                builder.addFormDataPart("package_chn","拼图抠图");
                builder.setType(MultipartBody.FORM);
                MultipartBody multipartBody = builder.build();
                showProcessDialog();
                NetConfig.getInstance().getINetService().clientReport("https://catapi.aisou.club/manysmall/public/addvice",multipartBody)
                        .observeOn(AndroidSchedulers.mainThread()).subscribeOn(Schedulers.io()).subscribe(new Observer<ResponseBody>() {
                            @Override
                            public void onSubscribe(Disposable d) {
                                mDisposable.add(d);
                            }

                            @Override
                            public void onNext(ResponseBody responseBody) {
                                try {
                                    String value = responseBody.string();
                                    JSONObject jsonObject = JSONObject.parseObject(value);
                                    String code = jsonObject.getString("code");
                                    if ("200".equals(code)){
                                        ToastUtil.showToast("反馈成功!");
                                        etPros.setText("");
                                        etName.setText("");
                                    }
                                    L.e(value);
                                } catch (IOException e) {
                                    e.printStackTrace();
                                }
                                hideProcessDialog();
                            }

                            @Override
                            public void onError(Throwable e) {
                                e.printStackTrace();
                                hideProcessDialog();
                            }

                            @Override
                            public void onComplete() {

                            }
                        });

            }
        });
    }
}
