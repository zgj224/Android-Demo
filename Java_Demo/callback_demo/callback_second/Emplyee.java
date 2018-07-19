public class Emplyee {
    //声明回调接口
    private CallBackInterface backInterface;
	
    //注册回调接口
    public void setBackInterface(CallBackInterface backInterface) {
	this.backInterface = backInterface;
    }
	
    public void doSometh(String ssid, int count) {
	//开始做事情
	backInterface.excute(ssid, count);
	System.out.println("doSometh(): " + ssid + " count: " + count+"");
    }
}
