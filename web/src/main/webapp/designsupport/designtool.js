define(function(){
	requireComponent(['menu']);
	window.FwBase.Wtf.Design = {};
	FwBase.Wtf.Design.DesignSupport = {
			designable : function() {
				$("[wtftype]").each(function(){
					if($(this).attr("designable") == 'done')
						return;
					$(this).attr("designable", "done");
					FwBase.Wtf.Design.DesignSupport.addMenu($(this));
				});
			},
			addMenu : function(designItem) {
				var type = designItem.attr("wtftype");
				if(type == "application")
					return;
				
				var ttimes = designItem.attr('wtftt');
				//			if(ttimes == null || ttimes == "" || ttimes == "1")
				designItem.mouseover(function(){
					FwBase.Wtf.Design.DesignSupport.showMenu($(this), type);
					return;
				});
				designItem.mouseout(function(){
					//FwBase.Wtf.Design.DesignSupport.hideMenu($(this));
					return false;
				});
			},
			showMenu : function(designItem, type) {
				if(!FwBase.Wtf.Design.DesignSupport.divs)
					FwBase.Wtf.Design.DesignSupport.divs = {};
				var key = type + "_div";
				if(FwBase.Wtf.Design.DesignSupport.divs[key] == null){
					var div = "<div class='designmenu' id='" + key + "'></div>";
					$(document.body).append(div);
					FwBase.Wtf.Design.DesignSupport.divs[key] = $('#' + key);
					FwBase.Wtf.Design.DesignSupport.createMenu(type);
				}
				FwBase.Wtf.Design.DesignSupport.clearOri();
				
				FwBase.Wtf.Design.DesignSupport.divs[key].currParent = designItem;
				$(designItem).addClass("designele");
				designItem.append(FwBase.Wtf.Design.DesignSupport.divs[key]);
			},
			
			clearOri : function(){
				for(var i in FwBase.Wtf.Design.DesignSupport.divs){
					var div = FwBase.Wtf.Design.DesignSupport.divs[i];
					if(div.currParent){
						div.currParent.removeClass('designele');
					}
					//don't use jquery, it will remove the events
					if(div[0].parentNode)
						div[0].parentNode.removeChild(div[0]);
				}
			},
			
			createMenu : function(type){
				if(type == "container"){
					var meta = {groups : [
					                      {menus : [
					                                {id:'design',name:'', icon: 'icon-th-list', menus : [{id:'addlayout',name:'Add Layout', icon: 'icon-edit'}, {id:'addtemplates',name:'Add Templates', icon: 'icon-edit'}, {id:'addcomponents',name:'Add Components', icon: 'icon-edit'}]}
					                                ]
					                      }
					                      ]
					};
					var menu = new FwBase.Wtf.View.Controls.Menu(FwBase.Wtf.Design.DesignSupport.divs[type + "_div"], meta, 'design_menu_' + type);
					menu.on('click', function(obj){
						if(obj.trigger.id == "addlayout")
							FwBase.Wtf.Design.DesignSupport.addLayout();
					});
					return menu;
				}
				else{
					var meta = {groups : [
					                      {menus : [
					                                {id:'design',name:'', icon: 'icon-th-list', menus : [{id:'edit',name:'Edit', icon: 'icon-edit'}, {id:'delete',name:'Delete', icon: 'icon-remove'}]}
					                                ]
					                      }
					                      ]
					};
					var menu = new FwBase.Wtf.View.Controls.Menu(FwBase.Wtf.Design.DesignSupport.divs[type + "_div"], meta, 'design_menu_' + type);
					menu.on('click', function(obj){
						alert(obj.source);
					});
					return menu;
				}
			},
//			hideMenu : function(designItem) {
//				designItem.find('#sys_1designmenu').remove();
//			},
			addLayout : function() {
				var url = "../designsupport/layout";
				FwBase.Wtf.Design.DesignSupport.popDialog(url);
			},
			popDialog : function(url, reqData) {
				FwBase.Wtf.Application.navigateToDialog(url, reqData);
			}
	};
	return FwBase.Wtf.Design.DesignSupport;
});