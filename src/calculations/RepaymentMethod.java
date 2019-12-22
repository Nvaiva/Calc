package calculations;

import java.util.ArrayList;
import java.util.stream.Collectors;
import java.util.stream.DoubleStream;

public class RepaymentMethod {
    protected double loanSum,interestRate,holidayInterest;
    protected int years, months;
    protected int start,end;
    public RepaymentMethod(double loanSum, int years, int months, double interest) {

        this.loanSum = loanSum;
        this.years = years;
        this.months = months;
        this.interestRate = interest;

    }
    public RepaymentMethod(double loanSum, int years, int months, double interest,int start,int end,double holidayInterest)
    {
        this.loanSum = loanSum;
        this.years = years;
        this.months = months;
        this.interestRate = interest;
        this.start = start;
        this.end = end;
        this.holidayInterest = holidayInterest;
    }
    public RepaymentMethod (){}
    public int getTotalMonths(){
        return years*12+months;
    }
    public double getInterest(){
        return interestRate/12/100;
    }
    public int getHolidaysLength(){
        return end-start;
    }
}
