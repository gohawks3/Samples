using Predixion;
using Predixion.Analytics.Interfaces;
using Predixion.MLSMClient.Interfaces;
using System;
using System.Collections.Generic;
using System.Linq;
using System.Text;

namespace MLSMSample
{
    class SingletonOnlineQuery
    {
        const string CSVFILENAME = "CollegePlans.csv";
        static String OUTPUTFILENAME = "Output_SingletonOnline.csv";


        string _userName;
        string _password;
        String _serverName;
        bool _loginWithPXCredentials;

        public SingletonOnlineQuery(string serverName, string userName, string password, bool loginWithPXCredentials)
        {
            _serverName = serverName;
            _userName = userName;
            _password = password;
            _loginWithPXCredentials = loginWithPXCredentials;
        }

        public void Run()
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
            IPXClient client = _loginWithPXCredentials ? Client.LoginWithPXCredentials(_serverName, _userName, _password) :
                                                      Client.Login(_serverName, _userName, _password);


            // //////////////////////////////////////////////////////////////////////////////////////////
            // Execute the query and get the output as reader
            // //////////////////////////////////////////////////////////////////////////////////////////
            IPXDataReader outputData = Client.ExecuteDirectQuery(client, jsonMacro);

            // //////////////////////////////////////////////////////////////////////////////////////////
            // Output Options (Either print to console or write to a csv file (only use one at a time because the reader cannot be reset)
            // //////////////////////////////////////////////////////////////////////////////////////////
            OutputHelper.WriteOutputReaderToConsole(outputData);
            //or
            //OutputHelper.WriteOutputReaderToFile(OUTPUTFILENAME , outputData, ',');
        }

    }
}
