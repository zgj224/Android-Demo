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
#define SAMPLE_CB_SERIVCE_DES "android.os.SampleCallback"
#define SRV_CODE 1
#define CB_CODE 1

class SampleService: public BBinder {
public:
  SampleService() {
    ALOGE("Server ------------------------------ %d",__LINE__);
    mydescriptor = String16(SAMPLE_SERIVCE_DES);
  }

  virtual ~SampleService() {
  }

  virtual const String16& getInterfaceDescriptor() const {
    return mydescriptor;
  }

protected:

  void callFunction(int val) {
    ALOGE("Server ------------------------------ %d",__LINE__);
    ALOGI( "Service: %s(), %d, val = %d",__FUNCTION__,__LINE__,val);
  }

  virtual status_t onTransact(uint32_t code, const Parcel& data, Parcel* reply, uint32_t flags = 0) {
    ALOGD( "Service onTransact,line = %d, code = %d",__LINE__, code);
    switch (code) {
    case SRV_CODE:
      //读取Client传过来的IBinder对象
      callback = data.readStrongBinder();

      if(callback != NULL)
       {
         Parcel _data, _reply;
	 //size_t start = _data.dataPosition();
	 //_data.writeInterfaceToken(String16(SAMPLE_CB_SERIVCE_DES));

	 //3.int32类型;写入数据通过Parcel传递给client端,用readInt32()读出来即可.
	 _data.writeInt32(7777777);
	 _data.writeInt32(8888888);
	 _data.writeInt32(9999999);

	 //2.String8类型
	 _data.writeString8(String8("Hello..."));
	 _data.writeString8(String8("are..."));
	 _data.writeString8(String8("you..."));

	 //_data.setDataPosition(start);
	 //回调客户端
         int ret = callback->transact(CB_CODE, _data, &_reply, 0);
       }
      //调用server端的
      callFunction(5555);
      break;
    default:
      return BBinder::onTransact(code, data, reply, flags);
    }
    return 0;
  }

private:
  String16 mydescriptor;
  sp<IBinder> callback;
};

int main() {
  sp<IServiceManager> sm = defaultServiceManager();
  SampleService* samServ = new SampleService();
  status_t ret = sm->addService(String16(SAMPLE_SERIVCE_DES), samServ);

  ALOGD("Service addservice");
  ProcessState::self()->startThreadPool();
  IPCThreadState::self()->joinThreadPool( true);
  //while(1);
  return 0;
}
