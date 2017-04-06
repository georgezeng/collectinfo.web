$(document).ready(function() {
	post({
		url: '/menus',
		success: function(menus) {
			var nav = $("#navList");
			createMenu(menus, nav, 1);
			nav.children('ul.nav').eq(0).metisMenu();
		}
	});
	
	function createMenu(menus, rootEl, level) {
		var ul = $("<ul class='nav'></ul>").appendTo(rootEl);
		if(level == 2) {
			ul.addClass("nav-second-level", "collapse");
		} else if(level > 2) {
			ul.addClass("nav-third-level", "collapse");
		}
		for(var i = 0; i < menus.length; i++) {
			var liEl = $("<li></li>").appendTo(ul);
			var aEl = $("<a href='javascript:;'></a>").appendTo(liEl);
			var item = menus[i];
			if(item.children) {
				aEl.append($('<i class="fa fa-sitemap fa-fw"></i>'));
			} else {
				(function(link) {
					aEl.click(function() {
						go(link);
					});
				})(item.authority.uri);
			}
			aEl.append($('<span></span>').text(item.name));
			if(item.children) {
				aEl.append($('<span class="fa arrow"></span>'));
				createMenu(item.children, liEl, ++level);
			}
		}
	}  
});
