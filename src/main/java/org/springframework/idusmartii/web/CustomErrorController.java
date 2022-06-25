package org.springframework.idusmartii.web;

import javax.servlet.RequestDispatcher;
import javax.servlet.http.HttpServletRequest;

import org.springframework.boot.web.servlet.error.ErrorController;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
public class CustomErrorController implements ErrorController {

	@Override
	public String getErrorPath() {
		// TODO Auto-generated method stub
		return "exception";
	}
	
	@RequestMapping("/error")
	public String handleError(HttpServletRequest request,  Exception ex) {
		Object status = request.getAttribute(RequestDispatcher.ERROR_STATUS_CODE);
	   	
	   	if(status != null) {
	   		int statusCode = Integer.parseInt(status.toString());
	   		if (statusCode == HttpStatus.NOT_FOUND.value()) {
	   			return "error-404";
	   		} else if (statusCode == HttpStatus.INTERNAL_SERVER_ERROR.value()) {
	   			return "error-500";
	   		} else if (statusCode == HttpStatus.FORBIDDEN.value()) {
	   			return "error-403";
	   		}
	   	}
	   	
        request.setAttribute("javax.servlet.error.request_uri", request.getPathInfo());
        request.setAttribute("javax.servlet.error.status_code", 400);
        request.setAttribute("exeption", ex);
        return "exception";
	}
	
}
