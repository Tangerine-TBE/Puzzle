package com.weilai.jigsawpuzzle.bean

import jp.co.cyberagent.android.gpuimage.filter.GPUImageFilter

data class FilterBean(val img: String,val filterType: GPUImageFilter,val filterName: String,val needVip: Boolean)
