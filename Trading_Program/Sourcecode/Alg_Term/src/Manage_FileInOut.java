import java.io.*;

public class Manage_FileInOut {

	String[] Basic_Information;
	
	private String	IPAddress_SERVER;
	private int		PortNum_SERVER;
	private int		Number_Of_Company;
	private int		Freq;
	
	public Manage_FileInOut(String File_Path) throws IOException{
		this.Basic_Information = Read_BasicInformation(File_Path);
		
		this.IPAddress_SERVER  = this.Basic_Information[0];
		
		this.PortNum_SERVER	   = Integer.parseInt(this.Basic_Information[1]);
		
        if( Integer.parseInt(this.Basic_Information[3])  > 0 )
            this.Freq               = (int) ( (float)1000 / Integer.parseInt(this.Basic_Information[2]) );

        else
        {
            this.Freq = 1;
            System.out.println(" 'Repetition Times per sec' in BasicInformation.txt file is inappropriate ");
        }
		
		this.Number_Of_Company = Integer.parseInt(this.Basic_Information[3]);
		
	}

	public String[] Read_BasicInformation(String File_Path) throws IOException {
        String[] BasicInformation = new String[4];
        int cnt = 0;

        File file = new File(File_Path);
        if(file.exists())
        {
            BufferedReader inFile = new BufferedReader(new FileReader(file));
            String sLine = null;
            while( (sLine = inFile.readLine()) != null )
            {
                BasicInformation[cnt] = sLine;
                cnt++;
            }
            
            inFile.close();
        }
        
        return BasicInformation;
	}
	
    public String IPaddressSERVER(){
    	return this.IPAddress_SERVER;
    }

    public int PortNumberSERVER(){
    	return this.PortNum_SERVER; 
    }

    public int NumberOfCompany()
    {
    	return this.Number_Of_Company;
    }

    public int Frequency(){
    	return this.Freq;
    }
}
