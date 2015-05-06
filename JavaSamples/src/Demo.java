

public class Demo
{

	
	public static void main(String[] args) throws Exception
	{
		try
		{
			/*
			The sample code below uses the Predixion Java Libraries. To obtain the libraries:
				1. Navigate to http://cloud.predixionsoftware.com/Predixion.WCFService/API/resources.aspx
				2. From the Java Libraries section, download the Predixion.MLSMClient.zip
				3. Unzip the file on your local machine and add reference to the jar Predixion.MLSMClient.jar
			 */
			
             String serverName = "cloud.predixionsoftware.com";
			 String userName = "enter you cloud username";
             String password = "raw password";
             boolean loginWithPXCredentials = true; // If false ,predixion will login in with windows credentials             

             AggregationSingleTable test1 = new AggregationSingleTable();
             AggregationMultiTable test2 = new AggregationMultiTable();
             DownloadMLSM test3 = new DownloadMLSM(serverName , userName, password , loginWithPXCredentials);
             OfflineScoring test4 = new OfflineScoring();
             SingletonOnlineQuery test5 = new SingletonOnlineQuery(serverName , userName, password, loginWithPXCredentials);
             OnlineExecution test6 = new OnlineExecution(serverName , userName, password, loginWithPXCredentials);

             System.out.println("------------------Running Test1--------------------------------");
             test1.Run();

             System.out.println("------------------Running Test2--------------------------------");
             test2.Run();
             
             System.out.println("------------------Running Test3--------------------------------");
             test3.Run();

             System.out.println("------------------Running Test4--------------------------------");
             test4.Run();
             
             System.out.println("------------------Running Test5--------------------------------");
             test5.Run();

             System.out.println("------------------Running Test6--------------------------------");
             test6.Run();
		}
		catch (Exception ex)
		{
			System.out.println(ex.getMessage());
			ex.printStackTrace();
		}

	}

}
