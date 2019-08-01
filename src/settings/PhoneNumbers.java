package settings;

import java.io.IOException;
import java.util.List;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import mangodb.MangoDB;

/**
 * Servlet implementation class PhoneNumbers
 */
@WebServlet("/PhoneNumbers")
public class PhoneNumbers extends HttpServlet {
	private static final long serialVersionUID = 1L;
       
    /**
     * @see HttpServlet#HttpServlet()
     */
    public PhoneNumbers() {
        super();
        // TODO Auto-generated constructor stub
    }

	/**
	 * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		String phoneNumbersjson =  MangoDB.getDocumentWithQuery("sanhoo-home-security", "phone-numbers", "0", null,true, null, null);
		response.setContentType("application/json");
		response.getWriter().append(phoneNumbersjson);
	}

	/**
	 * @see HttpServlet#doPost(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		StringBuilder phoneReq = new StringBuilder();
        String s;
        while ((s = request.getReader().readLine()) != null) {
        	phoneReq.append(s);
        }
        
		 MangoDB.createNewDocumentInCollection("sanhoo-home-security", "phone-numbers", phoneReq.toString(), null);
			response.setContentType("application/json");
			response.getWriter().append(phoneReq.toString());
	}

}
