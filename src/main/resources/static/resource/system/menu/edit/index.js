var listUrl = "/menu/list.html";

$(document).ready(function() {
	var id = $.getUrlParam("id");
	if (id) {
		$("#id").val(id);
		post({
			url : "/mvc/menu/" + id,
			success : function(menu) {
				$("#name").val(menu.name);
				$("#sort").val(menu.sort);
				setAllOptions(menu);
				if(menu.parent) {
					$("#parentList").val(menu.parent.id);
				}
				if(menu.authority) {
					$("#authList").val(menu.authority.id);
				}
			}
		});
	} else {
		post({
			url : "/mvc/menu/all",
			success : function(menu) {
				setAllOptions(menu);
			}
		});
	}

	$("#okBtn").click(function() {
		var name = _.trim($("#name").val());
		if (!name) {
			alert("菜单名不能为空");
		}
		var sort = _.trim($("#sort").val());
		if (!sort) {
			alert("序号不能为空");
		}
		post({
			url : "/mvc/menu/save",
			data : {
				id : id,
				name : name,
				sort : sort,
				parent: getListObj("parentList"),
				authority: getListObj("authList")
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

function getListObj(id) {
	var parent = null;
	var id = $("#"+id).val();
	if(id) {
		parent = {
			id: id
		}
	}
	return parent;
}

function setAllOptions(menu) {
	if(menu.allParents && menu.allParents.length > 0) {
		var parentList = $("#parentList");
		parentList.append("<option></option>");
		for(var i = 0; i < menu.allParents.length; i++) {
			var item = menu.allParents[i];
			parentList.append($("<option></option>").val(item.id).text(item.name));
		}
	}
	if(menu.allAuthorities && menu.allAuthorities.length > 0) {
		var authList = $("#authList");
		authList.append("<option></option>");
		for(var i = 0; i < menu.allAuthorities.length; i++) {
			var item = menu.allAuthorities[i];
			authList.append($("<option></option>").val(item.id).text(item.description + " - " + item.uri));
		}
	}
}
