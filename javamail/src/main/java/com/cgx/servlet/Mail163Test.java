package com.cgx.servlet;

import javax.mail.Message;
import javax.mail.Session;
import javax.mail.Transport;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;
import java.util.Properties;

/**
 * Created by chiguoxiong on 2018/8/15.
 */
public class Mail163Test {

    public static boolean sendMail(String to, String code) {

        try {
            Properties props = new Properties();
            props.put("username", "823336069@qq.com");
            props.put("password", "a1302422014");
            props.put("mail.transport.protocol", "smtp" );
//            props.put("mail.smtp.host", "mail.apexsoft.com.cn");
            props.put("mail.smtp.host", "smtp.qq.com");
//            props.put("mail.smtp.port", "25" );

            Session mailSession = Session.getDefaultInstance(props);

            Message msg = new MimeMessage(mailSession);
            msg.setFrom(new InternetAddress("823336069@qq.com"));
            msg.addRecipients(Message.RecipientType.TO, InternetAddress.parse(to));
            msg.setSubject("激活邮件");
            msg.setContent("<h1>此邮件为官方激活邮件</h1>","text/html;charset=UTF-8");

            msg.saveChanges();

            Transport transport = mailSession.getTransport("smtp");
            transport.connect(props.getProperty("mail.smtp.host"), props
                    .getProperty("username"), props.getProperty("password"));
            transport.sendMessage(msg, msg.getAllRecipients());
            transport.close();
        } catch (Exception e) {
            e.printStackTrace();
            System.out.println(e);
            return false;
        }
        return true;
    }
    public static void main(String[] args) {
        sendMail("chiguoxiong@apexsoft.com.cn", "89");
    }
}
