package paymentHolidays;

import calculations.Annuity;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.DoubleStream;

import static java.util.Arrays.copyOf;

public class AnnuityPaymentHolidays extends Annuity {
    int start, end;
    double interestHolidays;
    double interest;
    int totalHolidays;
    double monthHolidaysPayment;
    double totalHolidaysPayment;
    public AnnuityPaymentHolidays(double loanSum, int years, int months, double interest, int start, int end, double holInterest){
        super(loanSum,years,months,interest);
        this.start = start;
        this.end = end;
        interestHolidays = holInterest;

    }
    public void calculateHolidays(){
        totalHolidays = end-start;
        interest = interestHolidays/100/12;
        monthHolidaysPayment = super.loanSum*interest;
        System.out.println(start);
        System.out.println(end);
        System.out.println(super.totalRepaymentSum);
        System.out.println(monthHolidaysPayment);
        ArrayList <Double> list = DoubleStream.of(super.monthlyPayment).boxed().collect(Collectors.toCollection(ArrayList::new));
        for (int i = start; i < end; i++){
            list.add(i,monthHolidaysPayment);
            totalHolidaysPayment += monthHolidaysPayment;
        }
        super.monthlyPayment = copyOf(super.monthlyPayment,super.monthlyPayment.length+totalHolidays);
        System.out.println(super.monthlyPayment.length +" "+list.size());
        for (int i = 1; i <= list.size();i++){
            System.out.println(monthlyPayment[i] + "  " + list.get(i));
            monthlyPayment[i] = list.get(i);
        }
        //System.out.println(totalHolidaysPayment);
        super.totalRepaymentSum += totalHolidaysPayment;
        //System.out.println(super.totalRepaymentSum);
       // for (int i = end; i < super.totalMonths; i++)
    }
}
