package edu.cmu.cs.cs214.hw6.plugins;

import java.io.FileReader;
import java.io.Reader;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;

import org.apache.commons.csv.CSVFormat;
import org.apache.commons.csv.CSVRecord;

import edu.cmu.cs.cs214.hw6.framework.Coordinate;
import edu.cmu.cs.cs214.hw6.framework.DataPoint;
import edu.cmu.cs.cs214.hw6.framework.core.DataPlugin;

/**
 * DataPlugin for COVID Deaths per county in PA
 */
public class CovidPlugin implements DataPlugin {

    private HashMap<Coordinate, List<DataPoint>> dataMap = new HashMap<Coordinate, List<DataPoint>>();

    public CovidPlugin() {
        try {
            Reader in = new FileReader("src/main/java/edu/cmu/cs/cs214/hw6/datasources/COVID-19_Aggregate_Death_Data_Current_Weekly_County_Health.csv");
            Iterable<CSVRecord> records = CSVFormat.EXCEL.parse(in);
            for (CSVRecord record : records) {
                try {
                    DateTimeFormatter formatter = DateTimeFormatter.ofPattern("MM/dd/yyyy");
                    LocalDate date = LocalDate.parse(record.get(1), formatter);

                    String countyName = record.get(0);
                    String value = record.get(2);
                    double longitude = Double.parseDouble(record.get(10));
                    double latitude = Double.parseDouble(record.get(11));
                    Coordinate coord = new Coordinate(longitude, latitude, countyName);
                    if(value != null){
                        DataPoint dataPoint = new DataPoint(date, Double.parseDouble(value));

                        if (dataMap.get(coord) == null) {
                            dataMap.put(coord, new ArrayList<DataPoint>());
                        } else {
                            dataMap.get(coord).add(dataPoint);
                        }
                    }

                } catch (NumberFormatException | DateTimeParseException e) {
                    System.err.println("Couldn't parse line: " + record.toString());
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public String getDataSetName() {
        return "CovidDeathPACounty";
    }

    @Override
    public String getDataSetDescription() {
        return "Daily new covid deaths in Pennsylvania recorded by county (2020-2022). Provided by the Department of Health at https://data.pa.gov/Covid-19/COVID-19-Aggregate-Death-Data-Current-Weekly-Count/fbgu-sqgp";
    }

    @Override
    public HashMap<Coordinate, List<DataPoint>> getData() {
        return dataMap;
    }
}
