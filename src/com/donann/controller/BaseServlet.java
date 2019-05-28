package com.donann.controller;

import java.io.IOException;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * 所有serlvet的基类。提供了根据method请求参数，自动调用
 * Servlet方法的功能。
 */
public class BaseServlet extends HttpServlet {
	private static final long serialVersionUID = 1L;
       
    /**
     * @see HttpServlet#HttpServlet()
     */
    public BaseServlet() {
        super();
    }

	/**
	 * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		//1.先获取请求参数中的method值
		String method = request.getParameter("method");
		//2.通过反射，获取method对应的Method对象
		Method m =null;
		try {
			m = this.getClass().getDeclaredMethod(method, HttpServletRequest.class,HttpServletResponse.class);
		} catch (NoSuchMethodException|SecurityException e) {
			e.printStackTrace();
			response.sendError(HttpServletResponse.SC_BAD_REQUEST, e.getMessage());
		}
		
		//如果Method找到了，直接通过反射执行
		try {
			m.invoke(this, request,response);
		} catch (IllegalAccessException|IllegalArgumentException|InvocationTargetException e) {
			e.printStackTrace();
			response.sendError(HttpServletResponse.SC_BAD_REQUEST, e.getMessage());
		}
	}

	/**
	 * @see HttpServlet#doPost(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		doGet(request, response);
	}

}
