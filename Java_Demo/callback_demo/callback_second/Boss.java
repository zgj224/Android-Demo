public class Boss implements CallBackInterface{
    @Override
    public void excute(String ssid, int count) {
	System.out.println("excute(): " + ssid + " count: " + count+"");
    }
}
