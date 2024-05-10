<%@ page contentType="text/html; charset=UTF-8" pageEncoding="Big5"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ page import="com.ren.administrator.service.*"%>
<%@ page import="com.ren.administrator.model.*"%>

<%
   AdministratorVO administratorVO = (AdministratorVO) request.getAttribute("administratorVO");
%>

<html>
<head>
<meta http-equiv="X-UA-Compatible" content="IE=edge,chrome=1"/>
<title>管理員資料修改 - update_administrator_input.jsp</title>

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
	width: 450px;
	background-color: white;
	margin-top: 1px;
	margin-bottom: 1px;
  }
  table, th, td {
    border: 0px solid #CCCCFF;
  }
  th, td {
    padding: 1px;
  }
</style>

</head>
<body bgcolor='white'>

<table id="table-1">
	<tr><td>
		 <h3>管理員資料修改 - update_administrator_input.jsp</h3>
		 <h4><a href="select_administrator.jsp">回首頁</a></h4>
	</td></tr>
</table>

<h3>資料修改:</h3>

<%-- 錯誤表列 --%>
<c:if test="${not empty errorMsgs}">
	<font style="color:red">請修正以下錯誤:</font>
	<ul>
		<c:forEach var="message" items="${errorMsgs}">
			<li style="color:red">${message}</li>
		</c:forEach>
	</ul>
</c:if>

<FORM METHOD="post" ACTION="administrator.do" name="form1">
<table>
	<tr>
		<td>管理員編號:</td>
		<td><input type="TEXT" name="admNo" value="<%= (administratorVO==null)? "10000001" : administratorVO.getAdmNo()%>" size="45"/></td>
	</tr>
	<tr>
		<td>管理員密碼:</td>
		<td><input type="password" name="admPwd" size="45"/></td>
	</tr>
	<tr>
		<td>管理員名稱:</td>
		<td><input type="TEXT" name="admName" value="<%= (administratorVO==null)? "短短的襯衫" : administratorVO.getAdmName()%>" size="45"/></td>
	</tr>
	<tr>
		<td>尺寸:</td>
		<td><input type="TEXT" name="admStat" value="<%= (administratorVO==null)? "1" : administratorVO.getAdmStat()%>" size="45"/></td>
	</tr>
	<tr>
		<td>顏色:</td>
		<td><input type="TEXT" name="admEmail" value="<%= (administratorVO==null)? "藍色" : administratorVO.getAdmEmail()%>" size="45"/></td>
	</tr>
	<tr>
		<td>商品單價:</td>
		<td><input type="TEXT" name="titleNo" value="<%= (administratorVO==null)? "5000" : administratorVO.getTitleNo()%>" size="45"/></td>
	</tr>
	<tr>
		<td>商品狀態:</td>
		<td><input type="TEXT" name="admHireDate" value="<%= (administratorVO==null)? "1" : administratorVO.getAdmHireDate()%>" size="45"/></td>
	</tr>
	<tr>
		<td>商品已售出數量:</td>
		<td><input type="TEXT" name="admPhoto" value="<%= (administratorVO==null)? "200" : administratorVO.getAdmPhoto()%>" size="45"/></td>
	</tr>

	<jsp:useBean id="administratorSvc" scope="page" class="com.ren.administrator.service.AdministratorServiceImpl" />
	<tr>
		<td>商品類別編號:<font color=red><b>*</b></font></td>
		<td><select size="1" name="admPwd">
			<c:forEach var="administratorVO" items="${administratorSvc.all}">
				<option value="${administratorVO.admNo}" ${(administratorVO.admNo==administratorVO.admNo)? 'selected':'' } >${administratorVO.admName}
			</c:forEach>
		</select></td>
	</tr>
</table>

<br>

<input type="hidden" name="action" value="update">
<input type="hidden" name="admNo" value="<%=administratorVO.getAdmNo()%>">
<input type="submit" value="送出修改"></FORM>

</body>

</html>