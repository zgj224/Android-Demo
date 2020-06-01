#define LOG_TAG "android.hardware.led@1.0-service"

#include <android/hardware/led/1.0/ILed.h>
#include <hidl/LegacySupport.h>
#include "Led.h"
#define LOG_TAG "led_main"

using android::hardware::led::V1_0::ILed;
using android::hardware::led::V1_0::implementation::Led;
using android::hardware::defaultPassthroughServiceImplementation;
using android::hardware::configureRpcThreadpool;
using android::hardware::joinRpcThreadpool;
using android::sp;

int main() {
#if 0 
  // Passthrough   dlopen so方式
  return defaultPassthroughServiceImplementation<ILed>(); 
#else
  ALOGE("led---------->%s(), line = %d",__FUNCTION__,__LINE__);
  // Binder 方式
  sp<ILed> service = new Led();

  configureRpcThreadpool(1, true /*callerWillJoin*/);

  service->registerAsService();
  ALOGE("led---------->%s(), line = %d",__FUNCTION__,__LINE__);
  joinRpcThreadpool();
#endif
}
