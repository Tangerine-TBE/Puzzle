package com.weilai.jigsawpuzzle.util;

import com.kwad.sdk.api.KsCustomController;

/**
 * 信息获取控制工具
 */
public class UserDataObtainController extends KsCustomController {

  private boolean userAgree;

  private UserDataObtainController() {
  }

  private static class Holder {
    private static UserDataObtainController sInstance = new UserDataObtainController();
  }

  public static UserDataObtainController getInstance() {
    return Holder.sInstance;
  }

  public UserDataObtainController setUserAgree(boolean userAgree) {
    this.userAgree = userAgree;
    return this;
  }

  @Override
  public boolean canReadLocation() {
    // 为提高广告转化率，取得更好收益，建议媒体在用户同意隐私政策及权限信息后，允许SDK获取地理位置信息。
//    if (!userAgree) {
//      return false;
//    }
//    return super.canReadLocation();
    return false;
  }

  @Override
  public boolean canUsePhoneState() {
    // 为提高广告转化率，取得更好收益，建议媒体在用户同意隐私政策及权限信息后，允许SDK使用手机硬件信息。
    if (!userAgree) {
      return false;
    }
    return super.canUsePhoneState();
  }

  @Override
  public boolean canUseOaid() {
    // 为提高广告转化率，取得更好收益，建议媒体在用户同意隐私政策及权限信息后，允许SDK使用设备oaid。
    if (!userAgree) {
      return false;
    }
    return super.canUseOaid();
  }

  @Override
  public boolean canUseMacAddress() {
    // 为提高广告转化率，取得更好收益，建议媒体在用户同意隐私政策及权限信息后，允许SDK使用设备Mac地址。
    if (!userAgree) {
      return false;
    }
    return super.canUseMacAddress();
  }

  @Override
  public boolean canReadInstalledPackages() {
    // 为提高广告转化率，取得更好收益，建议媒体在用户同意隐私政策及权限信息后，允许SDK读取app安装列表。
//    if (!userAgree) {
//      return false;
//    }
//    return super.canReadInstalledPackages();
    return false;
  }

  @Override
  public boolean canUseStoragePermission() {
    // 为提升SDK的接入体验，广告展示更流畅，建议媒体在用户同意隐私政策及权限信息后，允许SDK使用存储权限。
    if (!userAgree) {
      return false;
    }
    return super.canUseStoragePermission();
  }

  @Override
  public boolean canUseNetworkState() {
    // 为提升SDK的接入体验，广告展示更流畅，建议媒体在用户同意隐私政策及权限信息后，允许SDK读取网络状态信息。
    if (!userAgree) {
      return false;
    }
    return super.canUseNetworkState();
  }
}
