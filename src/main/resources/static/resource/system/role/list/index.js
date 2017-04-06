var editUrl = "/role/edit.html";

$(document).ready(function() {
	var tableId = "dataTable";
	var datatable = createDataTable({
		id : tableId,
		searchPlaceholder : "角色名/描述",
		url : "/mvc/role/list",
		data : function(queryObj, infoSettings) {
			queryObj.name = infoSettings.search.value;
		},
		columns : withIdColumn([ {
			name : "name",
			data : "name"
		}, {
			name : "description",
			data : "description"
		}, {
			name : "operation",
			sortable: false
		} ]),
		columnDefs : withOperationAreaColumnDef(withIdColumnDef(), 3),
		operations : [
			function(id) {
				return $("<a href='javascript:;'>修改</a>").click(function() {
					go(editUrl + "?id=" + id);
				});
			}
		]
	});

	$("#addBtn").click(function() {
		go(editUrl);
	});

	$("#deleteBtn").click(createDeleteEvent(tableId, datatable, "/mvc/role/remove"));

});
