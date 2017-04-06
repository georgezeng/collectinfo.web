var entryUrl = "/";

$(document).ready(function() {

	$("#okBtn").click(function() {
		var oldPassword = _.trim($("#oldPassword").val());
		if (!oldPassword) {
			alert("原密码不能为空");
		}
		var password = _.trim($("#password").val());
		if (!password) {
			alert("新密码不能为空");
		}
		var confirmPassword = _.trim($("#confirmPassword").val());
		if (!confirmPassword) {
			alert("确认密码不能为空");
		}
		post({
			url : "/mvc/user/changePwd",
			data : {
				oldPassword : Base64.encode(oldPassword),
				password : Base64.encode(password),
				confirmPassword : Base64.encode(confirmPassword)
			},
			success : function() {
				go("/");
			}
		});
	});

	$("#backBtn").click(function() {
		window.history.back();
	});

});
