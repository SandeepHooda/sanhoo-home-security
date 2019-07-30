package GoogleScripts;

import java.io.IOException;
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
 * Servlet implementation class DeleteAllEmails
 */
@WebServlet("/DeleteAllEmails")
public class DeleteAllEmails extends HttpServlet {
	private static final long serialVersionUID = 1L;
       
    /**
     * @see HttpServlet#HttpServlet()
     */
    public DeleteAllEmails() {
        super();
        // TODO Auto-generated constructor stub
    }

	/**
	 * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		String deleteJson =  MangoDB.getDocumentWithQuery("sanhoo-home-security", "delete-all-emails", "0", null,true, null, null);
		Gson  json = new Gson();
		DeleteAll_VO vo = json.fromJson(deleteJson, new TypeToken<DeleteAll_VO>() {}.getType());
		if (vo.isDeleteAll()) {
			vo.setDeleteAll(false);
			deleteJson =  json.toJson(vo, new TypeToken<DeleteAll_VO>() {}.getType());
			MangoDB.createNewDocumentInCollection("sanhoo-home-security", "delete-all-emails", deleteJson, null);
			response.getWriter().append("Delete all emails ");
		}else {
			response.setStatus(HttpServletResponse.SC_ACCEPTED);
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
