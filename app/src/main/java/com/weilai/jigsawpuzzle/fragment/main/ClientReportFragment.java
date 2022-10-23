package com.weilai.jigsawpuzzle.fragment.main;

import android.text.TextUtils;
import android.view.View;
import android.widget.Toast;

import androidx.appcompat.widget.AppCompatEditText;

import com.weilai.jigsawpuzzle.R;
import com.weilai.jigsawpuzzle.base.BaseFragment;

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
                pop();
            }
        });
        view.findViewById(R.id.tv_save).setOnClickListener(new View.OnClickListener() {
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
                pop();

            }
        });
    }
}
