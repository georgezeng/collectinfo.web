$(document).ready(function() {
	$("#btn").click(function() {
		submit();
	})
});

function submit() {
	var username = _.trim($("#username").val());
	if(!username) {
		$("#tip").text("用户名不能为空").show();
		return;
	}
	var password = _.trim($("#password").val());
	if(!password) {
		$("#tip").text("密码不能为空").show();
		return;
	}
	var confirmPassword = _.trim($("#confirmPassword").val());
	if(!confirmPassword) {
		$("#tip").text("确认密码不能为空").show();
		return;
	}
	var email = _.trim($("#email").val());
	if(!email) {
		$("#tip").text("邮箱不能为空").show();
		return;
	}
	var nickname = _.trim($("#nickname").val());
	if(!nickname) {
		$("#tip").text("昵称不能为空").show();
		return;
	}
	var captcha = _.trim($("#captcha").val());
	if(!captcha) {
		$("#captcha").text("验证码不能为空").show();
		return;
	}
	post({
		url: '/register/save',
		data: {
			username: Base64.encode(username),
			password: Base64.encode(password),
			confirmPassword: Base64.encode(confirmPassword),
			email: Base64.encode(email),
			nickname: nickname,
			captcha: captcha
		},
		success: function(entryUri) {
			go(entryUri);
		},
		error: getDefaultErrorFunc()
	});
}