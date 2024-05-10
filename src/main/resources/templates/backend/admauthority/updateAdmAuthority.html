<%@ page contentType="text/html; charset=UTF-8" pageEncoding="Big5"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ page import="com.ren.admauthority.model.AdmAuthorityVO" %>

<%
   AdmAuthorityVO admAuthorityVO = (AdmAuthorityVO) request.getAttribute("admAuthorityVO");
%>

<html>
<head>
<meta http-equiv="X-UA-Compatible" content="IE=edge,chrome=1"/>
<title>管理員權限資料修改 - update_admAuthority_input.jsp</title>

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
		 <h3>管理員權限資料修改 - update_admAuthority_input.jsp</h3>
		 <h4><a href="select_admAuthority.jsp">回首頁</a></h4>
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

<FORM METHOD="post" ACTION="admAuthority.do" name="form1">
<table>
	<tr>
		<td>職位編號:</td>
		<td><input type="TEXT" name="titleNo" value="<%= (admAuthorityVO==null)? "1" : admAuthorityVO.getTitleNo()%>" size="45"/></td>
	</tr>
	<tr>
		<td>功能編號:</td>
		<td><input type="TEXT" name="authFuncNo" value="<%= (admAuthorityVO==null)? "查詢資料" : admAuthorityVO.getAuthFuncNo()%>" size="45"/></td>
	</tr>

	<jsp:useBean id="admAuthoritySvc" scope="page" class="com.ren.admauthority.service.AdmAuthorityServiceImpl" />
	<tr>
		<td>商品類別編號:<font color=red><b>*</b></font></td>
		<td><select size="1" name="titleNo">
			<c:forEach var="admAuthorityVO" items="${admAuthoritySvc.all}">
				<option value="${admAuthorityVO.titleNo}" ${(admAuthorityVO.titleNo==admAuthorityVO.titleNo)? 'selected':'' } >${admAuthorityVO.titleNo}
			</c:forEach>
		</select></td>
	</tr>
</table>
<br>
<input type="hidden" name="action" value="update">
<input type="hidden" name="titleNo" value="<%=admAuthorityVO.getTitleNo()%>">
<input type="submit" value="送出修改"></FORM>
</body>
</html>