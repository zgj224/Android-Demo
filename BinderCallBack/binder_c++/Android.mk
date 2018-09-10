LOCAL_PATH:= $(call my-dir)
include $(CLEAR_VARS)

LOCAL_SRC_FILES:= client.cpp
LOCAL_SHARED_LIBRARIES := \
    libcutils \
    libbinder \
    libutils \
    libhardware
LOCAL_MODULE:= client_binder_C++

include $(BUILD_EXECUTABLE)

include $(CLEAR_VARS)
LOCAL_SRC_FILES:=server.cpp

LOCAL_SHARED_LIBRARIES := libcutils libbinder libutils libhardware

LOCAL_MODULE:= server_binder_C++
include $(BUILD_EXECUTABLE)
