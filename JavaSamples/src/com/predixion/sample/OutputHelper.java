package com.predixion.sample;
import Interfaces.IPXDataReader;
import Streams.CSVFileHelper;


public class OutputHelper {
	public static void WriteOutputReaderToFile(String outputFileName , IPXDataReader outputReader , char csvSeparator) throws Exception
	{
		String outputDataFile = outputFileName;
		boolean writeHeader = true;
		CSVFileHelper.CreateCSVFileFromDataReader(
				outputReader, 
				outputDataFile, 
				writeHeader, 
				csvSeparator);
		System.out.println(String.format("Output written to file %s", outputFileName));
	}
	
	public static void WriteOutputReaderToConsole(IPXDataReader outputReader) throws Exception
	{
		while (outputReader.Read())
		{
			Object[] values = new Object[outputReader.FieldCount()];
			outputReader.GetValues(values);
			for (int i = 0; i < values.length; i++)
			{
				System.out.println(values[i] + ",");
			}
			System.out.println("\n");
		}
	}
}
