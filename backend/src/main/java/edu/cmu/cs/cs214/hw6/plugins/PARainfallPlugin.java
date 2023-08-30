package edu.cmu.cs.cs214.hw6.plugins;

import java.io.FileReader;
import java.io.Reader;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import java.time.LocalDate;
import java.time.YearMonth;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;

import org.apache.commons.csv.CSVFormat;
import org.apache.commons.csv.CSVRecord;

import edu.cmu.cs.cs214.hw6.framework.Coordinate;
import edu.cmu.cs.cs214.hw6.framework.DataPoint;
import edu.cmu.cs.cs214.hw6.framework.core.DataPlugin;

/**
 * DataPLugin for monthly rainfall per PA county
 */
public class PARainfallPlugin implements DataPlugin {

    private HashMap<Coordinate, List<DataPoint>> dataMap = new HashMap<Coordinate, List<DataPoint>>();

    public PARainfallPlugin() {
        try {
            Reader in = new FileReader("src/main/java/edu/cmu/cs/cs214/hw6/datasources/county-monthly-percip.csv");
            Iterable<CSVRecord> records = CSVFormat.EXCEL.parse(in);
            for (CSVRecord record : records) {
                if (record.size() == 6) {
                    try {
                        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM");
                        YearMonth ym = YearMonth.parse(record.get(4), formatter);
                        LocalDate date = ym.atDay(1);

                        String countyName = record.get(0);
                        double longitude = Double.parseDouble(record.get(1));
                        double latitude = Double.parseDouble(record.get(2));
                        // Date date = new SimpleDateFormat("yyyy-MM").parse(record.get(4));
                        double value = Double.parseDouble(record.get(5));

                        Coordinate coord = new Coordinate(longitude, latitude, countyName);
                        DataPoint dataPoint = new DataPoint(date, value);

                        if (dataMap.get(coord) == null) {
                            dataMap.put(coord, new ArrayList<DataPoint>());
                        } else {
                            dataMap.get(coord).add(dataPoint);
                        }
                    } catch (NumberFormatException | DateTimeParseException e) {
                        System.err.println("Couldn't parse line: " + record.toString());
                    }
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public String getDataSetName() {
        return "MonthlyPARainfall10Yrs";
    }

    @Override
    public String getDataSetDescription() {
        return "Monthly rainfall in inches per PA County over 10 years";
    }

    @Override
    public HashMap<Coordinate, List<DataPoint>> getData() {
        return dataMap;
    }
}
