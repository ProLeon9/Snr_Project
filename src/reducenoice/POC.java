package reducenoice;

import org.jfree.chart.ChartPanel;
import org.jfree.data.xy.XYDataset;
import panel.AlignDTWAndReduceNoicePOC;
import tools.CommonFunctions;
import tools.graph.chart.XYLineChart;
import tools.graph.util.ChartUtils;

import java.io.*;

public class POC extends ReduceNoiceToolBox{

    //������ز���
    private int attackSampleNum;
    private int attackSampleStart;
    private int attackCurveStart;
    private int attackCurveNum;
    //DTW��ز���
    private int baseCurveIndex;
    //��ʾ����ʹ��
    private int currentStatus;

    public ChartPanel excuteReduceNoice(AlignDTWAndReduceNoicePOC alignDTWAndReduceNoicePOC, String resultPath, String lastMethod) throws IOException{
        getParametersFromPanel(alignDTWAndReduceNoicePOC);
        BufferedReader curveReader = new BufferedReader(new FileReader(resultPath+"\\"+lastMethod+".txt"));
        BufferedWriter resultWriter = new BufferedWriter(new FileWriter(resultPath+"\\POC.txt"));
        //��ȡbaseCurve
        for(int i = 1; i <= this.baseCurveIndex-1; i++){
            curveReader.readLine();
        }
        double[] base_trace = CommonFunctions.powerCut(CommonFunctions.doubleStringToDoubleArray(curveReader.readLine()), this.attackSampleNum, this.attackSampleStart-1);
        curveReader.close();
        //�޳����õ�Curve
        curveReader = new BufferedReader(new FileReader(resultPath+"\\"+lastMethod+".txt"));
        for(int i = 1; i <= this.attackCurveStart-1; i++){
            curveReader.readLine();
        }
        //ִ�н���
        double[] curve, result, originalTrace = null, newTrace = null;
        for(int i = 1; i <= this.attackCurveNum; i++){
            curve = CommonFunctions.powerCut(CommonFunctions.doubleStringToDoubleArray(curveReader.readLine()), this.attackSampleNum, this.attackSampleStart-1);
            result = excutePOC(base_trace, curve);
            resultWriter.append(CommonFunctions.doubleArrayToString(result));
            if(i == 50){
                originalTrace = curve;
                newTrace = result;
            }
            currentStatus = i;
        }
        curveReader.close();
        resultWriter.close();
        //ִ����ͼ
        int[] xris = new int[this.attackSampleNum];
        for(int i = 0; i <= xris.length-1; i++){
            xris[i] = i+this.attackSampleStart;
        }
        double[][] yris = new double[2][this.attackSampleNum];

        assert originalTrace != null;
        assert newTrace != null;
        System.arraycopy(originalTrace, 0, yris[0], 0, xris.length);
        System.arraycopy(newTrace, 0, yris[1], 0, xris.length);
        XYDataset xyDataset = ChartUtils.createXYSeries(2, xris, yris, new String[]{"original_trace", "new_trace"});
        XYLineChart xyLineChart = new XYLineChart();
        super.resultChartPanel = xyLineChart.getChart("POC Result", "Sample", "Value", xyDataset, true);
        return super.resultChartPanel;
    }

    @Override
    public int getProcessStatus(){
        if(currentStatus < this.attackCurveNum){
            return currentStatus;
        }
        else{
            return -1;
        }
    }

    private void getParametersFromPanel(AlignDTWAndReduceNoicePOC alignDTWAndReduceNoicePOC){
        this.attackCurveStart = Integer.parseInt(alignDTWAndReduceNoicePOC.curve_start_textfiled.getText());
        this.attackCurveNum = Integer.parseInt(alignDTWAndReduceNoicePOC.curve_number_textfiled.getText());
        this.attackSampleStart = Integer.parseInt(alignDTWAndReduceNoicePOC.sample_start_textfiled.getText());
        this.attackSampleNum = Integer.parseInt(alignDTWAndReduceNoicePOC.sample_number_textfiled.getText());
        this.baseCurveIndex = Integer.parseInt(alignDTWAndReduceNoicePOC.base_curve_index_textfield.getText());
    }

    //��tpl������ref���߶���
    private double[] excutePOC(double[] ref, double[] tpl){
        int fft_size = ref.length;
        Complex[] curve1 = new Complex[fft_size];
        Complex[] curve2 = new Complex[fft_size];
        for(int i = 0; i < fft_size; ++i){
            curve1[i] = new Complex(ref[i], 0.0);
            curve2[i] = new Complex(tpl[i], 0.0);
        }
        Complex[] fft_curve1 = FFTInPOC.fft(curve1);
        Complex[] fft_curve2 = FFTInPOC.fft(curve2);
        Complex[] res = new Complex[fft_size];
        for(int i = 0; i < fft_size; ++i){

            double real = fft_curve2[i].re()*fft_curve1[i].re()+fft_curve2[i].im()*fft_curve1[i].im();
            // double imag = fft_curve1[i].im()*fft_curve2[i].re()-fft_curve1[i].re()*fft_curve2[i].im();
            double imag = fft_curve1[i].re()*fft_curve2[i].im()-fft_curve1[i].im()*fft_curve2[i].re();
            Complex x = new Complex(real, imag);
            double sqrt = x.abs();
            res[i] = new Complex(x.re()/sqrt, x.im()/sqrt);
        }
        Complex[] ifft_res = FFTInPOC.ifft(res);
        //double[] arr=new double[fft_size];
        double displacement = 0, max = -100000, tmpi = 0;
        for(int i = 0; i < fft_size; ++i){
            //arr[i]=ifft_res[i].re()/fft_size;
            double cur = ifft_res[i].re()/fft_size;
            if(cur > max){
                max = cur;
                displacement = tmpi;
            }
            tmpi++;
        }
        double[] dis = new double[fft_size];
        for(int i = 0; i < fft_size; ++i){
            dis[i] = 2*3.141592*i*displacement/fft_size;
        }
        Complex[] tmp = new Complex[fft_size];
        for(int i = 0; i < fft_size; ++i){
            Complex x = new Complex(fft_curve2[i].re()*Math.cos(dis[i])-fft_curve2[i].im()*Math.sin(dis[i]), fft_curve2[i].re()*Math.sin(dis[i])+fft_curve2[i].im()*Math.cos(dis[i]));
            tmp[i] = x;
        }
        Complex[] ifft_tmp = new Complex[fft_size];
        ifft_tmp = FFTInPOC.ifft(tmp);
        double[] ret = new double[fft_size];
        for(int i = 0; i < fft_size; ++i){
            ret[i] = ifft_tmp[i].re();//fft_size;
        }
        return ret;
    }
}


class Complex{
    private final double re;   // the real part
    private final double im;   // the imaginary part

    // create a new object with the given real and imaginary parts
    Complex(double real, double imag){
        re = real;
        im = imag;
    }

    // return a string representation of the invoking Complex object
    public String toString(){
        if(im == 0){
            return re+"";
        }
        if(re == 0){
            return im+"i";
        }
        if(im < 0){
            return re+" - "+(-im)+"i";
        }
        return re+" + "+im+"i";
    }

    // return abs/modulus/magnitude and angle/phase/argument
    double abs(){
        return Math.hypot(re, im);
    }  // Math.sqrt(re*re + im*im)

    private double phase(){
        return Math.atan2(im, re);
    }  // between -pi and pi

    // return a new Complex object whose value is (this + b)
    Complex plus(Complex b){
        Complex a = this;             // invoking object
        double real = a.re+b.re;
        double imag = a.im+b.im;
        return new Complex(real, imag);
    }

    // return a new Complex object whose value is (this - b)
    Complex minus(Complex b){
        Complex a = this;
        double real = a.re-b.re;
        double imag = a.im-b.im;
        return new Complex(real, imag);
    }

    // return a new Complex object whose value is (this * b)
    Complex times(Complex b){
        Complex a = this;
        double real = a.re*b.re-a.im*b.im;
        double imag = a.re*b.im+a.im*b.re;
        return new Complex(real, imag);
    }

    // scalar multiplication
    // return a new object whose value is (this * alpha)
    Complex times(double alpha){
        return new Complex(alpha*re, alpha*im);
    }

    // return a new Complex object whose value is the conjugate of this
    Complex conjugate(){
        return new Complex(re, -im);
    }

    // return a new Complex object whose value is the reciprocal of this
    private Complex reciprocal(){
        double scale = re*re+im*im;
        return new Complex(re/scale, -im/scale);
    }

    // return the real or imaginary part
    double re(){
        return re;
    }

    double im(){
        return im;
    }

    // return a / b
    private Complex divides(Complex b){
        Complex a = this;
        return a.times(b.reciprocal());
    }

    // return a new Complex object whose value is the complex exponential of this
    public Complex exp(){
        return new Complex(Math.exp(re)*Math.cos(im), Math.exp(re)*Math.sin(im));
    }

    // return a new Complex object whose value is the complex sine of this
    private Complex sin(){
        return new Complex(Math.sin(re)*Math.cosh(im), Math.cos(re)*Math.sinh(im));
    }

    // return a new Complex object whose value is the complex cosine of this
    private Complex cos(){
        return new Complex(Math.cos(re)*Math.cosh(im), -Math.sin(re)*Math.sinh(im));
    }

    // return a new Complex object whose value is the complex tangent of this
    private Complex tan(){
        return sin().divides(cos());
    }


    // a static version of plus
    public static Complex plus(Complex a, Complex b){
        double real = a.re+b.re;
        double imag = a.im+b.im;
        return new Complex(real, imag);
    }


    // sample client for testing
    public static void main(String[] args){
        Complex a = new Complex(5.0, 6.0);
        Complex b = new Complex(-3.0, 4.0);

        System.out.println("a            = "+a);
        System.out.println("b            = "+b);
        System.out.println("Re(a)        = "+a.re());
        System.out.println("Im(a)        = "+a.im());
        System.out.println("b + a        = "+b.plus(a));
        System.out.println("a - b        = "+a.minus(b));
        System.out.println("a * b        = "+a.times(b));
        System.out.println("b * a        = "+b.times(a));
        System.out.println("a / b        = "+a.divides(b));
        System.out.println("(a / b) * b  = "+a.divides(b).times(b));
        System.out.println("conj(a)      = "+a.conjugate());
        System.out.println("|a|          = "+a.abs());
        System.out.println("tan(a)       = "+a.tan());
    }

}


class FFTInPOC{

    static Complex[] fft(Complex[] x){
        //Modified by Qiu Zhenlong 2015.03.17
          /*      int n,m,j;
        n = _x.length;
		m = 0;
		j = 1;

		//if(n < 1 || n >1024) throw new Exception("�������еĸ���С��1�������1024");//��֤�������еĸ�������1��1024֮��
                //�ж��Ƿ�Ϊ2��ָ���η�
		if(n%2 == 0){
			m = (int)(Math.log(n)/Math.log(2)); //Ϊ2��n�η�
			j = n;
		}
		else{
			for(int i=1;i<(Math.log(n)/Math.log(2)) + 1;i++){
				j *= 2;
				m ++;
				if(j >= n) break;
			}
		}
		Complex[] x = new Complex[j];
		for(int i=0;i<j;i++){
			if(i < n)
				x[i] = new Complex(_x[i].re(),_x[i].im());
			else
				x[i] = new Complex(0,0);
		}
*/
        int N = x.length;
        // base case
        if(N == 1){
            return new Complex[]{x[0]};
        }
        // radix 2 Cooley-Tukey FFTInPOC
        if(N%2 != 0){
            throw new RuntimeException("N is not a power of 2");
        }

        // fft of even terms
        Complex[] even = new Complex[N/2];
        for(int k = 0; k < N/2; k++){
            even[k] = x[2*k];
        }
        Complex[] q = fft(even);

        // fft of odd terms
        Complex[] odd = even;  // reuse the array
        for(int k = 0; k < N/2; k++){
            odd[k] = x[2*k+1];
        }
        Complex[] r = fft(odd);

        // combine
        Complex[] y = new Complex[N];
        for(int k = 0; k < N/2; k++){
            double kth = -2*k*Math.PI/N;
            Complex wk = new Complex(Math.cos(kth), Math.sin(kth));
            y[k] = q[k].plus(wk.times(r[k]));
            y[k+N/2] = q[k].minus(wk.times(r[k]));
        }
        return y;
    }


    // compute the inverse FFTInPOC of x[], assuming its length is a power of 2
    static Complex[] ifft(Complex[] x){
        int N = x.length;
        Complex[] y = new Complex[N];

        // take conjugate
        for(int i = 0; i < N; i++){
            y[i] = x[i].conjugate();
        }

        // compute forward FFTInPOC
        y = fft(y);

        // take conjugate again
        for(int i = 0; i < N; i++){
            y[i] = y[i].conjugate();
        }

        // divide by N
        for(int i = 0; i < N; i++){
            y[i] = y[i].times(1.0/N);
        }

        return y;

    }

    // compute the circular convolution of x and y
    private static Complex[] cconvolve(Complex[] x, Complex[] y){

        // should probably pad x and y with 0s so that they have same length
        // and are powers of 2
        if(x.length != y.length){
            throw new RuntimeException("Dimensions don't agree");
        }

        int N = x.length;

        // compute FFTInPOC of each sequence
        Complex[] a = fft(x);
        Complex[] b = fft(y);

        // point-wise multiply
        Complex[] c = new Complex[N];
        for(int i = 0; i < N; i++){
            c[i] = a[i].times(b[i]);
        }

        // compute inverse FFTInPOC
        return ifft(c);
    }


    // compute the linear convolution of x and y
    private static Complex[] convolve(Complex[] x, Complex[] y){
        Complex ZERO = new Complex(0, 0);

        Complex[] a = new Complex[2*x.length];
        for(int i = 0; i < x.length; i++){
            a[i] = x[i];
        }
        for(int i = x.length; i < 2*x.length; i++){
            a[i] = ZERO;
        }

        Complex[] b = new Complex[2*y.length];
        for(int i = 0; i < y.length; i++){
            b[i] = y[i];
        }
        for(int i = y.length; i < 2*y.length; i++){
            b[i] = ZERO;
        }

        return cconvolve(a, b);
    }

    // display an array of Complex numbers to standard output
    private static void show(Complex[] x, String title){
        System.out.println(title);
        System.out.println("-------------------");
        for(int i = 0; i < x.length; i++){
            System.out.println(x[i]);
        }
        System.out.println();
    }


    public static void main(String[] args){
        int N = Integer.parseInt(args[0]);
        Complex[] x = new Complex[N];

        // original data
        for(int i = 0; i < N; i++){
            x[i] = new Complex(i, 0);
            x[i] = new Complex(-2*Math.random()+1, 0);
        }
        show(x, "x");

        // FFTInPOC of original data
        Complex[] y = fft(x);
        show(y, "y = fft(x)");

        // take inverse FFTInPOC
        Complex[] z = ifft(y);
        show(z, "z = ifft(y)");

        // circular convolution of x with itself
        Complex[] c = cconvolve(x, x);
        show(c, "c = cconvolve(x, x)");

        // linear convolution of x with itself
        Complex[] d = convolve(x, x);
        show(d, "d = convolve(x, x)");
    }

}