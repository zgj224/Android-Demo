#include <binder/IServiceManager.h>
#include <binder/IBinder.h>
#include <binder/Parcel.h>
#include <binder/ProcessState.h>
#include <binder/IPCThreadState.h>
using namespace android;
#ifdef LOG_TAG
#undef LOG_TAG
#endif

#define LOG_TAG "sampleService"
#define SAMPLE_SERIVCE_DES "sample.hello"
#define FUNC_CALLFUNCTION 1

class SampleService: public BBinder {
public:
  SampleService() {
    mydescriptor = String16(SAMPLE_SERIVCE_DES);
  }
     
  virtual ~SampleService() {
  }

  virtual const String16& getInterfaceDescriptor() const {
    return mydescriptor;
  }
     
protected:     
  void callFunction() {
    ALOGE( "Service callFunction-----------");
  }
     
  virtual status_t onTransact(uint32_t code, const Parcel& data,
			      Parcel* reply, uint32_t flags = 0) {
    ALOGD( "Service onTransact, code = %d" , code);
    switch (code) {
    case FUNC_CALLFUNCTION:
      callFunction();
      break;
    default:
      return BBinder::onTransact(code, data, reply, flags);
    }
    return 0;
  }

private:
  String16 mydescriptor;
};

int main() {
  sp < IServiceManager > sm = defaultServiceManager();
  SampleService* samServ = new SampleService();
  status_t ret = sm->addService(String16(SAMPLE_SERIVCE_DES), samServ);
  ALOGD("Service main addservice ");
  ProcessState::self()->startThreadPool();
  IPCThreadState::self()->joinThreadPool( true);
  return 0;
}
