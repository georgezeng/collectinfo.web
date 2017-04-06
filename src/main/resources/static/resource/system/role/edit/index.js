var listUrl = "/role/list.html";

$(document).ready(function() {
	var id = $.getUrlParam("id");
	if (id) {
		$("#id").val(id);
		post({
			url : "/mvc/role/" + id,
			success : function(role) {
				$("#name").val(role.name);
				$("#description").val(role.description);
				setAuths("unselectedAuths", role.unselectedAuthorities);
				setAuths("selectedAuths", role.authorities);
				setUsers("unselectedUsers", role.unselectedUsers);
				setUsers("selectedUsers", role.users);
			}
		});
	} else {
		post({
			url : "/mvc/user/all",
			success : function(list) {
				setUsers("unselectedUsers", list);
			}
		});
	}

	$("#okBtn").click(function() {
		var name = _.trim($("#name").val());
		if (!name) {
			alert("角色名不能为空");
		}
		var description = _.trim($("#description").val());
		if (!description) {
			alert("描述不能为空");
		}
		post({
			url : "/mvc/role/save",
			data : {
				id : id,
				name : name,
				description : description,
				authorities: getList("selectedAuths"),
				users: getList("selectedUsers")
			},
			success : function() {
				go(listUrl);
			}
		});
	});

	$("#backBtn").click(function() {
		go(listUrl);
	});
	
	$("#authUnselectBtn").click(createAuthSelectedEvent("selectedAuths", "unselectedAuths"));
	$("#authSelectBtn").click(createAuthSelectedEvent("unselectedAuths", "selectedAuths"));
	$("#userUnselectBtn").click(createUserSelectedEvent("selectedUsers", "unselectedUsers"));
	$("#userSelectBtn").click(createUserSelectedEvent("unselectedUsers", "selectedUsers"));

});

function getList(id) {
	var arr = [];
	$("#" + id).children().each(function(i) {
		arr[i] = {
			id: parseInt($(this).val())
		}
	});
	return arr;
}

function createAuthSelectedEvent(fromId, toId) {
	return createSelectedEvent(fromId, toId, "权限", function(opt) {
		return {
			id: opt.value,
			description: opt.text
		}
	}, setAuths);
}

function createUserSelectedEvent(fromId, toId) {
	return createSelectedEvent(fromId, toId, "用户", function(opt) {
		return {
			id: opt.value,
			username: opt.text
		}
	}, setUsers);
}

function createSelectedEvent(fromId, toId, word, createObj, action) {
	return function() {
		var fromEls = $("#" + fromId);
		var arr = []; 
		var count = 0;
		fromEls.children().each(function(i){ 
			if($(this).prop("selected")) {
				arr[i] = createObj({
					value: $(this).val(),
					text: $(this).text()
				});
				count++;
			} else {
				arr[i] = null;
			}
		});
		if(count == 0) {
			alert("请选择至少一个" + word);
			return;
		}
		var options = [];
		for(var i = 0; i < arr.length; i++) {
			if(arr[i]) {
				options.push(fromEls.children().eq(i));
			}
		}
		for(var i = 0; i < options.length; i++) {
			options[i].remove();
		}
		action(toId, arr);
	};
}

var setAuths = function(id, auths) {
	if(auths) {
		var authsSelector = $("#" + id);
		for(var i = 0; i < auths.length; i++) {
			if(auths[i]) {
				authsSelector.append("<option value='" + auths[i].id + "'>" + auths[i].description + "</option>")
			}
		}
	}
}

var setUsers = function (id, users) {
	if(users) {
		var usersSelector = $("#" + id);
		for(var i = 0; i < users.length; i++) {
			if(users[i]) {
				usersSelector.append("<option value='" + users[i].id + "'>" + users[i].username + "</option>")
			}
		}
	}
}

