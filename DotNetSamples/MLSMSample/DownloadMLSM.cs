using Predixion;
using Predixion.MLSMClient.Interfaces;
using System;
using System.Collections.Generic;
using System.Linq;
using System.Text;

namespace MLSMSample
{
    class DownloadMLSM
    {
        const string MLSMFILENAME = "DownloadMLSM.XML";

        string _userName;
        string _password;
        String _serverName;
        bool _loginWithPXCredentials;

        public DownloadMLSM(string serverName, string userName, string password, bool loginWithPXCredentials)
        {
            _serverName = serverName;
            _userName = userName;
            _password = password;
            _loginWithPXCredentials = loginWithPXCredentials;
        }

        public void Run()
        {
            ////////////////////////////////////////////////////////////////////////////////////////////
            // Download the MLSM from Predixion Server 
            ////////////////////////////////////////////////////////////////////////////////////////////
            String mlsmjson = "{ \"Query\": { \"PackageName\": \"Classify CollegePlans\", \"WorkspaceName\": \"Sample Packages\", \"SourceType\": \"Predixion Package\", \"InputData\": \"Classify CollegePlans\", \"QueryFlags\": \"SerializeMLSM\", \"OutputColumns\": { \"VMTableContent\": [ { \"Classify CollegePlans_LogReg - Probability of CollegePlans\": \"PredictProbability([Classify CollegePlans_LogReg].[CollegePlans])\" }, { \"Classify CollegePlans_LogReg - Predicted CollegePlans\": \"Predict([Classify CollegePlans_LogReg].[CollegePlans])\" } ] }, \"PackageColumnToInputColumnMapping\": { \"VMTableContent\": [ { \"Gender\": \"Gender\" }, { \"ParentIncome\": \"ParentIncome\" }, { \"IQ\": \"IQ\" }, { \"ParentEncouragement\": \"ParentEncouragement\" }, { \"CollegePlans\": \"CollegePlans\" } ] } }}";

            IPXClient client = _loginWithPXCredentials ? Client.LoginWithPXCredentials(_serverName, _userName, _password) :
                                                     Client.Login(_serverName, _userName, _password);

            //If using integrated , use this method
            //IPXClient client = Predixion.Client.Login(serverName);


            //Get package info
            DateTime dtLastModfied = DateTime.MinValue;
            Dictionary<string, object> packageInfo = Client.GetPackageInfo(client, "Sample%20Packages", "Classify%20CollegePlans");
            if (packageInfo.ContainsKey("timeOfLastAddition"))
            {
                try
                {
                    dtLastModfied = Convert.ToDateTime(Convert.ToString(packageInfo["timeOfLastAddition"]));
                }
                catch (Exception)
                {
                    dtLastModfied = DateTime.MinValue;
                }
            }
            Console.WriteLine("Last modified date: {0}", dtLastModfied);

            //Download the mlsm
            String mlsm = Predixion.Client.DownloadMLSM(client, mlsmjson);
            if (System.IO.File.Exists(MLSMFILENAME))
                System.IO.File.Delete(MLSMFILENAME);
            System.IO.File.WriteAllText(MLSMFILENAME, mlsm);
            Console.WriteLine("MLSM written to file {0}", MLSMFILENAME);

        }
    }
}
