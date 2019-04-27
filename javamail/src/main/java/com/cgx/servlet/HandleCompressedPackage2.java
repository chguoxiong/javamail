package com.cgx.servlet;

import com.github.junrar.Archive;
import com.github.junrar.rarfile.FileHeader;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.InputStream;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;


public class HandleCompressedPackage2 {

    public static void main(String[] args) throws Exception {
        try {
            readZipFile("D:\\QqFileReceive\\823336069\\FileRecv\\综合平台测试20190318.zip");
            readZipFile("D:\\QqFileReceive\\823336069\\FileRecv\\ceshi.zip");
            //readRarFile("D:\\QqFileReceive\\823336069\\FileRecv\\rar1.zip");
            boolean b = existZH("cdscds大城市的");
            boolean b1 = existZH("cdscds");
            //unrar("D:\\QqFileReceive\\823336069\\FileRecv\\java.rar");
        } catch (Exception e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
    }
    public static boolean existZH(String str) {
        String regEx = "[\\u4e00-\\u9fa5]";
        Pattern p = Pattern.compile(regEx);
        Matcher m = p.matcher(str);
        while (m.find()) {
            return true;
        }
        return false;
    }
    private static Map readRarFile(String compressedPackagePath) throws Exception {
        File fol = null, out = null;
        Map ret = new HashMap();
        Archive archive = null;
        try {
            archive = new Archive(new File(compressedPackagePath));
            if (archive != null) {
                //archive.getMainHeader().print(); // 打印文件信息.
                FileHeader fh = archive.nextFileHeader();
                while (fh != null) {
                    if (fh.isDirectory()) { // 文件夹
                    } else { // 文件
                        InputStream inputStream = archive.getInputStream(fh);
                        ByteArrayOutputStream baos = new ByteArrayOutputStream();
                        String fileName="";
                            fileName=fh.getFileNameW().trim();


                    }
                    fh = archive.nextFileHeader();
                }
                archive.close();
            }
        } catch (Exception e) {
            ret.put("code", "-1");
            ret.put("note", "转换失败");
        }
        return ret;
    }
    public static Map readZipFile(String compressedPackagePath) throws Exception {
        Map ret = new HashMap();
        File file = new File(compressedPackagePath);
        org.apache.tools.zip.ZipFile zip = new org.apache.tools.zip.ZipFile(new File(compressedPackagePath), "GBK");
        for (Enumeration entries = zip.getEntries(); entries.hasMoreElements(); ) {
            org.apache.tools.zip.ZipEntry entry = (org.apache.tools.zip.ZipEntry) entries.nextElement();
            if (entry.isDirectory()) {
                //是文件夹
            } else {
                String fileName="";
                if(entry.getName().indexOf("/")==-1){
                    fileName =entry.getName();
                }else{
                    String[] split = entry.getName().split("\\/");
                    fileName = split[1];
                }
                String yyb = fileName.substring(0, fileName.indexOf(".")).split("\\_")[0];
                String khh = fileName.substring(0, fileName.length() - 4).split("\\_")[1];
                fileName = new String(fileName.getBytes("GBK"), "iso-8859-1");
                String fileName1 = new String(fileName.getBytes("gb2312"), "iso-8859-1");
                InputStream in = zip.getInputStream(entry);
                try{
                    ByteArrayOutputStream baos = new ByteArrayOutputStream();
                    //写入文件 名长度
                    baos.write(fileName.getBytes("utf-8").length);
                    //写入文件名
                    baos.write(fileName.getBytes("utf-8"));
                    byte[] bs = new byte[2048];
                    int readSize;
                    while ((readSize = in.read(bs)) > 0) {
                        baos.write(bs, 0, readSize);
                    }
                }catch (Throwable e) {
                    ret.put("code", "-1");
                    ret.put("note", "转换失败");

                }
            }
        }
        zip.close();
        return ret;
    }
}
