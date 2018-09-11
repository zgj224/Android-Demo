#include <binder/IServiceManager.h>
#include <binder/IBinder.h>
#include <binder/Parcel.h>
#include <binder/ProcessState.h>
#include <binder/IPCThreadState.h>
#include <private/binder/binder_module.h>
#include <binder/IInterface.h>
#include <binder/Parcel.h>
#include <binder/Binder.h>

using namespace android;
#ifdef LOG_TAG
#undef LOG_TAG
#endif

#define LOG_TAG "sampleCallback"
#define SAMPLE_SERIVCE_DES "sample.hello"
#define SAMPLE_CB_SERIVCE_DES "android.os.SampleCallback"
#define SRV_CODE 1
#define CB_CODE 1
class SampeCallback : public BBinder
{
public:
  SampeCallback()
  {
    ALOGE("Client ------------------------------ %d",__LINE__);
    mydescriptor = String16(SAMPLE_CB_SERIVCE_DES);
  }
  virtual ~SampeCallback() {
  }
  virtual const String16& getInterfaceDescriptor() const{
    return mydescriptor;
  }
protected:

  void callbackFunction(int val) {
    ALOGE("Client -----------回调Client成功------------------- %d",__LINE__);
    ALOGI( "Client: %s(), %d, val = %d",__FUNCTION__,__LINE__,val);
  }

  virtual status_t onTransact( uint32_t code, const Parcel& data,Parcel* reply,uint32_t flags = 0){
    ALOGD( "Client onTransact, line = %d, code = %d",__LINE__,code);
    int val_1,val_2,val_3;
    String8 str_1,str_2,str_3;
    switch (code){
    case CB_CODE:
      //1.读取int32类型数据
      val_1 = data.readInt32();
      val_2 = data.readInt32();
      val_3 = data.readInt32();
      ALOGE("Client ------------------------------ %d, read int32 = %d",__LINE__,val_1);
      ALOGE("Client ------------------------------ %d, read int32 = %d",__LINE__,val_2);
      ALOGE("Client ------------------------------ %d, read int32 = %d",__LINE__,val_3);


      //2.读取String8类型字符串;str_1.string()-->String8转换char类型数组
      // str_1 = data.readString8();
      // str_2 = data.readString8();
      // str_3 = data.readString8();
      // ALOGE("Client ------------------------------ %d, read String = %s",__LINE__,str_1.string());
      // ALOGE("Client ------------------------------ %d, read String = %s",__LINE__,str_2.string());
      // ALOGE("Client ------------------------------ %d, read String = %s",__LINE__,str_3.string());

      callbackFunction(1234567);
      break;

    default:
      return BBinder::onTransact(code, data, reply, flags);
    }
    return 0;
  }
private:
  String16 mydescriptor;
};

int main()
{
  sp<IServiceManager> sm = defaultServiceManager();
  sp<IBinder> ibinder = sm->getService(String16(SAMPLE_SERIVCE_DES));
  if (ibinder == NULL){
    ALOGW( "Client can't find Service" );
           return -1;
     }
     Parcel _data,_reply;
     SampeCallback *callback = new SampeCallback();
     //注册回调函数
     _data.writeStrongBinder(sp<IBinder>(callback));
     int ret = ibinder->transact(SRV_CODE, _data, &_reply, 0);

//   ProcessState::self()->startThreadPool();
//   IPCThreadState::self()->joinThreadPool();
     while(1);
     return 0;
}
