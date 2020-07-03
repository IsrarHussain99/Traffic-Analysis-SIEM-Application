package com.traffic.analysis.controller;

import javafx.collections.FXCollections;
import javafx.fxml.FXML;
import javafx.scene.chart.BarChart;
import javafx.scene.chart.CategoryAxis;
import javafx.scene.chart.NumberAxis;
import javafx.scene.chart.XYChart;
import javafx.scene.layout.BorderPane;

import java.util.ArrayList;
import java.util.Map;

public class DisplayFrequencyController{
    @FXML
    private BorderPane displayFrequencyPane;

    private Map<String, Integer> data;

    public void setData(Map<String, Integer> data){
        this.data = data;
    }

    public void displayChartData(){
        // create the x-axis category
        CategoryAxis xAxis = new CategoryAxis();

        // set the categories
        xAxis.setCategories(FXCollections.observableList(new ArrayList<>(data.keySet())));

        // set the label.
        xAxis.setLabel("Group");

        // create the y-axis
        NumberAxis yAxis = new NumberAxis();

        // set the label for the y-axis
        yAxis.setLabel("Frequency");

        // create the bar chart.
        BarChart<String, Number> barChart = new BarChart<>(xAxis, yAxis);
        barChart.setTitle("Frequencies for the selected column");

        // create the series for the chart.
        XYChart.Series<String, Number> series = new XYChart.Series<>();

        // add the data in the map to the series
        data.forEach((key, value) -> series.getData().add(new XYChart.Data<>(key, value)));

        // now add the xy-series to the bar chart.
        barChart.getData().add(series);

        // place the bar chart at the center of the display frequency pane
        displayFrequencyPane.setCenter(barChart);
    }
}
