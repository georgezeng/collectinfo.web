function createDataTable(opt) {
	var datatable = $('#' + opt.id);

	var datatableObj = datatable
			.DataTable({
				responsive : true,
				bServerSide : true,
				bProcessing : true,
				oLanguage : {
					sEmptyTable : "暂时没有任何记录",
					sInfo : "显示 第_START_条 到 第_END_条，共_TOTAL_条记录",
					sInfoEmpty : "",
					sInfoFiltered : "(从总共_MAX_条记录中过滤的结果)",
					sLengthMenu : "显示 _MENU_ 条/页",
					sLoadingRecords : "加载中...",
					sProcessing : "处理中...",
					sSearch : "快速搜索:",
					sZeroRecords : "暂时没有任何记录",
					sSearchPlaceholder : opt.searchPlaceholder ? opt.searchPlaceholder
							: "",
					oPaginate : {
						sFirst : "首页",
						sLast : "尾页",
						sNext : "下一页",
						sPrevious : "上一页"
					}
				},
				lengthMenu : [ 10, 20, 50, 100 ],
				ajax : function(infoSettings, callback, settings) {
					var successHandler = opt.success;
					var queryInfo = createQueryInfo(infoSettings);
					queryInfo.object = {};
					if (!opt.dataCallback) {
						opt.dataCallback = opt.data;
					}
					opt.dataCallback(queryInfo.object, infoSettings);
					opt.data = queryInfo;
					opt.success = function(result) {
						if (typeof (successHandler) === 'function') {
							successHandler(result);
						}

						callback({
							recordsTotal : result.total,
							recordsFiltered : result.total,
							data : result.list
						});

						var rowCB = datatable.find("input.rowCB").unbind()
								.each(
										function(index) {
											$(this).attr("idkey",
													result.list[index].id);
										});
						rowCB.click(function() {
							var count = 0;
							rowCB.each(function() {
								if ($(this).prop("checked")) {
									count++;
								}
							});
							datatable.find("input.allCB").prop("checked",
									count == rowCB.length);
						});

						if (opt.operations) {
							datatable
									.find("div.operationArea")
									.each(
											function(index) {
												var id = datatable.find(
														"input.rowCB")
														.eq(index)
														.attr("idkey");
												var el = $(this);
												for (var i = 0; i < opt.operations.length; i++) {
													el.append(opt.operations[i]
															(id));
												}
											});
						}

					}
					post(opt);
				},
				columns : opt.columns,
				columnDefs : opt.columnDefs,
				order : opt.order ? opt.order : [ [ 1, "asc" ] ]
			});

	datatable.find("input.allCB").click(function() {
		datatable.find("input.rowCB").prop("checked", $(this).prop("checked"));
	});

	return datatableObj;
}

function createQueryInfo(infoSettings) {
	var queryInfo = {
		pageInfo : {
			pageSize : infoSettings.length,
			pageNo : infoSettings.start / infoSettings.length + 1,
			sorts : []
		}
	};
	if (infoSettings.order && infoSettings.order.length > 0) {
		for (var i = 0; i < infoSettings.order.length; i++) {
			queryInfo.pageInfo.sorts.push({
				property : infoSettings.columns[infoSettings.order[i].column].name,
				order : infoSettings.order[i].dir.toUpperCase()
			});
		}
	}
	return queryInfo;
}

function withIdColumn(array) {
	for (var i = array.length - 1; i >= 0; i--) {
		array[i + 1] = array[i];
	}
	array[0] = {
		name : "id",
		sortable : false
	};
	return array;
}

function withIdColumnDef(array) {
	var idDef = {
		targets : 0,
		data : null,
		defaultContent : "<input type='checkbox' class='rowCB' />"
	};
	if (array) {
		for (var i = array.length - 1; i >= 0; i--) {
			array[i + 1] = array[i];
		}
		array[0] = idDef;
	} else {
		array = [ idDef ];
	}
	return array;
}

function withOperationAreaColumnDef(array, index) {
	array.push({
		targets : index,
		data : null,
		defaultContent : "<div class='operationArea'></div>"
	});
	return array;
}

function getSelectedIds(id) {
	var arr = [];
	$("#" + id).find("input.rowCB").each(function() {
		if ($(this).prop("checked")) {
			arr.push($(this).attr("idkey"));
		}
	});
	return arr;
}

function createDeleteEvent(tableId, datatable, url) {
	return function() {
		var ids = getSelectedIds(tableId);
		if (ids.length > 0 && window.confirm("确定删除所选记录吗")) {
			post({
				url : url,
				data : {
					ids : ids
				},
				success : function() {
					datatable.ajax.reload();
				}
			});
		}
	};
}