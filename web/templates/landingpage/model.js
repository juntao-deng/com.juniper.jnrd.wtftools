wdefine(function(){
	var groups = [
					{menus : [{id:'add',name:'Add', icon:'dd'}, {id:'edit',name:'Edit'}, {id:'save',name:'Save'}, {id:'del',name:'Delete'}]},
					{menus : [{id:'actions',name:'Actions', menus:[{id:'action1', name:'Action1'}, {id:'action2', name:'Action2'}, {id:'action3', name:'Action3'}]}]},
					{menus : [{id:'export',name:'Export', menus: [{id:'print', name:'Print'}, {id:'printall', name:'Print All Pages'}, {divider:true},
					                                               {id:'export2csv', name:'Export to CSV'}, {id:'exportallcsv', name:'Export all pages to CSV'}, {divider:true},
					                                               {id:'export2pdf', name:'Export to PDF'}, {id:'exportallpdf', name:'Export all pages to PDF'}
					                                              ]
							  }]},
					{menus : [{id:'help',name:'Help', menus:[{id:'contents', name:'Contents'}, {id:'faq', name:'FAQ', disabled:true}, {divider:true}, {id:'more', name:'More', menus:[{id:'morea', name:'More A'}, {id:'moreb', name:'More B'}]}]}]}
				 ];
	$app.metadata('top_menu', {groups: groups, handler: 'FwBase.Wtf.UIPattern.Handler.CrudUIHandler'});
});