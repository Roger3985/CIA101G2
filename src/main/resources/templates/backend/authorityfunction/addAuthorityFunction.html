<%@ page contentType="text/html; charset=UTF-8" pageEncoding="Big5"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ page import="com.ren.authorityfunction.model.AuthorityFunctionVO" %>

<%
   AuthorityFunctionVO authorityFunctionVO = (AuthorityFunctionVO) request.getAttribute("authorityFunctionVO");
%>
--<%= authorityFunctionVO==null %>--${authorityFunctionVO.authFuncNo}--
<html>
<head>
<meta http-equiv="X-UA-Compatible" content="IE=edge,chrome=1"/>
<title>功能權限新增 - addAuthorityFunction.jsp</title>

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
		 <h3>功能權限新增 - addAuthorityFunction.jsp</h3></td><td>
		 <h4><a href="select_authorityFunction.jsp">回首頁</a></h4>
	</td></tr>
</table>

<h3>資料新增:</h3>

<%-- 錯誤表列 --%>
<c:if test="${not empty errorMsgs}">
	<font style="color:red">請修正以下錯誤:</font>
	<ul>
		<c:forEach var="message" items="${errorMsgs}">
			<li style="color:red">${message}</li>
		</c:forEach>
	</ul>
</c:if>

<FORM METHOD="post" ACTION="authorityFunction.do" name="form1">
<table>

	<tr>
		<td>功能權限編號:</td>
		<td><input type="TEXT" name="authFuncNo" value="<%= (authorityFunctionVO==null)? "10" : authorityFunctionVO.getAuthFuncNo()%>" size="45"/></td>
	</tr>
	<tr>
		<td>功能權限名稱:</td>
		<td><input type="TEXT" name="authFuncInfo" value="<%= (authorityFunctionVO==null)? "查詢資料" : authorityFunctionVO.getAuthFuncInfo()%>" size="45"/></td>
	</tr>

	<jsp:useBean id="authorityFunctionSvc" scope="page" class="com.ren.authorityfunction.service.AuthorityFunctionServiceImpl" />
	<tr>
		<td>功能權限編號:<font color=red><b>*</b></font></td>
		<td><select size="1" name="authFuncNo">
			<c:forEach var="authorityFunctionVO" items="${authorityFunctionSvc.all}">
				<option value="${authorityFunctionVO.authFuncNo}" ${(authorityFunctionVO.authFuncNo==authorityFunctionVO.authFuncNo)? 'selected':'' } >${authorityFunctionVO.authFuncInfo}
			</c:forEach>
		</select></td>
	</tr>

</table>
<br>
<input type="hidden" name="action" value="insert">
<input type="submit" value="送出新增"></FORM>

</body>
</html>