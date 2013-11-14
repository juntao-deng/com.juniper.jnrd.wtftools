define(function(){
	window.designApp = $app;
	requireComponent(['menu']);
	window.FwBase.Wtf.Design = {};
	/*
	 * find the nearest wtftype element
	 */
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
	window.DesignSupport = FwBase.Wtf.Design.DesignSupport = {
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
			
				designItem.click(function(event){
					if(!directChild(event.target, this))
						return;
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
				if(oriItem && oriItem[0] == designItem[0] && oriItem.children('.designmenu').length > 0)
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
					                                	         {id:'addwidget',name:'Add Widgets', icon: 'icon-edit'}, 
					                                	         {id:'clear',name:'Clear Content', icon: 'icon-edit'}
					                                	         ]}
					                                ]
					                      }
					                      ],
					             style: 'inverse'
					};
					var menu = new FwBase.Wtf.View.Controls.Menu(FwBase.Wtf.Design.DesignSupport.currParent.children('.designmenu'), meta, 'design_menu_' + type);
					menu.on('click', function(obj){
						if(obj.trigger.id == "addlayout")
							FwBase.Wtf.Design.DesignSupport.addLayout(obj);
						else if(obj.trigger.id == "addtemplate")
							FwBase.Wtf.Design.DesignSupport.addTemplate(obj);
						else if(obj.trigger.id == "addcomponent")
							FwBase.Wtf.Design.DesignSupport.addComponent(obj);
						else if(obj.trigger.id == "addwidget")
							FwBase.Wtf.Design.DesignSupport.addWidget(obj);
						else if(obj.trigger.id == "clear")
							FwBase.Wtf.Design.DesignSupport.clearContent(obj);
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
					                      ],
					            style: 'inverse'
					};
					var menu = new FwBase.Wtf.View.Controls.Menu(FwBase.Wtf.Design.DesignSupport.currParent.children('.designmenu'), meta, 'design_menu_' + type);
					menu.on('click', function(obj){
						if(obj.trigger.id == "edit"){
							FwBase.Wtf.Design.DesignSupport.editComponentAttr(obj);
						}
						else if(obj.trigger.id == "delete"){
							FwBase.Wtf.Design.DesignSupport.deleteComponent(obj);
						}
					});
					return menu;
				}
			},
			eventModelWrapper: function() {
				var eventsOptions = DesignSupport.getEventDescs();
				$app.metadata('actionsdropdown', {label:'&nbsp;&nbsp;Select Actions:', labelWidth:120, multiple: true, width: 400, options: eventsOptions, hint:'&nbsp;&nbsp;&nbsp;&nbsp;See Controller.js'});
			},
			eventControllerWrapper: function() {
				$app.component('actionsdropdown').on('choiseclick', function(options){
					var url = window.frameCtx + "/../designsupport/behavior";
					FwBase.Wtf.Design.DesignSupport.popDialog(url, null, {width:800, height: 500, title : 'Edit Behavior'});
				});
			},
			getEventDescs : function() {
				var id = FwBase.Wtf.Design.DesignSupport.currParent.attr("id");
				var comp = $app.parent.component(id);
				return comp.eventDescs();
			},
			
//			hideMenu : function(designItem) {
//				designItem.find('#sys_1designmenu').remove();
//			},
			addLayout : function() {
				var url = window.frameCtx + "/../designsupport/layout";
				FwBase.Wtf.Design.DesignSupport.popDialog(url, null, {width:700, height: 400, title : 'Add Layout'});
			},
			addTemplate : function() {
				var url = window.frameCtx + "/../designsupport/template";
				FwBase.Wtf.Design.DesignSupport.popDialog(url, null, {width:800, height: 400, title : 'Add Template'});
			},
			addWidget : function() {
				var url = window.frameCtx + "/../designsupport/widget";
				FwBase.Wtf.Design.DesignSupport.popDialog(url, null, {width:800, height: 400, title : 'Add Widget'});
			},
			addComponent : function() {
				var url = window.frameCtx + "/../designsupport/component";
				FwBase.Wtf.Design.DesignSupport.popDialog(url, null, {width:800, height: 400, title : 'Add Component'});
			},
			clearContent : function() {
				FwBase.Wtf.Design.DesignSupport.currParent.html("");
				FwBase.Wtf.Design.DesignSupport.syncHtml();
			},
			editComponentAttr : function(obj) {
				var type = FwBase.Wtf.Design.DesignSupport.currParent.attr('wtftype');
				var url = window.frameCtx + "/../designsupport/compattr/" + type;
				var height = 420;
				var width = 800;
				if(type == 'grid')
					height = 500;
				FwBase.Wtf.Design.DesignSupport.popDialog(url, null, {width:width, height: height, title : FwBase.Wtf.Lang.Utils.capitalize(type) + ' Attributes'});
				obj.eventCtx.stop = true;
			},
			deleteComponent : function(obj) {
				var id = FwBase.Wtf.Design.DesignSupport.currParent.attr("id");
				$app.removeComponent(id);
				FwBase.Wtf.Design.DesignSupport.currParent.remove();
				FwBase.Wtf.Design.DesignSupport.currParent = null;
			},
			editModel : function(){
				var url = window.frameCtx + "/../designsupport/model";
				FwBase.Wtf.Design.DesignSupport.popDialog(url, null, {width:800, height: 400});
			},
			editBehavior : function() {
				var url = window.frameCtx + "/../designsupport/behavior";
				FwBase.Wtf.Design.DesignSupport.popDialog(url, null, {width:800, height: 400});
			},
			popDialog : function(url, reqData, options) {
				FwBase.Wtf.Application.navigateToDialog(url, reqData, options);
			},
			getDesignApp : function() {
				var app = $app;
				while(app != window.designApp){
					app = app.parent;
				}
				return app;
			},
			//========================= for editor ======================================
			syncAppFiles : function(id, md, controller, rest) {
				$app.parent.component(id).reset(md);
				var action = 'updatejs';
				FwBase.Wtf.Design.DesignSupport.interactWithEclipse({action : action, compId: id, metadata : md, controller: controller, rest : rest});	
			},
			
			syncRest : function(rest) {
				//todo request rest file, update memory
				var action = 'updaterest';
				FwBase.Wtf.Design.DesignSupport.interactWithEclipse({action : action, rest: rest});	
			},
			
			syncHtml : function() {
				var action = 'updatehtml';
				FwBase.Wtf.Design.DesignSupport.interactWithEclipse({action : action, html: $('#sys_design_home_content_part').html()});	
			},
			interactWithEclipse : function(obj, callback) {
				var eventId = parseInt(Math.random() * 1000000);
				obj['eventId'] = eventId;
				if(callback){
					FwBase.Wtf.Design.DesignSupport.eventPool[eventId] = callback;
				}
				window.status = 'wtf_event:' + $.toJSON(obj);
			},
			
			fireInput : function(str) {
				var json = $.evalJSON(str);
				var eventId = json['eventId'];
				var callback = FwBase.Wtf.Design.DesignSupport.eventPool[eventId];
				if(callback){
					FwBase.Wtf.Design.DesignSupport.eventPool[eventId] = null;
					callback(json);
				}
			},
			
			eventPool : {}
			//===========================================================================
			
	};
	
	return FwBase.Wtf.Design.DesignSupport;
});