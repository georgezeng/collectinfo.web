var listUrl = "/authority/list.html";

$(document).ready(function() {
	var id = $.getUrlParam("id");
	if (id) {
		$("#id").val(id);
		post({
			url : "/mvc/authority/" + id,
			success : function(auth) {
				$("#name").val(auth.name);
				$("#description").val(auth.description);
				$("#uri").val(auth.uri);
			}
		});
	}

	$("#okBtn").click(function() {
		var name = _.trim($("#name").val());
		if (!name) {
			alert("权限名不能为空");
		}
		var description = _.trim($("#description").val());
		if (!description) {
			alert("描述不能为空");
		}
		var uri = _.trim($("#uri").val());
		if (!uri) {
			alert("URI不能为空");
		}
		post({
			url : "/mvc/authority/save",
			data : {
				id : id,
				name : name,
				description : description,
				uri : uri
			},
			success : function() {
				go(listUrl);
			}
		});
	});

	$("#backBtn").click(function() {
		go(listUrl);
	});

});
