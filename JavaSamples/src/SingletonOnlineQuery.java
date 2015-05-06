import org.predixion.restclient.core.Client;
import org.predixion.restclient.core.ScriptExecution;
import org.predixion.restclient.helpers.IPXClient;

import Interfaces.IPXDataReader;

public class SingletonOnlineQuery
{
	 static String CSVFILENAME = "..\\MLSMFiles\\CollegePlans.csv";
	 static String OUTPUTFILENAME = "Output_SingletonOnline.csv";


     String _userName;
     String _password;
     String _serverName;
 	boolean _loginWithPXCredentials;


     public SingletonOnlineQuery(String serverName, String userName, String password,boolean loginWithPXCredentials)
     {
    	 _serverName = serverName;
         _userName = userName;
         _password = password;
         _loginWithPXCredentials=loginWithPXCredentials;

     }
     
	public void Run() throws Exception
	{
		// //////////////////////////////////////////////////////////////////////////////////////////
		// JSON Macro specific to the use case
		// //////////////////////////////////////////////////////////////////////////////////////////
		String jsonMacro = 
				"{ \"Query\": "
					+ "{ \"PackageName\": \"Classify CollegePlans\", "
					+ "\"WorkspaceName\": \"Sample Packages\", "
					+ "\"OutputColumns\": "
						+ "{\"VMTableContent\":"
							+ "[{\"Classify CollegePlans_LogReg - Probability of CollegePlans\": \"PredictProbability([Classify CollegePlans_LogReg].[CollegePlans])\"},"
							+ "{\"Classify CollegePlans_LogReg - Predicted CollegePlans\": \"Predict([Classify CollegePlans_LogReg].[CollegePlans])\"}]}, "
					+ "\"SingletonInput\": "
						+ "{\"Gender\": \"Female\","
						+ " \"ParentIncome\": 40618.56, "
						+ "\"IQ\": 99.53, "
						+ "\"ParentEncouragement\": \"Encouraged\", "
						+ "\"CollegePlans\": \"Does not plan to attend\" } }}";
		
		// //////////////////////////////////////////////////////////////////////////////////////////
		// Login to the Predixion Server
		// //////////////////////////////////////////////////////////////////////////////////////////
		 IPXClient client = _loginWithPXCredentials ? Client.LoginWithPXCredentials(_serverName, _userName, _password):
			 										  Client.Login(_serverName, _userName, _password);

		// //////////////////////////////////////////////////////////////////////////////////////////
		// Execute the query and get the output as reader
		// //////////////////////////////////////////////////////////////////////////////////////////
		IPXDataReader outputData = ScriptExecution.ExecuteDirectQuery(client, jsonMacro);

		// //////////////////////////////////////////////////////////////////////////////////////////
        // Output Options (Either print to console or write to a csv file (only use one at a time because the reader cannot be reset)
		// //////////////////////////////////////////////////////////////////////////////////////////
		OutputHelper.WriteOutputReaderToConsole(outputData);
		//or
		//OutputHelper.WriteOutputReaderToFile(OUTPUTFILENAME , outputData , ',');
	}
}
