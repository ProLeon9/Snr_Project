package evaluation;

import static tools.CommonFunctions.getMax;
import static tools.CommonFunctions.getMaxIndex;
import static tools.CommonFunctions.getVariance;

public class SNR{
    private double[][] EXOriginal;
    private double[][] EX2Original;
    private double[][] EXNew;
    private double[][] EX2New;
    private double[] SNRBefore;
    private double[] SNRAfter;
    private double[] SNRInterval;
    private int[] number;


    public SNR(double[] origianlTrace, double[] newTrace){
        EXOriginal = new double[256][origianlTrace.length];
        EX2Original = new double[256][origianlTrace.length];
        EXNew = new double[256][newTrace.length];
        EX2New = new double[256][newTrace.length];
        SNRBefore = new double[origianlTrace.length];
        SNRAfter = new double[newTrace.length];
        number = new int[256];
        SNRInterval = new double[origianlTrace.length];
    }

    //s^2= E(x^2) -(E(x))^2
    public void excuteSNR(double[] originalTrace, double[] newTrace, int[] plain, int index){
        int type = plain[index];
        for(int i = 0; i <= EXOriginal[type].length-1; i++){
            EXOriginal[type][i] += originalTrace[i];
            EX2Original[type][i] += (originalTrace[i]*originalTrace[i]);
        }
        for(int i = 0; i <= EXNew[type].length-1; i++){
            EXNew[type][i] += newTrace[i];
            EX2New[type][i] += (newTrace[i]*newTrace[i]);
        }
        number[type]++;
    }

    public double[] getMaxSNR(){
        for(int i = 0; i <= 255; i++){
            for(int j = 0; j <= EXOriginal[i].length-1; j++){
                EXOriginal[i][j] /= number[i];
                EX2Original[i][j] /= number[i];
            }
        }
        for(int i = 0; i <= 255; i++){
            for(int j = 0; j <= EXNew[i].length-1; j++){
                EXNew[i][j] /= number[i];
                EX2New[i][j] /= number[i];
            }
        }
        for(int i = 0; i <= SNRBefore.length-1; i++){
            double[] temp = new double[256];
            for(int k = 0; k <= 255; k++){
                temp[k] = EXOriginal[k][i];
            }
            double signal = getVariance(temp);
            double varSum = 0;
            for(int k=0; k<=255; k++){
                varSum += (EX2Original[k][i]-EXOriginal[k][i]*EXOriginal[k][i]);
            }
            double noice = varSum/256;
            double tempResult = signal/noice;
            if(Double.isNaN(tempResult))
                SNRBefore[i] = SNRBefore[i-1];
            else
                SNRBefore[i] = Math.abs(tempResult);
        }

        for(int i = 0; i <= SNRAfter.length-1; i++){
            double[] temp = new double[256];
            for(int k = 0; k <= 255; k++){
                temp[k] = EXNew[k][i];
            }
            double signal = getVariance(temp);
            double varSum = 0;
            for(int k=0; k<=255; k++){
                varSum += (EX2New[k][i]-EXNew[k][i]*EXNew[k][i]);
            }
            double noice = varSum/256;
            double tempResult = signal/noice;
            if(Double.isNaN(tempResult))
                SNRAfter[i] = SNRAfter[i-1];
            else
                SNRAfter[i] = Math.abs(tempResult);
        }

        double[] result = new double[2];
        if(SNRAfter.length == SNRBefore.length){
            SNRInterval = getTwoArrayInterval(SNRBefore, SNRAfter);
            int index = getMaxIndex(SNRInterval);
            result[0] = SNRBefore[index];
            result[1] = SNRAfter[index];
            return result;
        }
        else{
            result=  new double[2];
            result[0] = getMax(SNRBefore);
            result[1] = getMax(SNRAfter);
        }

        return result;
    }

    private double[] getTwoArrayInterval(double[] oriTrace, double[] newTrace){
        double[] result = new double[oriTrace.length];
        for(int i=0; i<=oriTrace.length-1; i++){
            result[i] = newTrace[i] - oriTrace[i];
        }
        return result;
    }

    public double[] getBeforeSNR(){
        return this.SNRBefore;
    }

    public double[] getAfterSNR(){
        return this.SNRAfter;
    }

}
