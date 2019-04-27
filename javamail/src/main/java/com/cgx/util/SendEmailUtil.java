package com.cgx.util;

import javax.mail.*;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;
import java.util.Properties;

/**
 * 邮件发送工具类
 */
public class SendEmailUtil {

    /**
     * 发送邮件，本地邮件服务器的测试
     * @param to
     * @param code
     */
    public static void sendMail(String to,String code) throws Exception {
//        1.创建一个连接对象，连接到邮件服务器
        Properties properties = new Properties();
//        properties.setProperty("","");
//        properties.setProperty("","");
//        properties.setProperty("","");

        Session session = Session.getInstance(properties, new Authenticator() {
            @Override
            protected PasswordAuthentication getPasswordAuthentication() {
                return new PasswordAuthentication("service@store.com","111");
            }
        });
//        2.创建邮件对象
        Message message = new MimeMessage(session);
//      发件人
        message.setFrom(new InternetAddress("service@store.com"));
//       收件人
        message.setRecipient(Message.RecipientType.TO,new InternetAddress(to));
//       邮件主题
        message.setSubject("来自XXX网站的邮件激活");
//        邮件的正文
        message.setContent("<h1>来自xxx网站的激活邮件<h3><a href='local://localhost:8080/ActiveServlet?code="+code+"'>" +
                "local://localhost:8080/ActiveServlet?code="+code+"<h3></h1>","text/html;charser=utf-8");

        message.setFrom(new InternetAddress("service@store.com"));
        Transport transport = session.getTransport();
//        3.发送一封激活邮件
        transport.send(message);
        // 关闭连接
        transport.close();
    }

    /**
     * 网络发送邮件
     * @throws Exception
     */
    public static void sendMailByInter() throws Exception{
        Properties props = new Properties();
        // 开启debug调试
        props.setProperty("mail.debug", "true");
        // 发送服务器需要身份验证
        props.setProperty("mail.smtp.auth", "true");
        // 设置邮件服务器主机名
        props.setProperty("mail.host", "smtp.163.com");
        // 发送邮件协议名称
        props.setProperty("mail.transport.protocol", "smtp");

        // 设置环境信息
        Session session = Session.getInstance(props);

        // 创建邮件对象
        Message msg = new MimeMessage(session);
        msg.setSubject("JavaMail测试");
        // 设置邮件内容
        msg.setText("这是一封由JavaMail发送的邮件！");
        // 设置发件人
        msg.setFrom(new InternetAddress("startYaoxy@163.com"));

        Transport transport = session.getTransport();
        // 连接邮件服务器
        transport.connect("startYaoxy@163.com", "a04130812");
        // 发送邮件
        transport.sendMessage(msg, new Address[] {new InternetAddress("429572661@qq.com")});
        // 关闭连接
        transport.close();
    }
}
