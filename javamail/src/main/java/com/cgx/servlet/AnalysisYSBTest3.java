package com.cgx.servlet;

import com.github.junrar.Archive;
import com.github.junrar.rarfile.FileHeader;
import org.apache.tools.zip.ZipEntry;
import org.apache.tools.zip.ZipFile;

import java.io.*;
import java.util.ArrayList;
import java.util.Enumeration;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * @author cfang_W
 * @create 2019-03-19 8:47
 */
public class AnalysisYSBTest3 {
    public static void main(String[] args) throws Exception {
        String pathName = "D:\\QqFileReceive\\823336069\\FileRecv\\ceshi.zip";
        String pathName2 = "D:\\QqFileReceive\\823336069\\FileRecv\\aa.rar";

        Analysis(pathName2);
    }

    public static void Analysis(String pathName) throws Exception {
        String descDirRar = pathName.split("\\.")[0];
        String[] path = pathName.split("\\\\");
        String descDirZip = pathName.split(path[path.length-1])[0];

        if (pathName.toLowerCase().endsWith(".zip")) {
            unZipFiles(new File(pathName), descDirZip);
        }else if (pathName.toLowerCase().endsWith(".rar")){
            unRarFiles(pathName, descDirRar);
        }

        List<File> fileList = new ArrayList<>();
        fileList = getFileList(fileList, descDirRar);
        for (int i = 0; i < fileList.size(); i++) {
            String sourceFile = fileList.get(i).getAbsolutePath();
            String kzm = sourceFile.split("\\.")[1];
            String[] split = sourceFile.split("\\\\");
            String descDirRa = sourceFile.split("\\.")[0];
            descDirZip = sourceFile.split(split[split.length-1])[0];

            if ("zip".equals(kzm)) {
                unZipFiles(new File(sourceFile), descDirZip);
            }else if ("rar".equals(kzm)){
                unRarFiles(sourceFile, descDirRa);
            }
        }

        List<File> newFileList = new ArrayList<File>();
        newFileList = getFileList(newFileList, descDirRar);
        for(int i = 0; i < newFileList.size(); i++){
            List<String> slist = strSplit(newFileList.get(i).getName());
            if (!slist.isEmpty()){
                System.out.println("yyb:" + slist.get(0) + " " + "khh:" + slist.get(1));
            }
        }

        boolean a = delAllFile(descDirRar);
        System.out.println(a);
    }

    public static void unZipFiles(File zipFile, String descDir) throws IOException {
        File pathFile = new File(descDir);
        if (!pathFile.exists()) {
            pathFile.mkdirs();
        }

        ZipFile zip = new ZipFile(zipFile, "GBK");
        for (Enumeration entries = zip.getEntries(); entries.hasMoreElements(); ) {
            ZipEntry entry = (ZipEntry) entries.nextElement();
            String zipEntryName = entry.getName();
            InputStream in = zip.getInputStream(entry);
            String outPath = (descDir + zipEntryName).replaceAll("\\*", "/");

            //判断路径是否存在,不存在则创建文件路径
            File file = new File(outPath.substring(0, outPath.lastIndexOf('/')));
            if (!file.exists()) {
                file.mkdirs();
            }
            //判断文件全路径是否为文件夹,如果是上面已经上传,不需要解压
            if (new File(outPath).isDirectory()) {
                continue;
            }

            //输出文件路径信息
            //System.out.println(outPath);

            OutputStream out = new FileOutputStream(outPath);
            byte[] buf1 = new byte[1024];
            int len;
            while ((len = in.read(buf1)) > 0) {
                out.write(buf1, 0, len);
            }

            in.close();
            out.close();
        }
        zip.close();
        //System.out.println("******************解压完毕********************");
    }

    public static void unRarFiles(String srcRarPath, String dstDirectoryPath) throws Exception {
        if (!srcRarPath.toLowerCase().endsWith(".rar")) {
            System.out.println("非rar文件！");
            return;
        }
        File dstDiretory = new File(dstDirectoryPath);
        if (!dstDiretory.exists()) {// 目标目录不存在时，创建该文件夹
            dstDiretory.mkdirs();
        }
        File fol = null, out = null;
        Archive a = null;
        try {
            a = new Archive(new File(srcRarPath));
            if (a != null) {
                a.getMainHeader().print(); // 打印文件信息.
                FileHeader fh = a.nextFileHeader();
                while (fh != null) {
                    if (fh.isDirectory()) { // 文件夹
                        // 如果是中文路径，调用getFileNameW()方法，否则调用getFileNameString()方法，还可以使用if(fh.isUnicode())
                        if (existZH(fh.getFileNameW())) {
                            fol = new File(dstDirectoryPath + File.separator
                                    + fh.getFileNameW());
                        } else {
                            fol = new File(dstDirectoryPath + File.separator
                                    + fh.getFileNameString());
                        }

                        fol.mkdirs();
                    } else { // 文件
                        if (existZH(fh.getFileNameW())) {
                            out = new File(dstDirectoryPath + File.separator
                                    + fh.getFileNameW().trim());
                        } else {
                            out = new File(dstDirectoryPath + File.separator
                                    + fh.getFileNameString().trim());
                        }
                        
                        //System.out.println(out.getAbsolutePath());
                        try {// 之所以这么写try，是因为万一这里面有了异常，不影响继续解压.
                            if (!out.exists()) {
                                if (!out.getParentFile().exists()) {// 相对路径可能多级，可能需要创建父目录.
                                    out.getParentFile().mkdirs();
                                }
                                out.createNewFile();
                            }
                            FileOutputStream os = new FileOutputStream(out);
                            a.extractFile(fh, os);
                            os.close();
                        } catch (Exception ex) {
                            ex.printStackTrace();
                        }
                    }
                    fh = a.nextFileHeader();
                }
                a.close();
            }
        } catch (Exception e) {
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
    
    public static List<File> getFileList(List<File> filelist, String strPath) {
        File dir = new File(strPath);
        File[] files = dir.listFiles(); // 该文件目录下文件全部放入数组
        if (files != null) {
            for (int i = 0; i < files.length; i++) {
                String fileName = files[i].getName();
                if (files[i].isDirectory()) { // 判断是文件还是文件夹
                    getFileList(filelist, files[i].getAbsolutePath()); // 获取文件绝对路径
                } else {
                    filelist.add(files[i]);
                    continue;
                }
            }

        }
        return filelist;
    }

    private static List<String> strSplit(String str){
        List<String> list = new ArrayList<>();
        String yyb = "";
        String khh = "";

        String[] fileName = str.split("\\.");//去除文件扩展名
        String[] fNameSplit = fileName[0].split("\\+");
        if (fNameSplit.length > 1){
            yyb = fNameSplit[0];
            khh = fNameSplit[1].split(" ")[0];

            list.add(yyb);
            list.add(khh);
        }
        return list;
    }

    public static boolean delAllFile(String path) {
        boolean flag = false;
        File file = new File(path);
        if (!file.exists()) {
            return flag;
        }
        if (!file.isDirectory()) {
            return flag;
        }
        String[] tempList = file.list();
        File temp = null;
        for (int i = 0; i < tempList.length; i++) {
            if (path.endsWith(File.separator)) {
                temp = new File(path + tempList[i]);
            } else {
                temp = new File(path + File.separator + tempList[i]);
            }
            if (temp.isFile()) {
                temp.delete();
            }
            if (temp.isDirectory()) {
                delAllFile(path + "/" + tempList[i]);//先删除文件夹里面的文件
                delFolder(path + "/" + tempList[i]);//再删除空文件夹
                flag = true;
            }
        }
        file.delete();
        return flag;
    }

    public static void delFolder(String folderPath) {
        try {
            delAllFile(folderPath); //删除完里面所有内容
            String filePath = folderPath;
            filePath = filePath.toString();
            File myFilePath = new File(filePath);
            myFilePath.delete(); //删除空文件夹
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
