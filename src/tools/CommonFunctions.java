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
        // ��������
        Font xfont = new Font("����", Font.PLAIN, 16);// X��
        Font yfont = new Font("����", Font.PLAIN, 16);// Y��
        Font kfont = new Font("����", Font.PLAIN, 12);// �ײ�
        Font titleFont = new Font("����", Font.BOLD, 23); // ͼƬ����
        CategoryPlot plot = chart.getCategoryPlot();// ͼ�εĻ��ƽṹ����

        // ͼƬ����
        chart.setTitle(new TextTitle(chart.getTitle().getText(), titleFont));

        // �ײ�
        //chart.getLegend().setItemFont(kfont);

        // X ��
        CategoryAxis domainAxis = plot.getDomainAxis();
        domainAxis.setLabelFont(xfont);// �����
        domainAxis.setTickLabelFont(xfont);// ����ֵ
        //domainAxis.setTickLabelPaint(Color.BLUE) ; // ������ɫ
        //domainAxis.setCategoryLabelPositions(CategoryLabelPositions.UP_45); // �����ϵ�labelб��ʾ

        // Y ��
        ValueAxis rangeAxis = plot.getRangeAxis();
        rangeAxis.setLabelFont(yfont);
        //rangeAxis.setLabelPaint(Color.BLUE) ; // ������ɫ
        rangeAxis.setTickLabelFont(yfont);
    }

    public static void configXYlineChartFont(JFreeChart chart){
        // ��������
        Font xfont = new Font("����", Font.PLAIN, 16);// X��
        Font yfont = new Font("����", Font.PLAIN, 16);// Y��
        Font kfont = new Font("����", Font.PLAIN, 12);// �ײ�
        Font titleFont = new Font("����", Font.BOLD, 23); // ͼƬ����
        //CategoryPlot plot = chart.getCategoryPlot();// ͼ�εĻ��ƽṹ����
        XYPlot plot = chart.getXYPlot();
        // ͼƬ����
        chart.setTitle(new TextTitle(chart.getTitle().getText(), titleFont));

        // �ײ�
        //chart.getLegend().setItemFont(kfont);

        // X ��
        //CategoryAxis domainAxis = plot.getDomainAxis();
        ValueAxis domainAxis = plot.getDomainAxis();
        domainAxis.setLabelFont(xfont);// �����
        domainAxis.setTickLabelFont(xfont);// ����ֵ
        //domainAxis.setTickLabelPaint(Color.BLUE) ; // ������ɫ
        //domainAxis.setCategoryLabelPositions(CategoryLabelPositions.UP_45); // �����ϵ�labelб��ʾ

        // Y ��
        ValueAxis rangeAxis = plot.getRangeAxis();
        rangeAxis.setLabelFont(yfont);
        //rangeAxis.setLabelPaint(Color.BLUE) ; // ������ɫ
        rangeAxis.setTickLabelFont(yfont);
    }
}