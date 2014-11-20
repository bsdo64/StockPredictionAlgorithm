import java.io.*;

public class Main {

	/**
	 * @param args
	 * @throws IOException 
	 * @throws InterruptedException 
	 */
	public static void main(String[] args) throws IOException, InterruptedException {
		// TODO Auto-generated method stub
		  Manage_FileInOut oMFIO = new Manage_FileInOut("\\Alg_CLIENT_DATA\\BasicInformation.txt");
		  
		  Manage_Connection oMC = new Manage_Connection();
		  oMC.Open_Connection(oMFIO.IPaddressSERVER(), oMFIO.PortNumberSERVER());
		  
		  oMC.Send_and_Receive(oMFIO.Frequency());
		  
		  oMC.Wait();
	}

}
