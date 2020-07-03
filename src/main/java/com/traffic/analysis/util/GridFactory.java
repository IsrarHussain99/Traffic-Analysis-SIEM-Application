package com.traffic.analysis.util;

import com.traffic.analysis.controller.DisplayDataController;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import org.apache.commons.csv.CSVFormat;
import org.apache.commons.csv.CSVRecord;
import org.controlsfx.control.spreadsheet.Grid;
import org.controlsfx.control.spreadsheet.GridBase;
import org.controlsfx.control.spreadsheet.SpreadsheetCell;
import org.controlsfx.control.spreadsheet.SpreadsheetCellType;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.io.Reader;
import java.util.ArrayList;
import java.util.List;

@Component
public class GridFactory{

    @Autowired
    private DisplayDataController displayDataController;

    /**
     * Utility method to create a grid with data for the spreadsheet.
     * @param file The CSV file from which to load data.
     */
    public GridBase createGrid(File file) throws IOException {
        // get the grid data
        ObservableList<ObservableList<SpreadsheetCell>> cellGridData = csvRecordsToRows(getRecords(file));

        // get the row count
        int rowCount = cellGridData.size();

        // get the column count
        ObservableList<SpreadsheetCell> sampleRow = cellGridData.stream().findFirst().orElse(null);
        int columnCount = sampleRow == null ? 0 : sampleRow.size();

        // create the grid base
        GridBase gridBase = new GridBase(rowCount, columnCount);

        // now set the grid data
        gridBase.setRows(cellGridData);


        // return the grid base
        return gridBase;
    }

    // convert a CSV list of records to a collection of rows
    private ObservableList<ObservableList<SpreadsheetCell>> csvRecordsToRows(List<CSVRecord> csvRecords){
        ObservableList<ObservableList<SpreadsheetCell>> rows = FXCollections.observableArrayList();
        int rowIndex = 0;
        for(CSVRecord csvRecord : csvRecords){
            rows.add(FXCollections.observableArrayList(csvRecordToSpreadSheetRow(csvRecord, rowIndex)));
            rowIndex++;
        }
        return rows;
    }

    // convert a csv record to a list of spreadsheet cells (a row)
    private List<SpreadsheetCell> csvRecordToSpreadSheetRow(CSVRecord csvRecord, int rowIndex){
        List<SpreadsheetCell> spreadsheetCells = new ArrayList<>();
        int columnIndex = 0;
        for(String column : csvRecord){
            // create a cell.
            SpreadsheetCell cell =
                    SpreadsheetCellType.STRING.createCell(rowIndex, columnIndex, 1, 1, column);

            // add the cell to the spreadsheet cells.
            spreadsheetCells.add(cell);

            // increment the column index
            columnIndex++;
        }

        return spreadsheetCells;
    }

    // get the CSV records from the loaded file.
    private List<CSVRecord> getRecords(File loadedFile) throws IOException {
        // create the reader file from the loaded file.
        Reader reader = new FileReader(loadedFile);

        // get the delimiter character
        char delimiterChar = this.displayDataController.getDelimiterChar();

        // return the list of CSV records
        return CSVFormat.newFormat(delimiterChar)
                .parse(reader)
                .getRecords();
    }


}
