package edu.cmu.cs.cs214.hw6.plugins;

import edu.cmu.cs.cs214.hw6.framework.core.DataPlugin;
import edu.cmu.cs.cs214.hw6.framework.Coordinate;
import edu.cmu.cs.cs214.hw6.framework.DataPoint;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;

import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.*;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

/**
 * @brief US population data plug-in.
 * The data includes the population of every state from 2010-2019, organizing them per year and per state.
 * It was gathered from the United States Census Bureau: https://data.census.gov/
 */
public class USPopulationPlugin implements DataPlugin {
    private FileInputStream file;
    private Workbook workbook;

    private static final String FILEPATH = "src/main/java/edu/cmu/cs/cs214/hw6/datasources/USPopulationData.xlsx";

    private String name;
    private String description;
    private HashMap<Coordinate, List<DataPoint>> allData;

    /**
     * Constructor for the US population data plugin
     */
    public USPopulationPlugin() {
        this.name = "StatePopulation2010to2019";
        this.description = "This data set contains the population per state in the US. It is categorized by year from 2010 to " +
                           "2019. The data was obtained from the United States Census Bureau (https://data.census.gov/)";
        allData = new HashMap<>();

        retrieveFile(FILEPATH);
        loadData();
    }

    /**
     * Retrieves the file and stores it in {@code file}
     * @param pathName The path to the xlsx file
     */
    private void retrieveFile(String pathName) {
        try {
            file = new FileInputStream(pathName);
            workbook = new XSSFWorkbook(file);
        } catch (FileNotFoundException e1) {
            System.out.println("File not found");
        } catch (IOException e2) {
            System.out.println("Failed set up");
            e2.printStackTrace();
        }
    }

    /**
     * @brief Reads and loads the excel file information into {@code allData}
     * {@code allData} is a hash map. The keys are US states, and the values of are
     * a list of data points. The data points each include a date and populatio value.
     */
    private void loadData() {
        // iterate through states
        for (int i = 2; i < 54; i++) {
            // retrieve coordinate using state name
            String state = readStringCellData(i, 0);

            double latitude = readNumCellData(i, 11);
            double longitude = readNumCellData(i, 12);

            Coordinate coordinate = new Coordinate(longitude, latitude, state);

            if (coordinate != null) {
                // initialize a new list for each state
                List<DataPoint> data = new ArrayList<DataPoint>();

                // iterate through yearly data for each state
                for (int j = 1; j < 11; j++) {
                    // get date
                    LocalDate date = null;
                    try {
                        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("M/d/yyyy");
                        date = LocalDate.parse(readStringCellData(0, j), formatter);       
                    } catch (DateTimeParseException e) {
                        System.out.println("Invalid date");
                        e.printStackTrace();
                    }

                    // get populations
                    double value = Double.parseDouble(readStringCellData(i, j));

                    // create a DataPoint using date and value
                    DataPoint dp = new DataPoint(date, value);

                    // add to data points list
                    data.add(dp);
                }

                // add state's population data to hashmap
                allData.put(coordinate, data);
            }
        }
    }

    /**
     * Reads the value inside cell given its row and column number
     * @param vRow row number of the cell
     * @param vColumn column number of the cell
     * @return the {@code String} value inside the cell
     * Reference: https://www.javatpoint.com/how-to-read-excel-file-in-java
     */
    private String readStringCellData(int vRow, int vColumn) {
        String value = null;                           // variable for storing the cell value
        Sheet sheet = workbook.getSheetAt(0);    // getting the XSSFSheet object at given index
        Row row = sheet.getRow(vRow);                  // returns the logical row
        Cell cell = row.getCell(vColumn);              // getting the cell representing the given column
        value = cell.getStringCellValue();             // getting cell value
        return value;                                  // returns the cell value
    }

    /**
     * Reads the value inside cell given its row and column number
     * @param vRow row number of the cell
     * @param vColumn column number of the cell
     * @return the {@code double} value inside the cell
     * Reference: https://www.javatpoint.com/how-to-read-excel-file-in-java
     */
    private double readNumCellData(int vRow, int vColumn) {
        Sheet sheet = workbook.getSheetAt(0);    // getting the XSSFSheet object at given index
        Row row = sheet.getRow(vRow);                  // returns the logical row
        Cell cell = row.getCell(vColumn);              // getting the cell representing the given column
        return cell.getNumericCellValue();             // getting and returning cell value
    }

    @Override
    public String getDataSetName() {
        return this.name;
    }

    @Override
    public String getDataSetDescription() {
        return this.description;
    }

    @Override
    public HashMap<Coordinate, List<DataPoint>> getData() {
        return this.allData;
    }
}
