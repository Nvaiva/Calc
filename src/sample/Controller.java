package sample;

import calculations.Annuity;
import calculations.Linear;
import calculations.RepaymentMethod;
import calculations.Table;
import com.jfoenix.controls.JFXCheckBox;
import com.jfoenix.controls.JFXTextField;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Group;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.chart.CategoryAxis;
import javafx.scene.chart.LineChart;
import javafx.scene.chart.NumberAxis;
import javafx.scene.chart.XYChart;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;
import javafx.stage.FileChooser;
import javafx.stage.Popup;
import javafx.stage.Stage;
import paymentHolidays.AnnuityPaymentHolidays;

import java.io.*;
import java.util.Arrays;
import java.util.DoubleSummaryStatistics;
import java.util.IntSummaryStatistics;

public class Controller {

    public double loan,loanRate;
    public int years,months;
    boolean linearPayment, annuityPayment;
    //payment holidays
    public int holidaysStart, holidaysEnd;
    public double holidaysInterest;

    @FXML
    private JFXTextField loanAmount,loanTermYears,loanTermMonths,interestRate,totalPaymentSum,totalInterestRate;
    @FXML
    private JFXCheckBox linear, annuity;
    @FXML
    private LineChart<?, ?> chart;
    @FXML
    private NumberAxis x,y;
    @FXML
    private TableView <Table> tableView ;
    @FXML
    private JFXTextField paymentHolidaysStart, paymentHolidaysEnd, paymentHolidaysInterest;


    @FXML
    void graphButtonPressed(MouseEvent event) throws IOException {
        Group root = new Group(chart);
        Stage stage = new Stage();
        stage.setTitle("Repayment Graph");
        stage.setScene(new Scene(root));
        stage.show();
    }
    @FXML
   void tableButtonPressed(MouseEvent event) throws IOException {
        Stage stage = new Stage();
        stage.setTitle("Repayment Table");
        stage.setScene(new Scene(tableView,420,600));
        stage.show();

    }
    @FXML
    void calculateIsPressed(MouseEvent event) {
        if(loanAmount.getText().matches("^[0-9]+(\\.[0-9]{0,2})?$")){
            String loanSum=loanAmount.getText();
            loan = Double.parseDouble(loanSum);
            loanAmount.setStyle("-fx-background-color: #716B6B");
        }
        else{
            loanAmount.setStyle("-fx-background-color:#6e4141");
        }
        if(loanTermYears.getText().matches("^[0-9]{1,2}?$")){
            String loanTermY = loanTermYears.getText();
            years = Integer.parseInt(loanTermY);
            loanTermYears.setStyle("-fx-background-color: #716B6B");
        }
        else{
            loanTermYears.setStyle("-fx-background-color:#6e4141");
        }
        if(loanTermMonths.getText().matches("^[0-9]{1,2}?$") && Integer.parseInt(loanTermMonths.getText())<12){
            String loanTermM = loanTermMonths.getText();
            months = Integer.parseInt(loanTermM);
            loanTermMonths.setStyle("-fx-background-color: #716B6B");
        }
        else{
            loanTermMonths.setStyle("-fx-background-color:#6e4141");
        }
        if(interestRate.getText().matches("^-?[0-9]+(\\.[0-9]{0,2})?$") && Double.parseDouble(interestRate.getText())>0.0){
            String interest = interestRate.getText();
            loanRate = Double.parseDouble(interest);
            interestRate.setStyle("-fx-background-color: #716B6B");
        }
        else{
            interestRate.setStyle("-fx-background-color:#6e4141");
        }
        if (linear.isSelected()){
            linear.setStyle("-fx-background-color: #716B6B");
            annuity.setStyle("-fx-background-color: #716B6B");
            linearPayment = true;
            annuityPayment = false;
        }
        else if(annuity.isSelected()){
            linear.setStyle("-fx-background-color: #716B6B");
            annuity.setStyle("-fx-background-color: #716B6B");
            linearPayment = false;
            annuityPayment = true;
        }
        else{
            linear.setStyle("-fx-background-color:#6e4141");
            annuity.setStyle("-fx-background-color:#6e4141");
        }

        if(annuityPayment && loan != 0.0 && (years*12+months) != 0 && loanRate>0){
            Annuity calculateLoan = new Annuity(loan,years,months,loanRate);
            calculateLoan.calculate();
            totalPaymentSum.setText(String.valueOf(calculateLoan.totalRepaymentSum));
            totalInterestRate.setText(String.valueOf(String.format("%.2f",calculateLoan.totalInterestSum)));
            createChart(calculateLoan);
            createTable(calculateLoan);
        }
        if(linearPayment && loan != 0.0 && (years*12+months) != 0 && loanRate >0){
            Linear calculateLoan = new Linear(loan,years,months,loanRate);
            calculateLoan.calculate();
            totalPaymentSum.setText(String.valueOf(String.format("%.2f",calculateLoan.totalRepaymentSum)));
            totalInterestRate.setText(String.valueOf(String.format("%.2f",calculateLoan.totalInterestSum)));
            createChart(calculateLoan);
            createTable(calculateLoan);
        }
    }
    @FXML
    void paymentHolidaysCalculateIsPressed(MouseEvent event) {
        if(loan > 0.0 && loanRate > 0.0 && (annuityPayment==true || linearPayment == true) && years*12+months != 0){
            boolean first = false,second = false,third = false; //just to see if all textfields were complited
            if(paymentHolidaysStart.getText().matches("^[0-9]{1,4}?$") && Integer.parseInt(paymentHolidaysStart.getText())>0 &&
                    Integer.parseInt(paymentHolidaysStart.getText())< years*12+months){
                String start = paymentHolidaysStart.getText();
                holidaysStart = Integer.parseInt(start);
                paymentHolidaysStart.setStyle("-fx-background-color: #716B6B");
                first=true;
            }
            else{
                paymentHolidaysStart.setStyle("-fx-background-color:#6e4141");
            }
            if(paymentHolidaysEnd.getText().matches("^[0-9]{1,4}?$") && Integer.parseInt(paymentHolidaysEnd.getText())>0 &&
                    Integer.parseInt(paymentHolidaysEnd.getText())< years*12+months){
                String end = paymentHolidaysEnd.getText();
                holidaysEnd = Integer.parseInt(end);
                paymentHolidaysEnd.setStyle("-fx-background-color: #716B6B");
                second = true;
            }
            else{
                paymentHolidaysEnd.setStyle("-fx-background-color:#6e4141");
            }
            if(paymentHolidaysInterest.getText().matches("^-?[0-9]+(\\.[0-9]{0,2})?$") && Double.parseDouble(paymentHolidaysInterest.getText())>0){
                String interest = paymentHolidaysInterest.getText();
                holidaysInterest = Double.parseDouble(interest);
                paymentHolidaysInterest.setStyle("-fx-background-color: #716B6B");
                third = true;
            }
            else{
                paymentHolidaysInterest.setStyle("-fx-background-color:#6e4141");
            }
            if(first && second && third){
                if(annuityPayment){
                    Annuity calculateLoan = new Annuity(loan,years,months,loanRate,holidaysStart,holidaysEnd,holidaysInterest);
                    calculateLoan.calculate();
                    calculateLoan.holidays();
                    totalPaymentSum.setText(String.valueOf(String.format("%.2f",calculateLoan.totalRepaymentSum)));
                    totalInterestRate.setText(String.valueOf(String.format("%.2f",calculateLoan.totalInterestSum)));
                    createChart(calculateLoan);
                    createTable(calculateLoan);
                }
                if(linearPayment){
                    Linear calculateLoan = new Linear(loan,years,months,loanRate,holidaysStart,holidaysEnd,holidaysInterest);
                    calculateLoan.calculate();
                    calculateLoan.holidays();
                    totalPaymentSum.setText(String.valueOf(String.format("%.2f",calculateLoan.totalRepaymentSum)));
                    totalInterestRate.setText(String.valueOf(String.format("%.2f",calculateLoan.totalInterestSum)));
                    createChart(calculateLoan);
                    createTable(calculateLoan);
                }

            }
        }
    }
    @FXML
    void uncheckAnnuity(MouseEvent event) {
        annuity.setSelected(false);
    }

    @FXML
    void uncheckLinear(MouseEvent event) {
        linear.setSelected(false);
    }

    public void createChart(Annuity calculateLoan){
        DoubleSummaryStatistics stat = Arrays.stream(calculateLoan.monthlyPayment).summaryStatistics();
        if(calculateLoan.getTotalMonths()<=50){
            x = new NumberAxis(0,calculateLoan.getTotalMonths()+1,1);
            y = new NumberAxis((int)stat.getMin()-10,(int)stat.getMax()+10,1);
        }

        else if(calculateLoan.getTotalMonths()<=240){
            x = new NumberAxis(0,calculateLoan.getTotalMonths()+1,24);
            y = new NumberAxis((int)stat.getMin()-10,(int)stat.getMax()+20,24);
        }
        else{
            x = new NumberAxis(0,calculateLoan.getTotalMonths()+1,72);
            y = new NumberAxis(stat.getMin()-20,(int)stat.getMax()+20,72);
        }
        chart = new LineChart(x,y);
        XYChart.Series series = new XYChart.Series();

        for (int i = 1; i <= calculateLoan.getTotalMonths(); i++){
            series.getData().add(new XYChart.Data(i,calculateLoan.monthlyPayment[i]));
        }
        chart.getData().addAll(series);
    }
    public void createChart(Linear calculateLoan){
        DoubleSummaryStatistics stat = Arrays.stream(calculateLoan.monthlyPayment).summaryStatistics();
        if(calculateLoan.getTotalMonths()<=50){
            x = new NumberAxis(0,calculateLoan.getTotalMonths()+1,1);
            y = new NumberAxis((int)stat.getMin()-10,
                    (int)stat.getMax()+10,1);
        }

        else if(calculateLoan.getTotalMonths()<=240){
            x = new NumberAxis(0,calculateLoan.getTotalMonths()+1,24);
            y = new NumberAxis((int)stat.getMin()-10,
                    (int)stat.getMax()+10,24);
        }
        else{
            x = new NumberAxis(0,calculateLoan.getTotalMonths()+1,72);
            y = new NumberAxis((int)stat.getMin()-10,
                    (int)stat.getMax()+10,72);
        }
        chart = new LineChart(x,y);
        XYChart.Series series = new XYChart.Series();

        for (int i = 1; i <= calculateLoan.getTotalMonths(); i++){
            series.getData().add(new XYChart.Data(i,calculateLoan.monthlyPayment[i]));
        }
        chart.getData().addAll(series);
    }
    public void createTable(Annuity calculateLoan){
      table();
        final ObservableList<Table> data = FXCollections.observableArrayList();
        for (int i = 1; i <= calculateLoan.getTotalMonths(); i++){
            data.add(new Table(i,(String.valueOf(String.format("%.2f",calculateLoan.monthlyPayment[i]))),
                    (String.valueOf(String.format("%.2f",calculateLoan.totalRepaymentLeft[i-1]))),
                    (String.valueOf(String.format("%.2f",calculateLoan.monthlyInterestSum[i-1]))),
                    (String.valueOf(String.format("%.2f",calculateLoan.monthlyLoanSum[i])))));
        }
        tableView.setItems(data);
    }
    public void createTable(Linear calculateLoan){
       table();
        final ObservableList<Table> data = FXCollections.observableArrayList();
        for (int i = 1; i <= calculateLoan.getTotalMonths(); i++){
            data.add(new Table(i,(String.valueOf(String.format("%.2f",calculateLoan.monthlyPayment[i]))),
                    (String.valueOf(String.format("%.2f",calculateLoan.totalRepaymentLeft[i-1]))),
                    (String.valueOf(String.format("%.2f",calculateLoan.monthlyInterestSum[i]))),
                    (String.valueOf(String.format("%.2f",calculateLoan.monthlyLoanSum[i])))));
        }
        tableView.setItems(data);
    }
    public void table(){
        tableView = new TableView();

        TableColumn month = new TableColumn("Month");
        TableColumn monthlyPayment = new TableColumn("Monthly Payment");
        TableColumn repaymentLeft = new TableColumn("Left Repayment");
        TableColumn monthlyInterest = new TableColumn("Interest");
        TableColumn monthlyLoan = new TableColumn("Credit");
        tableView.getColumns().addAll(month,monthlyPayment,repaymentLeft,monthlyInterest,monthlyLoan);

        month.setCellValueFactory(new PropertyValueFactory<Table,String>("month"));
        monthlyPayment.setCellValueFactory(new PropertyValueFactory<Table,String>("monthlyPayment"));
        repaymentLeft.setCellValueFactory(new PropertyValueFactory<Table,String>("repaymentLeft"));
        monthlyInterest.setCellValueFactory(new PropertyValueFactory<Table,String>("monthlyInterest"));
        monthlyLoan.setCellValueFactory(new PropertyValueFactory<Table,String>("monthlyLoan"));

    }
    @FXML
    void saveToFilePressed(MouseEvent event) {
        try (BufferedWriter bw = new BufferedWriter(new FileWriter("users.txt", false))) {
            bw.write(String.format("%-7s%-20s%-20s%-20s%-20s","Month","Monthly payment", "Repayment left", "Monthly loan", "Monthly Interest"));
            bw.newLine();
            for (int i = 0; i < tableView.getItems().size(); i++){
                bw.write(String.format("%-7s%-20s%-20s%-20s%-20s",tableView.getItems().get(i).getMonth(),
                        tableView.getItems().get(i).getMonthlyPayment().toString(),
                        tableView.getItems().get(i).getRepaymentLeft().toString(),
                        tableView.getItems().get(i).getMonthlyLoan().toString(),
                        tableView.getItems().get(i).getMonthlyInterest().toString()));
                bw.newLine();
            }

        } catch (IOException e) {
            e.printStackTrace();

        }
    }
}

