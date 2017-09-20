<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<html>
<title>Spring文件上传</title>
<body>
<form name="upload" action="/manage/product/upload.do" enctype="multipart/form-data" method="post">
    <input type="file" name="upload_file"/>
    <input type="submit" value="SpringMVC上传文件" />
</form>

<form name="rich_upload" action="/manage/product/richtext_img_upload.do" enctype="multipart/form-data" method="post">
    <input type="file" name="upload_file">
    <input type="submit" value="富文本上传文件">
</form>
</body>
</html>
