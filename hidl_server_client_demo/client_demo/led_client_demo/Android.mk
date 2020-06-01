LOCAL_PATH := $(call my-dir)
include $(CLEAR_VARS)

LOCAL_SRC_FILES := led_client.cpp
LOCAL_MODULE := led_client_demo
LOCAL_MODULE_TAGS := optinal
LOCAL_SHARED_LIBRARIES := liblog libutils libhardware libhidlbase libhidltransport android.hardware.led@1.0
include $(BUILD_EXECUTABLE)
