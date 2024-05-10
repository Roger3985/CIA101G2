<%@ page contentType="text/html; charset=UTF-8" pageEncoding="Big5" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>

<html>
<head>
    <title>fallElove Administrator Manage</title>

    <link
            href="https://cdn.jsdelivr.net/npm/bootstrap@5.3.0-alpha3/dist/css/bootstrap.min.css"
            rel="stylesheet"
            integrity="sha384-KK94CHFLLe+nY2dmCWGMq91rCGa5gtU4mk92HdvYe+M/SXH301p5ILy+dN9+nJOZ"
            crossorigin="anonymous"/>
    <link rel="stylesheet"
          href="https://cdnjs.cloudflare.com/ajax/libs/font-awesome/6.4.0/css/all.min.css"
          integrity="sha512-iecdLmaskl7CVkqkXNQ/ZH/XLlvWZOJyj7Yy7tcenmpD1ypASozpmT/E0iPtmFIB46ZmdtAc9eNBvH0H/ZpiBw=="
          crossorigin="anonymous" referrerpolicy="no-referrer"/>

    <link rel="stylesheet" href="<%=request.getContextPath()%>/css/components/hamburger.css"/>

    <style>
        table#table-1 {
            width: 450px;
            background-color: #CCCCFF;
            margin-top: 5px;
            margin-bottom: 10px;
            border: 3px ridge Gray;
            height: 80px;
            text-align: center;
        }

        table#table-1 h4 {
            color: blue;
            display: block;
            margin-bottom: 1px;
        }

        h4 {
            color: blue;
            display: inline;
        }
    </style>

</head>

<body bgcolor='white'>

<!-- 導覽列 -->
<nav>
    <div>FallElove</div>
    <input id="hamburger" type="checkbox">
    <label class="hamburger-icon" for="hamburger">
        <div class="bar"></div>
    </label>
    <ul class="menu">
        <li><a href="<%=request.getContextPath()%>/administrator/select_administrator.jsp">管理員</a></li>
        <li><a href="<%=request.getContextPath()%>/admauthority/select_admAuthority.jsp">管理員權限</a></li>
        <li><a href="<%=request.getContextPath()%>/authorityfunction/select_authorityFunction.jsp">功能權限</a></li>
        <li><a href="<%=request.getContextPath()%>/title/select_title.jsp">職位</a></li>
        <li><a href="<%=request.getContextPath()%>/product/select_product.jsp">商品</a></li>
        <li><a href="<%=request.getContextPath()%>/productcategory/select_productCategory.jsp">商品種類</a></li>
    </ul>
</nav>

<table id="table-1">
    <tr>
        <td>
            <h3>管理員管理頁面</h3>
            <h4>
                <a class="icon-link" href="select_administrator.jsp">
                    <i class="fa-solid fa-house">首頁</i>
                </a>
            </h4>
        </td>
    </tr>
</table>

<p>This is the Home page for FallELove Administrator Management</p>

<h3>資料查詢:</h3>

<%-- 錯誤表列 --%>
<c:if test="${not empty errorMsgs}">
    <font style="color: red">請修正以下錯誤:</font>
    <ul>
        <c:forEach var="message" items="${errorMsgs}">
            <li style="color: red">${message}</li>
        </c:forEach>
    </ul>
</c:if>

<ul>
    <li><a href='listAllAdministrator.jsp'>List</a> all Administrators. <br> <br></li>


    <li>
        <FORM METHOD="post" ACTION="administrator.do">
            <b>輸入管理員編號 (從1開始):</b> <input type="text" name="admNo">
            <input type="hidden" name="action" value="getOne_For_Display">
            <input type="submit" value="送出">
        </FORM>
    </li>

    <jsp:useBean id="administratorSvc" scope="page"
                 class="com.ren.administrator.service.AdministratorServiceImpl"/>

    <li>
        <FORM METHOD="post" ACTION="administrator.do">
            <b>選擇管理員編號:</b> <select size="1" name="admNo">
            <c:forEach var="adminitratorVO" items="${administratorSvc.all}">
            <option value="${adminitratorVO.admNo}">${adminitratorVO.admNo}
                </c:forEach>
        </select> <input type="hidden" name="action" value="getOne_For_Display">
            <input type="submit" value="送出">
        </FORM>
    </li>

    <li>
        <FORM METHOD="post" ACTION="administrator.do">
            <b>選擇管理員名稱:</b> <select size="1" name="admNo">
            <c:forEach var="adminitratorVO" items="${administratorSvc.all}">
            <option value="${adminitratorVO.admNo}">${adminitratorVO.admName}
                </c:forEach>
        </select> <input type="hidden" name="action" value="getOne_For_Display">
            <input type="submit" value="送出">
        </FORM>
    </li>
</ul>


<h3>管理員新增</h3>
<ul>
    <li><a href='addAdministrator.jsp'>Add</a> a new Product.</li>
</ul>
<h3>大頭貼上傳</h3>
<ul>
    <li><a href='upload.jsp'>Add</a> photoSticker.</li>
</ul>

</body>
</html>