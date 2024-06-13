<%--
  Created by IntelliJ IDEA.
  User: 123
  Date: 2024/6/10
  Time: 下午6:21
  To change this template use File | Settings | File Templates.
--%>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ page import="Classes.*" %>
<%@ page import="java.util.ArrayList" %>
<%@ page import="Managers.QuestionsManager" %>
<%@ page import="java.sql.SQLException" %>
<%@ page import="java.util.Objects" %>
<%
//    目前定义的全局参数：[user (User) ， userid (int) ， Q (ArrayList<Questions>) ， keyword (String) ，组合查询id ，组合查询title ， 组合查询summary ， 组合查询likes ， 组合查询created_date]

    User user = (User)session.getAttribute("user");
    int userid = -1;
    if(user != null) {
        userid = user.getUid();
    }else{
        response.sendRedirect("index.jsp?login-err='something went wrong'");
    }
    String keyword = request.getParameter("keyword");
    if (keyword == null) {
        keyword = "";
    }

    //组合查询
    String id_s = request.getParameter("id");
    int id = -1; // 没有id筛选时为 -1
    if(!Objects.equals(id_s, "") && id_s!=null){
        id = Integer.parseInt(id_s);
    }
    String title = request.getParameter("title");
    String summary = request.getParameter("summary");
    String created_date = request.getParameter("created_date");
    if (title == null) {title = "";}
    if (summary == null) {summary = "";}
    if (created_date == null) {created_date = "";}


    ArrayList<Questions> Q = new ArrayList<Questions>();
    try {
        Q = QuestionsManager.search_in_owner(userid,id,title,summary,created_date);
    } catch (SQLException | ClassNotFoundException e) {
        throw new RuntimeException(e);
    }
%>
<!DOCTYPE html>
<html lang="en">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>个人空间</title>

    <link rel="stylesheet" href="assets/bootstrap/css/bootstrap.min.css">
    <script src="assets/bootstrap/js/bootstrap.min.js"></script>
    <script src="assets/js/jquery-3.5.1.slim.min.js"></script>
    <script src="assets/js/vue.min.js"></script>

    <link rel="stylesheet" href="assets/css/index.css">
    <link rel="stylesheet" href="assets/css/personalSpace.css">
    <script>
        var questionEdit = {
            ID: null,
            set_id: function(id) {
                this.ID = id;
            }
        }
        function goEdit() {
            var title = document.getElementById('edit_title').value;
            var text = document.getElementById('edit_text').value;
            window.location.href = "/JavaEEProject/questionEdit-servlet?qid="+questionEdit.ID+"&title="+title+"&description="+text;
        }


        function openEditor(id,title,description){
            questionEdit.set_id(id);
            $('#edit_title').val(title);
            $('#edit_text').val(description);
        }
    </script>
</head>
<body>
    <div id="app" class="p-5">

        <!-- 弹窗 -->
        <div id="OverlayBox" class="overlay" @mousedown.self="closePopup('OverlayBox')">
            <!-- 表单内容 -->
            <div class="form_overlay_box" @click.stop>
                <!-- 表单内容 -->
                <form>
                    <div class="mb-3">
                        <label for="InputTitle1" class="form-label">标题：</label>
                        <input id="edit_title" type="text" class="form-control" id="InputTitle1">
                    </div>
                    <div class="mb-3">
                        <label for="Textarea1" class="form-label">简述：</label>
                        <textarea id="edit_text" class="form-control" id="Textarea1" rows="3"></textarea>
                    </div>
                    <button type="button" class="btn btn-primary" onclick="goEdit()">修改</button>
                </form>
            </div>
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
                                <form class="input-group" action="personalSpace.jsp">
                                    <span class="input-group-text" id="search1">🔍</span>
                                    <input type="text" class="form-control" placeholder="关键词/标题" aria-label="关键词/标题"
                                           aria-describedby="search1" name="keyword" value="<%=keyword%>">
                                    <button type="submit" class="input-group-text btn btn-outline-light"
                                            aria-describedby="search1">搜索</button>
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

        <!-- 组合查询内容 -->
        <div class="form-container">
            <form action="personalSpace.jsp" method="get">
                <div class="form-group" style="flex:1">
                    <label for="id">编号</label>
                    <input type="text" id="id" name="id" value="<%=id_s==null?"":id_s%>">
                </div>
                <div class="form-group" style="flex:2">
                    <label for="title">标题</label>
                    <input type="text" id="title" name="title" value="<%=title%>">
                </div>
                <div class="form-group" style="flex:5">
                    <label for="summary">概述</label>
                    <input type="text" id="summary" name="summary" value="<%=summary%>">
                </div>
                <div class="form-group" style="flex:0.5">
                    <label for="likes">点赞数</label>
                    <input type="number" id="likes" name="likes" value="0" readonly >
                </div>
                <div class="form-group" style="flex:1.5">
                    <label for="created_date">创建日期</label>
                    <input type="date" id="created_date" name="created_date" value="<%=created_date%>">
                </div>
                <div class="form-group" style="flex:0.8">
                    <button type="submit">查询</button>
                </div>
            </form>
        </div>


        <table class="table table-striped table-bordered mt-5 w-100">
            <thead>
            <tr>
                <th>编号</th><th>标题</th><th>概述</th><th>点赞数</th><th>创建日期</th><th>操作</th>
            </tr>
            </thead>
            <tbody>
            <%
                for(Questions q:Q){
                    out.println("<tr><td>"+q.getId()+"</td>");
                    out.println("<td>"+q.getTitle()+"</td>");
                    out.println("<td>"+q.getDescription()+"</td>");
                    out.println("<td class='text-center'>暂时不做</td>");
                    out.println("<td>"+q.getCreatedDate()+"</td>");
                    out.println("<td>");
                    out.println("<button class='btn btn-primary btn-sm' onclick=\"openEditor("+q.getId()+",'"+q.getTitle()+"','"+q.getDescription()+"')\" @click=\"openPopup('OverlayBox')\">编辑</button>");
                    out.println("<a class=\"btn btn-danger btn-sm\" href=\"questionDelete-servlet?qid="+q.getId()+"\">删除</a>");
                    out.println("</td></tr>");
                }
            %>
            </tbody>
        </table>
    </div>

    <script src="assets/js/index.js"></script>
    <script>
        <%if(user != null){%>
        MainView.$data.displayButton = "isLogout";
        <%}else{ %>
        console.log("无用户");
        <%}%>
    </script>
</body>
</html>
