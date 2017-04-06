var listUrl = "/user/list.html";

$(document).ready(function() {
	var id = $.getUrlParam("id");
	if (id) {
		$("#id").val(id);
		$("#enabled").prop("checked", true);
		post({
			url : "/mvc/user/" + id,
			success : function(user) {
				$("#username").val(user.username);
				$("#nickname").val(user.nickname);
				$("#email").val(user.email);
				$("#enabled").val(user.enabled+"");
				setRoles("unselectedRoles", user.unselectedRoles);
				setRoles("selectedRoles", user.roles);
			}
		});
	} else {
		post({
			url : "/mvc/role/all",
			success : function(list) {
				setRoles("unselectedRoles", list);
			}
		});
	}

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
		var enabled = $("#enabled").val();
		post({
			url : "/mvc/user/save",
			data : {
				id : id,
				username : username,
				nickname : nickname,
				email : email,
				enabled: enabled == "true",
				roles: getRoles()
			},
			success : function() {
				go(listUrl);
			}
		});
	});

	$("#backBtn").click(function() {
		go(listUrl);
	});
	
	$("#unselectBtn").click(createSelectedEvent("selectedRoles", "unselectedRoles"));
	$("#selectBtn").click(createSelectedEvent("unselectedRoles", "selectedRoles"));

});

function getRoles() {
	var arr = [];
	$("#selectedRoles").children().each(function(i) {
		arr[i] = {
			id: parseInt($(this).val())
		}
	});
	return arr;
}

function createSelectedEvent(fromId, toId) {
	return function() {
		var fromRoles = $("#" + fromId);
		var arr = []; 
		var count = 0;
		fromRoles.children().each(function(i){ 
			if($(this).prop("selected")) {
				arr[i] = {
					id: $(this).val(),
					description: $(this).text()
				};
				count++;
			} else {
				arr[i] = null;
			}
		});
		if(count == 0) {
			alert("请选择至少一个角色")
			return;
		}
		var options = [];
		for(var i = 0; i < arr.length; i++) {
			if(arr[i]) {
				options.push(fromRoles.children().eq(i));
			}
		}
		for(var i = 0; i < options.length; i++) {
			options[i].remove();
		}
		setRoles(toId, arr);
	};
}

function setRoles(id, roles) {
	if(roles) {
		var rolesSelector = $("#" + id);
		for(var i = 0; i < roles.length; i++) {
			if(roles[i]) {
				rolesSelector.append("<option value='" + roles[i].id + "'>" + roles[i].description + "</option>")
			}
		}
	}
}
