#include "IMyService.h"
#define LOG_TAG "binder_native"
namespace android
{
//IMPLEMENT_META_INTERFACE(MyService, "android.demo.IMyService"); //利用binder引用调用远程sayHello()方法
IMPLEMENT_META_INTERFACE(MyService, "IMyService123"); //利用binder引用调用远程sayHello()方法;第二个参数：IMyService123可以任意写
  //客户端初始化
  BpMyService::BpMyService(const sp<IBinder>& impl) : BpInterface<IMyService>(impl) {
    ALOGD("xxx-----> %s(), line = %d",__FUNCTION__,__LINE__);
  }
	
  //实现客户端sayHello方法
  void BpMyService::sayHello(int &p) {
    ALOGD("xxx-----> %s(), line = %d",__FUNCTION__,__LINE__);
    Parcel data, reply;
    data.writeInterfaceToken(IMyService::getInterfaceDescriptor());
    remote()->transact(HELLO, data, &reply);
    //通过reply.readInt32()读到rdata返回给p
    p = reply.readInt32();
    ALOGD("xxx-----> %s(), line = %d, readInt32 = %d",__FUNCTION__,__LINE__,reply.readInt32());
  }
	
  //服务端，接收远程消息，处理onTransact方法
  status_t BnMyService::onTransact(uint_t code, const Parcel& data,
				   Parcel* reply, uint32_t flags) {
    switch (code) {
    case HELLO: {    //收到HELLO命令的处理流程
      ALOGD("xxx-----> %s(), line = %d",__FUNCTION__,__LINE__);
      CHECK_INTERFACE(IMyService, data, reply);
      ALOGD("xxx-----> %s(), line = %d",__FUNCTION__,__LINE__);

      //调用BN服务端实现的sayHello方法
      int rdata = 0;
      //调用sayHello()实现,然后通过reply->writeInt32(rdata)读到的将rdata返回给客户端
      sayHello(rdata);
      ALOGD("xxx-----> %s(), line = %d, rdata = %d",__FUNCTION__,__LINE__,rdata);
      reply->writeInt32(rdata);
      ALOGD("xxx-----> %s(), line = %d",__FUNCTION__,__LINE__);
      return NO_ERROR;
    }
      break;
    default:
      break;
    }
    return NO_ERROR;
  }
	
  // 实现服务端sayHello方法
  void BnMyService::sayHello(int &rdata) {
    rdata = 2018;
    ALOGD("xxx-----> %s(), line = %d, rdata = %d",__FUNCTION__,__LINE__,rdata);
  };
}
