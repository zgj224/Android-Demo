LOCAL_PATH:= $(call my-dir)

include $(CLEAR_VARS)
LOCAL_MODULE_TAGS := optional
LOCAL_CERTIFICATE := platform
#LOCAL_MODULE_TAGS := tests
LOCAL_PACKAGE_NAME := AudioDemo
LOCAL_DEX_PREOPT := false
LOCAL_SDK_VERSION := 21

LOCAL_SRC_FILES := $(call all-java-files-under, java)
include $(BUILD_PACKAGE)

