var editUrl = "/authority/edit.html";
$(document).ready(function() {
	var tableId = "dataTable";
	var datatable = createDataTable({
		id : tableId,
		searchPlaceholder : "权限名/描述/URI",
		url : "/mvc/authority/list",
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
			name : "uri",
			data : "uri"
		}, {
			name : "operation",
			sortable: false
		} ]),
		columnDefs : withOperationAreaColumnDef(withIdColumnDef(), 4),
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

	$("#deleteBtn").click(createDeleteEvent(tableId, datatable, "/mvc/authority/remove"));

});
