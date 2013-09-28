define(["base/base"], function(base){	
	FwBase.Wtf.View.Controls.Menu = function(){};
	$.extend(FwBase.Wtf.View.Controls.Menu.prototype, FwBase.Wtf.View.Controls.BaseControl.prototype, 
		{		
			template: _.template($('#sys_atom_controls_menu').html()),
			type: "menu",
			postInit : function() {
				for(var i = 0; i < this.metadata.groups.length; i ++){
					var group = this.metadata.groups[i];
					for(var j = 0; j < group.menus.length; j ++){
						var menu = group.menus[j];
						if(menu.events)
							this.registerEvent(menu);
						var oThis = this;
						$('#' + this.metadata.id + "_" + menu.id).click(function(menuObj){
							oThis.fireEvent("click", menu);
						});
					};
				};
			},
			registerEvent : function(menu) {
				for(var i = 0; i < menu.events.length; i ++){
					var event = menu.events[i];
					this.addListener(event.name, event.listener);
				}
			}
		}
	);
});

