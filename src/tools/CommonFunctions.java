package tools;

import org.jfree.chart.JFreeChart;
import org.jfree.chart.axis.CategoryAxis;
import org.jfree.chart.axis.ValueAxis;
import org.jfree.chart.plot.CategoryPlot;
import org.jfree.chart.plot.XYPlot;
import org.jfree.chart.title.TextTitle;

import java.awt.*;

public class CommonFunctions{

    public static int[] hexStringTOIntArray(String hexString){
        String[] plain_s = hexString.split(" ");
        int[] result = new int[plain_s.length];
        for(int i = 0; i < result.length; i++){
            result[i] = Integer.parseInt(plain_s[i], 16);
        }
        return result;
    }

    public static double[] doubleStringToDoubleArray(String doubleString){
        String[] power_s = doubleString.split(" ");
        double result[] = new double[power_s.length];
        for(int i = 0; i < power_s.length; i++){
            result[i] = Double.parseDouble(power_s[i]);
        }
        return result;
    }

    public static String doubleArrayToString(double[] trace){
        StringBuilder content = new StringBuilder("");
        for(int i = 0; i < trace.length; i++){
            content.append(trace[i]).append(" ");
        }
        content.append("\n");
        return content.toString();
    }

    public static double[] powerCut(double[] tmp, int attack_points, int begin){
        double dou_tmp[] = new double[attack_points];
        for(int i = 0; i < tmp.length; i++){
            if(i >= begin && i < attack_points+begin){
                dou_tmp[i-begin] = tmp[i];
            }
        }
        return dou_tmp;
    }

    public static void configBarChartFont(JFreeChart chart){
        // 配置字体
        Font xfont = new Font("宋体", Font.PLAIN, 16);// X轴
        Font yfont = new Font("宋体", Font.PLAIN, 16);// Y轴
        Font kfont = new Font("宋体", Font.PLAIN, 12);// 底部
        Font titleFont = new Font("隶书", Font.BOLD, 23); // 图片标题
        CategoryPlot plot = chart.getCategoryPlot();// 图形的绘制结构对象

        // 图片标题
        chart.setTitle(new TextTitle(chart.getTitle().getText(), titleFont));

        // 底部
        //chart.getLegend().setItemFont(kfont);

        // X 轴
        CategoryAxis domainAxis = plot.getDomainAxis();
        domainAxis.setLabelFont(xfont);// 轴标题
        domainAxis.setTickLabelFont(xfont);// 轴数值
        //domainAxis.setTickLabelPaint(Color.BLUE) ; // 字体颜色
        //domainAxis.setCategoryLabelPositions(CategoryLabelPositions.UP_45); // 横轴上的label斜显示

        // Y 轴
        ValueAxis rangeAxis = plot.getRangeAxis();
        rangeAxis.setLabelFont(yfont);
        //rangeAxis.setLabelPaint(Color.BLUE) ; // 字体颜色
        rangeAxis.setTickLabelFont(yfont);
    }

    public static void configXYlineChartFont(JFreeChart chart){
        // 配置字体
        Font xfont = new Font("宋体", Font.PLAIN, 16);// X轴
        Font yfont = new Font("宋体", Font.PLAIN, 16);// Y轴
        Font kfont = new Font("宋体", Font.PLAIN, 12);// 底部
        Font titleFont = new Font("隶书", Font.BOLD, 23); // 图片标题
        //CategoryPlot plot = chart.getCategoryPlot();// 图形的绘制结构对象
        XYPlot plot = chart.getXYPlot();
        // 图片标题
        chart.setTitle(new TextTitle(chart.getTitle().getText(), titleFont));

        // 底部
        //chart.getLegend().setItemFont(kfont);

        // X 轴
        //CategoryAxis domainAxis = plot.getDomainAxis();
        ValueAxis domainAxis = plot.getDomainAxis();
        domainAxis.setLabelFont(xfont);// 轴标题
        domainAxis.setTickLabelFont(xfont);// 轴数值
        //domainAxis.setTickLabelPaint(Color.BLUE) ; // 字体颜色
        //domainAxis.setCategoryLabelPositions(CategoryLabelPositions.UP_45); // 横轴上的label斜显示

        // Y 轴
        ValueAxis rangeAxis = plot.getRangeAxis();
        rangeAxis.setLabelFont(yfont);
        //rangeAxis.setLabelPaint(Color.BLUE) ; // 字体颜色
        rangeAxis.setTickLabelFont(yfont);
    }

    //求方差、均值、和的函数
    private static double getSum(double[] inputData){
        if(inputData == null || inputData.length == 0){
            return -1;
        }
        double sum = 0;
        for(double anInputData : inputData){
            sum = sum+anInputData;
        }
        return sum;
    }

    public static double getAverage(double[] inputData){
        if(inputData == null || inputData.length == 0){
            return -1;
        }
        int len = inputData.length;
        double result;
        result = getSum(inputData)/len;

        return result;
    }

    public static double getVariance(double[] inputData){
        int count = getCount(inputData);
        double sqrsum = getSquareSum(inputData);
        double average = getAverage(inputData);
        double result;
        result = (sqrsum-count*average*average)/count;
        return result;
    }

    private static int getCount(double[] inputData){
        if(inputData == null){
            return -1;
        }
        return inputData.length;
    }

    private static double getSquareSum(double[] inputData){
        if(inputData == null || inputData.length == 0){
            return -1;
        }
        double sqrsum = 0.0;
        for(double anInputData : inputData){
            sqrsum = sqrsum+anInputData*anInputData;
        }
        return sqrsum;
    }

    //标准差σ=sqrt(s^2)
    public static double getStandardDiviation(double[] x) {
        int m=x.length;
        double sum=0;
        for(int i=0;i<m;i++){//求和
            sum+=x[i];
        }
        double dAve=sum/m;//求平均值
        double dVar=0;
        for(int i=0;i<m;i++){//求方差
            dVar+=(x[i]-dAve)*(x[i]-dAve);
        }
        return Math.sqrt(dVar/m);
    }
}
