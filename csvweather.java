
/**
 * Write a description of csvweather here.
 * 
 * @author (your name) 
 * @version (a version number or a date)
 */
import edu.duke.*;
import org.apache.commons.csv.*;
import java.io.*;
public class csvweather {
public CSVRecord coldestHourInFile(CSVParser parser)
{
    CSVRecord smallestSoFar = null;
    for(CSVRecord currentRow : parser)
    {
        if(smallestSoFar==null)
        {
            smallestSoFar=currentRow;
        }
        else
        {
            double currentTemp=Double.parseDouble(currentRow.get("TemperatureF"));
            double smallestTemp=Double.parseDouble(smallestSoFar.get("TemperatureF"));
            if(currentTemp<smallestTemp&&currentTemp!=-9999)
            smallestSoFar = currentRow;
        }
    }
    return smallestSoFar;
}
public File fileWithColdestTemperature()
{
    CSVRecord coldestSoFar = null;
    DirectoryResource dr = new DirectoryResource();
    File s=null;
    for(File f : dr.selectedFiles())
    {
        FileResource fr = new FileResource(f);
        CSVRecord currentRow = coldestHourInFile(fr.getCSVParser());
        if(coldestSoFar==null)
        {
            coldestSoFar=currentRow;
            s=f;
        }
        else
        {
            double currentTemp=Double.parseDouble(currentRow.get("TemperatureF"));
            double smallestTemp=Double.parseDouble(coldestSoFar.get("TemperatureF"));
            if(currentTemp<smallestTemp&&currentTemp!=-9999)
            {
                coldestSoFar = currentRow;
                s=f;
            }
        }
    }
    return s;
}
public CSVRecord lowestHumidityInFile(CSVParser parser)
{
    CSVRecord lowestSoFar = null;
    for(CSVRecord currentRow : parser)
    {
        if(lowestSoFar==null)
        {
            lowestSoFar=currentRow;
        }
        else
        {
            int currentHum;
            try
            {
                currentHum=Integer.parseInt(currentRow.get("Humidity"));
            }catch(NumberFormatException ex){
            currentHum=999;
            }
            int lowestHum=Integer.parseInt(lowestSoFar.get("Humidity"));
            if(currentHum<lowestHum)
            lowestSoFar=currentRow;
        }
    }
    return lowestSoFar;
}
public void testLowestHumidityInFile()
{
    FileResource fr = new FileResource();
    CSVParser parser = fr.getCSVParser();
    CSVRecord csv = lowestHumidityInFile(parser);
    System.out.println("Lowest Humidity was "+csv.get("Humidity")+" at "+csv.get("DateUTC"));
}
public File lowestHumidityInManyFiles()
{
    CSVRecord lowest=null;
    DirectoryResource dr=new DirectoryResource();
    File s=null;
    for(File f:dr.selectedFiles())
    {
        FileResource fr=new FileResource(f);
        CSVRecord currentRow=lowestHumidityInFile(fr.getCSVParser());
        if(lowest==null)
        {
            lowest=currentRow;
            s=f;
        }
        else
        {
            int currentHum;
            try
            {
                currentHum=Integer.parseInt(currentRow.get("Humidity"));
            }catch(NumberFormatException ex){
            currentHum=999;
            }
            int lowestHum=Integer.parseInt(lowest.get("Humidity"));
            if(currentHum<lowestHum)
            {
                lowest=currentRow;
                s=f;
            }
        }
    }
    return s;
}
public double averageTemperatureInFile(CSVParser parser)
{
    double sum=0,temp,count=0;
    for(CSVRecord currentRow:parser)
    {
        temp=Double.parseDouble(currentRow.get("TemperatureF"));
        if(temp!=-9999)
        sum=sum+temp;
        count+=1;
    }
    return sum/count;
}
public double averageTemperatureWithHighHumidity(CSVParser parser,int value)
{
    double sum=0,temp,hum,count=0;
    for(CSVRecord currentRow:parser)
    {
       temp=Double.parseDouble(currentRow.get("TemperatureF"));
       hum=Integer.parseInt(currentRow.get("Humidity"));
       if(hum>value)
       {
           sum=sum+temp;
           count+=1;
       }
    }
    return sum/count;
}
public void testaverageTemperatureWithHighHumidity()
{
    FileResource fr=new FileResource();
    System.out.println("Average temperature in File with humidity greater than: " + averageTemperatureWithHighHumidity(fr.getCSVParser(),80));
}
public void testaverageTemperatureInFile()
{
    FileResource fr=new FileResource();
    System.out.println("Average temperature in File: " + averageTemperatureInFile(fr.getCSVParser()));
}
public void testLowestHumidityInManyFiles()
{
    File fr=lowestHumidityInManyFiles();
    FileResource f=new FileResource(fr);
    System.out.println("lowestHumidity on that day was "+lowestHumidityInFile(f.getCSVParser()).get("Humidity")+" "+lowestHumidityInFile(f.getCSVParser()).get("DateUTC"));
}
public void testColdestHourInFile()
{
    FileResource fr=new FileResource();
    CSVRecord smallest = coldestHourInFile(fr.getCSVParser());
    System.out.println("The Coldest Hour is "+smallest.get("TemperatureF")+" at "+smallest.get("TimeEDT"));
}
public void testFileWithColdestTemp()throws IOException
{
    File fr=fileWithColdestTemperature();
    FileResource f=new FileResource(fr);
    System.out.println("Coldest day was in file "+fr.getCanonicalPath());
    System.out.println("ColdestTemperature on that day was "+coldestHourInFile(f.getCSVParser()).get("TemperatureF"));
    System.out.println("All temperatures on that day were:");
    for(CSVRecord record : f.getCSVParser())
    {
        System.out.println(record.get("DateUTC")+" "+record.get("TimeEST")+" "+record.get("TemperatureF"));
    }
}
}
