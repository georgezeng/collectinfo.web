var editUrl = "/menu/edit.html";

$(document).ready(function() {
	var tableId = "dataTable";
	var datatable = createDataTable({
		id : tableId,
		searchPlaceholder : "菜单名/权限名/URI",
		url : "/mvc/menu/list",
		data : function(queryObj, infoSettings) {
			queryObj.name = infoSettings.search.value;
		},
		columns : withIdColumn([ {
			name : "name",
			data : "name"
		}, {
			name : "sort",
			data : "sort"
		}, {
			name : "parent.name",
			sortable: false
		}, {
			name : "a.description"
		}, {
			name : "a.uri"
		}, {
			name : "operation",
			sortable: false
		} ]),
		columnDefs : withOperationAreaColumnDef(withIdColumnDef([
			{
			    targets: 3,
			    data: function ( row, type, val, meta ) {
			    	if(row.parent) {
			    		return row.parent.name;
			    	}
			    	return "";
			    }
			},
			{
				targets: 4,
				data: function ( row, type, val, meta ) {
					if(row.authority) {
						return row.authority.description;
					}
					return "";
				}
			},
			{
				targets: 5,
				data: function ( row, type, val, meta ) {
					if(row.authority) {
						return row.authority.uri;
					}
					return "";
				}
			},
		]), 6),
		operations : [
			function(id) {
				return $("<a href='javascript:;'>修改</a>").click(function() {
					go(editUrl + "?id=" + id);
				});
			}
		],
		order: [[ 2, "asc" ]]
	});

	$("#addBtn").click(function() {
		go(editUrl);
	});

	$("#deleteBtn").click(createDeleteEvent(tableId, datatable, "/mvc/menu/remove"));

});
