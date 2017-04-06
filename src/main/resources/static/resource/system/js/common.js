var CONTEXT_PATH = "";

function postOpt(option) {
	return {
		url: CONTEXT_PATH + option.url,
		cache: false,
		method: option.method ? option.method : "post",
		contentType: 'application/json',
		data: JSON.stringify(option.data),
		dataType: 'json', // this is for return type
		beforeSend: function (request) {
			var csrfInfo = getCsrfInfo();
			if(csrfInfo.header) {
				request.setRequestHeader(csrfInfo.header, csrfInfo.token);
			}
        },
		success: function(result) {
			if (result && result.status == '1') {
				if (typeof option.success === 'function') {
					option.success(result.data);
				}
			} else {
				if (typeof option.error === 'function') {
					option.error(result.error)
				} else {
					if (result && result.error) {
						alert("[" + result.error.id + "]: " + result.error.msg)
					} else {
						alert("系统错误");
					}
				}
			}
		},
		error: function(jqXHR, textStatus, errorThrown) {
			if (jqXHR.status == 401) {
				window.location.href='/';
			} else {
				if(typeof option.requestError === 'function') {
					option.requestError(jqXHR, textStatus, errorThrown);
					return;
				}
				if(jqXHR.responseJSON && jqXHR.responseJSON.message) {
					alert(jqXHR.responseJSON.message);
				} else {
					alert(textStatus);
				}
			}
		}
	};
}

function getDefaultErrorFunc() {
	return function(error) {
		$("#tip").text("[" + error.id + "]: " + error.msg).show();
	};
}

function post(opt) {
	$.ajax(postOpt(opt));
}

function getCsrfInfo() { 
   return {
	   token: $("meta[name='_csrf']").attr("content"),
	   header: $("meta[name='_csrf_header']").attr("content")
   };
}

function go(uri) {
	window.location.href = CONTEXT_PATH + uri;
}

function formatDateBoxFull(val){
	if (val != null) {
		 var date = new Date(val);
		 this.hour = date.getHours() < 10 ? "0" + date.getHours() : date.getHours();   
		 this.minute = date.getMinutes() < 10 ? "0" + date.getMinutes() : date.getMinutes();   
		 this.second = date.getSeconds() < 10 ? "0" + date.getSeconds() : date.getSeconds(); 
		 return date.getFullYear() + '-' + (date.getMonth() + 1) + '-'
		 + date.getDate() + " " +  this.hour  + ":" + this.minute + ":" + this.second ;   
		} 
}

/*
 * Simplified Chinese translation for bootstrap-datetimepicker Yuan Cheung
 * <advanimal@gmail.com>
 */
(function($){
   if($.fn.datetimepicker) {
	   $.fn.datetimepicker.dates['zh-CN'] = {
			   days: ["星期日", "星期一", "星期二", "星期三", "星期四", "星期五", "星期六", "星期日"],
			   daysShort: ["周日", "周一", "周二", "周三", "周四", "周五", "周六", "周日"],
			   daysMin:  ["日", "一", "二", "三", "四", "五", "六", "日"],
			   months: ["一月", "二月", "三月", "四月", "五月", "六月", "七月", "八月", "九月", "十月", "十一月", "十二月"],
			   monthsShort: ["一月", "二月", "三月", "四月", "五月", "六月", "七月", "八月", "九月", "十月", "十一月", "十二月"],
			   today: "今天",
			   suffix: [],
			   meridiem: ["上午", "下午"]
	   };
   }
   
   $.getUrlParam = function (name) {
       var reg = new RegExp("(^|&)" + name + "=([^&]*)(&|$)");
       var r = window.location.search.substr(1).match(reg);
       if (r != null) return unescape(r[2]); return null;
   }
}(jQuery));

