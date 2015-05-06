using System;
using System.Collections.Generic;
using System.Linq;
using System.Text;
using System.Threading.Tasks;

namespace MLSMSample
{
    class Program
    {
        static void Main(string[] args)
        {
            try
            {
                /*
                 1. The sample code below uses the Predixion .Net Libraries. To obtain the libraries:
                        1. Navigate to http://cloud.predixionsoftware.com/Predixion.WCFService/API/resources.aspx
                        2. From the .Net Libraries section, download the Predixion.API.zip (or Predixion.APIFX35.zip for .Net 3.5)
                        3. Unzip the file on your local machine and add reference to the two dlls:
                                a. Predixion.AnalyticsCore.dll
                                b. Predixion.MLSMClient.dll

                 2. Your username and password is required for running the following tests
                       1. DownloadMLSM
                       2. SingletonOnlineQuery
                       3. OnlineExecution
                */

                String serverName = "cloud.predixionsoftware.com"; //you can replace this for your local predixion server as well
                String userName = "";//enter you cloud username(or if using a local predixion server , either a predixion user or a windows account(domain/username))
                String password = "";//raw password
                bool loginWithPXCredentials = true; // If false , predixion will login as a windows user, otherwise as a predixion user        



                AggregationSingleTable aggSingleTable = new AggregationSingleTable();
                AggregationMultiTable aggMultiTable = new AggregationMultiTable();
                DownloadMLSM downloadMLSM = new DownloadMLSM(serverName, userName, password, loginWithPXCredentials);
                OfflineScoring offlineScoring = new OfflineScoring();
                SingletonOnlineQuery singletonOnline = new SingletonOnlineQuery(serverName, userName, password, loginWithPXCredentials);
                OnlineExecution online = new OnlineExecution(serverName, userName, password, loginWithPXCredentials);



                Console.WriteLine("------------------Running Test AggregationSingleTable--------------------------------");
                aggSingleTable.Run();

                Console.WriteLine("------------------Running Test AggregationMultiTable--------------------------------");
                aggMultiTable.Run();

                Console.WriteLine("------------------Running Test DownloadMLSM--------------------------------");
                downloadMLSM.Run();

                Console.WriteLine("------------------Running Test OfflineScoring--------------------------------");
                offlineScoring.Run();

                Console.WriteLine("------------------Running Test SingletonOnlineQuery--------------------------------");
                singletonOnline.Run();

                Console.WriteLine("------------------Running Test OnlineExecution--------------------------------");
                online.Run();
            }
            catch (Exception ex)
            {
                Console.WriteLine(ex.Message);
                throw ex;
            }
        }
    }
}
