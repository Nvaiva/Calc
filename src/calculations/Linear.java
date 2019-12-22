package calculations;

import java.util.ArrayList;
import java.util.stream.Collectors;
import java.util.stream.DoubleStream;

public class Linear extends RepaymentMethod{

    protected int totalMonths;
    public double totalRepaymentSum;
    public double totalInterestSum, holidaysPaymentTotal;
    private double interest;
    public double[] monthlyPayment;
    public double [] totalRepaymentLeft;
    public double [] monthlyInterestSum;
    public double [] monthlyLoanSum;

    public Linear(double loanSum, int years, int months, double interest) {
        super(loanSum,years,months,interest);
    }
    public Linear(double loanSum, int years, int months, double interest, int start,int end, double holidayInterest) {
        super(loanSum,years,months,interest,start,end,holidayInterest);
    }
    public void calculate(){
        interest = super.getInterest();
        totalMonths = super.getTotalMonths();

        totalInterestSum = super.loanSum * interest * totalMonths;
        System.out.println(totalInterestSum);

        monthlyPayment = new double[totalMonths+1];
        for (int i = 1; i <= totalMonths; i++){
            monthlyPayment[i] = super.loanSum / totalMonths;
        }
        double firstMonthInterest = totalInterestSum / totalMonths;
        double scale = firstMonthInterest / totalMonths;


        monthlyInterestSum = new double [totalMonths+1];
        totalRepaymentLeft = new double[totalMonths+2];
        monthlyLoanSum = new double[totalMonths+1];
        for (int i = 1; i <= totalMonths; i++){
            monthlyPayment[i] += (totalMonths + 1 - i)*scale;
            totalRepaymentSum += monthlyPayment[i];
            monthlyInterestSum[i] = (totalMonths + 1 - i)*scale;
            monthlyLoanSum[i] = monthlyPayment[i] - monthlyInterestSum[i];
            //System.out.println(monthlyPayment[i]);
        }
        totalRepaymentLeft[0] = totalRepaymentSum;
        totalRepaymentLeft[1] = totalRepaymentSum;
        for (int i = 1; i <= totalMonths; i++){
            totalRepaymentLeft[i] -= monthlyPayment[i];
            totalRepaymentLeft[i+1] = totalRepaymentLeft[i];
        }
        totalInterestSum = totalRepaymentSum - super.loanSum;

    }
    public void holidays() {
        ArrayList<Double> list = DoubleStream.of(monthlyPayment).boxed().collect(Collectors.toCollection(ArrayList::new));

        double interestHol = super.loanSum * super.holidayInterest / 100 / 12;
        double monthlyPaymentUnpaid = 0.0;
        //adding only holiday interest values to the list (no monthly payments or anything)
        for (int i = super.start; i < super.end; i++) {
            list.add(i, (interestHol));
            holidaysPaymentTotal += interestHol;
            monthlyPaymentUnpaid += monthlyPayment[i];
        }
        for (int i = 0; i < end; i++) {
            monthlyPayment[i] = list.get(i);
            System.out.println(monthlyPayment[i] + "  " + list.get(i));
        }

        for (int i = end; i < monthlyPayment.length; i++) {
            monthlyPayment[i] = monthlyPayment[i] + monthlyPaymentUnpaid / (monthlyPayment.length - super.end);
           // monthlyInterestSum[i] = monthlyInterestSum[i] + monthlyPaymentUnpaid / (monthlyPayment.length - super.end);
        }

        totalRepaymentSum += holidaysPaymentTotal;
        totalInterestSum += holidaysPaymentTotal;
        totalRepaymentLeft[0] = totalRepaymentSum;

        for(int i = 1; i <=getTotalMonths(); i++){
            totalRepaymentLeft[i] = totalRepaymentLeft[i-1]-monthlyPayment[i];
        }
    }
}
