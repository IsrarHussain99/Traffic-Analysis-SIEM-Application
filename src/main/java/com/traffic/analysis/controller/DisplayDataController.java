package com.traffic.analysis.controller;

import com.traffic.analysis.TrafficAnalysisApplication;
import com.traffic.analysis.util.AlertHelper;
import com.traffic.analysis.util.Delimiter;
import com.traffic.analysis.util.GridFactory;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.EventType;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.SelectionMode;
import javafx.scene.control.TablePosition;
import javafx.scene.layout.BorderPane;
import javafx.stage.FileChooser;
import javafx.stage.Stage;
import org.controlsfx.control.spreadsheet.GridBase;
import org.controlsfx.control.spreadsheet.SpreadsheetCell;
import org.controlsfx.control.spreadsheet.SpreadsheetView;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;

import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.util.HashMap;
import java.util.Map;
import java.util.ResourceBundle;

@Controller
public class DisplayDataController implements Initializable {

    @FXML
    private BorderPane displayDataPane;

    @FXML
    private Button groupColumnBtn;

    @FXML
    private Button loadCsvBtn;
    @FXML
    private ComboBox<Delimiter> csvDelimitersCBox;

    @Autowired
    private AlertHelper alertHelper;

    @Autowired
    private GridFactory gridFactory;

    private SpreadsheetView currentSpreadSheetView;


    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        // create the supported delimiters.
        ObservableList<Delimiter> delimiters = FXCollections.observableArrayList(new Delimiter("comma", ','),
                new Delimiter("tab", '\t'),
                new Delimiter("colon", ':'),
                new Delimiter("semi-colon", ';'));

        // display the delimiters in the combo box
        this.csvDelimitersCBox.setItems(delimiters);

        // show the default delimiter
        this.csvDelimitersCBox.getSelectionModel().selectFirst();
    }

    /**
     * Callback when the 'loadCsvBtn' is clicked.
     */
    public void handleLoadFileAction(){
        // load the target file.
        File loadedFile = loadFile();
        if(loadedFile != null){ // ensure the file is non-null.
            try {
                // create the grid
                GridBase grid = gridFactory.createGrid(loadedFile);

                // create the spreadsheet view
                SpreadsheetView spreadsheetView = new SpreadsheetView();

                spreadsheetView.getSelectionModel().setSelectionMode(SelectionMode.SINGLE);

                // set the grid of the spread sheet.
                spreadsheetView.setGrid(grid);

                // display the spreadsheet view
                this.displayDataPane.setCenter(spreadsheetView);

                // save the spreadsheet view
                this.currentSpreadSheetView = spreadsheetView;
            } catch (IOException e) {
                alertHelper.showErrorAlert("Error loading file! " + e.getMessage());
                e.printStackTrace();
            }
        }
    }


    /**
     * Load the file via file chooser.
     * @return The loaded file or {@code null} if no file was loaded.
     */
    private File loadFile(){
        FileChooser fileChooser = new FileChooser();
        fileChooser.setTitle("Select CSV file");
        fileChooser.getExtensionFilters().add(new FileChooser.ExtensionFilter("CSV", "*.csv"));
        return fileChooser.showOpenDialog(null);
    }

    /**
     * @return The currently selected delimiter character.
     */
    public char getDelimiterChar(){
        return this.csvDelimitersCBox.getSelectionModel().getSelectedItem().getDelimiterChar();
    }

    // group columns based on the selected column.
    public void groupColumn(){
        if(this.currentSpreadSheetView == null){
            alertHelper.showErrorAlert("No file loaded!");
            return;
        }
        // get the currently selected cell, and therefore the column.
        TablePosition tablePosition = this.currentSpreadSheetView.getSelectionModel()
                .getSelectedCells()
                .stream()
                .findFirst()
                .orElse(null);

        if(tablePosition == null){
            alertHelper.showErrorAlert("No Column Selected!");
        } else {
            try {
                // get the frequency data for the selected column.
                Map<String, Integer> frequencyData = getFrequencyData(tablePosition.getColumn());

                // now load the display frequency window
                FXMLLoader fxmlLoader =
                        new FXMLLoader(TrafficAnalysisApplication.class.getResource("display_frequency.fxml"));

                // now load the FXML UI file
                Parent parent = fxmlLoader.load();

                // get the display frequency controller
                DisplayFrequencyController controller = fxmlLoader.getController();

                // ask the controller class to init the map data
                controller.setData(frequencyData);

                // now display the data
                controller.displayChartData();

                // create scene
                Scene scene = new Scene(parent);

                // create stage
                Stage stage = new Stage();

                // set the scene of the stage
                stage.setScene(scene);

                // show the stage
                stage.show();

            } catch (IOException e) {
                alertHelper.showErrorAlert("Error showing frequency window!");
                e.printStackTrace();
            }
        }
    }

    /**
     * Get the frequency data.
     * @param columnIndex The column index.
     * @return The frequency data as a map.
     */
    private Map<String, Integer> getFrequencyData(int columnIndex){
        // declare frequency data.
        Map<String, Integer> data = new HashMap<>();

        // get all the rows
        ObservableList<ObservableList<SpreadsheetCell>> rows = this.currentSpreadSheetView.getItems();
        for(ObservableList<SpreadsheetCell> row : rows){
            // get the item in the specified column, for this row.
            String item = (String) row.get(columnIndex).getItem();

            if(data.containsKey(item)){ // the item already exists
                int count = data.get(item) + 1;
                // add increment the count of the data item.
                data.put(item, count);
            } else {
                // it is the first time we are adding this item,
                data.put(item, 1);
            }

        }

        return data;
    }



}
