define(["base/base"], function(base){	
	FwBase.Wtf.View.Controls.Menu = function(){
		this.menuitems = [];
		FwBase.Wtf.View.Controls.BaseControl.apply(this, arguments);
	};
	$.extend(FwBase.Wtf.View.Controls.Menu.prototype, FwBase.Wtf.View.Controls.BaseControl.prototype, 
		{		
			template: _.template($('#sys_atom_controls_menu').html()),
			type: "menu",
			postInit : function() {
				for(var i = 0; i < this.metadata.groups.length; i ++){
					var group = this.metadata.groups[i];
					for(var j = 0; j < group.menus.length; j ++){
						var menu = group.menus[j];
						this.createMenuItem(menu);
					};
				};
				var oThis = this;
				this.el.find("[wtfinnertype='wtfitem']").click(function(){
					var itemid = $(this).attr('id');
					var menu = oThis.getMenuItem(itemid);
					if(menu){
						var source = {source : oThis, trigger : menu};
						menu.fireEvent("click", source);
					}
					return false;
				});
			},
			makeDefault : function(metadata){
				if(metadata.groups == null){
					var SAMPLE_MENU_FUNC = {
						fire : function(eventObj){
							alert("Menu Clicked:" + eventObj.source.id + "." + eventObj.trigger.id);
						}
					}
					var groups = [
									{menus : [{id:'add',name:'Add', events:[{name:"click", listener: SAMPLE_MENU_FUNC}]}, {id:'edit',name:'Edit', events:[{name:"click", listener: SAMPLE_MENU_FUNC}]}, {id:'del',name:'Delete', events:[{name:"click", listener: SAMPLE_MENU_FUNC}]}]},
									{menus : [{id:'save',name:'Save', events:[{name:"click", listener: SAMPLE_MENU_FUNC}]}, {id:'disable',name:'Disable', events:[{name:"click", listener: SAMPLE_MENU_FUNC}]}]},
									{menus : [{id:'help',name:'Help', menus:[{id:'samplea', name:'Samplea'}, {id:'sampleb', name:'Sampleb', disabled:true}, {divider:true}, {id:'more', name:'More', menus:[{id:'linka', name:'Linka'}, {id:'linkb', name:'Linkb'}]}]}]}
								 ];
					metadata.groups = groups;
				}else{
					
				}
			},
			createMenuItem : function(itemmetadata) {
				var menuitem = new FwBase.Wtf.View.Controls.MenuItem(this, itemmetadata);
				this.menuitems.push(menuitem);
			},
			getMenuItem : function(itemid){
				for(var i = 0; i < this.menuitems.length; i ++){
					if(this.menuitems[i].id == itemid)
						return this.menuitems[i];
				}
				return null;
			}
		}
	);
	
	FwBase.Wtf.View.Controls.MenuItem = function(menubar, metadata){
		this.menubar = menubar;
		this.metadata = metadata;
		this.id = metadata.id;
		this.create();
	};
	$.extend(FwBase.Wtf.View.Controls.MenuItem.prototype, FwBase.Wtf.View.Controls.Listener.prototype, {
		create : function() {
			if(this.metadata.events)
				this.registerEvent(this.metadata);
		},
		registerEvent : function(metadata) {
			for(var i = 0; i < metadata.events.length; i ++){
				var event = metadata.events[i];
				this.addListener("click", event.listener);
			}
		}
	});
	return FwBase.Wtf.View.Controls.Menu;
});

