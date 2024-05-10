<%@ page contentType="text/html; charset=UTF-8" pageEncoding="Big5"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ page import="com.ren.title.model.TitleVO" %>

<% //��com.ren.title.controller.TitleServlet.java�s�Jreq��titleVO���� (������J�榡�����~�ɪ�titleVO����)
   TitleVO titleVO = (TitleVO) request.getAttribute("titleVO");
%>
--<%= titleVO==null %>--${titleVO.titleName}--
<html>
<head>
<meta http-equiv="X-UA-Compatible" content="IE=edge,chrome=1"/>
<title>¾���Ʒs�W - addTitle.jsp</title>

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
		 <h3>¾���Ʒs�W - addTitle.jsp</h3></td><td>
		 <h4><a href="select_title.jsp">�^����</a></h4>
	</td></tr>
</table>

<h3>��Ʒs�W:</h3>

<%-- ���~��C --%>
<c:if test="${not empty errorMsgs}">
	<font style="color:red">�Эץ��H�U���~:</font>
	<ul>
		<c:forEach var="message" items="${errorMsgs}">
			<li style="color:red">${message}</li>
		</c:forEach>
	</ul>
</c:if>

<FORM METHOD="post" ACTION="title.do" name="form1">
<table>

	<tr>
		<td>¾��s��:</td>
		<td><input type="TEXT" name="titleNo" value="<%= (titleVO==null)? "10" : titleVO.getTitleNo()%>" size="45"/></td>
	</tr>
	<tr>
		<td>¾��W��:</td>
		<td><input type="TEXT" name="titleName" value="<%= (titleVO==null)? "���ƪ�" : titleVO.getTitleName()%>" size="45"/></td>
	</tr>

	<jsp:useBean id="titleSvc" scope="page" class="com.ren.title.service.TitleServiceImpl" />
	<tr>
		<td>¾��s��:<font color=red><b>*</b></font></td>
		<td><select size="1" name="titleNo">
			<c:forEach var="titleVO" items="${titleSvc.all}">
				<option value="${titleVO.titleNo}" ${(titleVO.titleNo==titleVO.titleNo)? 'selected':'' } >${titleVO.titleName}
			</c:forEach>
		</select></td>
	</tr>

</table>
<br>
<input type="hidden" name="action" value="insert">
<input type="submit" value="�e�X�s�W"></FORM>

</body>
</html>