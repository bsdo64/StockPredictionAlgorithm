import java.io.*;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map.Entry;

class DATA{
	public ArrayList<String> date_data = new ArrayList<String>();
	public ArrayList<String> time_data = new ArrayList<String>();
	public ArrayList<String> volume_data = new ArrayList<String>();
	public ArrayList<String> price_data = new ArrayList<String>();
	public ArrayList<String> updown_data = new ArrayList<String>();

	DATA(	ArrayList<String> date,
			ArrayList<String> time,
			ArrayList<String> volume,
			ArrayList<String> price,
			ArrayList<String> updown){

		date_data = date;
		time_data = time;
		volume_data = volume;
		price_data = price;
		updown_data = updown;
	}
	DATA() {};
}

class AvDATA{
	public ArrayList<String> date_data = new ArrayList<String>();
	public ArrayList<String> time_data = new ArrayList<String>();
	public ArrayList<Float> ave_price5 = new ArrayList<Float>();
	public ArrayList<Integer> ave_volume5 = new ArrayList<Integer>();
	public ArrayList<Float> ave_price10 = new ArrayList<Float>();
	public ArrayList<Integer> ave_volume10 = new ArrayList<Integer>();
	public ArrayList<Float> ave_price20 = new ArrayList<Float>();
	public ArrayList<Integer> ave_volume20 = new ArrayList<Integer>();
	public ArrayList<Float> ave_price60 = new ArrayList<Float>();
	public ArrayList<Integer> ave_volume60 = new ArrayList<Integer>();
	public ArrayList<Float> ave_price120 = new ArrayList<Float>();
	public ArrayList<Integer> ave_volume120 = new ArrayList<Integer>();
}

class TradeInfo{
	public String MaxValue;
	public String MinValue;
	public String MaxVolume;
	public String MinVolume;
	public String MaxValueTime;
	public String MinValueTime;
	public String MaxVolumeTime;
	public String MinVolumeTime;

	TradeInfo(){
		MaxValue = "0";
		MinValue = "0";
		MaxVolume = "0";
		MinVolume = "0";
		MaxValueTime = "0";
		MinValueTime = "0";
		MaxVolumeTime = "0";
		MinVolumeTime = "0";
	}

	public void putInfo(String tick[]){
		//tick[] = code, day, time, price, volume, updown
		int[] tick_int = {Integer.parseInt(tick[0]), Integer.parseInt(tick[1]), Integer.parseInt(tick[2]), Integer.parseInt(tick[3]), Integer.parseInt(tick[4]), Integer.parseInt(tick[5])};
		if(tick_int[3]>Integer.parseInt(this.MaxValue)){
			MaxValue = tick[3];
			MaxValueTime = tick[2];
		}
		if(tick_int[4]>Integer.parseInt(this.MaxVolume)){
			MaxVolume = tick[4];
			MaxVolumeTime = tick[2];
		}
		if(tick_int[3]<Integer.parseInt(this.MinValue)){
			MinValue = tick[3];
			MinValueTime = tick[2];
		}
		if(tick_int[4]<Integer.parseInt(this.MinVolume)){
			MinVolume = tick[4];
			MinVolumeTime = tick[2];
		}
	}
}

public class Manage_Data {
	private String signal="";
	private String[] TICK_DATA;

	public HashMap<String, DATA> All_Data;
	public HashMap<String, AvDATA> All_AvData;
	public HashMap<String, TradeInfo> All_Info;
	public Account MyAccount;

	public Manage_Data(String[] TICK_, HashMap<String, DATA> Info, HashMap<String, AvDATA> Ave_Info,HashMap<String, TradeInfo> Trd_Info, Account My){
		TICK_DATA = TICK_;
		All_Data = Info; 
		All_AvData = Ave_Info;
		All_Info = Trd_Info;
		MyAccount = My;
		Boolean check = Check_Num(TICK_DATA);
		Input_Data(check);
	}

	private Boolean Check_Num(String[] tICK_DATA2) {
		// TODO Auto-generated method stub
		if(All_Data.get(tICK_DATA2[0])!=null){
			return true;
		} else
			return false;
	}

	public void Input_Data(Boolean chk){
		DATA d;
		TradeInfo ti = All_Info.get(this.TICK_DATA[0]);
		if(chk){
			d = All_Data.get(this.TICK_DATA[0]);
			d.date_data.add(this.TICK_DATA[1]);
			d.time_data.add(this.TICK_DATA[2]);
			d.volume_data.add(this.TICK_DATA[4]);
			d.price_data.add(this.TICK_DATA[3]);
			d.updown_data.add(this.TICK_DATA[5]);
			
		} else {
			d = new DATA();
			d.date_data.add(this.TICK_DATA[1]);
			d.time_data.add(this.TICK_DATA[2]);
			d.volume_data.add(this.TICK_DATA[4]);
			d.price_data.add(this.TICK_DATA[3]);
			d.updown_data.add(this.TICK_DATA[5]);
			All_Data.put(this.TICK_DATA[0], d);

		}

		if(ti!=null){
			ti = All_Info.get(this.TICK_DATA[0]);
			ti.putInfo(this.TICK_DATA);
		} else{

			ti = new TradeInfo();
			ti.putInfo(this.TICK_DATA);
			All_Info.put(this.TICK_DATA[0], ti);
		}
	}
	public void Cal_Aver() {

		DATA d = All_Data.get(TICK_DATA[0]);
		AvDATA ad = All_AvData.get(TICK_DATA[0]);

		int endIndex = d.price_data.size();

		if(ad==null) ad = new AvDATA();

		ad.date_data.add(TICK_DATA[1]);
		ad.time_data.add(TICK_DATA[2]);

		//5이평
		if(endIndex > 5){
			float ave_price = 0.0f;
			int ave_volume = 0;

			for(int i=0; i<5; i++){
				ave_price += Float.valueOf(d.price_data.get(endIndex-i-1));
				ave_volume += Integer.valueOf(d.volume_data.get(endIndex-i-1));	
			}
			ave_price /= 5.0f;
			ave_volume /= 5;

			ad.ave_price5.add(ave_price); 
			ad.ave_volume5.add(ave_volume);

		}else {
			ad.ave_price5.add(null);
			ad.ave_volume5.add(null);
		}
		//10이평
		if(endIndex > 10) {
			float ave_price = 0.0f;
			int ave_volume = 0;

			for(int i=0; i<10; i++){
				ave_price += Float.valueOf(d.price_data.get(endIndex-i-1));
				ave_volume += Integer.valueOf(d.volume_data.get(endIndex-i-1));	
			}
			ave_price /= 10.0f;
			ave_volume /= 10;

			ad.ave_price10.add(ave_price);
			ad.ave_volume10.add(ave_volume);

		}else {
			ad.ave_price10.add(null);
			ad.ave_volume10.add(null);
		}
		//20이평
		if(endIndex > 20) {
			float ave_price = 0.0f;
			int ave_volume = 0;

			for(int i=0; i<20; i++){
				ave_price += Float.valueOf(d.price_data.get(endIndex-i-1));
				ave_volume += Integer.valueOf(d.volume_data.get(endIndex-i-1));	
			}
			ave_price /= 20.0f;
			ave_volume /= 20;

			ad.ave_price20.add(ave_price);
			ad.ave_volume20.add(ave_volume);

		}else {
			ad.ave_price20.add(null);
			ad.ave_volume20.add(null);
		}
		//60이평
		if(endIndex > 60) {
			float ave_price = 0.0f;
			int ave_volume = 0;

			for(int i=0; i<60; i++){
				ave_price += Float.valueOf(d.price_data.get(endIndex-i-1));
				ave_volume += Integer.valueOf(d.volume_data.get(endIndex-i-1));	
			}
			ave_price /= 60.0f;
			ave_volume /= 60;

			ad.ave_price60.add(ave_price);
			ad.ave_volume60.add(ave_volume);

		}else {
			ad.ave_price60.add(null);
			ad.ave_volume60.add(null);
		}
		//120이평
		if(endIndex > 120) {
			float ave_price = 0.0f;
			int ave_volume = 0;

			for(int i=0; i<120; i++){
				ave_price += Float.valueOf(d.price_data.get(endIndex-i-1));
				ave_volume += Integer.valueOf(d.volume_data.get(endIndex-i-1));	
			}
			ave_price /= 120.0f;
			ave_volume /= 120;

			ad.ave_price120.add(ave_price);
			ad.ave_volume120.add(ave_volume);

		}else {
			ad.ave_price120.add(null);
			ad.ave_volume120.add(null);
		}
		All_AvData.put(TICK_DATA[0], ad);
	}

	public void Analize(){
		AvDATA ad = All_AvData.get(TICK_DATA[0]);
		TradeInfo ti = All_Info.get(TICK_DATA[0]);
		MyStock ms = MyAccount.stock.get(TICK_DATA[0]);
		DATA data = All_Data.get(TICK_DATA[0]);

		int index = ad.date_data.size()-1;
		if(ad.ave_price120.size()>123){

			float a = ad.ave_price5.get(index);
			float b = ad.ave_price10.get(index);
			float c = ad.ave_price20.get(index);
			float d = ad.ave_price60.get(index);
			float e = ad.ave_price120.get(index);

			float av = ad.ave_volume5.get(index);
			float bv = ad.ave_volume10.get(index);
			float cv = ad.ave_volume20.get(index);
			float dv = ad.ave_volume60.get(index);
			float ev = ad.ave_volume120.get(index);


			float a_1 = ad.ave_price5.get(index-1);
			float b_1 = ad.ave_price10.get(index-1);
			float c_1 = ad.ave_price20.get(index-1);
			float d_1 = ad.ave_price60.get(index-1);
			float e_1 = ad.ave_price120.get(index-1);

			float av_1 = ad.ave_volume5.get(index-1);
			float bv_1 = ad.ave_volume10.get(index-1);
			float cv_1 = ad.ave_volume20.get(index-1);
			float dv_1 = ad.ave_volume60.get(index-1);
			float ev_1 = ad.ave_volume120.get(index-1);
			
			float a_2 = ad.ave_price5.get(index-2);
			float b_2 = ad.ave_price10.get(index-2);
			float c_2 = ad.ave_price20.get(index-2);
			float d_2 = ad.ave_price60.get(index-2);
			float e_2 = ad.ave_price120.get(index-2);

			float av_2 = ad.ave_volume5.get(index-2);
			float bv_2 = ad.ave_volume10.get(index-2);
			float cv_2 = ad.ave_volume20.get(index-2);
			float dv_2 = ad.ave_volume60.get(index-2);
			float ev_2 = ad.ave_volume120.get(index-2);
			
			float c_3 = ad.ave_price20.get(index-3);
			
			int Ta;
			int Tb;
			int Tc;
			int Td;
			int Te;
			int Tf;
			int Tg;
			int Th;
			
			Ta = Integer.parseInt(ti.MaxValue);
			Tb = Integer.parseInt(ti.MinValue);
			Tc = Integer.parseInt(ti.MaxValueTime);
			Td = Integer.parseInt(ti.MinValueTime);
			Te = Integer.parseInt(ti.MaxVolume);
			Tf = Integer.parseInt(ti.MaxVolumeTime);
			Tg = Integer.parseInt(ti.MinVolume);
			Th = Integer.parseInt(ti.MinVolumeTime);
		
			float day0 = Float.parseFloat(data.price_data.get(index));
			float day1 = Float.parseFloat(data.price_data.get(index-1));
			float day2 = Float.parseFloat(data.price_data.get(index-2));
			float day3 = Float.parseFloat(data.price_data.get(index-3));
			float day4 = Float.parseFloat(data.price_data.get(index-4));
			
			//이평선 알고리즘
			if(e_2>e_1&& e_1>e){
				if(a>b && b>c && c>d){
					if(a_1>b_1 && b_1>c_1 && c_1>d_1){
						if(a_2>b_2 && b_2>c_2 && c_2>d_2){
							float gap = (day0-day1)/day1;
							if(e>a && gap> 0.003 ){

								this.signal = "B";
							}
						}
					}
				}
			}
			//if(a>b && b>c && c>d && d>e) this.signal = "B";

			//if(a<b || b<c || c<d || d<e) this.signal = "S";

			//거래량 알고리즘
			
//			if(145500< Integer.parseInt(TICK_DATA[2]) &&Integer.parseInt(TICK_DATA[2])<150000)
//				this.signal="S";
//
//			//팔기
			if(ms!=null ){
//				if(c_3>c_2 && c_2>c_1 && c_1>c && a<c && e>c 
//						&& a_1>a && b_1>b && Integer.parseInt(TICK_DATA[4])<10000)
//					this.signal = "S";
				
				
				//손절매
				float Sell_signal = (float)(Ta - ms.value)/ms.value;
				if(Sell_signal > 0.1){
					this.signal = "S";
				}
//
//				//최대 수익 매매 한계
//				float Sell_Limit = (ms.value - ms.buyvalue)/ms.buyvalue;
//				if(Sell_Limit>0.2){
//					this.signal = "S";
//				}
//				
				//Max와 Value가 많이 손해 
				float Sell_gap = (ms.buyvalue-ms.value)/ms.buyvalue;
				if(Sell_gap>0.20){
					this.signal = "S";
				}
				if(ms.value > ms.buyvalue*1.15){
					this.signal = "S";
				}
				
			}
		}else {
			this.signal = null;
		}
		if(ad.ave_price120.size()>131) {
			for(int i=0; i<9; i++){
				this.All_AvData.get(TICK_DATA[0]).ave_price5.remove(i);
				this.All_AvData.get(TICK_DATA[0]).ave_price10.remove(i);
				this.All_AvData.get(TICK_DATA[0]).ave_price20.remove(i);
				this.All_AvData.get(TICK_DATA[0]).ave_price60.remove(i);
				this.All_AvData.get(TICK_DATA[0]).ave_price120.remove(i);
				this.All_AvData.get(TICK_DATA[0]).ave_volume5.remove(i);
				this.All_AvData.get(TICK_DATA[0]).ave_volume10.remove(i);
				this.All_AvData.get(TICK_DATA[0]).ave_volume20.remove(i);
				this.All_AvData.get(TICK_DATA[0]).ave_volume60.remove(i);
				this.All_AvData.get(TICK_DATA[0]).ave_volume120.remove(i);
				this.All_AvData.get(TICK_DATA[0]).date_data.remove(i);
				this.All_AvData.get(TICK_DATA[0]).time_data.remove(i);

				this.All_Data.get(TICK_DATA[0]).date_data.remove(i);
				this.All_Data.get(TICK_DATA[0]).price_data.remove(i);
				this.All_Data.get(TICK_DATA[0]).time_data.remove(i);
				this.All_Data.get(TICK_DATA[0]).updown_data.remove(i);
				this.All_Data.get(TICK_DATA[0]).volume_data.remove(i);

			}

		} 


	}

	public String getSignal(){
		return this.signal;
	}


}
