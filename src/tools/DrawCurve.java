package tools;

import java.awt.*;
import java.awt.image.BufferedImage;
import java.util.ArrayList;
import java.util.List;


public class DrawCurve{
    private int width = 600;
    private int height = 400;
    private int padding = 25;
    private int labelPadding = 25;
    private Color lineColor = Color.BLUE;//new Color(44, 102, 230, 180);
    private Color pointColor = new Color(100, 100, 100, 180);
    private Color gridColor = new Color(200, 200, 200, 200);
    private static final Stroke GRAPH_STROKE = new BasicStroke(1f);
    private int pointWidth = 4;
    private int numberYDivisions = 10;
    private List<Double> scores;

    public DrawCurve(){
        /*     test
        DrawCurve dc = new DrawCurve();
        double[] data = new double[9];
        for(int i = 0; i < data.length; i++) data[i] = i;
       ImageIO.write(dc.generateImage(data), "jpg", new File("F:\\text.jpg"));
        */
    }

    private List<Double> Double2List(double[] points){
        if(points.length <= 0){
            return null;
        }
        List<Double> scores = new ArrayList<>();
        int maxDataPoints = points.length;
        for(int i = 0; i < maxDataPoints; i++){
            scores.add(points[i]);
        }
        return scores;
    }

    public BufferedImage generateImage(double[] points){
        this.scores = Double2List(points);


        BufferedImage image = new BufferedImage(width, height, BufferedImage.TYPE_INT_RGB);
        // »æÖÆÍ¼Ïñ
        Graphics g = image.getGraphics();
        Graphics2D g2 = (Graphics2D) g;
        // ±³¾°É«
        g.setColor(Color.WHITE);
        g.fillRect(0, 0, width, height);
        // ×ÖÌåÑÕÉ«
        //g.setColor(Color.BLACK);
        g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);

        double xScale = ((double) width-(2*padding)-labelPadding)/(scores.size()-1);
        double yScale = ((double) height-2*padding-labelPadding)/(getMaxScore()-getMinScore());

        List<Point> graphPoints = new ArrayList<>();
        for(int i = 0; i < scores.size(); i++){
            int x1 = (int) (i*xScale+padding+labelPadding);
            int y1 = (int) ((getMaxScore()-scores.get(i))*yScale+padding);
            graphPoints.add(new Point(x1, y1));
        }

        // draw white background
        g2.setColor(Color.WHITE);
        g2.fillRect(padding+labelPadding, padding, width-(2*padding)-labelPadding, height-2*padding-labelPadding);
        g2.setColor(Color.BLACK);

        // create hatch marks and grid lines for y axis.
        for(int i = 0; i < numberYDivisions+1; i++){
            int x0 = padding+labelPadding;
            int x1 = pointWidth+padding+labelPadding;
            int y0 = height-((i*(height-padding*2-labelPadding))/numberYDivisions+padding+labelPadding);
            int y1 = y0;
            if(scores.size() > 0){
                g2.setColor(gridColor);
                g2.drawLine(padding+labelPadding+1+pointWidth, y0, width-padding, y1);
                g2.setColor(Color.BLACK);
                String yLabel = ((int) ((getMinScore()+(getMaxScore()-getMinScore())*((i*1.0)/numberYDivisions))*100))/100.0+"";
                FontMetrics metrics = g2.getFontMetrics();
                int labelWidth = metrics.stringWidth(yLabel);
                g2.drawString(yLabel, x0-labelWidth-5, y0+(metrics.getHeight()/2)-3);
            }
            g2.drawLine(x0, y0, x1, y1);
        }
        int div = (int) (scores.size()/10.0);
        if(scores.size() < 10){
            div = 1;
        }
        // and for x axis
        for(int i = 0; i < scores.size(); i++){
            if(scores.size() > 1){
                int x0 = i*(width-padding*2-labelPadding)/(scores.size()-1)+padding+labelPadding;
                int x1 = x0;
                int y0 = height-padding-labelPadding;
                int y1 = y0-pointWidth;

                if(((i+1)%div) == 0){
                    g2.setColor(gridColor);
                    g2.drawLine(x0, height-padding-labelPadding-1-pointWidth, x1, padding);
                    g2.setColor(Color.BLACK);

                    String xLabel = (i+1)+"";
                    FontMetrics metrics = g2.getFontMetrics();
                    int labelWidth = metrics.stringWidth(xLabel);
                    g2.drawString(xLabel, x0-labelWidth/2, y0+metrics.getHeight()+3);
                    g2.drawLine(x0, y0, x1, y1);
                }

                // g2.drawLine(x0, y0, x1, y1);
            }
        }

        // create x and y axes
        g2.drawLine(padding+labelPadding, height-padding-labelPadding, padding+labelPadding, padding);
        g2.drawLine(padding+labelPadding, height-padding-labelPadding, width-padding, height-padding-labelPadding);

        Stroke oldStroke = g2.getStroke();
        g2.setColor(lineColor);
        g2.setStroke(GRAPH_STROKE);
        for(int i = 0; i < graphPoints.size()-1; i++){
            int x1 = graphPoints.get(i).x;
            int y1 = graphPoints.get(i).y;
            int x2 = graphPoints.get(i+1).x;
            int y2 = graphPoints.get(i+1).y;
            g2.drawLine(x1, y1, x2, y2);
        }
        /*draw dot
        g2.setStroke(oldStroke);
        g2.setColor(pointColor);
        for (int i = 0; i < graphPoints.size(); i++) {
            int x = graphPoints.get(i).x - pointWidth / 2;
            int y = graphPoints.get(i).y - pointWidth / 2;
            int ovalW = pointWidth;
            int ovalH = pointWidth;
            g2.fillOval(x, y, ovalW, ovalH);
        }
        */
        return image;

    }

    private double getMinScore(){
        double minScore = Double.MAX_VALUE;
        for(Double score : scores){
            minScore = Math.min(minScore, score);
        }
        return minScore;
    }

    private double getMaxScore(){
        double maxScore = Double.MIN_VALUE;
        for(Double score : scores){
            maxScore = Math.max(maxScore, score);
        }
        return maxScore;
    }

    public void setScores(List<Double> scores){
        this.scores = scores;
    }

    public List<Double> getScores(){
        return scores;
    }

    public void setWidthHeight(int w, int h){
        this.width = w;
        this.height = h;
    }

}
