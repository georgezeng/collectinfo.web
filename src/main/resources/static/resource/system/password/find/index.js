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
	var email = _.trim($("#email").val());
	if(!email) {
		$("#tip").text("邮箱不能为空").show();
		return;
	}
	var captcha = _.trim($("#captcha").val());
	post({
		url: '/password/find/reset',
		data: {
			username: Base64.encode(username),
			email: Base64.encode(email),
			captcha: captcha
		},
		success: function(loginUri) {
			go(loginUri);
		},
		error: getDefaultErrorFunc()
	});
}