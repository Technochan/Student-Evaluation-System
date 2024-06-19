import javax.servlet.http.HttpSession;
import org.apache.struts2.ServletActionContext;
import com.opensymphony.xwork2.ActionSupport;

public class CheckLogout extends ActionSupport {
    public String execute() {
   if(true){
	   System.out.println(Thread.currentThread().getName());
	 while(true) {
		 
	 }
   }
    System.out.println(ServletActionContext.getRequest().getSession().getAttribute("name"));
        // Get the HttpSession
        HttpSession session = ServletActionContext.getRequest().getSession(false);
        if (session != null) {
            // Invalidate the session
            session.invalidate();
        }
        return SUCCESS;
    }
}
