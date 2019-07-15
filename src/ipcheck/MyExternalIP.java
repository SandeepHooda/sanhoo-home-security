package ipcheck;

import java.io.IOException;
import java.io.PrintWriter;
import java.util.Date;
import java.util.Iterator;
import java.util.List;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import health.vo.Device;
import mangodb.MangoDB;

/**
 * Servlet implementation class MyExternalIP
 */
@WebServlet("/MyExternalIP")
public class MyExternalIP extends HttpServlet {
	private static final long serialVersionUID = 1L;
       
    /**
     * @see HttpServlet#HttpServlet()
     */
    public MyExternalIP() {
        super();
        // TODO Auto-generated constructor stub
    }

	/**
	 * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		String ip= request.getParameter("ip");
		if (null != ip ) {
			String ipJson = "{\"_id\": \"ip\",\"myIP\": \""+ip+"\", \"time\": \""+(new Date().getTime())+"\"}";
			MangoDB.createNewDocumentInCollection("sanhoo-home-security", "external-ip", ipJson, null);
			response.getWriter().append("Your IP is : ").append(ip);
		}else {
			String ipJson =  MangoDB.getDocumentWithQuery("sanhoo-home-security", "external-ip", null, null,true, null, null);
			
			response.setContentType("application/json");
			response.setCharacterEncoding("UTF-8");
			PrintWriter out = response.getWriter();
			out.print(ipJson);
		}
		
	}

	/**
	 * @see HttpServlet#doPost(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		// TODO Auto-generated method stub
		doGet(request, response);
	}

}
