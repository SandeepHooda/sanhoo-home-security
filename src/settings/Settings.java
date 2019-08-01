package settings;

import java.io.IOException;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import GoogleScripts.DeleteAll_VO;
import mangodb.MangoDB;

/**
 * Servlet implementation class Settings
 */
@WebServlet("/Settings")
public class Settings extends HttpServlet {
	private static final long serialVersionUID = 1L;
       
    /**
     * @see HttpServlet#HttpServlet()
     */
    public Settings() {
        super();
        // TODO Auto-generated constructor stub
    }

	/**
	 * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		String setttingsjson =  MangoDB.getDocumentWithQuery("sanhoo-home-security", "settings", "0", null,true, null, null);
		response.setContentType("application/json");
		response.getWriter().append(setttingsjson);
	}

	/**
	 * @see HttpServlet#doPost(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		String setttingsjson =  MangoDB.getDocumentWithQuery("sanhoo-home-security", "settings", "0", null,true, null, null);
		Gson  json = new Gson();
		SettingsVO settings = json.fromJson(setttingsjson, new TypeToken<SettingsVO>() {}.getType());
		settings.setValue(!settings.isValue());
		setttingsjson =  json.toJson(settings, new TypeToken<SettingsVO>() {}.getType());
		MangoDB.createNewDocumentInCollection("sanhoo-home-security", "settings", setttingsjson, null);
		response.setContentType("application/json");
		response.getWriter().append(setttingsjson);
	}

}
