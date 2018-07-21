#include <jni.h>
#include <android/log.h>

#define TAG "GJ_JNI"
#define NELEM(x) ((int) (sizeof(x) / sizeof((x)[0])))
#define LOGV(...) __android_log_print(ANDROID_LOG_VERBOSE, TAG, __VA_ARGS__)
#define LOGI(...) __android_log_print(ANDROID_LOG_INFO, TAG, __VA_ARGS__)
#define LOGD(...) __android_log_print(ANDROID_LOG_DEBUG, TAG, __VA_ARGS__)
#define LOGE(...) __android_log_print(ANDROID_LOG_ERROR, TAG, __VA_ARGS__)

static const char *classPathName =  "com/example/gjz/ndk_demo/MainActivity";

static jstring stringFromJNI(JNIEnv *env, jclass clazz)
{
    char str[] = "Helllo C JNI  ";
    jstring restr = (*env)->NewStringUTF(env, str);
    LOGI("%s(), line = %d, ttt = %s", __func__, __LINE__,str);

    return restr;
}

static JNINativeMethod gMethods[] = {
        { "stringFromJNI", "()Ljava/lang/String;", (void*)stringFromJNI },
};

static int registerNatives(JNIEnv* env)
{
    jclass clazz;

    clazz = (*env)->FindClass(env, classPathName);
    if (clazz == NULL) {
        LOGI("%s(), line = %d, FindClass() Failed!!!", __func__, __LINE__);
        return JNI_FALSE;
    }
    if ((*env)->RegisterNatives(env, clazz, gMethods, sizeof(gMethods) / sizeof(gMethods[0])) < 0) {
        LOGI("%s(), line = %d, RegisterNatives() Failed!!!", __func__, __LINE__);
        return JNI_FALSE;
    }

    return JNI_TRUE;
}


JNIEXPORT jint JNICALL JNI_OnLoad(JavaVM* vm, void* reserved)
{
    JNIEnv* env = NULL;

    if ((*vm)->GetEnv(vm, (void**)&env, JNI_VERSION_1_4) != JNI_OK) {
        LOGI("%s(), line = %d, GetEnv() Failed!!!", __func__, __LINE__);
        return -1;
    }

    if (!registerNatives(env)) {
        LOGE("%s(), line = %d, registerNatives() 注册失败",__func__,__LINE__);
        return -1;
    }

    return JNI_VERSION_1_4;
}
