<%@ page contentType="text/html; charset=UTF-8" pageEncoding="Big5" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ page import="java.util.*" %>
<%@ page import="com.ren.administrator.service.*" %>
<%@ page import="com.ren.administrator.model.AdministratorVO" %>
<%-- 此頁暫練習採用 Script 的寫法取值 --%>

<%
    AdministratorVO administratorVO = (AdministratorVO) request.getAttribute("administratorVO"); //ProductServlet.java(Concroller), 存入req的administratorVO物件
    String photoBase64 = (String) request.getAttribute("photoBase64");
%>

<html>
<head>
    <title>商品資料 - listOneAdministrator.jsp</title>

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
            width: 600px;
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

<h4>此頁暫練習採用 Script 的寫法取值:</h4>
<table id="table-1">
    <tr>
        <td>
            <h3>單筆商品資料</h3>
            <h4><a class="icon-link" href="select_administrator.jsp">
                <i class="fa-solid fa-house">首頁</i>
            </a></h4>
        </td>
    </tr>
</table>

<div style="display: flex; flex-direction: row;">
    <table>
        <tr>
            <th>管理員編號</th>
            <th>管理員姓名</th>
            <th>管理員狀態</th>
            <th>管理員Email</th>
            <th>職位編號</th>
            <th>入職時間</th>
            <th>管理員大頭貼</th>
            <th>修改</th>
            <th>刪除</th>
        </tr>
        <tr>
            <td>${administratorVO.admNo}</td>
            <td>${administratorVO.admName}</td>
            <td>${administratorVO.admStat}</td>
            <td>${administratorVO.admEmail}</td>
            <td>${administratorVO.titleNo}</td>
            <td>${administratorVO.admHireDate}</td>
            <td><c:if test="${administratorVO.admPhoto != null}"><img src="data:image/jpeg;base64,${photoBase64}"></c:if></td>
            <td>
                <form METHOD="post" ACTION="<%=request.getContextPath()%>/administrator/administrator.do"
                      style="margin-bottom: 0px;">
                    <button type="submit">
                        <i class="fa-solid fa-pen-to-square"></i>
                        <input type="hidden" name="admNo" value="${administratorVO.admNo}">
                        <input type="hidden" name="action" value="getOne_For_Update">
                    </button>
                </form>
            </td>
            <td>
                <form id="deleteForm" method="post" action="<%=request.getContextPath()%>/administrator/administrator.do" style="margin-bottom: 0px;">
                    <button type="button" onclick="confirmDelete()">
                        <i class="fa-solid fa-trash-can"></i>
                        <input type="hidden" name="admNo" value="${administratorVO.admNo}">
                        <input type="hidden" name="action" value="delete">
                    </button>
                </form>
            </td>
        </tr>
    </table>
</div>

<script>
    function confirmDelete() {
        if (confirm("確定要刪除嗎？")) {
            document.getElementById("deleteForm").submit(); // 提交表
            alert("刪除成功！"); // 彈出成功訊息!
            // window.location.href = "select_administrator.jsp"; // 導引至首頁
        }
    }
</script>

<%--<!-- 定義一個 <img> 元素，並將其 src 屬性設置為 Base64 編碼的字串 -->--%>
<%--<img id="photoBase64" src="" alt="Base64 Image"/>--%>

<%--<script>--%>
<%--    // 從後端獲取 Base64 編碼的圖像數據--%>
<%--    // 假設後端返回的數據為一個 JSON 對象，其中包含 Base64 編碼的圖像--%>
<%--    fetch('your_api_endpoint')--%>
<%--        .then(response => response.json())--%>
<%--        .then(data => {--%>
<%--            // 獲取 Base64 編碼的圖像--%>
<%--            const base64Image = data.base64Image;--%>

<%--            // 設置 <img> 元素的 src 屬性為 Base64 編碼的字串--%>
<%--            document.getElementById('base64Image').src = 'data:image/jpeg;base64,' + base64Image;--%>
<%--        })--%>
<%--        .catch(error => console.error('Error fetching Base64 image:', error));--%>
<%--</script>--%>

</body>

</html>