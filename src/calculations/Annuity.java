package calculations;

import javafx.beans.property.SimpleDoubleProperty;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.ArrayList;
import java.util.stream.Collectors;
import java.util.stream.DoubleStream;

import static java.util.Arrays.copyOf;

public class Annuity extends RepaymentMethod {

    protected int totalMonths;
    public BigDecimal totalRepaymentSumBig, holidaysInterestBig;
    public double totalRepaymentSum, holidaysPaymentTotal;
    public double totalInterestSum;
    private double interest, holidayInterest;
    public double[] monthlyPayment;
    public  double [] totalRepaymentLeft;
    public double [] monthlyInterestSum;
    public double [] monthlyLoanSum;
    public double [] holidayMonthlyPayment;
    public Annuity(double loanSum, int years, int months, double interest) {
        super(loanSum,years,months,interest);
    }
    public Annuity(double loanSum, int years, int months, double interest, int start,int end, double holidayInterest) {
        super(loanSum,years,months,interest,start,end,holidayInterest);
    }
    public Annuity(){ }
    public void calculate()
    {
        interest = super.getInterest();
        totalMonths = super.getTotalMonths();

        totalRepaymentSumBig = new BigDecimal(((interest * Math.pow(1 + interest, totalMonths)) /
                (Math.pow(1 + interest, totalMonths) - 1))*super.loanSum*totalMonths);

        totalRepaymentSum = totalRepaymentSumBig.setScale(2, RoundingMode.HALF_UP).doubleValue();
        //System.out.println(totalRepaymentSum);
        totalInterestSum = totalRepaymentSum - super.loanSum;
        //System.out.println(totalInterestSum);
        monthlyInterestSum = new double [totalMonths+1];
        totalRepaymentLeft = new double[totalMonths+2];
        totalRepaymentLeft[0] = totalRepaymentSum;
        totalRepaymentLeft[1] = totalRepaymentSum;
        monthlyInterestSum[0] = totalRepaymentLeft[0]*interest;
        monthlyPayment = new double[totalMonths+1];
        monthlyLoanSum = new double[totalMonths+1];
        for (int i = 1; i <= totalMonths; i++) {
            monthlyPayment[i] = totalRepaymentSum / totalMonths;
            monthlyLoanSum[i] = monthlyPayment[i] - totalRepaymentLeft[i]*interest;
            monthlyInterestSum[i] = monthlyPayment[i] - monthlyLoanSum[i];
            totalRepaymentLeft[i] -= monthlyPayment[i];
            totalRepaymentLeft[i+1] = totalRepaymentLeft[i];

        }
    }
    public void holidays(){
        ArrayList<Double> list = DoubleStream.of(monthlyPayment).boxed().collect(Collectors.toCollection(ArrayList::new));

        double interestHol = super.loanSum * super.holidayInterest/100/12;
        double monthlyPaymentUnpaid = 0.0;

        //adding only holiday interest values to the list (no monthly payments or anything)
        for (int i = super.start; i < super.end; i++){
            list.add(i,(interestHol));
            holidaysPaymentTotal += interestHol;
            monthlyPaymentUnpaid += monthlyPayment[i];
        }
        for (int i = 0; i < end;i++){
            monthlyPayment[i] = list.get(i);
        }

        for (int i = end; i < getTotalMonths(); i++){
            monthlyPayment[i] = monthlyPayment[i] + monthlyPaymentUnpaid/(getTotalMonths()-super.end);
        }


        totalRepaymentSum += holidaysPaymentTotal;
        totalInterestSum += holidaysPaymentTotal;
        totalRepaymentLeft[0] = totalRepaymentSum;

        for(int i = 1; i <=getTotalMonths(); i++){
            totalRepaymentLeft[i] = totalRepaymentLeft[i-1]-monthlyPayment[i];
        }
    }
}

