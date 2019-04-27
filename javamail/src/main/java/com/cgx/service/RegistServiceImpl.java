package com.cgx.service;

import com.cgx.util.SendEmailUtil;

public class RegistServiceImpl implements RegistService {

    public void sendEmil(String to,String code) throws Exception {
//        SendEmailUtil.sendMail(to,code);
        SendEmailUtil.sendMailByInter();
    }
}
