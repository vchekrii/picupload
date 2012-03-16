package ua.dod.picload.utils;

import java.io.IOException;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.FilterConfig;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;

public class DisableEtagFilter implements Filter{

	@Override
	public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain) throws IOException, ServletException {
		HttpServletRequest req = (HttpServletRequest)request;
		if(req.getRequestURI().contains("_x106.jpg")){
			((HttpServletResponse) response).setHeader("Cache-Control", "no-cache, no-store, must-revalidate"); // HTTP 1.1.
			((HttpServletResponse) response).setHeader("Pragma", "no-cache"); // HTTP 1.0.
			((HttpServletResponse) response).setDateHeader("Expires", 0); // Proxies.
		}
		chain.doFilter(request, response);
	}

	@Override
	public void destroy() {}
	
	@Override
	public void init(FilterConfig filterConfig) throws ServletException {}
}
