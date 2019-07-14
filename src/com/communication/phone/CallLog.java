package com.communication.phone;

import java.io.IOException;

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
 * Servlet implementation class CallLog
 */
@WebServlet("/CallLog")
public class CallLog extends HttpServlet {
	private static final long serialVersionUID = 1L;
       
    /**
     * @see HttpServlet#HttpServlet()
     */
    public CallLog() {
        super();
        // TODO Auto-generated constructor stub
    }

	/**
	 * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse response)
	 */
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		response.setContentType("application/xml");
		String id = request.getParameter("id");
		String deviceJson = MangoDB.getDocumentWithQuery("sanhoo-home-security", "device-id", id, null,true, null, null);
		Gson  json = new Gson();
		Device device = json.fromJson(deviceJson, new TypeToken<Device>() {}.getType());
		String messageToSpeak ="There is an suspicious activity on house number 55 Sector 27. Please call police to check.. I repeat , There is an suspicious activity on house number 55 Sector 27. Please call police to check";
		if (null != device && null != device.getAlermNotificationText() && device.getAlermNotificationText().length()> 10) {
			device.setAlermNotificationText(device.getAlermNotificationText() + "Please check security camera and call police.");
			messageToSpeak = device.getAlermNotificationText()+". I repeat , "+device.getAlermNotificationText();
		}
		
		String message = "<Response><Speak>"+messageToSpeak+".</Speak></Response>";
		response.getWriter().print(message);
	}

	/**
	 * @see HttpServlet#doPost(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		// TODO Auto-generated method stub
		doGet(request, response);
	}

}
