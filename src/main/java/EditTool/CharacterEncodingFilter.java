package EditTool;

import javax.servlet.*;
import java.io.IOException;


// 请求过滤器，防止某些浏览器采用UTF-8之外的编码
// 这个需要在web.xml设置过
// 这个几乎就是专门为了<jsp:param name="keyword" value="<%=keyword%>"/>加的，否则传参就乱码
public class CharacterEncodingFilter implements Filter {
    public void init(FilterConfig filterConfig) throws ServletException {
        // 过滤器初始化代码
        // 例如，您可以在这里设置字符编码
        System.out.println("CharacterEncodingFilter initialized");
    }

    public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain)
            throws IOException, ServletException {
        // 设置请求和响应的编码
        request.setCharacterEncoding("UTF-8");
        response.setContentType("text/html; charset=UTF-8");

        // 继续处理请求
        chain.doFilter(request, response);
    }

    public void destroy() {
        // 过滤器销毁时调用
        System.out.println("CharacterEncodingFilter destroyed");
    }
}