define(["base/base", "css!./sidenav"], function(base){	
	FwBase.Wtf.View.Controls.Sidenav = function(){
		this.menuitems = [];
		FwBase.Wtf.View.Controls.BaseControl.apply(this, arguments);
	};
	$.extend(FwBase.Wtf.View.Controls.Sidenav.prototype, FwBase.Wtf.View.Controls.BaseControl.prototype, 
		{	
			template: _.template($('#sys_atom_controls_sidenav').html()),
			itemtemplate: _.template($('#sys_atom_controls_sidenav_item').html()),
			grouptemplate: _.template($('#sys_atom_controls_sidenav_group').html()),
			postInit : function() {
			},
			makeDefault : function(metadata){
			},
			addNavGroup : function(item) {
				var pItem = this.el.children("#sidenavdiv");
				pItem.append(this.grouptemplate(item));
			},
			addNavItem : function(item){
				var pItem = this.el.children('#sidenavdiv').children('#' + item['groupId']);
				pItem.append(this.itemtemplate(item));
				var currItem = pItem.find("a").last();
				currItem.click(function(){
					var title = $(this).html();
					if(title == "Monitored Servers"){
						FwBase.Wtf.Application.navigateToStack($(this).attr('href'), null, {title: $(this).html()});
					}
					else
						FwBase.Wtf.Application.navigateTo($(this).attr('href'), null, {title: $(this).html()});
					return false;
				});
			},
			addItem : function(item){
				var groupId = item["groupId"];
				if(groupId == null)
					this.addNavGroup(item);
				else
					this.addNavItem(item);
			}
		}
	);
	
	return FwBase.Wtf.View.Controls.Sidenav;
});

