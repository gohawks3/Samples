using Predixion.Analytics.Interfaces;
using Predixion.MLSMClient.Helpers;
using System;
using System.Collections.Generic;
using System.Linq;
using System.Text;

namespace MLSMSample
{
    class OutputHelper
    {
    

	public static void WriteOutputReaderToFile(string outputFileName , IPXDataReader outputReader , char csvSeparator)
	{
        String outputDataFile = outputFileName;
		bool writeHeader = true;
		CSVFileHelper.CreateCSVFileFromDataReader(
				outputReader, 
				outputDataFile, 
				writeHeader, 
				csvSeparator);
        Console.WriteLine(string.Format("Output written to file {0}", outputFileName));
	}
	
	public static void WriteOutputReaderToConsole(IPXDataReader outputReader)
	{
		while (outputReader.Read())
		{
			Object[] values = new Object[outputReader.FieldCount];
			outputReader.GetValues(values);
			for (int i = 0; i < values.Length; i++)
			{
				Console.WriteLine(values[i] + ",");
			}
			Console.WriteLine("\n");
		}
	}
    }
}
