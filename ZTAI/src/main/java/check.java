import com.opensymphony.xwork2.ActionSupport;
import java.util.Map;

import javax.servlet.http.HttpSession;

import org.apache.struts2.ServletActionContext;
import org.apache.struts2.interceptor.SessionAware;

public class check extends ActionSupport {
    private String username;
    private String password;
    private String name;
    
    
    // Getters and setters for username and password
    public String getUsername() {
        return username;
    }

    public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public void setUsername(String username) {
        this.username = username;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

  

    @Override
    public String execute() throws Exception {
    	 System.out.println(Thread.currentThread().getName());
        // Simple authentication logic (replace with real authentication)
        if ("admin".equals(username) && "pass".equals(password)) {
            // Store user information in session
        	HttpSession session = ServletActionContext.getRequest().getSession();
        
        	session.setAttribute("name",name);
            System.out.println("hai");
            return SUCCESS;
        } else {
        	System.out.println("not foud");
            return INPUT;
        }
    }
}
