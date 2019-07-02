<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html>
<head>
<meta charset="UTF-8">
<title>Insert title here</title>
</head>
<body>
<form action="${pageContext.request.contextPath }/UploadServlet?method=upload"
      method="post" 
	  enctype="multipart/form-data">  
    
    <input type="file" name="filename"/>
    <input type="submit"  />
</form>
</body>
</html>
