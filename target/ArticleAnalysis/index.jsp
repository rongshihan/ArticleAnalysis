<%--
  Created by IntelliJ IDEA.
  User: rsh
  Date: 2021/7/10
  Time: 18:02
  To change this template use File | Settings | File Templates.
--%>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<html>
<head>
    <title>词云图</title>
    <link rel="stylesheet" href="CSS/bootstrap.min.css">
    <link rel="stylesheet" href="CSS/fileinput.min.css">

    <script src="JS/jquery-3.6.0.min.js" type="text/javascript"></script>
    <script src="JS/bootstrap.min.js" type="text/javascript"></script>
    <script src="JS/fileinput.min.js" type="text/javascript"></script>
    <script src="JS/zh.js" type="text/javascript"></script>
</head>
<body>
<div class="container my-4">
    <center>
        <h1>Social Network Analysis Centrality Calculation</h1>
    </center>
    <hr>
    <form enctype="multipart/form-data" action="uploadServlet" method="post">
        <div class="upload-wrap">
            <input type="file" id="File" multiple="multiple" data-min-file-count="1" name="file" accept=".pdf,.docx,.txt"/>
        </div>
        <script>
            $('#File').fileinput({
                language: 'zh',//语言设置
                uploadUrl: 'http://localhost:8080/ArticleAnalysis_war_exploded/uploadServlet',//上传地址
                //dropZoneTitle:'拖拽文件到这里 …\n' + '支持多文件同时上传',
                showCaption: true,//是否显示被选文件的简介
                showUpload: true,//是否显示上传按钮
                showRemove: true,//是否显示删除按钮
                showClose: true,//是否显示关闭按钮
                enctype: 'multipart/form-data',
                allowedFileExtensions: ['pdf', 'docx','txt'],//允许接收的文件类型
                uploadAsync: false, //false 同步上传，后台用数组接收，true 异步上传，每次上传一个file,会调用多次接口
                layoutTemplates: {
                    //actionUpload:'',//去除上传预览缩略图中的上传图片
                    //actionZoom:'',   //去除上传预览缩略图中的查看详情预览的缩略图标
                    //actionDownload:'' ,//去除上传预览缩略图中的下载图标
                    //actionDelete:'', //去除上传预览的缩略图中的删除图标
                },
                browseClass: 'btn btn-primary',//文件选择按钮的CSS样式
                previewFileIcon: "<i class='glyphicon glyphicon-king'></i>",//当检测到用于预览的不可读文件类型时，将在每个预览文件缩略图中显示的图标。默认为<i class="glyphicon glyphicon-file"></i>
                maxFileCount: 0,//最大上传文件数，设置0表示无限制.默认0
                minFileCount: 1,//最小文件上传数，设置0表示可选。默认0
            }).on('filebatchuploadsuccess', function (event, data, previewId, index) {
                //同步上传成功处理
                $('#result').css('display', 'block');
                console.log(data);
            }).on('filebatchuploaderror', function (event, data, msg) {
                //同步上传错误处理
                console.log(msg);
            }).on("fileuploaded", function (event, data, previewId, index) {
                //异步上传成功处理
                $('#result').css('display', 'block');
                console.log(data);
            }).on('fileerror', function (event, data, msg) {
                //异步上传错误处理
                console.log(msg);
            });
        </script>
    </form>
    <form action="segServlet" method="post">
        <input id="result" name="result" type="submit" class="btn btn-primary" value="查看结果" style="display: none;width: 100%">
    </form>
</div>
</body>
</html>
