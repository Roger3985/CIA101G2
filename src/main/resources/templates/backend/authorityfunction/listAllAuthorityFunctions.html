<%@ page contentType="text/html; charset=UTF-8" pageEncoding="Big5" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ page import="java.util.*" %>
<%@ page import="com.ren.authorityfunction.service.AuthorityFunctionServiceImpl" %>
<%@ page import="com.ren.authorityfunction.model.AuthorityFunctionVO" %>
<%-- 此頁練習採用 EL 的寫法取值 --%>

<%
    AuthorityFunctionServiceImpl authorityFunctionSvc = new AuthorityFunctionServiceImpl();
    List<AuthorityFunctionVO> list = authorityFunctionSvc.getAll();
    pageContext.setAttribute("list", list);
%>


<html>
<head>
    <title>功能權限資料 - listAllAuthorityFunction.jsp</title>

    <link
            href="https://cdn.jsdelivr.net/npm/bootstrap@5.3.0-alpha3/dist/css/bootstrap.min.css"
            rel="stylesheet"
            integrity="sha384-KK94CHFLLe+nY2dmCWGMq91rCGa5gtU4mk92HdvYe+M/SXH301p5ILy+dN9+nJOZ"
            crossorigin="anonymous"/>
    <link rel="stylesheet"
          href="https://cdnjs.cloudflare.com/ajax/libs/font-awesome/6.4.0/css/all.min.css"
          integrity="sha512-iecdLmaskl7CVkqkXNQ/ZH/XLlvWZOJyj7Yy7tcenmpD1ypASozpmT/E0iPtmFIB46ZmdtAc9eNBvH0H/ZpiBw=="
          crossorigin="anonymous" referrerpolicy="no-referrer"/>

    <style>
        table#table-1 {
            background-color: #CCCCFF;
            border: 2px solid black;
            text-align: center;
        }

        table#table-1 h4 {
            color: red;
            display: block;
            margin-bottom: 1px;
        }

        h4 {
            color: blue;
            display: inline;
        }
    </style>

    <style>
        table {
            width: 800px;
            background-color: white;
            margin-top: 5px;
            margin-bottom: 5px;
        }

        table, th, td {
            border: 1px solid #CCCCFF;
        }

        th, td {
            padding: 5px;
            text-align: center;
        }
    </style>

</head>
<body bgcolor='white'>

<h4>此頁練習採用 EL 的寫法取值:</h4>
<table id="table-1">
    <tr>
        <td>
            <h3>所有功能權限資料 - listAllAuthorityFunction.jsp</h3>
            <h4><a href="select_authorityFunction.jsp"><i class="fa-solid fa-house">回首頁</i></a></h4>
        </td>
    </tr>
</table>

<table>
    <tr>
        <th>功能權限編號</th>
        <th>功能權限名稱</th>
        <th>修改</th>
        <th>刪除</th>
    </tr>
    <%@ include file="page1.file" %>
    <c:forEach var="authorityFunctionVO" items="${list}" begin="<%=pageIndex%>" end="<%=pageIndex+rowsPerPage-1%>">

        <tr>
            <td>${authorityFunctionVO.authFuncNo}</td>
            <td>${authorityFunctionVO.authFuncInfo}</td>
            <td>
                <form METHOD="post" ACTION="<%=request.getContextPath()%>/authorityfunction/authorityFunction.do"
                      style="margin-bottom: 0px;">
                    <button type="submit">
                        <i class="fa-solid fa-pen-to-square"></i>
                        <input type="hidden" name="authFuncNo" value="${authorityFunctionVO.authFuncNo}">
                        <input type="hidden" name="action" value="getOne_For_Update">
                </form>
            </td>
            <td>
                <form id="deleteForm" method="post" action="<%=request.getContextPath()%>/authorityfunction/authorityFunction.do" style="margin-bottom: 0px;">
                    <button type="button" onclick="confirmDelete()">
                        <i class="fa-solid fa-trash-can"></i>
                        <input type="hidden" name="authFuncNo" value="${authorityFunctionVO.authFuncNo}">
                        <input type="hidden" name="action" value="delete">
                    </button>
                </form>
            </td>
        </tr>
    </c:forEach>
</table>
<%@ include file="page2.file" %>

<script>
    function confirmDelete() {
        if (confirm("確定要刪除嗎？")) {
            document.getElementById("deleteForm").submit(); // 提交表?
            alert("刪除成功！"); // 彈出成功訊息!
        }
    }
</script>

</body>
</html>