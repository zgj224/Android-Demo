#include "IMyService.h"

int main() {
  sp < IServiceManager > sm = defaultServiceManager(); //获取service manager引用
  sm->addService(String16("service.myservice"), new BnMyService()); //获取service manager引用
  ProcessState::self()->startThreadPool(); //启动线程池
  IPCThreadState::self()->joinThreadPool(); //把主线程加入线程池
  return 0;
}
