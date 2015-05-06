package com.predixion.sample;
import java.util.ArrayList;

import org.predixion.mlsmclient.interfaces.IMLSMExecute;
import org.predixion.mlsmclient.mlsm.wrapper.MLSMWrapper;

import predixion.Helpers.SchemaDefinition;


public class OfflineScoring
{
	static String MLSMFILENAME = "..\\MLSMFiles\\CollegePlans.XML";

    public void Run() throws Exception
    {
        ////////////////////////////////////////////////////////////////////////////////////////////
        // Step 1 : Create the MLSM Engine wrapper
        ////////////////////////////////////////////////////////////////////////////////////////////
        MLSMWrapper mlsmWrapper = MLSMWrapper.CreateWrapper();

        ////////////////////////////////////////////////////////////////////////////////////////////
        // Step 2 : Set the MLSM from a file or a string
        ////////////////////////////////////////////////////////////////////////////////////////////
        String mlsmFile = MLSMFILENAME;
        mlsmWrapper.SetMLSMFile(mlsmFile);

        //string mlsm = "...";// MLSM string loaded from a file or downloaded from the server directly
        //mlsmWrapper.SetMLSM(mlsm);

        ////////////////////////////////////////////////////////////////////////////////////////////
        // Step 3 : Set additional options
        ////////////////////////////////////////////////////////////////////////////////////////////
        //Optional: 
        //    If not specified, the input schema defined in the MLSM will be used.
        //    Each input schema object contains a column name and an optional data type.
        //            If the data type is null, then the data type of the bound column will be used. If it is an unbound column, String datatype will be used.
        //SchemaDefinition[] schemaDefiniton = new SchemaDefinition[6];
        //schemaDefiniton[0] = new SchemaDefinition("ColName", typeof(double));
        //mlsmWrapper.SetInputSchema(null, schemaDefiniton);

        ////////////////////////////////////////////////////////////////////////////////////////////
        // Step 4 : Get a scorer interface from the wrapper
        ////////////////////////////////////////////////////////////////////////////////////////////
        IMLSMExecute mlsmExecute = mlsmWrapper.GetExecutionEngine();

        //Output schema 
        ArrayList<SchemaDefinition> outputSchema = mlsmExecute.GetOutputSchema();

        ////////////////////////////////////////////////////////////////////////////////////////////
        // Step 5 : Set input rows and score
        ////////////////////////////////////////////////////////////////////////////////////////////
        Object[] inputValues = new Object[6];
        while (true /*or while the input data stream has data*/)
        {
            inputValues[0] = 4004.15; // StudentID
            inputValues[1] = "Female"; // Gender
            inputValues[2] = 40618.56; // ParentIncome
            inputValues[3] = 99.53; // IQ
            inputValues[4] = "Encouraged"; // ParentEncouragement
            inputValues[5] = "Does not plan to attend"; // CollegePlans


            //Score the input row
            Iterable<Object[]> outputValues = mlsmExecute.Execute(inputValues);
            for (Object[] output : outputValues)
            {
                for (int j = 0; j < output.length; j++)
                {
                	System.out.println(String.format("%s:\t%s", outputSchema.get(j).getColumnName(), output[j]));
                }
            }

            break;
        }
    }
}
