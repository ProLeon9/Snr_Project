package tools;

import com.itextpdf.text.*;
import com.itextpdf.text.pdf.PdfPCell;
import com.itextpdf.text.pdf.PdfPTable;
import com.itextpdf.text.pdf.PdfWriter;

import java.awt.image.BufferedImage;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.List;
import java.util.Objects;


public class GenerateReoport{

    public void writeReport(String reportPath, List<String> methodList, BufferedImage[][] result_image){
        //页面大小
        Rectangle rect = new Rectangle(PageSize.A4);
        //页面背景色
        //rect.setBackgroundColor(BaseColor.ORANGE);
        //Step 1—Create a Document.
        Document document = new Document(rect);
        //Step 2—Get a PdfWriter instance.
        try{
            PdfWriter.getInstance(document, new FileOutputStream(reportPath+"\\Preprocess_Evaluation_Report.pdf"));
            //Step 3—Open the Document.
            //文档属性
            document.addTitle("Preprocess Report");
            document.addAuthor("Leon");
            document.addSubject("");
            document.addKeywords("");
            document.addCreator("");
            document.open();

            //Step 4—Add content.
            //写内容
            Font font = new Font(Font.FontFamily.HELVETICA, 18, Font.BOLDITALIC, BaseColor.RED);
            //Chunk id = new Chunk("chinese", font);
            Paragraph title = new Paragraph("Preprocess Evaluation Report", font);
            title.setAlignment(Rectangle.ALIGN_CENTER); //标题剧中
            Chapter chapter = new Chapter(title, 2);
            chapter.setNumberDepth(0);

            Paragraph Blank_line = new Paragraph(" "); //空行
            chapter.add(Blank_line);

            Font font1 = new Font(Font.FontFamily.HELVETICA, 13, Font.BOLDITALIC, BaseColor.BLACK);
            Paragraph sub_title1 = new Paragraph("PreProcess Method:", font1);
            chapter.add(sub_title1);
            chapter.add(Blank_line);

            ZapfDingbatsList zapfDingbatsList = new ZapfDingbatsList(43, 30);
            for(int i=0; i<=methodList.size()-1; i++){
                zapfDingbatsList.add(new ListItem(changeToEnglish(methodList.get(i))));
            }
            chapter.add(zapfDingbatsList);
            document.add(chapter);
            //  Paragraph PlaintextSize = new Paragraph("Plaintext Size: " + config.plain_bytesize + " bytes");
            //  chapter.add(PlaintextSize);

            //Evaluation metrics
            for(int i = 0; i < methodList.size(); i++){
                document.newPage();
                document.add(Blank_line);
                Paragraph sub_title3 = new Paragraph("Preprocess Result for " + changeToEnglish(methodList.get(i)), font1);
                document.add(sub_title3);
                document.add(Blank_line);

                String method = changeToEnglish(methodList.get(i));
                //Draw image
                Image img = Image.getInstance(result_image[i][0], null);
                //  Image img = Image.getInstance("D:\\Project\\JAVA_Project\\pdf_gen\\Curve.jpg");
                // img.scaleToFit(30,20);
                PdfPTable table = new PdfPTable(1);
                table.setHorizontalAlignment(Element.ALIGN_CENTER);
                // table.setTotalWidth(77);
                PdfPCell cell = new PdfPCell();
                // cell.addElement(new Chunk(img, 0, 0));
                cell.setBackgroundColor(BaseColor.WHITE);
                cell.setHorizontalAlignment(Element.ALIGN_CENTER);
                cell.setImage(img);
                table.addCell(cell);
                document.add(table);
                Paragraph pic_title = new Paragraph("SNR Before " + method);
                pic_title.setAlignment(Rectangle.ALIGN_CENTER); //; //
                document.add(pic_title);
                document.add(Blank_line);

                document.add(Blank_line);
                Image img1 = Image.getInstance(result_image[i][1], null);
                // img1.scaleToFit(300,250);
                PdfPTable table1 = new PdfPTable(1);
                table1.setHorizontalAlignment(Element.ALIGN_CENTER);
                // table1.setTotalWidth(77);
                PdfPCell cell1 = new PdfPCell();
                // cell.addElement(new Chunk(img, 0, 0));
                cell1.setBackgroundColor(BaseColor.WHITE);
                cell1.setHorizontalAlignment(Element.ALIGN_CENTER);
                cell1.setImage(img1);
                table1.addCell(cell1);
                document.add(table1);
                Paragraph pic_title1 = new Paragraph("SNR After " + method);
                pic_title1.setAlignment(Rectangle.ALIGN_CENTER); //; //
                document.add(pic_title1);
                document.add(Blank_line);

                document.newPage();
                document.add(Blank_line);
                Image img2 = Image.getInstance(result_image[i][2], null);
                //  Image img = Image.getInstance("D:\\Project\\JAVA_Project\\pdf_gen\\Curve.jpg");
                // img.scaleToFit(30,20);
                PdfPTable table2 = new PdfPTable(1);
                table2.setHorizontalAlignment(Element.ALIGN_CENTER);
                // table.setTotalWidth(77);
                PdfPCell cell2 = new PdfPCell();
                // cell.addElement(new Chunk(img, 0, 0));
                cell2.setBackgroundColor(BaseColor.WHITE);
                cell2.setHorizontalAlignment(Element.ALIGN_CENTER);
                cell2.setImage(img2);
                table2.addCell(cell2);
                document.add(table2);
                Paragraph pic_title2 = new Paragraph("PI Before " + method);
                pic_title2.setAlignment(Rectangle.ALIGN_CENTER); //; //
                document.add(pic_title2);
                document.add(Blank_line);

                document.add(Blank_line);
                Image img3 = Image.getInstance(result_image[i][3], null);
                //  Image img = Image.getInstance("D:\\Project\\JAVA_Project\\pdf_gen\\Curve.jpg");
                // img.scaleToFit(30,20);
                PdfPTable table3 = new PdfPTable(1);
                table3.setHorizontalAlignment(Element.ALIGN_CENTER);
                // table.setTotalWidth(77);
                PdfPCell cell3 = new PdfPCell();
                // cell.addElement(new Chunk(img, 0, 0));
                cell3.setBackgroundColor(BaseColor.WHITE);
                cell3.setHorizontalAlignment(Element.ALIGN_CENTER);
                cell3.setImage(img3);
                table3.addCell(cell3);
                document.add(table3);
                Paragraph pic_title3 = new Paragraph("PI Before " + method);
                pic_title3.setAlignment(Rectangle.ALIGN_CENTER); //; //
                document.add(pic_title3);

            }
            //Step 5—Close the Document.
            document.close();
        }
        catch(DocumentException | IOException e){
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
    }

    private String changeToEnglish(String method){
        if(Objects.equals(method, "静态对齐"))
            return "Static Align";
        else if(Objects.equals(method, "动态对齐"))
            return "DTW";
        else if(Objects.equals(method, "卡尔曼滤波"))
            return "Kalman Filter";
        else
            return method;
    }

}
