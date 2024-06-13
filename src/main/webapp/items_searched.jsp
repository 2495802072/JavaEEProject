<%--
  Created by IntelliJ IDEA.
  User: 123
  Date: 2024/6/8
  Time: 下午5:05
  To change this template use File | Settings | File Templates.
--%>
<%@ page contentType="text/html;charset=UTF-8" language="java" import="Classes.*,javax.servlet.http.HttpServletRequest" %>
<%@ page import="java.util.ArrayList" %>
<%@ page import="Managers.QuestionsManager" %>
<%@ page import="java.sql.SQLException" %>
<%@ page import="Managers.ListShower" %>

<%
    int lengthPerPage = 5;
    String keyword = request.getParameter("keyword");
    if (keyword == null) {
        keyword = "";
    }
    ArrayList<Questions> list_all = new ArrayList<Questions>();
    try {
        list_all = QuestionsManager.search_by_keyword(keyword);
    } catch (SQLException | ClassNotFoundException e) {
        throw new RuntimeException(e);
    }
    int pageForQuestions = Integer.parseInt(request.getParameter("pageForQuestions")==null?"1":request.getParameter("pageForQuestions"));
    ArrayList<Questions> list = ListShower.showQuestionsInPage(lengthPerPage,pageForQuestions,list_all); // 每页x项，第z页
    int pageCount = ListShower.GetTotalPageCount(lengthPerPage,list_all);// 每页x项，共y页
%>
<html>
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>搜索结果</title>

    <link rel="stylesheet" href="assets/bootstrap/css/bootstrap.min.css">
    <script src="assets/bootstrap/js/bootstrap.min.js"></script>
    <script src="assets/js/vue.min.js"></script>

    <link rel="stylesheet" href="assets/css/index.css">
</head>

<script>
    function page_add(page) {
        if(page < <%=pageCount%>){
            page+=1;
        }
        if (window.location.search !== '') {
            var currentUrl = window.location.href;

            // 然后解析查询字符串
            var queryString = currentUrl.split('?')[1]; // 如果存在查询字符串
            var params = queryString ? queryString.split('&') : [];

            // 修改 'question_id' 参数的值
            var Index = params.findIndex(function (param) {
                return param.startsWith('pageForQuestions=');
            });

            if (Index !== -1) {
                // 如果参数存在，修改它的值
                params[Index] = 'pageForQuestions=' + (page);
            } else {
                // 如果不存在，添加一个新的参数
                params.push('pageForQuestions=' + (page));
            }

            // 重新组合查询字符串
            var newQueryString = params.join('&');

            // 更新 URL
            if (newQueryString) {
                window.location.replace(currentUrl.replace(queryString, newQueryString));
            }
        }else{
            window.location.replace(window.location.href + "?pageForQuestions=" + (page))
        }
    }
    function page_minus(page) {
        if(page > 1){
            page-=1;
        }
        if (window.location.search !== '') {
            var currentUrl = window.location.href;
            // 然后解析查询字符串
            var queryString = currentUrl.split('?')[1]; // 如果存在查询字符串
            var params = queryString ? queryString.split('&') : [];
            // 修改 'question_id' 参数的值
            var Index = params.findIndex(function (param) {
                return param.startsWith('pageForQuestions=');
            });
            if (Index !== -1) {
                // 如果参数存在，修改它的值
                params[Index] = 'pageForQuestions=' + (page);
            } else {
                // 如果不存在，添加一个新的参数
                params.push('pageForQuestions=' + (page));
            }
            // 重新组合查询字符串
            var newQueryString = params.join('&');
            // 更新 URL
            if (newQueryString) {
                window.location.replace(currentUrl.replace(queryString, newQueryString));
            }
        } else {
            window.location.replace(window.location.href.replace(/\?.*$/, '') + "?pageForQuestions=" + (page));
        }
    }
</script>
<body>
    <ol id="search" class="list-group list-group-numbered">
        <%
            for(Questions Q:list){
                out.println("<li class='list-group-item d-flex justify-content-between align-items-start btn m-1' onclick='show_question("+Q.getId()+")'>");
                out.println("<div class='ms-2 me-auto text-truncate text-left'>");
                out.println("<div class='fw-bold text-truncate'>"+Q.getTitle()+"</div>");
                out.println(Q.getDescription());
                out.println("</div>");
                out.println("<span class='badge bg-primary rounded-pill'>1</span><!--新信息提示-->");
                out.println("</li>");
            }

        %>
    </ol>
    <hr style="margin-top: auto;">
    <div class="d-flex w-100 p-1 justify-content-around page_button" >
        <button onclick="page_minus(<%=pageForQuestions%>)">⇦</button>
        <button><%=pageForQuestions%>&nbsp; / &nbsp;<%=pageCount%> </button>
        <button onclick="page_add(<%=pageForQuestions%>)">⇨</button>
    </div>

<script>
    console.log(<%=keyword%>);
    console.log(<%=list_all.size()%>);
</script>
</body>

</html>
