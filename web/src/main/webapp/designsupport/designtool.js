define(function(){
	requireComponent(['menu']);
	window.FwBase.Wtf.Design = {};
	function directChild(child, target){
		var pnode = child;
		while(pnode && pnode != document.body){
			if(pnode.getAttribute('wtftype') != null){
				return pnode == target;
			}
			pnode = pnode.parentNode;
		}
		return false;
	}
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
					if(!directChild(event.target, this))
						return;
//					FwBase.Wtf.Design.DesignSupport.currentItem = $(this);
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
				if(oriItem && oriItem[0] == designItem[0])
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
					                                {id:'design',name:'', icon: 'icon-th-list', 
					                                	menus : [{id:'addlayout',name:'Add Layout', icon: 'icon-edit'}, 
					                                	         {id:'addtemplate',name:'Add Templates', icon: 'icon-edit'}, 
					                                	         {id:'addcomponent',name:'Add Components', icon: 'icon-edit'},
					                                	         {id:'clear',name:'Clear Content', icon: 'icon-edit'}
					                                	         ]}
					                                ]
					                      }
					                      ]
					};
					var menu = new FwBase.Wtf.View.Controls.Menu(FwBase.Wtf.Design.DesignSupport.currParent.children('.designmenu'), meta, 'design_menu_' + type);
					menu.on('click', function(obj){
						if(obj.trigger.id == "addlayout")
							FwBase.Wtf.Design.DesignSupport.addLayout();
						else if(obj.trigger.id == "addtemplate")
							FwBase.Wtf.Design.DesignSupport.addTemplate();
						else if(obj.trigger.id == "addcomponent")
							FwBase.Wtf.Design.DesignSupport.addComponent();
						else if(obj.trigger.id == "clear")
							FwBase.Wtf.Design.DesignSupport.clearContent();
					});
					return menu;
				}
				else{
					var meta = {groups : [
					                      {menus : [
					                                {id:'design',name:'', icon: 'icon-th-list', 
					                                	menus : [{id:'edit',name:'Edit', icon: 'icon-edit'}, 
					                                	         {id:'delete',name:'Delete', icon: 'icon-remove'}
					                                	]}
					                                ]
					                      }
					                      ]
					};
					var menu = new FwBase.Wtf.View.Controls.Menu(FwBase.Wtf.Design.DesignSupport.currParent.children('.designmenu'), meta, 'design_menu_' + type);
					menu.on('click', function(obj){
						if(obj.trigger.id == "edit"){
							FwBase.Wtf.Design.DesignSupport.editComponentAttr();
						}
					});
					return menu;
				}
			},
//			hideMenu : function(designItem) {
//				designItem.find('#sys_1designmenu').remove();
//			},
			addLayout : function() {
				var url = "../designsupport/layout";
				FwBase.Wtf.Design.DesignSupport.popDialog(url, null, null);
			},
			addTemplate : function() {
				var url = "../designsupport/template";
				FwBase.Wtf.Design.DesignSupport.popDialog(url, null, {width:800, height: 400});
			},
			
			addComponent : function() {
				var url = "../designsupport/component";
				FwBase.Wtf.Design.DesignSupport.popDialog(url, null, {width:800, height: 400});
			},
			clearContent : function() {
				FwBase.Wtf.Design.DesignSupport.currParent.html("");
			},
			editComponentAttr : function() {
				var type = FwBase.Wtf.Design.DesignSupport.currParent.attr('wtftype');
				var url = "../designsupport/compattr/" + type;
				FwBase.Wtf.Design.DesignSupport.popDialog(url, null, {width:800, height: 400});
			},
			popDialog : function(url, reqData, options) {
				FwBase.Wtf.Application.navigateToDialog(url, reqData, options);
			},
			
			//========================= for editor ======================================
			syncAppFiles : function(id, md, controller, rest) {
				$app.component(id).reset(md);
				var action = 'updatejs';
				FwBase.Wtf.Design.DesignSupport.interactWithEclipse({action : action, metadata : md, controller: controller, rest : rest});	
			},
			
			interactWithEclipse : function(obj, callback) {
				var eventId = Math.random();
				obj['eventId'] = eventId;
				window.status = 'wtf_event:' + $.toJSON(obj);
				if(callback){
					FwBase.Wtf.Design.DesignSupport.eventPool[eventId] = callback;
				}
			},
			
			fireInput : function(str) {
				var json = toJson(str);
				var eventId = json.eventId;
				if(FwBase.Wtf.Design.DesignSupport.eventPool[eventId])
					FwBase.Wtf.Design.DesignSupport.eventPool[eventId]();
			},
			
			eventPool : {}
			//===========================================================================
			
	};
	
	return FwBase.Wtf.Design.DesignSupport;
});