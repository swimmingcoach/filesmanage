<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html>
<head>
<title>图片查看/上传</title>
<meta charset="UTF-8" />
<meta name="robots" content="none">
<meta http-equiv="X-UA-Compatible" content="IE=edge,Chrome=1" />
<meta name="viewport" content="width=device-width, initial-scale=1.0" />

<link href="resources/bootstrap-3.3.7/css/bootstrap.min.css"
	rel="stylesheet" />
<link href="resources/bootstrap-fileinput/css/fileinput.css"
	rel="stylesheet" />
<link href="resources/css/css.css" rel="stylesheet" />

<script src="resources/jquery/jquery-3.2.1.min.js"></script>
<script src="resources/bootstrap-3.3.7/js/bootstrap.min.js"></script>
<script src="resources/bootstrap-fileinput/js/fileinput.js"></script>
<script src="resources/bootstrap-fileinput/js/locales/zh.js"></script>

<script type="text/javascript">
	$(document).ready(function() {
		$('#file-Portrait').fileinput({
			language : 'zh', //设置语言
			uploadUrl : "./file.web?action=uploadPic&filePath=", //上传的地址
			allowedFileExtensions : [ 'jpeg', 'jpg', 'png', 'gif' ],//接收的文件后缀,
			maxFileCount : 10,
			enctype : 'multipart/form-data',
			showUpload : true, //是否显示上传按钮
			showCaption : false,//是否显示标题
			browseClass : "btn btn-primary", //按钮样式             
			previewFileIcon : "<i class='glyphicon glyphicon-king'></i>",
			msgFilesTooMany : "选择上传的文件数量({n}) 超过允许的最大数值{m}！",
		});
	});
	function getFile() {
		$("#img").removeAttr("src");
		$("#img").hide();
		var filePath = $("#filePath").val();
		var url = "./file.web?action=getPic&filePath=" + filePath;
		$("#img").attr('src', url);
		$("#img").show();
	}

	function menuChange(index) {
		var lis = $(".menu ul li");
		$(lis).removeClass("active");
		$(lis).each(function(i, li) {
			if (index == i) {
				$(li).addClass("active");
			}
		});

		var divs = $(".container>div");
		$(divs).hide();
		$(divs).each(function(i, div) {
			if (index == i) {
				$(div).show();
			}
		});
	}
</script>
</head>
<body>
	<div class="menu">
		<ul class="nav nav-tabs">
			<li class="active"><a href="javascript:menuChange(0);">图片查看</a></li>
			<li><a href="javascript:menuChange(1);">图片上传</a></li>
		</ul>
	</div>
	<div class="container">
		<div id="imgShow">
			<div class="tools">
				<div class="input-group">
					<input id="filePath" type="text" class="form-control"
						placeholder="请输入路径"> <span class="input-group-btn">
						<button class="btn btn-default" type="button" onclick="getFile();">获取</button>
					</span>
				</div>
			</div>
			<div class="image">
				<img id="img" src="" style="display: none;" />
			</div>
		</div>
		<div id="imgUpload" style="display: none;">
			<div class="tools">
				<input id="file-Portrait" type="file">
			</div>
		</div>
	</div>
</body>
</html>