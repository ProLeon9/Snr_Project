package evaluation;

import static tools.CommonFunctions.getMax;
import static tools.CommonFunctions.getMaxIndex;

public class PI{

    private int[][] middle;
    private double[][] originalTrace;
    private double[][] newTrace;
    private int keyIndex;
    private int numOfCurve;
    private int currentNumber;
    private double[] originalIOL;
    private double[] newIOL;


    public PI(double[] originalTrace, double[] newTrace, int traceNumber){
        this.middle = new int[traceNumber][16];
        this.originalTrace = new double[traceNumber][originalTrace.length];
        this.newTrace = new double[traceNumber][newTrace.length];
        this.numOfCurve = traceNumber;
        this.currentNumber = 0;
        this.originalIOL = new double[originalTrace.length];
        this.newIOL = new double[newTrace.length];
    }

    public void excutePI(double[] originalTrace, double[] newTrace, int[] middle, int keyIndex){
        this.keyIndex = keyIndex;
        System.arraycopy(originalTrace, 0, this.originalTrace[currentNumber], 0, originalTrace.length);
        System.arraycopy(newTrace, 0, this.newTrace[currentNumber], 0, newTrace.length);
        System.arraycopy(middle, 0, this.middle[currentNumber], 0, middle.length);
        currentNumber++;
    }

    private void getOriginalPI(){
        // I(L,O) = HO - HOL
        // HO = ∑ PO*log(PO)
        // HOL = ∑ (PL*∑ POL*log(POL))

        double[] Lcounter = new double[256];   // 256种曲线出现的数量
        double[] PL = new double[256];   // 256种曲线对应的概率分布
        double[] Ocounter;   // 曲线根据值划分的种类中每类出现的数量
        double[] PO;   // 曲线根据值划分的种类中每类的概率分布
        double[][] OLcounter = new double[256][];   //  256种曲线类别中根据值划分的种类中每类出现的数量
        double[] POL;   //  256种曲线类别中根据值划分的种类中每类的概率分布
        double[] HO;
        double[] HOL;
        double[] ILO;
        int numOfPoint = originalTrace[0].length;

        for(int i = 0; i < 256; i++){
            Lcounter[i] = 0;
        }
        for(int i = 0; i < numOfCurve; i++){
            Lcounter[middle[i][keyIndex]]++;
        }
        for(int i = 0; i < 256; i++){
            PL[i] = Lcounter[i]/numOfCurve;
        }
        double max, min, sum, aver, s, binwidth;
        int binNum;
        HO = new double[numOfPoint];
        HOL = new double[numOfPoint];
        ILO = new double[numOfPoint];
        for(int i = 0; i < numOfPoint; i++){
            HO[i] = 0;
            HOL[i] = 0;
            ILO[i] = 0;
            sum = 0;
            s = 0;
            max = originalTrace[0][i];
            min = originalTrace[0][i];
            for(int j = 0; j < numOfCurve; j++){
                if(originalTrace[j][i] > max){
                    max = originalTrace[j][i];
                }
                if(originalTrace[j][i] < min){
                    min = originalTrace[j][i];
                }
                sum = sum+originalTrace[j][i];
            }
            aver = sum/numOfCurve;
            for(int j = 0; j < numOfCurve; j++){
                s = s+Math.pow(originalTrace[j][i]-aver, 2);
            }
            s = s/numOfCurve;
            s = Math.sqrt(s);

            double t1 = -1.0/3;
            double t2 = numOfCurve;
            double pow_value = Math.pow(t2, t1);
            binwidth = 3.49*s*pow_value;
            binNum = (int) ((max-min)/binwidth)+1;  //bitNum is the number of devided group

            if(binNum != 1){
                Ocounter = new double[binNum];   //Ocounter is the number of the data of every group
                PO = new double[binNum];         //PO is the probabilities of the HO
                POL = new double[binNum];        //POL is the probabilities of the HOL
                for(int j = 0; j < binNum; j++){
                    Ocounter[j] = 0;
                }
                for(int j = 0; j < 256; j++){
                    OLcounter[j] = new double[binNum];
                    for(int l = 0; l < binNum; l++){
                        OLcounter[j][l] = 0;
                    }
                }
                for(int j = 0; j < numOfCurve; j++){
                    int tmpc = (int) ((originalTrace[j][i]-min)/binwidth);
                    Ocounter[tmpc]++;
                }
                for(int j = 0; j < binNum; j++){
                    PO[j] = Ocounter[j]/numOfCurve;
                    if(PO[j] != 0){
                        HO[i] += (PO[j]*Math.log(PO[j]));
                    }
                }
                if(HO[i] != 0){
                    HO[i] = 0-HO[i];
                }
                for(int j = 0; j < numOfCurve; j++){
                    int tmpc = (int) ((originalTrace[j][i]-min)/binwidth);
                    OLcounter[middle[j][keyIndex]][tmpc]++;
                }
                double tmp_sum;
                for(int j = 0; j < 256; j++){
                    tmp_sum = 0;
                    if(Lcounter[j] != 0){
                        for(int l = 0; l < binNum; l++){
                            POL[l] = OLcounter[j][l]/Lcounter[j];
                            if(POL[l] != 0){
                                tmp_sum += (POL[l]*Math.log(POL[l]));
                            }
                        }
                    }
                    else{
                        tmp_sum = 0;
                    }
                    HOL[i] += PL[j]*tmp_sum;
                }
                if(HOL[i] != 0){
                    HOL[i] = 0-HOL[i];
                }
                ILO[i] = Math.abs(HO[i]-HOL[i]);
            }
            else{
                ILO[i] = 0;
            }
        }
        originalIOL = ILO;
    }

    private void getNewPI(){
        // I(L,O) = HO - HOL
        // HO = ∑ PO*log(PO)
        // HOL = ∑ (PL*∑ POL*log(POL))

        double[] Lcounter = new double[256];   // 256种曲线出现的数量
        double[] PL = new double[256];   // 256种曲线对应的概率分布
        double[] Ocounter;   // 曲线根据值划分的种类中每类出现的数量
        double[] PO;   // 曲线根据值划分的种类中每类的概率分布
        double[][] OLcounter = new double[256][];   //  256种曲线类别中根据值划分的种类中每类出现的数量
        double[] POL;   //  256种曲线类别中根据值划分的种类中每类的概率分布
        double[] HO;
        double[] HOL;
        double[] ILO;
        int numOfPoint = newTrace[0].length;

        for(int i = 0; i < 256; i++){
            Lcounter[i] = 0;
        }
        for(int i = 0; i < numOfCurve; i++){
            Lcounter[middle[i][keyIndex]]++;
        }
        for(int i = 0; i < 256; i++){
            PL[i] = Lcounter[i]/numOfCurve;
        }
        double max, min, sum, aver, s, binwidth;
        int binNum;
        HO = new double[numOfPoint];
        HOL = new double[numOfPoint];
        ILO = new double[numOfPoint];
        for(int i = 0; i < numOfPoint; i++){
            HO[i] = 0;
            HOL[i] = 0;
            ILO[i] = 0;
            sum = 0;
            s = 0;
            max = newTrace[0][i];
            min = newTrace[0][i];
            for(int j = 0; j < numOfCurve; j++){
                if(newTrace[j][i] > max){
                    max = newTrace[j][i];
                }
                if(newTrace[j][i] < min){
                    min = newTrace[j][i];
                }
                sum = sum+newTrace[j][i];
            }
            aver = sum/numOfCurve;
            for(int j = 0; j < numOfCurve; j++){
                s = s+Math.pow(newTrace[j][i]-aver, 2);
            }
            s = s/numOfCurve;
            s = Math.sqrt(s);

            double t1 = -1.0/3;
            double t2 = numOfCurve;
            double pow_value = Math.pow(t2, t1);
            binwidth = 3.49*s*pow_value;
            binNum = (int) ((max-min)/binwidth)+1;  //bitNum is the number of devided group

            if(binNum != 1){
                Ocounter = new double[binNum];   //Ocounter is the number of the data of every group
                PO = new double[binNum];         //PO is the probabilities of the HO
                POL = new double[binNum];        //POL is the probabilities of the HOL
                for(int j = 0; j < binNum; j++){
                    Ocounter[j] = 0;
                }
                for(int j = 0; j < 256; j++){
                    OLcounter[j] = new double[binNum];
                    for(int l = 0; l < binNum; l++){
                        OLcounter[j][l] = 0;
                    }
                }
                for(int j = 0; j < numOfCurve; j++){
                    int tmpc = (int) ((newTrace[j][i]-min)/binwidth);
                    Ocounter[tmpc]++;
                }
                for(int j = 0; j < binNum; j++){
                    PO[j] = Ocounter[j]/numOfCurve;
                    if(PO[j] != 0){
                        HO[i] += (PO[j]*Math.log(PO[j]));
                    }
                }
                if(HO[i] != 0){
                    HO[i] = 0-HO[i];
                }
                for(int j = 0; j < numOfCurve; j++){
                    int tmpc = (int) ((newTrace[j][i]-min)/binwidth);
                    OLcounter[middle[j][keyIndex]][tmpc]++;
                }
                double tmp_sum;
                for(int j = 0; j < 256; j++){
                    tmp_sum = 0;
                    if(Lcounter[j] != 0){
                        for(int l = 0; l < binNum; l++){
                            POL[l] = OLcounter[j][l]/Lcounter[j];
                            if(POL[l] != 0){
                                tmp_sum += (POL[l]*Math.log(POL[l]));
                            }
                        }
                    }
                    else{
                        tmp_sum = 0;
                    }
                    HOL[i] += PL[j]*tmp_sum;
                }
                if(HOL[i] != 0){
                    HOL[i] = 0-HOL[i];
                }
                ILO[i] = Math.abs(HO[i]-HOL[i]);
            }
            else{
                ILO[i] = 0;
            }
        }
        newIOL = ILO;
    }

    public double[] returnPI(){
        getOriginalPI();
        getNewPI();

        double[] result = new double[2];
        if(originalTrace[0].length == newTrace[0].length){
            double[] PIInterval = new double[originalTrace.length];
            PIInterval = getTwoArrayInterval(originalIOL, newIOL);
            int index = getMaxIndex(PIInterval);
            result[0] = originalIOL[index];
            result[1] = newIOL[index];
            return result;
        }
        else{
            result=  new double[2];
            result[0] = getMax(originalIOL);
            result[1] = getMax(newIOL);
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

    public double[] getBeforePI(){
        return this.originalIOL;
    }

    public double[] getAfterPI(){
        return this.newIOL;
    }

}
