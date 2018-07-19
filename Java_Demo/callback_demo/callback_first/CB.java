public class CB{

  public interface CallBack {
    public void oncall();
  }

  public static class A {
    private CallBack callback;
    //注册一个事件
    public void register(CallBack callback){
	System.out.println("this.callback = " + this.callback);
	System.out.println("callback = " + callback);
      this.callback = callback;
    }

    // 需要调用的时候回调
    public void call(){
      callback.oncall();
    }
  }

  public static void main(String[] args){
      
    A mCallback = new A();
    mCallback.register(new CallBack() {
	@Override
	public void oncall() {//重写CallBack接口的oncall()方法
	  System.out.println("回调函数被调用");
	}
      });

    mCallback.call();
  }
}
