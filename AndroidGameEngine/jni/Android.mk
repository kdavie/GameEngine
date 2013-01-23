LOCAL_PATH:= $(call my-dir)

include $(CLEAR_VARS)

LOCAL_MODULE    := Utils
LOCAL_CFLAGS    := -Werror
LOCAL_SRC_FILES := Utils.cpp
LOCAL_LDLIBS    := -llog 

include $(BUILD_SHARED_LIBRARY)