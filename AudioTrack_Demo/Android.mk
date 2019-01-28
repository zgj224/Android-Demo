LOCAL_PATH:= $(call my-dir)

include $(CLEAR_VARS)

#/system/app
#LOCAL_MODULE_TAGS := optional
#/data/app
LOCAL_MODULE_TAGS := tests
LOCAL_SRC_FILES := $(call all-java-files-under, src)
LOCAL_PACKAGE_NAME := audiotrack_demo
#LOCAL_CERTIFICATE := platform

include $(BUILD_PACKAGE)
