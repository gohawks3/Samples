using Predixion.AnalyticsCore.Helpers;
using Predixion.MLSMClient.Interfaces;
using Predixion.MLSMClient.Wrapper;
using System;
using System.Collections.Generic;
using System.Linq;
using System.Text;

namespace MLSMSample
{
    class OfflineScoring
    {
        const string MLSMFILENAME = "CollegePlans.XML";

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
            List<SchemaDefinition> outputSchema = mlsmExecute.GetOutputSchema();

            ////////////////////////////////////////////////////////////////////////////////////////////
            // Step 5 : Set input rows and score
            ////////////////////////////////////////////////////////////////////////////////////////////
            object[] inputValues = new object[6];
            while (true /*or while the input data stream has data*/)
            {
                inputValues[0] = 4004.15; // StudentID
                inputValues[1] = "Female"; // Gender
                inputValues[2] = 40618; // ParentIncome
                inputValues[3] = 99; // IQ
                inputValues[4] = "Encouraged"; // ParentEncouragement
                inputValues[5] = "Does not plan to attend"; // CollegePlans


                //Score the input row
                IEnumerable<object[]> outputValues = mlsmExecute.Execute(inputValues);
                foreach (object[] output in outputValues)
                {
                    for (int j = 0; j < output.Length; j++)
                    {
                        Console.WriteLine("{0}:\t{1}", outputSchema[j].getColumnName(), output[j]);
                    }
                }

                break;
            }
        }
    }
}
