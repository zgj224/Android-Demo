LOCAL_PATH:= $(call my-dir)

include $(CLEAR_VARS)
LOCAL_MODULE_TAGS := tests
# LOCAL_MODULE_TAGS := optional
# LOCAL_CERTIFICATE := platform
LOCAL_PACKAGE_NAME := getversion
LOCAL_PROGUARD_ENABLED := disabled
LOCAL_DEX_PREOPT := false

LOCAL_SRC_FILES := $(call all-java-files-under, java)
include $(BUILD_PACKAGE)

