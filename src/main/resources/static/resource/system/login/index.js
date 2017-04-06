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
	post({
		url: '/login/check',
		data: {
			username: Base64.encode(username),
			password: Base64.encode(password),
			rememberMe: $("#rememberMe").prop("checked")
		},
		success: function(entryUri) {
			go(entryUri);
		},
		error: getDefaultErrorFunc()
	});
}