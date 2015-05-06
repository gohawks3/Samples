package com.predixion.sample;


public class Demo
{
	public static void main(String[] args) throws Exception
	{
		try
		{
			/*
			1. The sample code below uses the Predixion Java Libraries. To obtain the libraries:
				1. Navigate to http://cloud.predixionsoftware.com/Predixion.WCFService/API/resources.aspx
				2. From the Java Libraries section, download the Predixion.MLSMClient.zip
				3. Unzip the file on your local machine and add reference to the jar Predixion.MLSMClient.jar
			  
			2. Your username and password is required for running the following tests
               1. DownloadMLSM
               2. SingletonOnlineQuery
               3. OnlineExecution
            */
			
             String serverName = "cloud.predixionsoftware.com"; //you can replace this for your local predixion server as well
			 String userName = "";//enter you cloud username(or if using a local predixion server , either a predixion user or a windows account(domain/username))
             String password = "";//raw password
             boolean loginWithPXCredentials = true; // If false , predixion will login as a windows user, otherwise as a predixion user             

             AggregationSingleTable aggSingleTable = new AggregationSingleTable();
             AggregationMultiTable aggMultiTable = new AggregationMultiTable();
             DownloadMLSM downloadMLSM = new DownloadMLSM(serverName , userName, password , loginWithPXCredentials);
             OfflineScoring offlineScoring = new OfflineScoring();
             SingletonOnlineQuery singletonOnline = new SingletonOnlineQuery(serverName , userName, password, loginWithPXCredentials);
             OnlineExecution online = new OnlineExecution(serverName , userName, password, loginWithPXCredentials);

             System.out.println("------------------Running Test AggregationSingleTable--------------------------------");
             aggSingleTable.Run();

             System.out.println("------------------Running Test AggregationMultiTable--------------------------------");
             aggMultiTable.Run();
             
             System.out.println("------------------Running Test DownloadMLSM--------------------------------");
             downloadMLSM.Run();

             System.out.println("------------------Running Test OfflineScoring--------------------------------");
             offlineScoring.Run();
             
             System.out.println("------------------Running Test SingletonOnlineQuery--------------------------------");
             singletonOnline.Run();

             System.out.println("------------------Running Test OnlineExecution--------------------------------");
             online.Run();
		}
		catch (Exception ex)
		{
			System.out.println(ex.getMessage());
			ex.printStackTrace();
			throw ex;
		}

	}

}
