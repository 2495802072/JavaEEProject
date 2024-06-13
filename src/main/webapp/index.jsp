<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" import="Classes.User" %>
<%@ page import="Classes.Questions" %>
<%@ page import="Managers.QuestionsManager" %>
<%@ page import="java.sql.SQLException" %>
<%@ page import="java.net.URLEncoder" %>
<%@ page import="java.nio.charset.StandardCharsets" %>
<%@ page import="Classes.Answer" %>
<%@ page import="java.util.ArrayList" %>
<%@ page import="Managers.AnswersManager" %>
<%@ page import="Managers.UserManager" %>
<%
    User user = (User)session.getAttribute("user");

    boolean loginErr = request.getParameter("login-err") != null;
    // 出现 “登录错误”
    String keyword = request.getParameter("keyword");
    if (keyword == null) {
        keyword = "";
    }
    String question_id_s = request.getParameter("question_id");
    Questions Q = new Questions();
    ArrayList<Answer> A = new ArrayList<Answer>();
    if (question_id_s != null) {
        int question_id_i = Integer.parseInt(request.getParameter("question_id"));
        try {
            Q = QuestionsManager.search_by_id(question_id_i);
            A = AnswersManager.search_by_question_id(question_id_i);
        } catch (SQLException | ClassNotFoundException e) {
            throw new RuntimeException(e);
        }
    }
    int pageForQuestions = Integer.parseInt(request.getParameter("pageForQuestions")==null?"1":request.getParameter("pageForQuestions"));
%>
<!DOCTYPE html>
<html>
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>社区问答</title>

    <link rel="stylesheet" href="assets/bootstrap/css/bootstrap.min.css">
    <script src="assets/bootstrap/js/bootstrap.min.js"></script>
    <script src="assets/js/jquery-3.5.1.slim.min.js"></script>
    <script src="assets/js/vue.min.js"></script>
    
    <link rel="stylesheet" href="assets/css/index.css">
    <script>
        function show_question(question_id) {
            if (window.location.search !== '') {
                var currentUrl = window.location.href;

                // 然后解析查询字符串
                var queryString = currentUrl.split('?')[1]; // 如果存在查询字符串
                var params = queryString ? queryString.split('&') : [];

                // 修改 'question_id' 参数的值
                var Index = params.findIndex(function (param) {
                    return param.startsWith('question_id=');
                });

                if (Index !== -1) {
                    // 如果参数存在，修改它的值
                    params[Index] = 'question_id=' + question_id;
                } else {
                    // 如果不存在，添加一个新的参数
                    params.push('question_id=' + question_id);
                }

                // 重新组合查询字符串
                var newQueryString = params.join('&');

                // 更新 URL
                if (newQueryString) {
                    window.location.replace(currentUrl.replace(queryString, newQueryString));
                }
            }else{
                window.location.replace(window.location.href + "?question_id=" + question_id)
            }
        }
        $(document).ready(function () {
            console.log(<%=A.size()%>);
        })
    </script>
</head>

<body>
    <div id="app">

        <!-- 弹窗 -->
        <div id="OverlayBox" class="overlay" @mousedown.self="closePopup('OverlayBox')">
            <!-- 表单内容 -->
            <component v-bind:is="displayOverlay"></component>
        </div>

        <!--顶部导航栏-->
        <nav class="navbar fixed-top navbar-expand-md top_nav">
            <div class="container-fluid">
                <img class="navbar-brand" src="assets\Art\image\学无止境.png" alt="LOGO">
                <!-- 收束按钮-->
                <button style="background-color: #e1c68b;" class="navbar-toggler" type="button" data-bs-toggle="offcanvas"
                    data-bs-target="#offcanvasNavbar" aria-controls="offcanvasNavbar">
                    <span class="navbar-toggler-icon"></span>
                </button>

                <div class="offcanvas offcanvas-end" tabindex="-1" id="offcanvasNavbar" aria-labelledby="offcanvasNavbarLabel">
                    <div class="offcanvas-header">
                        <h5 class="offcanvas-title" id="offcanvasNavbarLabel">导航栏</h5>
                        <button type="button" class="btn-close" data-bs-dismiss="offcanvas" aria-label="Close"></button>
                    </div>
                    <div class="offcanvas-body">
                        <ul class="navbar-nav justify-content-end flex-grow-1">
                            <li class="nav-item">
                                <a class="nav-link" aria-current="page" href="index.jsp">首页</a>
                            </li>
                            <li class="nav-item">
                                <a class="nav-link" aria-current="page" href="#">其他</a>
                            </li>
                            <!--搜搜框-->
                            <li class="nav-item " style="flex: 1;">
                                <form class="input-group" action="index.jsp">
                                    <span class="input-group-text" id="search1">🔍</span>
                                    <input type="text" class="form-control" placeholder="关键词/标题" aria-label="关键词/标题" aria-describedby="search1" name="keyword" value="<%=keyword%>">
                                    <button type="submit" class="input-group-text btn btn-outline-light" aria-describedby="search1">搜索</button>
                                </form>
                            </li>
                            <li class="nav-item">
                                <component v-bind:is="displayButton"></component>
                            </li>
                            <li class="nav-item head_portrait">
                                <img src="assets/Art/image/默认头像.png" alt="头像">
                            </li>
                        </ul>
                    </div>
                </div>
            </div>
        </nav>
        <!-- 右侧主体box -->
        <div class="main_box">
            <!-- 左半 -->
            <div class="main_left">
                <!-- 列表 这有空再尝试使用iframe -->
                <jsp:include page="items_searched.jsp">
                    <jsp:param name="keyword" value="<%=keyword%>"/>
                    <jsp:param name="pageForQuestions" value="<%=pageForQuestions%>"/>
                </jsp:include>

            </div>
            <!-- 右半 -->
            <div class="main_right">
                <div class="title d-flex justify-content-center flex-column">
                    <label class="text-md-start text-sm-center fs-4 text-warning-emphasis text-truncate" id="right_box_title">
                        <% if (question_id_s == null)
                        {out.println("Good Day");
                        }else
                        {out.println(Q.getDescription());}
                        %>
                    </label>
                </div>
                <!-- 内容/细节 -->
                <div class="details">
                    <div class="welcome" <% if (question_id_s != null) {out.println(" style='display: none'");} %> >
                        <div class="clock_face1"><div class="clock_face2">
                            <div class="time-now">
                                <div>{{ currentDate }}</div>
                                <div>{{ currentTime }}</div>
                            </div>
                        </div></div>
                    </div>
                    <div class="w-100 h-100 answers_list d-flex" style="background-color: #4c515d;" >
                        <%
                            if (!A.isEmpty()){
                                for(Answer a:A){
                                    out.println("<div class='answer_item'>");

                                    try {
                                        out.println("<h4>"+ UserManager.getUserName(a.getOwner_id())+"</h4>");
                                    } catch (SQLException | ClassNotFoundException e) {
                                        out.println("<h4>名称获取失败</h4>");
                                    }

                                    out.println("<p class='text-truncate'>创建时间："+a.getDate()+"</p>");
                                    out.println("<hr>");
                                    out.println("<p>"+a.getAnswer()+"</p>");
                                    out.println("</div>");
                                }
                            }else{
                                out.println("<div class='clock_face1 mt-8'><div class='clock_face2'><div class='time-now'>");
                                out.println("<div>暂无回答</div>");
                                out.println("</div></div></div>");
                            }
                        %>
                    </div>
                </div>
                <!-- 输入框 -->
                <div class="answer_line">
                    <form class="input-group">
                        <span class="input-group-text" id="answer_line1">※</span>
                        <input type="text" class="form-control" placeholder="参与回答" aria-label="参与回答" aria-describedby="answer_line1" name="answer_text">
                        <button type="submit" class="input-group-text btn btn-outline-dark" aria-describedby="answer_line1"
                                style="border-radius: 0 0 27px;">Enter</button>
                    </form>
                </div>
            </div>
        </div>
        <!-- 左侧box -->
        <div class="menu">
            <div class="menu_div1">
                <div class="btn-group-vertical w-100" role="group" aria-label="Vertical radio toggle button group">
                    <input type="radio" class="btn-check" name="vbtn-radio" id="vbtn-radio1" autocomplete="off" checked>
                    <label class="btn btn-outline-light" for="vbtn-radio1">热门问题</label>
                    <input type="radio" class="btn-check" name="vbtn-radio" id="vbtn-radio2" autocomplete="off">
                    <label class="btn btn-outline-light" for="vbtn-radio2" @click="openURL('personalSpace.jsp')">我的提问</label>
                    <input type="radio" class="btn-check" name="vbtn-radio" id="vbtn-radio3" autocomplete="off">
                    <label class="btn btn-outline-light" for="vbtn-radio3">我的回答</label>
                </div>
                ※注：这里原本打算按钮切换右侧div以实现功能的切换，但是因为Vue create cli的工程文件文件损坏，目前仅作按钮使用※
                <div style="flex: 10;"></div><!--隔断-->
                <div class="w-100">
                    <button class="btn btn-outline-warning w-100" @click="openPopup('OverlayBox'),showOverlay('newQuestion')">＋&nbsp;新建&nbsp;&nbsp;&nbsp;</button>
                </div>
            </div>
        </div>
    </div>

    <!--版权声明--->
    <div class="copyright">
        &copy; 2024 秦彦悦（顾霖轩） 版权所有
    </div>

    <!--Vue-->
    <script src="assets/js/index.js"></script>
    <script>
        <%if(user != null){%>
        MainView.$data.displayButton = "isLogout";
        console.log("登陆成功");
        <%}else if(loginErr){ %>
        alert("用户获取失败");
        <% }else{ %>
        console.log("无用户");
        <%}%>
    </script>
</body>

</html>