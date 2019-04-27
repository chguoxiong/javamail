package com.cgx.servlet;

import com.cgx.service.RegistService;
import com.cgx.service.RegistServiceImpl;
import com.cgx.util.CodeUUidUtil;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

@WebServlet(name = "registUserServlet")
public class registUserServlet extends HttpServlet {

    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        //接受数据
       request.setCharacterEncoding("UTF-8");
       String username =  request.getParameter("username");
       String password =  request.getParameter("password");
       String nickname =  request.getParameter("nickname");
       String email =  request.getParameter("email");
       String code = CodeUUidUtil.getUUID();

//      封装数据
//      调用业务层处理数据
        RegistService registService = new RegistServiceImpl();
        try {
            registService.sendEmil(email,code);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        doPost(request,response);
    }
}
