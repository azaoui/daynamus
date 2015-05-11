package com.daynamus.web;

import java.io.IOException;
import java.io.PrintWriter;
import java.util.List;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.exoplatform.services.security.ConversationState;
import org.exoplatform.web.filter.Filter;

import com.daynamus.service.UserService;

public class MySpacesFilter implements Filter {
    public void doFilter(ServletRequest servletRequest, ServletResponse servletResponse, FilterChain filterChain) throws IOException, ServletException {
        HttpServletRequest httpServletRequest = (HttpServletRequest) servletRequest;
        HttpServletResponse httpServletResponse = (HttpServletResponse) servletResponse;
        String initialURI = httpServletRequest.getRequestURI();
        ConversationState state = ConversationState.getCurrent();
        String userId = (state != null) ? state.getIdentity().getUserId() : null;
        if(userId!=null &&!userId.equals("__anonim")&&UserService.connectedusermap.get(userId)!=null&&UserService.connectedusermap.get(userId).equals("login"))
        {UserService.connectedusermap.put(userId, "login"); 
            List<String> mySpaceList = CustomLoginListener.showMySpaceURLs(userId);
           if (mySpaceList.size()==0)
        	   filterChain.doFilter(servletRequest, servletResponse);
            if(mySpaceList.size()>0){
            	String requestUrl = httpServletRequest.getRequestURL().toString();
            	String contextPath = httpServletRequest.getContextPath();
            	String path = requestUrl.substring(0, requestUrl.indexOf(contextPath));
                if (mySpaceList.size()==1){
                    httpServletResponse.sendRedirect(path+"/"+mySpaceList.get(0));
                    UserService.connectedusermap.put(userId, "logged");
                    return;
                }
                httpServletResponse.setContentType("text/html;charset=UTF-8");
                PrintWriter out = servletResponse.getWriter();
                out.println("<!DOCTYPE html>");  // HTML 5
                out.println("<html><head>");
                out.println("<meta http-equiv='Content-Type' content='text/html; charset=UTF-8'>");
                out.println("<body>");
                out.println("<br/>My spaces list :<br/><hr/>");
            	for (String spaceName : mySpaceList) {
            		String fullURL=path+"/"+spaceName;
            		out.println("<a href='"+fullURL+"'/>"+spaceName.substring(spaceName.lastIndexOf("/")+1)+"<br/>");
            	}
            	 out.println("</body></html>");
            }
            UserService.connectedusermap.put(userId, "logged");
            return;
        }
        filterChain.doFilter(servletRequest, servletResponse);
    }
}



    
