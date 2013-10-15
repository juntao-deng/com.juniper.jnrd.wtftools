define(function(){
	requireComponent(['menu']);
	window.FwBase.Wtf.Design = {};
	FwBase.Wtf.Design.DesignSupport = {
			designable : function() {
				$('#design_container').find("[wtftype]").each(function(){
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
				if(type == "container"){
					designItem.addClass("designele_sign");
				}
				//var ttimes = designItem.attr('wtftt');
				//			if(ttimes == null || ttimes == "" || ttimes == "1")
				designItem.click(function(event){
					if(event.target != this)
						return;
					FwBase.Wtf.Design.DesignSupport.currentItem = $(this);
					FwBase.Wtf.Design.DesignSupport.showMenu($(this), type);
				});
				designItem.mouseout(function(){
					//FwBase.Wtf.Design.DesignSupport.hideMenu($(this));
					return false;
				});
			},
			showMenu : function(designItem, type) {
//				$(document.body).append(div);
				var oriItem = FwBase.Wtf.Design.DesignSupport.currParent;
				if(oriItem == designItem)
					return;
				if(oriItem){
					if(oriItem.attr('wtftype') == 'container')
						oriItem.addClass('designele_sign');
					oriItem.removeClass('designele');
					oriItem.children('.designmenu').remove();
				}
				FwBase.Wtf.Design.DesignSupport.currParent = designItem;
				designItem.removeClass('designele_sign');
				designItem.addClass('designele');
				var div = "<div class='designmenu'></div>";
				designItem.append(div);
				FwBase.Wtf.Design.DesignSupport.createMenu(type);
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
					var menu = new FwBase.Wtf.View.Controls.Menu(FwBase.Wtf.Design.DesignSupport.currParent.children('.designmenu'), meta, 'design_menu_' + type);
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
					var menu = new FwBase.Wtf.View.Controls.Menu(FwBase.Wtf.Design.DesignSupport.currParent.children('.designmenu'), meta, 'design_menu_' + type);
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