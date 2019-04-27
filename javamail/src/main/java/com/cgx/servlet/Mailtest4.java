package com.cgx.servlet;


import com.sun.mail.util.MailSSLSocketFactory;

import javax.activation.DataHandler;
import javax.activation.DataSource;
import javax.activation.FileDataSource;
import javax.mail.*;
import javax.mail.internet.*;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Properties;

public class Mailtest4 {
    public static void main(String [] args) throws Exception {
        // 收件人电子邮箱
        String to = "761245962@qq.com";
//        String to = "hejb@swhysc.com";

        // 发件人电子邮箱
        String from = "823336069@qq.com";

        // 指定发送邮件的主机为 smtp.qq.com
        String host = "smtp.qq.com";  //QQ 邮件服务器

        // 获取系统属性
        Properties properties = System.getProperties();

        // 设置邮件服务器
        properties.setProperty("mail.smtp.host", host);

        properties.put("mail.smtp.auth", "true");
        MailSSLSocketFactory sf = new MailSSLSocketFactory();
        sf.setTrustAllHosts(true);
        properties.put("mail.smtp.ssl.enable", "true");
        properties.put("mail.smtp.ssl.socketFactory", sf);
        // 获取默认session对象
        Session session = Session.getDefaultInstance(properties,new Authenticator(){
            public PasswordAuthentication getPasswordAuthentication()
            {     //qq邮箱服务器账户、第三方登录授权码
                return new PasswordAuthentication(from, "xkuivdnwxjgwbeid"); //发件人邮件用户名、密码
            }
        });
        String pdf="C:\\Users\\82333\\Desktop\\java.pdf";
        try{
            // Create a default MimeMessage object.
            Message message = new MimeMessage(session);

            // Set From: header field of the header.
            message.setFrom(new InternetAddress(from));

            // Set To: header field of the header.
            message.setRecipients(Message.RecipientType.TO,
                    InternetAddress.parse(to));

            // Set Subject: header field
            message.setSubject("Testing Subject");

            // This mail has 2 part, the BODY and the embedded image
            MimeMultipart multipart = new MimeMultipart("related");

            // first part (the html)
            BodyPart messageBodyPart = new MimeBodyPart();
            String htmlText = renderHtmlStr(creatList(pdf))+"<img src=\"cid:attach\">";
            messageBodyPart.setContent(htmlText, "text/html;charset=gb2312");
//            messageBodyPart.setContent(htmlText, "text/html");
            // add it
            multipart.addBodyPart(messageBodyPart);

            // 图片
            File file = new File(pdf);
            // String targerFolder = "D:\\QqFileReceive\\823336069\\FileRecv";
            String targerFolder = "D:/QqFileReceive/";

            String picturePos = PdfUtils.extractImages(file, targerFolder);//pdf中提取图片
            messageBodyPart = new MimeBodyPart();
            DataSource fds = new FileDataSource(targerFolder+picturePos+".jpg");
            messageBodyPart.setHeader("Content-Type", "image/png");
            messageBodyPart.setDataHandler(new DataHandler(fds));
            messageBodyPart.setDisposition(MimeBodyPart.INLINE);
            messageBodyPart.setHeader("Content-ID", "<attach>");
            messageBodyPart.setFileName("attach.png");
            // add image to the multipart
            multipart.addBodyPart(messageBodyPart);

//            // 附件部分
            messageBodyPart = new MimeBodyPart();
            //设置要发送附件的文件路径
            String filename = pdf;
            DataSource source = new FileDataSource(filename);
            messageBodyPart.setDataHandler(new DataHandler(source));

            //messageBodyPart.setFileName(filename);
            //处理附件名称中文（附带文件路径）乱码问题
            messageBodyPart.setFileName(MimeUtility.encodeText(filename));
            multipart.addBodyPart(messageBodyPart);
            // put everything together
            message.setContent(multipart);

            //excel
            // Send message
            Transport.send(message);
            System.out.println("Sent message successfully....");
            File pictureFile = new File(targerFolder + picturePos + ".jpg");
            pictureFile.delete();
        }catch (MessagingException mex) {
            mex.printStackTrace();
        }
    }
    public static String renderHtmlStr(List<List<String>> conteList) throws Exception {
        String htmlStr ="";
        for(int i=0;i<conteList.size();i++){
            htmlStr = createHTML(htmlStr,conteList.get(i),i==0?true:false,i==conteList.size()-1?true:false);
        }
        return htmlStr;
    }
    public static List creatList(String pdfPath){
        List<List<String>> list = new ArrayList<List<String>>();
        List<String> headList = new ArrayList<String>();
        List<String> clouList = null;
        try {
            String context = PdfUtils.extractText(pdfPath);
            String[] contextSplit = context.split("\r\n");
            String[] headListSplit = contextSplit[0].split(" ");
            for(String head:headListSplit){
                headList.add(head);
            }
            list.add(headList);
            for(int i=1;i<contextSplit.length;i++){
                String[] colSplit = contextSplit[i].split(" ");
                clouList = new ArrayList<String>();
                for(String col:colSplit){
                    clouList.add(col);
                }
                list.add(clouList);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return list;
    }
    private static String createHTML(String originHtml, List<String> data,boolean headFlage,boolean endFlage) {
        String html_msg="";
        if(headFlage){
            html_msg = "<table border=\"1\" width='80%' height='80'>";
            html_msg = html_msg+"<tr bgcolor='#B6DDE6'>";
            for(int column=0;column<data.size();column++){
                html_msg = html_msg +"<td width='12%'>"+data.get(column)+"</td>";
            }
            html_msg = html_msg+"</tr>";
        }else{
            html_msg = html_msg+"<tr>";
            for(int column=0;column<data.size();column++){
                html_msg = html_msg +"<td>"+data.get(column)+"</td>";
            }
            html_msg = html_msg+"</tr>";
        }
        if(endFlage){
            html_msg = html_msg + "</table>";
        }
        return originHtml+html_msg;

    }
}
