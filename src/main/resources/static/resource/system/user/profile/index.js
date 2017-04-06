$(document).ready(function() {
	
	post({
		url : "/mvc/user/profile",
		success : function(user) {
			$("#id").val(user.id);
			$("#username").val(user.username);
			$("#nickname").val(user.nickname);
			$("#email").val(user.email);
		}
	});

	$("#okBtn").click(function() {
		var username = _.trim($("#username").val());
		if (!username) {
			alert("用户名不能为空");
		}
		var nickname = _.trim($("#nickname").val());
		if (!nickname) {
			alert("昵称不能为空");
		}
		var email = _.trim($("#email").val());
		if (!email) {
			alert("邮箱不能为空");
		}
		post({
			url : "/mvc/user/saveProfile",
			data : {
				username : username,
				nickname : nickname,
				email : email
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
