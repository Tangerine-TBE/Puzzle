package com.weilai.jigsawpuzzle.bean

    data class VipTxBean(
    val name:String,
    val vipUser:String,
    val ptUser:String
)

data class IdMakeBean(
    val idName:String,
    val idPxTip:String,
    val idMmTip:String,
    var isSelect:Boolean=false
)