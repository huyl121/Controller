package com.example.bian.email;

import com.example.bian.client.bushu.PrivateConfig;
import com.sun.mail.util.MailSSLSocketFactory;
import java.security.GeneralSecurityException;
import java.util.Properties;
import javax.mail.*;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;

public class SendEmail {

    public static void main(String[] args) throws GeneralSecurityException {
        method("胡公司", "测试，不用管", false);
    }

    public static void method(String head, String body, Boolean isMe)
            throws GeneralSecurityException {


        String from = "1811074340@qq.com";


        String host = "smtp.qq.com";


        Properties properties = System.getProperties();

        properties.setProperty("mail.smtp.host", host);

        properties.put("mail.smtp.auth", "true");
        MailSSLSocketFactory sf = new MailSSLSocketFactory();
        sf.setTrustAllHosts(true);
        properties.put("mail.smtp.ssl.enable", "true");
        properties.put("mail.smtp.ssl.socketFactory", sf);

        // 获取默认session对象
        Session session = Session.getDefaultInstance(properties, new Authenticator() {
            @Override
            public PasswordAuthentication getPasswordAuthentication() {
                return new PasswordAuthentication("1811074340@qq.com", PrivateConfig.password); //发件人邮件用户名、授权码
            }
        });
        try {

            if("0".equals(PrivateConfig.ceShi)){
                if(isMe){
                    send(session, from, "381471618@qq.com", body, head);
                    Thread.sleep(1000);
                }else {
                    send(session, from, "381471618@qq.com", body, head);
                    Thread.sleep(2000);
                    send(session, from, "343316073@qq.com", body, head);
                    Thread.sleep(2000);
                    send(session, from, "1028761565@qq.com", body, head);
                    Thread.sleep(2000);
                    send(session, from, "805385922@qq.com", body, head);
                    Thread.sleep(2000);
                }
            }
            /*if(PrivateConfig.ceShi.equals("0")){
                send(session, from, "805385922@qq.com", body, head);
                Thread.sleep(1000);
                send(session, from, "1028761565@qq.com", body, head);
                Thread.sleep(1000);
            }
            if(isMe){
                send(session, from, "381471618@qq.com", body, head);
                Thread.sleep(1000);
            }*/
        } catch (MessagingException mex) {
            mex.printStackTrace();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    public static void methodMe(String head, String body)
            throws GeneralSecurityException {

        String from = "1811074340@qq.com";

        String host = "smtp.qq.com";

        Properties properties = System.getProperties();

        properties.setProperty("mail.smtp.host", host);

        properties.put("mail.smtp.auth", "true");
        MailSSLSocketFactory sf = new MailSSLSocketFactory();
        sf.setTrustAllHosts(true);
        properties.put("mail.smtp.ssl.enable", "true");
        properties.put("mail.smtp.ssl.socketFactory", sf);

        // 获取默认session对象
        Session session = Session.getDefaultInstance(properties, new Authenticator() {
            @Override
            public PasswordAuthentication getPasswordAuthentication() {
                return new PasswordAuthentication("1811074340@qq.com", PrivateConfig.password); //发件人邮件用户名、授权码
            }
        });
        try {
            send(session, from, "1811074340@qq.com", head, body);
            Thread.sleep(1000);

        } catch (MessagingException mex) {
            mex.printStackTrace();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    public static void send(Session session, String from, String toEmail, String body, String head)
            throws MessagingException {
        MimeMessage message = new MimeMessage(session);

        message.setFrom(new InternetAddress(from));

        message.addRecipient(Message.RecipientType.TO, new InternetAddress(toEmail));

        message.setSubject(head);

        message.setText(body);

        Transport.send(message);
    }
}
