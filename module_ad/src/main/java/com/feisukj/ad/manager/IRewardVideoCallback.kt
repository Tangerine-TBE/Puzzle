package com.feisukj.ad.manager

interface IRewardVideoCallback {
    fun onAdShow(){}
    fun onAdVideoBarClick(){}
    fun onAdClose(){}
    fun onVideoComplete()
    fun onRewardVerify(isValid:Boolean,rewardCount:Int,rewardName:String?){}
    fun onVideoError(){}
    fun onLoadVideoCached(){}
}