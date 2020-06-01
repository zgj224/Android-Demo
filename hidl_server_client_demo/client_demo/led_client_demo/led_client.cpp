#define LOG_TAG "led_client"
#include <android/hardware/led/1.0/ILed.h>
#include <log/log.h>

using android::hardware::led::V1_0::ILed;
using android::hardware::hidl_vec;
using android::hardware::hidl_string;
using android::sp;


int main(){
  sp<ILed> service = ILed::getService();
  if( service == nullptr ){
    ALOGE("Can't find ILed service...");
    return -1;
  }

  ALOGE("led---------->%s(), line = %d",__FUNCTION__,__LINE__);
  service->on();
  ALOGE("led---------->%s(), line = %d",__FUNCTION__,__LINE__);

  
  service->helloWorld("HIDL!!!!",[&](hidl_string result){
    ALOGE("led---------->%s(), line = %d, result = %s",__FUNCTION__,__LINE__,result.c_str());
  });
  service->on();
  
  return 0;
}
