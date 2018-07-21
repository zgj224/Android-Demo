#include <jni.h>
#include <android/log.h>

#define TAG "GJ_JNI"
#define NELEM(x) ((int) (sizeof(x) / sizeof((x)[0])))
#define LOGV(...) __android_log_print(ANDROID_LOG_VERBOSE, TAG, __VA_ARGS__)
#define LOGI(...) __android_log_print(ANDROID_LOG_INFO, TAG, __VA_ARGS__)
#define LOGD(...) __android_log_print(ANDROID_LOG_DEBUG, TAG, __VA_ARGS__)
#define LOGE(...) __android_log_print(ANDROID_LOG_ERROR, TAG, __VA_ARGS__)


static const char *classPathName =  "com/example/gjz/ndk_demo/MainActivity";

static jstring stringFromJNI(JNIEnv *env, jobject){
    char str[]="Hello C++ JNI  ";
    jstring hello = env->NewStringUTF(str);
    LOGI("%s(), line = %d, str = %s", __func__, __LINE__, str);
    return hello;
}

static const JNINativeMethod gMethods[] = {
        { "stringFromJNI","()Ljava/lang/String;",(void*)stringFromJNI },
};

static int registerNatives(JNIEnv* engv)
{
    jclass  clazz;
    LOGI("registerNatives begin");
    clazz = engv->FindClass(classPathName);

    if (clazz == NULL) {
        LOGI("clazz is null");
        return JNI_FALSE;
    }

    if (engv->RegisterNatives(clazz, gMethods, NELEM(gMethods)) < 0) {
        LOGI("RegisterNatives error");
        return JNI_FALSE;
    }

    return JNI_TRUE;
}


JNIEXPORT jint JNI_OnLoad(JavaVM* vm, void* reserved)
{
    LOGI("jni_OnLoad begin");
    void* tmpenv = NULL;
    JNIEnv* env = NULL;

    if (vm->GetEnv(&tmpenv, JNI_VERSION_1_4) != JNI_OK) {
        LOGI("ERROR: GetEnv failed\n");
        return -1;
    }

    env = (JNIEnv *)(tmpenv);
    if(env ==  NULL){
        LOGI("%s(), line = %d, env = NULL Failed!!!",__func__, __LINE__);
    }

    registerNatives(env);

    return JNI_VERSION_1_4;
}