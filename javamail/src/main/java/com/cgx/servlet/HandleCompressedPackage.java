package com.cgx.servlet;

import com.cgx.util.CompresszZipFile;
import de.innosystec.unrar.Archive;
import de.innosystec.unrar.NativeStorage;
import de.innosystec.unrar.rarfile.FileHeader;
import java.io.File;
import java.io.FileOutputStream;
import java.io.*;
import java.util.HashMap;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.zip.ZipEntry;
import java.util.zip.ZipFile;
import java.util.zip.ZipInputStream;

public class HandleCompressedPackage {

    public static void main(String[] args) throws Exception {
        try {
           // readZipFile("D:\\QqFileReceive\\823336069\\FileRecv\\2.FJXX");
           // readZipFile("D:\\QqFileReceive\\823336069\\FileRecv\\1.zip");
           // readRarFile("D:\\QqFileReceive\\823336069\\FileRecv\\FileRecv.rar");
            unrar("D:\\QqFileReceive\\823336069\\FileRecv\\aa.rar");
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
    private static void unrar(String sourceRar) throws Exception{
        Archive a = null;
        FileOutputStream fos = null;
        try{
            File file = new File(sourceRar);
            a = new Archive(new NativeStorage(file));
            FileHeader fh = a.nextFileHeader();
            while(fh!=null){
                if(!fh.isDirectory()){
//1 根据不同的操作系统拿到相应的 destDirName 和 destFileName
                    String compressFileName = fh.getFileNameString().trim();
                    String destFileName = "";
                    String destDirName = "";
//非windows系统
                    if(File.separator.equals("/")){
                        //destFileName = destDir + compressFileName.replaceAll("\\\\", "/");
                        destDirName = destFileName.substring(0, destFileName.lastIndexOf("/"));
//windows系统
                    }else{
                        //destFileName = destDir + compressFileName.replaceAll("/", "\\\\");
                        destDirName = destFileName.substring(0, destFileName.lastIndexOf("\\"));
                    }
//2创建文件夹
                    File dir = new File(destDirName);
                    if(!dir.exists()||!dir.isDirectory()){
                        dir.mkdirs();
                    }
//3解压缩文件
                    fos = new FileOutputStream(new File(destFileName));
                    a.extractFile(fh, fos);
                    fos.close();
                    fos = null;
                }
                fh = a.nextFileHeader();
            }
            a.close();
            a = null;
        }catch(Exception e){
            throw e;
        }finally{
            if(fos!=null){
                try{fos.close();fos=null;}catch(Exception e){e.printStackTrace();}
            }
            if(a!=null){
                try{a.close();a=null;}catch(Exception e){e.printStackTrace();}
            }
        }
    }
    public static Map readZipFile(String compressedPackagePath) throws Exception {
        Map ret = new HashMap();
        ZipFile zf = new ZipFile(compressedPackagePath);
        InputStream in = new BufferedInputStream(new FileInputStream(compressedPackagePath));
        ZipInputStream zin = new ZipInputStream(in);
        ZipEntry ze;
        while ((ze = zin.getNextEntry()) != null) {
            if (ze.isDirectory()) {
            } else {
                try{
                    String yyb = ze.getName().split("\\+")[0];
                    String khh = ze.getName().split("\\+")[1];
                    long size = ze.getSize();
                    ByteArrayOutputStream baos = new ByteArrayOutputStream();
                    //写入文件 名长度
                    baos.write(ze.getName().getBytes().length);
                    //写入文件名
                    baos.write(ze.getName().getBytes());
                    byte[] bs = new byte[2048];

                }catch (Throwable e) {
                    ret.put("code", "-1");
                    ret.put("note", "转换失败");
                }
            }
        }
        zin.closeEntry();
        return ret;
    }
}
