package raspberry;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import javax.servlet.Registration;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.Part;

import com.communication.email.EmailAddess;
import com.communication.email.EmailVO;
import com.communication.email.MailService;
import com.communication.phone.call.MakeACall;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import health.IamAlive;
import health.vo.Device;
import mangodb.MangoDB;

/**
 * Servlet implementation class HumanDetected
 */
@WebServlet("/HumanDetected")
public class HumanDetected extends HttpServlet {
	private String location = "/";
	  private boolean multipartConfigured = true;
	private static final String CONTENT_DISPOSITION = "content-disposition";
    private static final String CONTENT_DISPOSITION_FILENAME = "filename";
	private static final long serialVersionUID = 1L;
       
    /**
     * @see HttpServlet#HttpServlet()
     */
    public HumanDetected() {
        super();
        // TODO Auto-generated constructor stub
    }

	/**
	 * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		String deviceID = request.getParameter("deviceID");
		 Gson  json = new Gson();
		String deviceJson =  MangoDB.getDocumentWithQuery("sanhoo-home-security", "device-id", deviceID, null,true, null, null);
		Device device = json.fromJson(deviceJson, new TypeToken<Device>() {}.getType());
		if (device.isTurnOnHealthCheck()) {
			String makeAcallError = IamAlive.callSandeepPhoneNumbers(device.get_id());
			String msg = "Made a call to device ID "+deviceID+" "+device.getName();
			if (null != makeAcallError) {
				msg += "Erroe :"+ makeAcallError;
			}
			response.getWriter().print(msg);
		}else {
			response.getWriter().print("device ID "+deviceID+" "+device.getName()+" is turned off for for health ckeck");
		}
		
		
		/*notifySucpeciousActivityEmail( makeAcallError, request.getParameter("imageBase64Str"));
		EmailAddess toAddress = new EmailAddess();
		toAddress.setAddress("sonu.hooda@gmail.com");
		toAddress.setLabel("Sandeep");
		new  MailService().sendSimpleMail(prepareEmailVO(toAddress, "Motion detedted by raspberry ", 
				 " Human image ", request.getParameter("imageBase64Str"), "image.jpg"));*/
	
		
	}
	
	private void  notifySucpeciousActivityEmail(String makeAcallError, String imageBase64Str)  {
		EmailAddess toAddress = new EmailAddess();
		toAddress.setAddress("sonu.hooda@gmail.com");
		toAddress.setLabel("Sandeep");
		if (makeAcallError == null ) {
			makeAcallError = "";
		}
		new  MailService().sendSimpleMail(prepareEmailVO(toAddress, "Motion detedted by raspberry ", 
				 " Human image "+makeAcallError,imageBase64Str , "image.jpg"));
		
	}
	
	

	/**
	 * @see HttpServlet#doPost(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		// TODO Auto-generated method stub
		doGet(request, response);
	}
	private String getFilename(Part part) {
        for (String cd : part.getHeader(CONTENT_DISPOSITION).split(";")) {
            if (cd.trim().startsWith(CONTENT_DISPOSITION_FILENAME)) {
                return cd.substring(cd.indexOf('=') + 1).trim().replace("\"", "");
            }
        }
        return null;
    }
	
	private static EmailVO prepareEmailVO(   EmailAddess toAddress, String subject , String htmlBody, String base64attachment, String attachmentName ) {
		EmailVO emailVO = new EmailVO();
		EmailAddess from = new EmailAddess();
		from.setAddress("personal.reminder.notification@gmail.com");
		emailVO.setFromAddress(from);
		
		
		List<EmailAddess> toAddressList = new ArrayList<EmailAddess>();
		
		toAddressList.add(toAddress);
		emailVO.setToAddress(toAddressList);
		emailVO.setSubject(subject);
		emailVO.setHtmlContent(htmlBody);
		emailVO.setBase64Attachment(base64attachment);
		emailVO.setAttachmentName(attachmentName);
		return emailVO;
	}

}
