#include <binder/IServiceManager.h>
#include <binder/IBinder.h>
#include <binder/Parcel.h>
#include <binder/ProcessState.h>
#include <binder/IPCThreadState.h>
#include <private/binder/binder_module.h>
using namespace android;
#ifdef LOG_TAG
#undef LOG_TAG
#endif
#define LOG_TAG "sampleCallback"
#define SAMPLE_SERIVCE_DES "sample.hello"
#define FUNC_CALLFUNCTION 1

int main()
{
  sp<IServiceManager> sm = defaultServiceManager();
  sp<IBinder> ibinder = sm->getService(String16(SAMPLE_SERIVCE_DES));
  if (ibinder == NULL){
    ALOGW( "Client can't find Service" );
    return -1;
  }
  Parcel _data,_reply;
  int ret = ibinder->transact(FUNC_CALLFUNCTION, _data, &_reply, 0);
  ALOGD( "Client transact return %d", ret);
  ProcessState::self()->startThreadPool();
  IPCThreadState::self()->joinThreadPool();
  return 0;
}
