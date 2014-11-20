import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.net.InetSocketAddress;
import java.net.Socket;
import java.net.SocketAddress;
import java.net.UnknownHostException;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map.Entry;

class MyStock{
	float buyvalue = 0;
	int count = 0;
	float value = 0;
	float all_value = 0;	//���� �ֽ��� ��ġ
	MyStock(){
		
	}
}

class Account{
	float money_origin = 10000000; //�ʱⰪ
	float cost = 0.33f;
	float stock_value = 0.0f;	//�� �ֽ��� ��ġ
	
	float money = 10000000;
	
	HashMap<String, MyStock> stock =  new HashMap<String, MyStock>();
	
	int date = 0;
	public Account (){
		
	}
	public void change_value(String[] tick){
		MyStock ms = this.stock.get(tick[0]);
		if(ms!=null){
			ms.value = Float.parseFloat(tick[3]);
			ms.all_value = ms.value * ms.count;
			Iterator<Entry<String, MyStock>> it = this.stock.entrySet().iterator();
			float v = 0;
			while(it.hasNext()){
				v += it.next().getValue().all_value;
			}
			this.stock_value = v;
			if(this.date!= Integer.parseInt(tick[1])){
				date = Integer.parseInt(tick[1]);
				print_money(tick);
			}
		}
	}
	public void print_money(String[] tick){
		System.out.println("��¥ : "+tick[1]+"\t�ڻ� : " + (this.money + this.stock_value));
	}
	public void print_tran(String[] tick, String sig){
		if(this.stock.size()>0){
			Iterator<Entry<String, MyStock>> it = this.stock.entrySet().iterator();
			MyStock ms = this.stock.get(tick[0]);
			float v = 0;
			float av = ms.all_value;
			
			while(it.hasNext()){
				v += it.next().getValue().all_value;
			}
			
			if(sig=="S"){
				this.stock_value = v-av;
			} else if(sig=="B"){
				this.stock_value = v;
			}
			System.out.println("��¥ : "+tick[1]+"\t�ð� : "+tick[2]);
			System.out.print("���� : "+tick[0]+"\t���� : "+ms.value+"*"+ms.count+ "\t�ŷ� : "+sig+"\t���� : "+this.money+"\t�ְ� : "+this.stock_value+"\t���  : "+(int)(this.money +this.stock_value)+"\t�ŷ��� : " + tick[4]);
		} else {
			System.out.println(money);
		}
		
	}
	
}
public class Manage_Connection {
	private static final String ENTER = System.getProperty("line.separator");
	
	private BufferedReader	READ_Stream;
	private BufferedWriter	WRITE_Stream;

	private SocketAddress	SERVER_Address;
	private Socket	SERVER_Socket;
	

	public Manage_Connection() {

	}

	public void Open_Connection(String SERVER_IP_Address, int SERVER_PortNumber) throws UnknownHostException, IOException{

		this.SERVER_Address	=	new InetSocketAddress(SERVER_IP_Address, SERVER_PortNumber);
		this.SERVER_Socket	=	new Socket(SERVER_IP_Address, SERVER_PortNumber);

		//this.SERVER_Socket.connect(this.SERVER_Address);

		InputStreamReader isr = new InputStreamReader(this.SERVER_Socket.getInputStream());
		OutputStreamWriter osr = new OutputStreamWriter(this.SERVER_Socket.getOutputStream());

		this.WRITE_Stream	= new BufferedWriter(osr);
		this.READ_Stream	= new BufferedReader(isr);

		System.out.println("connected");
	}

	public void Send_and_Receive(int Freq) throws IOException, InterruptedException {
		// TODO Auto-generated method stub
		int cnt = 0;
		String Msg = "TICK"+ENTER;
		
		HashMap<String, DATA> INFO = new HashMap<String, DATA>();
		HashMap<String, AvDATA> Av_INFO = new HashMap<String, AvDATA>();
		HashMap<String, TradeInfo> Trade_INFO = new HashMap<String, TradeInfo>();

		Account MyMoney = new Account();
		
		while(Msg.equals("END"+ENTER) == false ){
			if(this.SERVER_Socket.isConnected()){

				
				WRITE_Stream.write("TICK"+ENTER);
				WRITE_Stream.flush();

				Msg = READ_Stream.readLine();

				//�м� ����
				
				//1. ���� TICK �����ֱ�
				//System.out.println(Msg);
				
				/*2. TICK �м��ؼ� �ڸ���
				 * 	TICK_[0] = CODE
				 *  TICK_[1] = DATE
				 *  TICK_[2] = TIME
				 *  TICK_[3] = PRICE
				 *  TICK_[4] = VOLUME
				 *  TICK_[5] = UP/DOWN
				 */
				String[] TICK_ = Msg.split("/");
				if(TICK_[1]==null){
					break;
				}
				
				
				//4. ���¿� ���� ���� ����
				MyMoney.change_value(TICK_);
				
				
				/*3. �� �ڵ� ���� �����͸� ����
				//HashMap  <�ڵ�, <Date,Time,price,volume,up/down>>
				//ArrayList 
					Date
					Time
					price
					volume
					up/down
				 */	
				Manage_Data oMD = new Manage_Data(TICK_, INFO, Av_INFO, Trade_INFO, MyMoney);
				oMD.Cal_Aver();
				//������ ����ȭ�ϱ�
				
				INFO = oMD.All_Data;
				Av_INFO = oMD.All_AvData;
				Trade_INFO = oMD.All_Info;
				//����ȭ ������ �޾ƿ���
				
				oMD.Analize();
				String sig = oMD.getSignal(); 
				//�˰��� �м�
				
				
				if(sig=="B"){
					if(MyMoney.stock.get(TICK_[0])==null){
						String scount = "1000";
						int icount = 1000;
						
						if(Integer.parseInt(TICK_[3])<1000){
							scount = "5000";
							icount = 5000;
						}

						float all_val = icount*Float.parseFloat(TICK_[3]);
						float one_val = Float.parseFloat(TICK_[3]);
						if(all_val<MyMoney.money){

							String buy = TICK_[0]+"//"+sig+"//"+scount;
							WRITE_Stream.write(buy+ENTER);
							WRITE_Stream.flush();

							float c = all_val*(MyMoney.cost/100);
							MyMoney.money -= all_val;
							MyMoney.money -= c;

							MyStock ms = MyMoney.stock.get(TICK_[0]);

							if(ms!=null){
								ms.buyvalue = one_val;
								ms.count += icount;
								ms.value = one_val;
								ms.all_value = ms.count * one_val;
								MyMoney.stock.put(TICK_[0], ms);
								MyMoney.print_tran(TICK_, sig);
								System.out.println("\t������ : "+c);
							} else if(ms==null){
								ms = new MyStock();
								ms.buyvalue = one_val;
								ms.count += icount;
								ms.value = one_val;
								ms.all_value = ms.count * one_val;
								MyMoney.stock.put(TICK_[0], ms);
								MyMoney.print_tran(TICK_, sig);
								System.out.println("\t������ : "+c);
							}

						}
					}
				} else if(sig=="S"){
					
					if(MyMoney.stock.containsKey(TICK_[0])){
						int check = MyMoney.stock.get(TICK_[0]).count;
						if(check>0){

							MyStock ms = MyMoney.stock.get(TICK_[0]);
							float my = ms.all_value;
							MyMoney.money += my;
							
							String count = Integer.toString(ms.count);
							String sell = TICK_[0]+"//"+sig+"//"+ count;

							WRITE_Stream.write(sell+ENTER);
							WRITE_Stream.flush();
							
							MyMoney.print_tran(TICK_, sig);
							System.out.println();

							MyMoney.stock.remove(TICK_[0]);
							Trade_INFO.remove(TICK_[0]);				
						}
					}else {
						
					}
					
				} else {
					
					
					
				}
				
				Thread.sleep(Freq);
				cnt++;
			}
			else
				System.out.println("fail");
		}
		
		
	}
	public void Close_Connection() throws IOException
	{
		this.SERVER_Socket.close();
	}

	public void Wait() throws IOException
	{
		READ_Stream.readLine();
	}

	public  SocketAddress  Server_Address(){
		return this.SERVER_Address; 
	}

	public  Socket      Server_Socket(){
		return this.SERVER_Socket;
	}

	public void BUY_TEST()
	{ 

	}
}