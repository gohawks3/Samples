import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.Date;
import java.util.HashMap;

import org.predixion.restclient.core.Client;
import org.predixion.restclient.core.DownloadMLSMClient;
import org.predixion.restclient.core.Metadata;
import org.predixion.restclient.helpers.IPXClient;


public class DownloadMLSM
{
	static String MLSMFILENAME = "DownloadMLSM.XML";

	String _userName;
	String _password;
	String _serverName;
	boolean _loginWithPXCredentials;

    public DownloadMLSM(String serverName , String userName, String password,boolean loginWithPXCredentials)
    {
    	_serverName= serverName;
        _userName = userName;
        _password = password;   
        _loginWithPXCredentials=loginWithPXCredentials;
    }

    
	public void Run() throws Exception
    {
        ////////////////////////////////////////////////////////////////////////////////////////////
        // Download the MLSM from Predixion Server 
        ////////////////////////////////////////////////////////////////////////////////////////////
        String mlsmjson = "{ \"Query\": { \"PackageName\": \"Classify CollegePlans\", \"WorkspaceName\": \"Sample Packages\", \"SourceType\": \"Predixion Package\", \"InputData\": \"Classify CollegePlans\", \"QueryFlags\": \"SerializeMLSM\", \"OutputColumns\": { \"VMTableContent\": [ { \"Classify CollegePlans_LogReg - Probability of CollegePlans\": \"PredictProbability([Classify CollegePlans_LogReg].[CollegePlans])\" }, { \"Classify CollegePlans_LogReg - Predicted CollegePlans\": \"Predict([Classify CollegePlans_LogReg].[CollegePlans])\" } ] }, \"PackageColumnToInputColumnMapping\": { \"VMTableContent\": [ { \"Gender\": \"Gender\" }, { \"ParentIncome\": \"ParentIncome\" }, { \"IQ\": \"IQ\" }, { \"ParentEncouragement\": \"ParentEncouragement\" }, { \"CollegePlans\": \"CollegePlans\" } ] } }}";;

        IPXClient client = _loginWithPXCredentials ? Client.LoginWithPXCredentials(_serverName, _userName, _password):
    												 Client.Login(_serverName, _userName, _password);

        

        //Get package info
        Object dtLastModfied = new Date();
        HashMap<String, Object> packageInfo = Metadata.GetPackageInfo(client, "Sample%20Packages", "Classify%20CollegePlans");
        if (packageInfo.containsKey("timeOfLastAddition"))
        {
        	dtLastModfied = packageInfo.get("timeOfLastAddition");
        }
        System.out.println(String.format("Last modified date: %s", dtLastModfied));

        //Download the mlsm
        String mlsm = DownloadMLSMClient.DownloadMLSM(client, mlsmjson);
        
        
        Files.write(Paths.get(MLSMFILENAME), mlsm.getBytes());
        System.out.println(String.format("MLSM written to file %s", MLSMFILENAME));

    }
}
