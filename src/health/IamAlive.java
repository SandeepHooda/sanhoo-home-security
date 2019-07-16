package health;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.communication.email.EmailAddess;
import com.communication.email.EmailVO;
import com.communication.email.MailService;
import com.communication.phone.call.MakeACall;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import health.vo.Device;
import mangodb.MangoDB;

/**
 * Servlet implementation class IamAlive
 */
@WebServlet("/IamAlive")
public class IamAlive extends HttpServlet {
	private static final long serialVersionUID = 1L;

    /**
     * Default constructor. 
     */
    public IamAlive() {
        // TODO Auto-generated constructor stub
    }

	/**
	 * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		String id = request.getParameter("id");
		String alarmTriggered = request.getParameter("alarmTriggered");
		
		String deviceJson = MangoDB.getDocumentWithQuery("sanhoo-home-security", "device-id", id, null,true, null, null);
		Gson  json = new Gson();
		Device device = json.fromJson(deviceJson, new TypeToken<Device>() {}.getType());
		String monitorStatus = ". This device is turned off for suspecious activity. ";
		if (device!= null) {
			device.setHealthCheckTime(new Date().getTime());
			if ("y".equalsIgnoreCase(alarmTriggered)){
				device.setAlarmTriggered(true);
				device.setLastAlarmTime(device.getHealthCheckTime());
			}else {
				device.setAlarmTriggered(false);
			}
					
			if (device.isAlarmTriggered() && device.isTurnOnHealthCheck()) {
				//Call
				String makeAcallError = callSandeepPhoneNumbers(device.get_id());
				//Send email
				notifySucpeciousActivityEmail(device, makeAcallError);
				monitorStatus = device.getAlermNotificationText();
				if (null != makeAcallError) {
					monitorStatus += makeAcallError;
				}
			}
			deviceJson = json.toJson(device, new TypeToken<Device>() {}.getType());
			
	        MangoDB.createNewDocumentInCollection("sanhoo-home-security", "device-id", deviceJson, null);
			response.getWriter().append("Device name ").append(device.getName()+monitorStatus);
		}else {
			response.setStatus(HttpServletResponse.SC_BAD_REQUEST);
	    	return;
		}
		
	}
	
	private String callSandeepPhoneNumbers(String id) {
		String makeAcallError = null;
		try {
			String fromPhoneNumber = "111111110"+id;
			MakeACall.call("919216411835", id,fromPhoneNumber);
		}catch(Exception e) {
			e.printStackTrace();
			makeAcallError  = e.getLocalizedMessage();
		}
		
		try {
			//MakeACall.call("919316046895", id);
		}catch(Exception e) {
			e.printStackTrace();
			makeAcallError  = e.getLocalizedMessage();
		}
		return makeAcallError;
	}
	
	private void  notifySucpeciousActivityEmail(Device device, String makeAcallError)  {
		EmailVO emalVO = new EmailVO();
		emalVO.setUserName("personal.reminder.notification@gmail.com");
		
		emalVO.setSubject("Suspecious Activity in House number 55 Sector 27");
		emalVO.setHtmlContent("<b>Suspecious Activity  : </b>"+device.getAlermNotificationText());
		if (null != makeAcallError) {
			emalVO.setHtmlContent(emalVO.getHtmlContent()+"<br/></br/> <b> could not call phone numbers <b/>"+makeAcallError);
		}
		EmailAddess from = new EmailAddess();
		from.setAddress(emalVO.getUserName());
		
		List<EmailAddess> receipients = new ArrayList<>();
		EmailAddess to = new EmailAddess();
		to.setAddress("sonu.hooda@gmail.com");
		emalVO.setFromAddress(from);
		receipients.add(to);
		emalVO.setToAddress(receipients);
		if (!MailService.sendSimpleMail(emalVO)) {
			System.err.println(" email not sent "+emalVO.getHtmlContent());
		}else {
			System.out.println(" email  sent "+emalVO.getHtmlContent());
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
