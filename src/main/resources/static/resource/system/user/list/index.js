var editUrl = "/user/edit.html";

$(document).ready(function() {
	var tableId = "dataTable";
	var datatable = createDataTable({
		id : tableId,
		searchPlaceholder : "用户名/昵称/邮箱",
		url : "/mvc/user/list",
		data : function(queryObj, infoSettings) {
			queryObj.username = infoSettings.search.value;
		},
		success : function(result) {
			for (var i = 0; i < result.list.length; i++) {
				var item = result.list[i];
				item.enabled = item.enabled ? "启用" : "禁用";
			}
		},
		columns : withIdColumn([ {
			name : "username",
			data : "username"
		}, {
			name : "nickname",
			data : "nickname"
		}, {
			name : "email",
			data : "email"
		}, {
			name : "enabled",
			data : "enabled"
		}, {
			name : "operation",
			sortable: false
		} ]),
		columnDefs : withOperationAreaColumnDef(withIdColumnDef(), 5),
		operations : [
			function(id) {
				return $("<a href='javascript:;'>修改</a>").click(function() {
					go(editUrl + "?id=" + id);
				})
			},
			function(id) {
				return $("<a class='operationAnchor' href='javascript:;'>重置密码</a>").click(function() {
					if(window.confirm("确定重置密码吗？")) {
						post({
							url: "/mvc/user/resetPwd/" + id,
							success: function() {
								alert("重置成功");
							}
						});
					}
				})
			}
		]
	});

	$("#addBtn").click(function() {
		go(editUrl);
	});

	$("#deleteBtn").click(createDeleteEvent(tableId, datatable, "/mvc/user/remove"));

});
