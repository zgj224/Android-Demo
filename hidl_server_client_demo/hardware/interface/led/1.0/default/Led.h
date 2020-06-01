#ifndef ANDROID_HARDWARE_LED_V1_0_LED_H
#define ANDROID_HARDWARE_LED_V1_0_LED_H

#include <android/hardware/led/1.0/ILed.h>
#include <hidl/MQDescriptor.h>
#include <hidl/Status.h>

namespace android {
namespace hardware {
namespace led {
namespace V1_0 {
namespace implementation {

using ::android::hardware::hidl_array;
using ::android::hardware::hidl_memory;
using ::android::hardware::hidl_string;
using ::android::hardware::hidl_vec;
using ::android::hardware::Return;
using ::android::hardware::Void;
using ::android::sp;

struct Led : public ILed {
    // Methods from ILed follow.
    Return<void> on() override;
    Return<void> helloWorld(const hidl_string& name, helloWorld_cb _hidl_cb) override;

    // Methods from ::android::hidl::base::V1_0::IBase follow.

};

// FIXME: most likely delete, this is only for passthrough implementations
// extern "C" ILed* HIDL_FETCH_ILed(const char* name);

}  // namespace implementation
}  // namespace V1_0
}  // namespace led
}  // namespace hardware
}  // namespace android

#endif  // ANDROID_HARDWARE_LED_V1_0_LED_H
