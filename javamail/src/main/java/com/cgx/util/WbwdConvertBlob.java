//package com.cgx.util;
//
//import java.io.*;
//import java.sql.Blob;
//import java.sql.PreparedStatement;
//import java.sql.SQLException;
//import java.util.HashMap;
//import java.util.Map;
//
//public class WbwdConvertBlob {
//import com.apex.form.SystemConfig;
//import org.apache.log4j.Logger;
//import plugins.bean.DatabaseUtils;
//import plugins.bean.OutDB;
//
//import java.io.*;
//import java.sql.Connection;
//import java.sql.PreparedStatement;
//import java.sql.SQLException;
//import java.util.HashMap;
//import java.util.Map;
//
//    /**
//     * 外部文档转化成BLOB字段
//     */
//    public class WbwdConvertBlob {
//
//        private static Logger log = Logger.getLogger(WbwdConvertBlob.class);
//
//        public static Map convert(String oldTable, String oldField, String oldId, String newTable, String newField, String newId) throws IOException {
//            DatabaseUtils db = new OutDB().getDB();
//            Map ret = new HashMap();
//            try {
//                ret = parseFile(db, oldTable, oldField, oldId, newTable, newField, newId);
//            } catch (Throwable e) {
//                log.warn("外部文档转化成BLOB字段异常：" + e.getMessage());
//            } finally {
//                if (db != null) db.close();
//            }
//
//            return ret;
//        }
//
//        public static Map parseFile(DatabaseUtils db, String oldTable, String oldField, String oldId, String newTable, String newField, String newId) throws IOException, SQLException {
//            String fileName = "";
//            String file = "";
//            String srcSQL = "UPDATE " + newTable + " SET " + newField + " =? where ID=" + newId;//插入语句
//            String sql = "SELECT " + oldField + "  FROM " + oldTable + " WHERE ID=? ";//查询文件名sql
//            file = db.getFirstValue(sql, new String[]{oldId});//文件名加后缀
//            fileName = file;
//            File f = new File(SystemConfig.INSTANCE.getDocumentPath() + "/" + oldTable + "/" + oldId + "." + oldField);
//            Map ret = new HashMap();
//            if (f.exists()) {
//                Connection conn = db.getConnection();
//                FileInputStream fis = new FileInputStream(f);  //获取输入流
//                PreparedStatement ps = conn.prepareStatement(srcSQL);
//                ;
//                try {
//                    int idx = 0;
//                    ByteArrayOutputStream baos = new ByteArrayOutputStream();
//                    //写入文件 名长度
//                    baos.write(fileName.length());
//                    //写入文件名
//                    baos.write(fileName.getBytes());
//                    byte[] bs = new byte[2048];
//                    int readSize;
//                    while ((readSize = fis.read(bs)) > 0) {
//                        baos.write(bs, 0, readSize);
//                    }
//                    ByteArrayInputStream bais = new ByteArrayInputStream(baos.toByteArray());
//                    ps.setBinaryStream(++idx, bais, baos.size());
//                    int i = ps.executeUpdate();
//                    ret.put("code", "1");
//                    ret.put("note", "外部文档转化成BLOB字段成功!");
//                } catch (Throwable e) {
//                    ret.put("code", "-1");
//                    ret.put("note", "外部文档转化成BLOB字段异常" + e.getMessage());
//                    log.warn("外部文档转化成BLOB字段异常：" + e.getMessage());
//                } finally {
//                    try {
//                        ps.close();
//                        fis.close();
//                    } catch (Throwable e) {
//                        log.warn(e.getMessage());
//                    }
//                }
//            } else {
//                ret.put("code", "-1");
//                ret.put("note", "文件不存在");
//                log.warn(f.getAbsolutePath());
//            }
//            return ret;
//        }
//
//    }
//
//    public static String getCrmBlobFilename(Blob blobFile) throws Exception {
//        String fileName = "";
//        BufferedInputStream in = null;
//        try {
//            if (blobFile != null) {
//                in = new BufferedInputStream(blobFile.getBinaryStream(), 1024);
////该文件是否是pdf文件
//                int filenameLen = in.read();//第一个字节存文件名大小
//                byte[] fileNameBytes = new byte[filenameLen];
//                in.read(fileNameBytes);//根据文件名的长度读取文件名
//                fileName = new String(fileNameBytes);
//            }
//        } catch (Exception e) {
//            //log.error("读取blob文件名出错" + e.toString());
//        } finally {
//            if (in != null) {
//                in.close();
//            }
//        }
//        return fileName;
//    }
//}
