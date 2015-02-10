LOCAL_PATH:= $(call my-dir)

include $(CLEAR_VARS)
LOCAL_MODULE_TAGS := eng
LOCAL_SRC_FILES := $(call all-java-files-under, src)
LOCAL_AAPT_FLAGS += -c mdpi

LOCAL_PACKAGE_NAME := DeviceTest
LOCAL_CERTIFICATE := platform

LOCAL_PROGUARD_ENABLED := disabled

include $(BUILD_PACKAGE)
