package com.weilai.jigsawpuzzle.util

import com.abc.matting.tencentcloudapi.bda.v20200324.BdaClient
import com.abc.matting.tencentcloudapi.bda.v20200324.models.*
import com.abc.matting.tencentcloudapi.common.Credential
import com.abc.matting.tencentcloudapi.common.exception.TencentCloudSDKException
import com.abc.matting.tencentcloudapi.common.profile.ClientProfile
import com.abc.matting.tencentcloudapi.common.profile.HttpProfile
import com.abc.matting.tencentcloudapi.ft.v20200304.FtClient
import com.abc.matting.tencentcloudapi.ft.v20200304.models.*
import com.abc.matting.tencentcloudapi.iai.v20200303.IaiClient
import com.abc.matting.tencentcloudapi.iai.v20200303.models.DetectFaceRequest
import com.abc.matting.tencentcloudapi.iai.v20200303.models.DetectFaceResponse
import com.weilai.jigsawpuzzle.Resources
import com.weilai.jigsawpuzzle.application.PuzzleApplication
import okhttp3.*
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import java.io.File


object EffectUtilss {
    /**
     * 变老
     */
    fun toOld(age: Long, base64: String): String {
        return try {
            val cred = Credential(
                Resources.tencentcloudapi_SecretId,
                Resources.tencentcloudapi_SecretKey
            )
            val httpProfile = HttpProfile()
            httpProfile.endpoint = "ft.tencentcloudapi.com"
            val clientProfile = ClientProfile()
            clientProfile.httpProfile = httpProfile
            val client = FtClient(cred, "ap-guangzhou", clientProfile)
            val req = ChangeAgePicRequest()
            req.image = base64
            val ageInfos1 = arrayOfNulls<AgeInfo>(1)
            val ageInfo1 = AgeInfo()
            ageInfo1.age = age
            ageInfos1[0] = ageInfo1
            req.ageInfos = ageInfos1
            req.rspImgType = "url"
            val resp = client.ChangeAgePic(req)
            ChangeAgePicResponse.toJsonString(resp)
        } catch (e: TencentCloudSDKException) {
            val msg = getMsg(e)
            PuzzleApplication.handler.post {
                ToastUtil.showCenterToast(msg)
            }
            ""
        }
    }

    /**
     * 变性
     * */
    fun toSex(type: Long, base64: String):String{
        return try {
            val cred = Credential(
                Resources.tencentcloudapi_SecretId,
                Resources.tencentcloudapi_SecretKey
            )
            val httpProfile = HttpProfile()
            httpProfile.endpoint = "ft.tencentcloudapi.com"
            val clientProfile = ClientProfile()
            clientProfile.httpProfile = httpProfile
            val client = FtClient(cred, "ap-guangzhou", clientProfile)
            val req = SwapGenderPicRequest()
            req.image = base64
            val genderInfos1 = arrayOfNulls<GenderInfo>(1)
            val genderInfo1 = GenderInfo()
            genderInfo1.gender = type
            genderInfos1[0] = genderInfo1
            req.genderInfos = genderInfos1
            req.rspImgType = "url"
            val resp = client.SwapGenderPic(req)
            SwapGenderPicResponse.toJsonString(resp)
        } catch (e: TencentCloudSDKException) {
            val msg = getMsg(e)
            PuzzleApplication.handler.post {
                ToastUtil.showCenterToast(msg)
            }
            ""
        }
    }

    /**
     * 动漫
     * */
    fun toComic(base64: String):String{
        return try {
            val cred = Credential(
                Resources.tencentcloudapi_SecretId,
                Resources.tencentcloudapi_SecretKey
            )
            val httpProfile = HttpProfile()
            httpProfile.endpoint = "ft.tencentcloudapi.com"
            val clientProfile = ClientProfile()
            clientProfile.httpProfile = httpProfile
            val client = FtClient(cred, "ap-guangzhou", clientProfile)
            val req = FaceCartoonPicRequest()
            req.image = base64
            req.disableGlobalEffect = "true"
            req.rspImgType = "url"
            val resp = client.FaceCartoonPic(req)
            FaceCartoonPicResponse.toJsonString(resp)
        } catch (e: TencentCloudSDKException) {
            val msg = getMsg(e)
            PuzzleApplication.handler.post {
                ToastUtil.showCenterToast(msg)
            }
            ""
        }
    }

    /**
     * 分析人脸
     * */
    fun analyzeTheFace(base64: String): Boolean{
        return try {
            // 实例化一个认证对象，入参需要传入腾讯云账户secretId，secretKey,此处还需注意密钥对的保密
            // 密钥可前往https://console.cloud.tencent.com/cam/capi网站进行获取
            val cred = Credential(
                Resources.tencentcloudapi_SecretId,
                Resources.tencentcloudapi_SecretKey
            )
            // 实例化一个http选项，可选的，没有特殊需求可以跳过
            val httpProfile = HttpProfile()
            httpProfile.endpoint = "iai.tencentcloudapi.com"
            // 实例化一个client选项，可选的，没有特殊需求可以跳过
            val clientProfile = ClientProfile()
            clientProfile.httpProfile = httpProfile
            // 实例化要请求产品的client对象,clientProfile是可选的
            val client = IaiClient(cred, "ap-guangzhou", clientProfile)
            // 实例化一个请求对象,每个接口都会对应一个request对象
            val req = DetectFaceRequest()
//            req.maxFaceNum = 3L
            req.image = base64
            req.faceModelVersion = "3.0"
            req.needRotateDetection = 1L
            // 返回的resp是一个DetectFaceResponse的实例，与请求对象对应
            val resp: DetectFaceResponse = client.DetectFace(req)
            // 输出json格式的字符串回包
            System.out.println(DetectFaceResponse.toJsonString(resp))
            true
        } catch (e: TencentCloudSDKException) {
            val msg = getMsg(e)
            PuzzleApplication.handler.post {
                ToastUtil.showCenterToast(msg)
            }
            false
        }
    }

    /**
     * 人像抠图
     * */
    fun portraitCutout(base64: String): String{
        return try {
            // 实例化一个认证对象，入参需要传入腾讯云账户secretId，secretKey,此处还需注意密钥对的保密
            // 密钥可前往https://console.cloud.tencent.com/cam/capi网站进行获取
            val cred = Credential(
                Resources.tencentcloudapi_SecretId,
                Resources.tencentcloudapi_SecretKey
            )
            // 实例化一个http选项，可选的，没有特殊需求可以跳过
            val httpProfile = HttpProfile()
            httpProfile.endpoint = "bda.tencentcloudapi.com"
            // 实例化一个client选项，可选的，没有特殊需求可以跳过
                val clientProfile = ClientProfile()
            clientProfile.httpProfile = httpProfile
            // 实例化要请求产品的client对象,clientProfile是可选的
            val client = BdaClient(cred, "ap-guangzhou", clientProfile)
            // 实例化一个请求对象,每个接口都会对应一个request对象
            val req = SegmentPortraitPicRequest()
            req.image = base64
            // 返回的resp是一个SegmentPortraitPicResponse的实例，与请求对象对应
            val resp: SegmentPortraitPicResponse = client.SegmentPortraitPic(req)
            // 输出json格式的字符串回包
            SegmentPortraitPicResponse.toJsonString(resp)
        } catch (e: TencentCloudSDKException) {
            val msg = getMsg(e)
            PuzzleApplication.handler.post {
                ToastUtil.showCenterToast(msg)
            }
            ""
        }
    }

    /**
     * 智能抠图
     * */
    fun intelligentCutout(path: String): String{
        return try {
            val okHttpClient = OkHttpClient()
            val file = File(path)
            val image: RequestBody = RequestBody.create(
                "application/octet-stream".toMediaTypeOrNull(),
                file
            )
            val requestBody: RequestBody = MultipartBody.Builder()
                .setType(MultipartBody.FORM)
                .addFormDataPart("file", path, image)
                .build()
            val request: Request = Request.Builder()
                .header("Content-Type", "multipart/form-data")
                .header("APIKEY", Resources.pic_up_app_key)
                .url("https://picupapi.tukeli.net/api/v1/matting2?mattingType=6")
                .post(requestBody)
                .build()
            val call = okHttpClient.newCall(request)
            val resp = call.execute()
            resp.body?.string()?:""
        } catch (e: Exception) {
            ""
        }
    }



    private fun getMsg(e: TencentCloudSDKException):String{
        return when(e.errorCode){
            "FailedOperation.ConflictOperation" -> "操作冲突，请勿同时操作相同的Person"
            "FailedOperation.DuplicatedGroupDescription" -> "同一人员库中自定义描述字段不可重复"
            "FailedOperation.GroupInDeletedState" -> "当前组正处于删除状态，请等待"
            "FailedOperation.GroupPersonMapExist" -> "组中已包含对应的人员Id"
            "FailedOperation.GroupPersonMapNotExist" -> "组中不包含对应的人员Id"
            "FailedOperation.ImageFacedetectFailed" -> "人脸检测失败"
            "FailedOperation.RequestLimitExceeded" -> "请求频率超过限制"
            "FailedOperation.SearchFacesExceed" -> "检索人脸个数超过限制"
            "InternalError" -> "内部错误"
            "InvalidParameterValue.AccountFaceNumExceed" -> "账号脸数量超出限制"
            "InvalidParameterValue.DeleteFaceNumExceed" -> "删除人脸数量超出限制。每个人员至少需要包含一张人脸"
            "InvalidParameterValue.FaceModelVersionIllegal" -> "算法模型版本不合法"
            "InvalidParameterValue.GroupExDescriptionsExceed" -> "人员库自定义描述字段数组长度超过限制。最多可以创建5个"
            "InvalidParameterValue.GroupExDescriptionsNameIdentical" -> "人员库自定义描述字段名称不可重复"
            "InvalidParameterValue.GroupExDescriptionsNameIllegal" -> "人员库自定义描述字段名称包含非法字符。人员库自定义描述字段名称只支持中英文、-、_、数字"
            "InvalidParameterValue.GroupExDescriptionsNameTooLong" -> "人员库自定义描述字段名称长度超出限制"
            "InvalidParameterValue.GroupFaceNumExceed" -> "人员库人脸数量超出限制"
            "InvalidParameterValue.GroupIdAlreadyExist" -> "人员库ID已经存在。人员库ID不可重复"
            "InvalidParameterValue.GroupIdIllegal" -> "人员库ID包含非法字符。人员库ID只支持英文、数字、-%@#&_"
            "InvalidParameterValue.GroupIdNotExist" -> "人员库ID不存在"
            "InvalidParameterValue.GroupIdTooLong" -> "人员库ID超出长度限制"
            "InvalidParameterValue.GroupIdsExceed" -> "传入的人员库列表超过限制"
            "InvalidParameterValue.GroupNameAlreadyExist" -> "人员库名称已经存在。人员库名称不可重复"
            "InvalidParameterValue.GroupNameIllegal" -> "人员库名称包含非法字符。人员库名称只支持中英文、-、_、数字"
            "InvalidParameterValue.GroupNameTooLong" -> "人员库名称超出长度限制"
            "InvalidParameterValue.GroupNumExceed" -> "人员库数量超出限制。如需增加，请联系我们"
            "InvalidParameterValue.GroupNumPerPersonExceed" -> "人员库数量超出限制。单个人员最多可被添加至100个人员库"
            "InvalidParameterValue.GroupTagIllegal" -> "人员库备注包含非法字符。人员库备注只支持中英文、-、_、数字"
            "InvalidParameterValue.GroupTagTooLong" -> "人员库备注超出长度限制。"
            "InvalidParameterValue.LimitExceed" -> "返回数量超出限制"
            "InvalidParameterValue.NoFaceInGroups" -> "指定分组中没有人脸"
            "InvalidParameterValue.OffsetExceed" -> "起始序号过大。请检查需要请求的数组长度"
            "InvalidParameterValue.PersonExDescriptionInfosExceed" -> "人员自定义描述字段数组长度超过限制。最多5个。"
            "InvalidParameterValue.PersonExDescriptionsNameIdentical" -> "人员自定义描述字段名称不可重复"
            "InvalidParameterValue.PersonExDescriptionsNameIllegal" -> "人员自定义描述字段名称包含非法字符。人员自定义描述字段名称只支持中英文、-、_、数字"
            "InvalidParameterValue.PersonExDescriptionsNameTooLong" -> "人员自定义描述字段名称长度超出限制"
            "InvalidParameterValue.PersonExistInGroup" -> "组中已包含对应的人员Id"
            "InvalidParameterValue.PersonFaceNumExceed" -> "人员人脸数量超出限制。单个人员最多可以包含五张人脸"
            "InvalidParameterValue.PersonGenderIllegal" -> "人员性别设置出错。0代表未填写，1代表男性，2代表女性"
            "InvalidParameterValue.PersonIdAlreadyExist" -> "人员ID已经存在。人员ID不可重复"
            "InvalidParameterValue.PersonIdIllegal" -> "人员ID包含非法字符。人员ID只支持英文、数字、-%@#&_"
            "InvalidParameterValue.PersonIdNotExist" -> "人员ID不存在"
            "InvalidParameterValue.PersonIdTooLong" -> "人员ID超出长度限制"
            "InvalidParameterValue.PersonNameIllegal" -> "人员名称包含非法字符。人员名称只支持中英文、-、_、数字"
            "InvalidParameterValue.PersonNameTooLong" -> "人员名称超出长度限制"
            "InvalidParameterValue.SearchPersonsExceed" -> "搜索的人员数目超过限制"
            "InvalidParameterValue.UploadFaceNumExceed" -> "一次最多上传四张人脸"
            "InvalidParameterValue.UrlIllegal" -> "URL格式不合法"
            "LimitExceeded.ErrorFaceNumExceed" -> "人脸个数超过限制"
            "MissingParameter.ErrorParameterEmpty" -> "必选参数为空"
            "ResourceUnavailable.NotExist" -> "计费状态未知，请确认是否已在控制台开通服务。"
            "UnsupportedOperation.UnknowMethod" -> "未知方法名"

            "FailedOperation.ImageNotSupported" -> "不支持的图片文件"
            "FailedOperation.ImageResolutionExceed" -> "图片分辨率过大"
            "ImageResolutionInsufficient" -> "图片分辨率过小"
            "FailedOperation.ImageSizeExceed" -> "base64编码后的图片数据过大"
            "FailedOperation.ProfileNumExceed" -> "人像数过多"
            "FailedOperation.SegmentFailed" -> "人像分割失败"
            "FailedOperation.ServerError" -> "算法服务异常，请重试"
            "FailedOperation.UnKnowError" -> "内部错误"
            "InvalidParameter.InvalidParameter" -> "参数不合法"
            "InvalidParameterValue.ImageEmpty" -> "图片为空"

            "FailedOperation.DetectNoFace" -> "未检测到人脸"
            "FailedOperation.FaceExceedBorder" -> "人脸出框，无法使用"
            "FailedOperation.FaceSizeTooSmall" -> "人脸因太小被过滤，建议人脸分辨率不小于34*34"
            "FailedOperation.FreqCtrl" -> "操作太频繁，触发频控，请稍后重试"
            "FailedOperation.ImageDecodeFailed" -> "图片解码失败"
            "FailedOperation.ImageDownloadError" -> "图片下载错误"
            "FailedOperation.ImageResolutionTooSmall" -> "图片短边分辨率太小，小于64"
            "FailedOperation.InnerError" -> "服务内部错误，请重试"
            "FailedOperation.RequestEntityTooLarge" -> "整个请求体太大（通常主要是图片）"
            "FailedOperation.RequestTimeout" -> "后端服务超时"
            "InvalidParameterValue.FaceRectInvalidFirst" -> "第1个人脸框参数不合法"
            "InvalidParameterValue.FaceRectInvalidSecond" -> "第2个人脸框参数不合法"
            "InvalidParameterValue.FaceRectInvalidThrid" -> "第3个人脸框参数不合法"
            "InvalidParameterValue.ImageSizeExceed" -> "图片数据太大"
            "InvalidParameterValue.NoFaceInPhoto" -> "图片中没有人脸"
            "InvalidParameterValue.ParameterValueError" -> "参数不合法"
            "ResourceUnavailable.Delivering" -> "资源正在发货中"
            "ResourceUnavailable.Freeze" -> "帐号已被冻结"
            "ResourceUnavailable.GetAuthInfoError" -> "获取认证信息失败"
            "ResourceUnavailable.InArrears" -> "帐号已欠费"
            "ResourceUnavailable.LowBalance" -> "余额不足"
            "ResourceUnavailable.NotReady" -> "服务未开通"
            "ResourceUnavailable.Recover" -> "资源已被回收"
            "ResourceUnavailable.StopUsing" -> "帐号已停服"
            "ResourceUnavailable.UnknownStatus" -> "计费状态未知"
            "ResourcesSoldOut.ChargeStatusException" -> "帐号已欠费"
            else -> "未知错误"
        }
    }
}