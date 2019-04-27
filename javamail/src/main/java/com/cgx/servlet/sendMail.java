package com.cgx.servlet;

import java.util.Timer;

public class sendMail {

    public static void main(String[] args){
        Timer timer = new Timer();
        MailSendTask mailSendTask = new MailSendTask();
        timer.schedule(mailSendTask,2000L,5000L);
    }

}
