package com.predixion.sample;
import java.util.ArrayList;
import java.util.Date;

import org.predixion.mlsmclient.interfaces.IMLSMExecuteWithCache;
import org.predixion.mlsmclient.mlsm.wrapper.MLSMWrapper;

import Aggregation.CacheInfo;
import predixion.Helpers.SchemaDefinition;


public class AggregationSingleTable
{
	static String MLSMFILENAME = "..\\MLSMFiles\\AggregationSingleTable.XML";


    @SuppressWarnings("deprecation")
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

        //Optional: Max rows creates an upper bound for the maximum rows to cache for aggregation calculations only
        mlsmWrapper.SetCacheMaxRows(1000);

        //Optional: 
        //    If not specified, the input schema defined in the MLSM will be used.
        //    Each input schema object contains a column name and an optional data type.
        //            If the data type is null, then the data type of the bound column will be used. If it is an unbound column, String datatype will be used.
        //SchemaDefinition[] schemaDef = new SchemaDefinition[3];
        //schemaDef[0] = new SchemaDefinition("ColName", double.class);
        //mlsmWrapper.SetInputSchema(schemaDef);

        ////////////////////////////////////////////////////////////////////////////////////////////
        // Step 4 : Get a scorer interface from the wrapper
        ////////////////////////////////////////////////////////////////////////////////////////////
        IMLSMExecuteWithCache mlsmExecuteWithCache = mlsmWrapper.GetAggregationEngine();

        //Output schema 
        ArrayList<SchemaDefinition> outputSchema = mlsmExecuteWithCache.GetOutputSchema();

        ////////////////////////////////////////////////////////////////////////////////////////////
        // Step 5 : Set input rows and score
        ////////////////////////////////////////////////////////////////////////////////////////////
        Object[] inputValues = new Object[3];
        int maxRows = 100;
        int rowCount = 0;
        int device = 1;
        Date dt = PXHelper.NewDate(1976, 3, 31);
        while (true /*or while the input data stream has data*/)
        {
        	inputValues[0] = device; // DeviceID
        	inputValues[1] = dt; // MsmtTime
        	inputValues[2] = dt.getHours() * device; // MsmtVal


            //Add rows to the cache
            mlsmExecuteWithCache.AddRow(inputValues);

            //Score the last added case table row. Some output expressions will result in multiple output rows 
            //being returned for a single input row
            Iterable<Object[]> outputValues = mlsmExecuteWithCache.Execute();
            for (Object[] output : outputValues)
            {
                for (int j = 0; j < output.length; j++)
                {
                	System.out.println(String.format("%s:\t%s", outputSchema.get(j).getColumnName(), output[j]));
                }
            }

            dt = PXHelper.AddHours(dt, 1);
            rowCount++;
            if (rowCount > maxRows)
                break;
        }


        OptionalMethods(mlsmExecuteWithCache);
    }


    @SuppressWarnings("unused")
	public void OptionalMethods(IMLSMExecuteWithCache mlsmExecuteWithCache) throws Exception
    {
        ////////////////////////////////////////////////////////////////////////////////////////////
        // Optional outputs:
        ////////////////////////////////////////////////////////////////////////////////////////////

        //Input schema for the case table
        ArrayList<SchemaDefinition> caseTableInputSchema = mlsmExecuteWithCache.GetInputSchema();

        //Cache info - when the MLSM has WINDOW or aggregates. There can be multiple cache created, one for each input table
        Iterable<CacheInfo> cacheInfoList = mlsmExecuteWithCache.GetCacheInfo();
        for (CacheInfo cacheInfo : cacheInfoList)
        {
        	System.out.println(String.format("Table name : %s", cacheInfo.TableName));
        	System.out.println(String.format("Buffer size: %s", cacheInfo.BufferRows));
			System.out.println(String.format("Index size : %s", cacheInfo.IndexSize));
        }
    }
}
