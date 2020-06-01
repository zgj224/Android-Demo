LOCAL_PATH:= $(call my-dir)
include $(CLEAR_VARS)

LOCAL_MODULE_TAGS := optional
LOCAL_CERTIFICATE := platform
LOCAL_PACKAGE_NAME := hidl_client_demo
LOCAL_DEX_PREOPT := false

LOCAL_STATIC_JAVA_LIBRARIES := android.hardware.led-V1.0-java
LOCAL_SRC_FILES := $(call all-java-files-under, java)
include $(BUILD_PACKAGE)

