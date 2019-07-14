package health;

import java.io.IOException;
import java.io.PrintWriter;
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
 * Servlet implementation class GetDevices
 */
@WebServlet("/Devices")
public class Devices extends HttpServlet {
	private static final long serialVersionUID = 1L;
       
    /**
     * @see HttpServlet#HttpServlet()
     */
    public Devices() {
        super();
        // TODO Auto-generated constructor stub
    }

	/**
	 * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		String deviceJson = "["+ MangoDB.getDocumentWithQuery("sanhoo-home-security", "device-id", null, null,true, null, null)+"]";
		Gson  json = new Gson();
		List<Device> allDevices = json.fromJson(deviceJson, new TypeToken<List<Device>>() {}.getType());
		Iterator<Device> itr = allDevices.iterator();
		while(itr.hasNext()) {
			Device d = itr.next();
			if ("0".equals(d.get_id())) {
				itr.remove();
				break;
			}
		}
		deviceJson = json.toJson(allDevices, new TypeToken<List<Device>>() {}.getType());
		response.setContentType("application/json");
		response.setCharacterEncoding("UTF-8");
		PrintWriter out = response.getWriter();
		out.print(deviceJson);
	}

	/**
	 * @see HttpServlet#doPost(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		 StringBuilder sb = new StringBuilder();
	        String s;
	        while ((s = request.getReader().readLine()) != null) {
	            sb.append(s);
	        }
	     
	       
	        MangoDB.createNewDocumentInCollection("sanhoo-home-security", "device-id", sb.toString(), null);
	        doGet(request,  response);
	}

}
