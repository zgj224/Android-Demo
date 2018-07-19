public class Client{
    public static void main(String[] args){
	String ssid = "Success";
	int count = 123456;
	
	Emplyee emplyee = new Emplyee();
	emplyee.setBackInterface(new Boss());
	        
	emplyee.doSometh(ssid, count);
	System.out.println("Client(): " + ssid + " count: " + count+"");
    }
}
