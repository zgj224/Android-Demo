#include "IMyService.h"
#define LOG_TAG "binder_native"

int main() {
  sp < IServiceManager > sm = defaultServiceManager(); //获取service manager引用
  sp < IBinder > binder = sm->getService(String16("service.myservice"));//获取名为"service.myservice"的binder接口
  sp<IMyService> cs = interface_cast<IMyService> (binder);//将biner对象转换为强引用类型的IMyService
  int rdata = 0;
  ALOGD("xxx-----> %s(), line = %d, rdata = %d",__FUNCTION__,__LINE__,rdata);
  cs->sayHello(rdata);//利用binder引用调用远程sayHello()方法
  ALOGD("xxx-----> %s(), line = %d, rdata = %d",__FUNCTION__,__LINE__,rdata);
  return 0;
}
