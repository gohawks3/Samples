

using Predixion;
using Predixion.Analytics.Interfaces;
using Predixion.MLSMClient.Helpers;
using Predixion.MLSMClient.Interfaces;
using System;

namespace MLSMSample
{
    class OnlineExecution
    {
        const string OUTPUTFILENAME = "Output_Online.csv";
        const string CSVFILENAME = "CollegePlans.csv";

        string _userName;
        string _password;
        String _serverName;
        bool _loginWithPXCredentials;


        public OnlineExecution(string serverName, string userName, string password, bool loginWithPXCredentials)
        {
            _serverName = serverName;
            _userName = userName;
            _password = password;
            _loginWithPXCredentials = loginWithPXCredentials;
        }

        public bool Run()
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
            IPXClient client = _loginWithPXCredentials ? Client.LoginWithPXCredentials(_serverName, _userName, _password) :
                                                     Client.Login(_serverName, _userName, _password);
		

            // //////////////////////////////////////////////////////////////////////////////////////////
            // Create a reader from the input csv data
            // //////////////////////////////////////////////////////////////////////////////////////////
            String inputDataFile = CSVFILENAME;

            //List of the column names in the input data file
            String[] headers = new String[] { 
				"Gender",
				"ParentIncome",
				"IQ",
				"ParentEncouragement",
				"CollegePlans"
				
		};

            //List of the data types in the input data file
            Type[] columnTypes = new Type[] { 
				typeof(string),
				typeof(double),
				typeof(long),
				typeof(string),
				typeof(string),
				
		};

            char csvSeparator = ','; // set your input data separator
            bool fileHasHeader = true; //set to false, if the input file does not contain header


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
            IPXDataReader outputData = Client.ExecuteQuery(client, jsonMacro, reader); ;


            // //////////////////////////////////////////////////////////////////////////////////////////
            // Output Options (Either print to console or write to a csv file (only use one at a time because the reader cannot be reset)
            // //////////////////////////////////////////////////////////////////////////////////////////
            //OutputHelper.WriteOutputReaderToConsole(outputData);
            //or
            OutputHelper.WriteOutputReaderToFile(OUTPUTFILENAME, outputData, ',');

            return true;
        }

    }
}
