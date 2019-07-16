package health;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.communication.email.EmailAddess;
import com.communication.email.EmailVO;
import com.communication.email.MailService;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import health.vo.Device;
import mangodb.MangoDB;

/**
 * Servlet implementation class HealthCheck
 */
@WebServlet("/HealthCheck")
public class HealthCheck extends HttpServlet {
	private static final long serialVersionUID = 1L;
       
    /**
     * @see HttpServlet#HttpServlet()
     */
    public HealthCheck() {
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
		response.getWriter().append(notifyHealthStatus(allDevices));
	}

	/**
	 * @see HttpServlet#doPost(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		// TODO Auto-generated method stub
		doGet(request, response);
	}
	
	private static String notifyHealthStatus(List<Device> allDevices)  {
		
		    if (null !=allDevices && allDevices.size()>0 ) {
		    	Set<String> healthy = new HashSet<String>();
		    	Set<String> notWell = new HashSet<String>();
		    	Set<String> sleepMode = new HashSet<String>();
		    	for (Device aDevice: allDevices) {
		    		if ("0".equals(aDevice.get_id())) {
		    			if(!aDevice.isTurnOnHealthCheck()) {
		    				return "Please turn on \"TurnOnHealthCheck\" flag for device id \"0\" on \"sanhoo-home-security@device-id \"";//No Need to send health status email;
		    			}
		    		}else if(aDevice.isTurnOnHealthCheck()) {
		    			if ( (new Date().getTime() -aDevice.getHealthCheckTime()) > 60000*5) {//No status update since last five minute
		    				notWell.add(aDevice.getName());//Un plugged and device is being monitored
		    			}else {
		    				healthy.add(aDevice.getName());
		    			}
		    		}else {
		    			sleepMode.add(aDevice.getName());
		    		}
		    		
		    	}
		    	if (notWell.size() < 1) {
		    		return "All devices that are not snoozed are working good.";
		    	}
		    	EmailVO emalVO = new EmailVO();
				emalVO.setUserName("personal.reminder.notification@gmail.com");
				
				emalVO.setSubject("Health status of Monitoring devices");
				emalVO.setHtmlContent("<b>Device Not working : </b>"+notWell+"<br/><br/><b>Device In good health:</b> "+healthy+
						"<br/><br/><b>Sleep mode :</b> "+sleepMode+"<br/><br/>"+
						"If you don't want to receive these emails then please turn off \"TurnOnHealthCheck\" flag for device id \"0\" on \"sanhoo-home-security@device-id \"");
				EmailAddess from = new EmailAddess();
				from.setAddress(emalVO.getUserName());
				
				List<EmailAddess> receipients = new ArrayList<>();
				EmailAddess to = new EmailAddess();
				to.setAddress("sonu.hooda@gmail.com");
				emalVO.setFromAddress(from);
				receipients.add(to);
				emalVO.setToAddress(receipients);
				if (!MailService.sendSimpleMail(emalVO)) {
					return " health check email not working";
				}else {
					return "Health email sent";
				}
		    }else {
		    	return "No devices found to monitor";
		    }
	}

}
