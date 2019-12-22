package calculations;

import javafx.beans.property.DoubleProperty;
import javafx.beans.property.SimpleDoubleProperty;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.property.SimpleStringProperty;

import java.io.Serializable;

public class Table implements Serializable {
    private final SimpleIntegerProperty finalMonths;
    private final SimpleStringProperty finalMonthlyPayment;
    private final SimpleStringProperty finalRepaymentLeft;
    private final SimpleStringProperty finalMonthlyInterest;
    private final SimpleStringProperty finalMonthlyLoan;

    public Table(int finalMonth, String finalMonthlyPayment, String finalRepaymentLeft,String finalMonthlyInterest, String finalMonthlyLoan){
        this.finalMonthlyPayment = new SimpleStringProperty(finalMonthlyPayment);
        this.finalMonths = new SimpleIntegerProperty(finalMonth);
        this.finalRepaymentLeft = new SimpleStringProperty(finalRepaymentLeft);
        this.finalMonthlyInterest = new SimpleStringProperty(finalMonthlyInterest);
        this.finalMonthlyLoan = new SimpleStringProperty(finalMonthlyLoan);
    }
    public String getMonthlyPayment(){
        return finalMonthlyPayment.get();
    }
    public int getMonth(){
        return finalMonths.get();
    }
    public String getRepaymentLeft(){
        return finalRepaymentLeft.get();
    }
    public String getMonthlyInterest(){
        return finalMonthlyInterest.get();
    }
    public String getMonthlyLoan(){
        return finalMonthlyLoan.get();
    }
}
