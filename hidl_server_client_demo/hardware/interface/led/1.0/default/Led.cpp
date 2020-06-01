#include "Led.h"
#include <log/log.h>
#define LOG_TAG "led_server"

namespace android {
namespace hardware {
namespace led {
namespace V1_0 {
namespace implementation {

// Methods from ILed follow.
Return<void> Led::on() {
  ALOGE("led---------->%s(), line = %d",__FUNCTION__,__LINE__);
  return Void();
}

Return<void> Led::helloWorld(const hidl_string& name, helloWorld_cb _hidl_cb) {
  char buf[100];
  memset(buf, 0x00, 100);
  snprintf(buf, 100, "Hello World, %s", name.c_str());
  ALOGE("led---------->%s(), line = %d, buf = %s",__FUNCTION__,__LINE__,buf);
  hidl_string result(buf);
  _hidl_cb(result);
  return Void();
}


// Methods from ::android::hidl::base::V1_0::IBase follow.

//ILed* HIDL_FETCH_ILed(const char* /* name */) {
//    return new Led();
//}

}  // namespace implementation
}  // namespace V1_0
}  // namespace led
}  // namespace hardware
}  // namespace android
