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
                String serverName = "cloud.predixionsoftware.com";
                String userName = "enter you cloud username";
                String password = "raw password";
                bool loginWithPXCredentials = true;

                /*
	                The sample code below uses the Predixion .Net Libraries. To obtain the libraries:
				        1. Navigate to http://cloud.predixionsoftware.com/Predixion.WCFService/API/resources.aspx
		                2. From the .Net Libraries section, download the Predixion.API.zip (or Predixion.APIFX35.zip for .Net 3.5)
		                3. Unzip the file on your local machine and add reference to the two dlls:
				                a. Predixion.AnalyticsCore.dll
				                b. Predixion.MLSMClient.dll
                */

                AggregationSingleTable test1 = new AggregationSingleTable();
                AggregationMultiTable test2 = new AggregationMultiTable();
                DownloadMLSM test3 = new DownloadMLSM(serverName, userName, password, loginWithPXCredentials);
                OfflineScoring test4 = new OfflineScoring();
                SingletonOnlineQuery test5 = new SingletonOnlineQuery(serverName, userName, password, loginWithPXCredentials);
                OnlineExecution test6 = new OnlineExecution(serverName, userName, password, loginWithPXCredentials);



                Console.WriteLine("------------------Running Test1--------------------------------");
                test1.Run();

                Console.WriteLine("------------------Running Test2--------------------------------");
                test2.Run();

                Console.WriteLine("------------------Running Test3--------------------------------");
                test3.Run();

                Console.WriteLine("------------------Running Test4--------------------------------");
                test4.Run();

                Console.WriteLine("------------------Running Test5--------------------------------");
                test5.Run();

                Console.WriteLine("------------------Running Test6--------------------------------");
                test6.Run();
            }
            catch (Exception ex)
            {
                Console.WriteLine(ex.Message);
                throw ex;
            }
        }
    }
}
