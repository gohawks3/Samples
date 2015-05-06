import org.predixion.restclient.core.Client;
import org.predixion.restclient.core.ScriptExecution;
import org.predixion.restclient.helpers.IPXClient;

import Interfaces.IPXDataReader;
import Streams.CSVFileHelper;


public class OnlineExecution
{
	static String OUTPUTFILENAME = "Output_Online.csv";
	static String CSVFILENAME = "..\\MLSMFiles\\CollegePlans.csv";
	String _userName;
	String _password;
	String _serverName;
	boolean _loginWithPXCredentials;


    public OnlineExecution(String serverName, String userName, String password,boolean loginWithPXCredentials)
    {
    	_serverName = serverName;
        _userName = userName;
        _password = password;
        _loginWithPXCredentials=loginWithPXCredentials;

    }

    
	public boolean Run() throws Exception
	{
		// //////////////////////////////////////////////////////////////////////////////////////////
        // JSON Macro specific to the use case
        // //////////////////////////////////////////////////////////////////////////////////////////
        String jsonMacro = 
        		"{ \"Query\": "
	        		+ "{ \"PackageName\": \"Classify CollegePlans\", "
	        		+ "\"WorkspaceName\": \"Sample packages\", "
	        		+ "\"SourceType\": \"\", "
	        		+ "\"InputData\": \"CaseTable\", "
	        		+ "\"QueryFlags\": \"None\", "
	        		+ "\"OutputColumns\": "
	        			+ "{ \"VMTableContent\": "
	        				+ "[ { \"Classify CollegePlans_LogReg - Probability of CollegePlans\": \"PredictProbability([Classify CollegePlans_LogReg].[CollegePlans])\" }, "
	        				+ "{ \"Classify CollegePlans_LogReg - Predicted CollegePlans\": \"Predict([Classify CollegePlans_LogReg].[CollegePlans])\" } ] }, "
					+ "\"PackageColumnToInputColumnMapping\": "
						+ "{ \"VMTableContent\": "
							+ "[ { \"[Gender]\": \"[Gender]\" }, "
							+ "{ \"[ParentIncome]\": \"[ParentIncome]\" }, "
							+ "{ \"[IQ]\": \"[IQ]\" }, "
							+ "{ \"[ParentEncouragement]\": \"[ParentEncouragement]\" }, "
							+ "{ \"[CollegePlans]\": \"[CollegePlans]\" } "
				+ "] } }}";
	
		// //////////////////////////////////////////////////////////////////////////////////////////
		// Login to the Predixion Server
		// //////////////////////////////////////////////////////////////////////////////////////////
        IPXClient client = _loginWithPXCredentials ? Client.LoginWithPXCredentials(_serverName, _userName, _password):
			 										 Client.Login(_serverName, _userName, _password);
		
		// //////////////////////////////////////////////////////////////////////////////////////////
		// Create a reader from the input csv data
		// //////////////////////////////////////////////////////////////////////////////////////////
		String inputDataFile = CSVFILENAME;
		
		//List of the column names in the input data file
		String[] headers = new String [] { 
				"Gender",
				"ParentIncome",
				"IQ",
				"ParentEncouragement",
				"CollegePlans"
				
		};

		//List of the data types in the input data file
		Class<?>[] columnTypes = new Class<?> [] { 
				String.class,
				double.class,
				long.class,
				String.class,
				String.class
				
		};
		
		char csvSeparator = ','; // set your input data separator
		boolean fileHasHeader = true; //set to false, if the input file does not contain header
		
		//Create a reader from the input CSV file
		IPXDataReader reader = CSVFileHelper.CreateDataReaderFromCSVFile(
				inputDataFile, 
				fileHasHeader, 
				headers, 
				columnTypes, 
				csvSeparator);
		
		// //////////////////////////////////////////////////////////////////////////////////////////
		// Execute the query and get the output as reader
		// //////////////////////////////////////////////////////////////////////////////////////////
		IPXDataReader outputData = ScriptExecution.ExecuteQuery(client, jsonMacro, reader);


		// Output Options (Either print to console or write to a csv file (use on or the other because the reader cannot be reset)
		// //////////////////////////////////////////////////////////////////////////////////////////
		
		//OutputHelper.WriteOutputReaderToConsole(outputData);
		//or
		OutputHelper.WriteOutputReaderToFile(OUTPUTFILENAME , outputData , ',');
		
		return true;
	}
}
