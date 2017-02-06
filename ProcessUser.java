package com.cg.airspace.controller;

import java.io.IOException;
import java.util.Date;

import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.cg.airspace.beans.Users;
import com.cg.airspace.exception.AirSpaceException;
import com.cg.airspace.service.AirSpaceServiceImpl;
import com.cg.airspace.service.IAirSpaceService;


@WebServlet("/ProcessUser")
public class ProcessUser extends HttpServlet {

	private IAirSpaceService service = null;
	
	@Override
	public void init() throws ServletException {
		service = new AirSpaceServiceImpl();
	}
	

	
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
	    String action = request.getParameter("action");
	    Users user = null;
	    
	    switch (action) {
		case "register":
			String name = request.getParameter("name");
			String phone = request.getParameter("phone");
			String uname = request.getParameter("uname");
			String password = request.getParameter("pass");
			
			user = new Users(name, uname, password, phone);
			
			try 
			{
				service.addUser(user);
				request.getSession().setAttribute("user", user);
			}
			catch (AirSpaceException e) {
				
			
				System.out.println(e.getMessage());
				request.getSession().setAttribute("error", e.getMessage());
			}
			RequestDispatcher rd = request.getRequestDispatcher("CustomerHome.jsp");
			rd.forward(request, response);
			
			
			
			return;
			
		case "payBill":
		  Date billDate = new Date();
		  String amount = request.getParameter("amount");
		  int amounPaid = Integer.parseInt(amount);
		  int balanceAmount = 750 - amounPaid;
		  request.getSession().setAttribute("balanceAmount", balanceAmount);
		  request.getSession().setAttribute("billDate", billDate);
			try 
			{
				int billId = service.generateBillId();
				request.getSession().setAttribute("billId", billId );
			}
			catch (AirSpaceException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			RequestDispatcher rd1 = request.getRequestDispatcher("Success.jsp");
			rd1.forward(request, response);
			return;

		default:
			break;
		}
	    
	    
		
		
		
	}

	
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		doGet(request, response);
	}

}
