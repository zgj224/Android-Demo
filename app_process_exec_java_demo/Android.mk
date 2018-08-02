LOCAL_PATH:= $(call my-dir)
include $(CLEAR_VARS)
LOCAL_SRC_FILES := $(call all-subdir-java-files)
LOCAL_MODULE := HelloWorld
#LOCAL_MODULE_TAGS := optional
LOCAL_MODULE_TAGS := tests
LOCAL_DEX_PREOPT := false
include $(BUILD_JAVA_LIBRARY)

# 方式一
# app_process执行java命令,这个HellWorld.jar其实里面已经被编译成classes.dex文件,用于在android上执行
# push HelloWorld.jar /sdcard
# adb shell CLASSPATH=/sdcard/HelloWorld.jar exec app_process /sdcard HelloWorld "$@"
# 或者执行省去exec也可以
# adb shell CLASSPATH=/sdcard/HelloWorld.jar app_process /sdcard HelloWorld "$@"


#方式二
# 把编译生成的HelloWorld可执行文件
# push HelloWorld /system/bin
# push HelloWorld.jar /sdcard
# adb shell HelloWorld
 
#编译生成HelloWorld可执行文件，执行一些环境变量和命令
# include $(CLEAR_VARS)
# LOCAL_MODULE := HelloWorld
# LOCAL_SRC_FILES := HelloWorld
# LOCAL_MODULE_CLASS := EXECUTABLES
# LOCAL_MODULE_TAGS := optional
# LOCAL_DEX_PREOPT := false
# include $(BUILD_PREBUILT) 

