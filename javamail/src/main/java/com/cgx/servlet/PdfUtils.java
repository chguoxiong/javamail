package com.cgx.servlet;


import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import org.apache.pdfbox.pdmodel.PDDocument;
import org.apache.pdfbox.pdmodel.PDPage;
import org.apache.pdfbox.pdmodel.PDResources;
import org.apache.pdfbox.pdmodel.graphics.xobject.PDXObjectImage;
import org.apache.pdfbox.util.PDFTextStripper;

public class PdfUtils {

    public static String extractText(String pdfPath ) throws IOException {
        // 是否排序
        boolean sort = false;
        // 开始提取页数
        int startPage = 1;
        // 结束提取页数
        int endPage = Integer.MAX_VALUE;
        String content = null;
        PrintWriter writer = null;

        PDDocument document = null;
        try {
            document = PDDocument.load(new File(pdfPath));

            PDFTextStripper pts = new PDFTextStripper();
            endPage = document.getNumberOfPages();
            System.out.println("Total Page: " + endPage);
            pts.setStartPage(startPage);
            pts.setEndPage(endPage);
            //content就是从pdf中解析出来的文本
            content = pts.getText(document);
        } catch (Exception e) {
            System.out.println(e.getMessage());
        } finally {
            if (null != document)
                document.close();
        }
        return content;
    }
    public static String extractImages(File file, String targetFolder) throws IOException {
        boolean result = true;
        String name="";
        PDDocument document=null;
        try{
             document = PDDocument.load(file);

            List<PDPage> pages = document.getDocumentCatalog().getAllPages();
            Iterator<PDPage> iter = pages.iterator();
            int count = 0;
            while( iter.hasNext()){
                PDPage page = (PDPage)iter.next();
                PDResources resources = page.getResources();
                Map<String, PDXObjectImage> images = resources.getImages();
                if(images != null)
                {
                    Iterator<String> imageIter = images.keySet().iterator();
                    while(imageIter.hasNext())
                    {
                        count++;
                        String key = (String)imageIter.next();
                        PDXObjectImage image = (PDXObjectImage)images.get( key );
                        name = file.getName().substring(0,file.getName().length()-4);	// 图片文件名
                        image.write2file(targetFolder + name);		// 保存图片
                    }
                }

            }
        } catch(IOException ex){
            ex.printStackTrace();
            return "";
        }finally {
            if(null!=document)
              document.close();
        }

        return name;
    }



    public static void main(String[] args) {
        String pdfPath="C:\\Users\\82333\\Desktop\\java.pdf";
        File file = new File(pdfPath);
       // String targerFolder = "D:\\QqFileReceive\\823336069\\FileRecv";
        String targerFolder = "D:/QqFileReceive/";

        try {
            String conext = extractText(pdfPath);
            conext.split("//r//n");
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
