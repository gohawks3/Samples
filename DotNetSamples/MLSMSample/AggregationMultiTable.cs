﻿using Predixion.AnalyticsCore.Aggregation;
using Predixion.AnalyticsCore.Helpers;
using Predixion.MLSMClient.Interfaces;
using Predixion.MLSMClient.Wrapper;
using System;
using System.Collections.Generic;
using System.Linq;
using System.Text;

namespace MLSMSample
{
    class AggregationMultiTable
    {
        const string MLSMFILENAME = "AggregationMultiTable.XML";

        public void Run()
        {
            ////////////////////////////////////////////////////////////////////////////////////////////
            // Step 1 : Create the MLSM Engine wrapper
            ////////////////////////////////////////////////////////////////////////////////////////////
            MLSMWrapper mlsmWrapper = MLSMWrapper.CreateWrapper();

            ////////////////////////////////////////////////////////////////////////////////////////////
            // Step 2 : Set the MLSM from a file or a string
            ////////////////////////////////////////////////////////////////////////////////////////////
            string mlsmFile = MLSMFILENAME;
            mlsmWrapper.SetMLSMFile(mlsmFile);

            //string mlsm = "...";// MLSM string loaded from a file or downloaded from the server directly
            //mlsmWrapper.SetMLSM(mlsm);

            ////////////////////////////////////////////////////////////////////////////////////////////
            // Step 3 : Set additional options
            ////////////////////////////////////////////////////////////////////////////////////////////

            //Optional: Max rows creates an upper bound for the maximum rows to cache for aggregation calculations only
            //mlsmWrapper.SetCacheMaxRows(1000);

            //Optional: 
            //    If not specified, the input schema defined in the MLSM will be used.
            //    Each input schema object contains a column name and an optional data type.
            //            If the data type is null, then the data type of the bound column will be used. If it is an unbound column, String datatype will be used.
            //SchemaDefinition[] schemaDef = new SchemaDefinition[3];
            //schemaDef[0] = new SchemaDefinition("ColName", typeof(double));
            //mlsmWrapper.SetInputSchema(schemaDef);

            ////////////////////////////////////////////////////////////////////////////////////////////
            // Step 4 : Get a scorer interface from the wrapper
            ////////////////////////////////////////////////////////////////////////////////////////////
            IMLSMExecuteWithCache mlsmExecuteWithCache = mlsmWrapper.GetAggregationEngine();

            //Output schema 
            List<SchemaDefinition> outputSchema = mlsmExecuteWithCache.GetOutputSchema();

            ////////////////////////////////////////////////////////////////////////////////////////////
            // Step 5 : Set input rows and score
            ////////////////////////////////////////////////////////////////////////////////////////////
            object[] nestedInput = new object[3];
            object[] caseInput = new object[2];
            Random rnd = new Random();

            int maxRows = 1000;
            int rowCount = 0;
            int device = 1;
            DateTime dt = new DateTime(1976, 3, 31);
            while (true /*or while the input data stream has data*/)
            {
                if (rowCount % 10 == 9)
                {
                    //Push a case table row and score
                    caseInput[0] = 1;
                    caseInput[1] = dt.AddHours(2);

                    mlsmExecuteWithCache.AddRow(null, caseInput);

                    //Score the last added case table row. Some output expressions will result in multiple output rows 
                    //being returned for a single input row
                    IEnumerable<object[]> outputValues = mlsmExecuteWithCache.Execute();
                    foreach (object[] output in outputValues)
                    {
                        for (int j = 0; j < output.Length; j++)
                        {
                            Console.WriteLine("{0}:\t{1}", outputSchema[j].getColumnName(), output[j]);
                        }
                    }
                }

                nestedInput[0] = device;
                nestedInput[1] = dt; // MsmtTime
                nestedInput[2] = rnd.Next();

                //Add nested table rows to the cache
                mlsmExecuteWithCache.AddRow("Measurements", nestedInput);


                dt = dt.AddMinutes(1);
                rowCount++;
                if (rowCount > maxRows)
                    break;
            }

            OptionalMethods(mlsmExecuteWithCache);
        }


        public void OptionalMethods(IMLSMExecuteWithCache mlsmExecuteWithCache)
        {
            ////////////////////////////////////////////////////////////////////////////////////////////
            // Optional outputs:
            ////////////////////////////////////////////////////////////////////////////////////////////

            //Input schema for the case table
            List<SchemaDefinition> caseTableInputSchema = mlsmExecuteWithCache.GetInputSchema();
            List<SchemaDefinition> nestedTableInputSchema = mlsmExecuteWithCache.GetInputSchema("Measurements");

            //Cache info - when the MLSM has WINDOW or aggregates. There can be multiple cache created, one for each input table
            IEnumerable<CacheInfo> cacheInfoList = mlsmExecuteWithCache.GetCacheInfo();
            foreach (CacheInfo cacheInfo in cacheInfoList)
            {
                Console.WriteLine("Table name: {0}", cacheInfo.TableName);
                Console.WriteLine("Buffer size: {0}", cacheInfo.BufferRows);
                Console.WriteLine("Index size : {0}", cacheInfo.IndexSize);
            }
        }
    }
}
