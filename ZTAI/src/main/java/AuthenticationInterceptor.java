import java.util.Map;

import com.opensymphony.xwork2.ActionContext;
import com.opensymphony.xwork2.ActionInvocation;
import com.opensymphony.xwork2.interceptor.AbstractInterceptor;

public class AuthenticationInterceptor extends AbstractInterceptor {
	
    @Override
    public String intercept(ActionInvocation invocation) throws Exception {
        Map<String, Object> session = ActionContext.getContext().getSession();
        String user = (String) session.get("user");
       System.out.println("yes geting");
        if (user == null) {
            // User is not logged in, redirect to login page
            return "login";
        } else {
            // User is logged in, proceed with the action
            return invocation.invoke();
        }
    }
}
