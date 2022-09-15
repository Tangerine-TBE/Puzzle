#include <jni.h>
#include <string>
#include "utils.cpp"
#include "android/log.h"

#define LOGE(...) __android_log_print(ANDROID_LOG_ERROR,__VA_ARGS__ ,__VA_ARGS__) // 定义LOGE类型
using namespace std;





extern "C"
JNIEXPORT void JNICALL
Java_com_weilai_jigsawpuzzle_weight_template_TemplateEditView_imageEdgeCutting(JNIEnv *env,
                                                                            jobject thiz,
                                                                            jobject bitmap_src,
                                                                            jint type) {
    //源图像
    Mat src, src_copy;
    //将Bitmap转换为Mat
    BitmapToMat(env, bitmap_src, src, JNI_FALSE);
    //复制
    src.copyTo(src_copy);
    //转换为灰度图
    cvtColor(src_copy, src_copy, COLOR_BGR2GRAY);
    //图像二值化
    threshold(src_copy, src_copy, 120, 250, THRESH_BINARY);
    //轮廓发现
    vector<vector<Point>> contours;
    vector<Vec4i> hierarchy;
    //检测外围轮廓
    findContours(src_copy, contours, hierarchy, CV_RETR_LIST, CV_CHAIN_APPROX_NONE,
                 Point(0, 0));//轮廓检测

    //找出最大轮廓
    jclass activityClazz = env->GetObjectClass(thiz);
    jmethodID activityMethod = env->GetMethodID(activityClazz, "setListBitmapSize",
                                                "(Lcom/weilai/jigsawpuzzle/weight/template/BitMapInfo;)V");
    jclass beanClazz = env->FindClass("com/weilai/jigsawpuzzle/weight/template/BitMapInfo");
    jmethodID beanMethod = env->GetMethodID(beanClazz, "<init>",
                                            "(IIII)V");
    vector<Point> vector;
    for (int i = 0; i < contours.size() - 1; i++) {
        //并不是所有的轮廓都可以进入这里
        //先进行轮廓筛选
        //1.轮廓等于本身的不要 2.轮廓太小?(后台告诉我有多少个镂空位子，比如有两个，那么就进行面积判断，取最大的两个)
        for (int j = 0; j < contours.size() - 1 - i; j++) {
            Rect rectJ1 = boundingRect(contours[j + 1]);
            Rect rectJ = boundingRect(contours[j]);
            if (rectJ.width * rectJ.height > rectJ1.width * rectJ1.width) {
                vector = contours[j];
                contours[j] = contours[j + 1];
                contours[j + 1] = vector;
            }
        }
    }
    const int size = 2;//后台返回的图片里带多少个位子的镂空
    for (int k = 0; k <contours.size(); k++) {
        if(k<contours.size()-1&& k >=contours.size()-1-size){
            //准确获取到了轮廓的位置
            Rect  rect = boundingRect(contours[k]);
            jobject bitMapBean = env->NewObject(beanClazz, beanMethod,rect.x,rect.y, rect.width, rect.height );
            //將坐標傳給java
            env->CallVoidMethod(thiz, activityMethod, bitMapBean);
            LOGE("轮廓", "宽：%d,高：%d,位置：%d,%d", rect.width, rect.height, rect.x, rect.y);
        }
    }
    //释放Mat
    src.release();
    src_copy.release();
}

