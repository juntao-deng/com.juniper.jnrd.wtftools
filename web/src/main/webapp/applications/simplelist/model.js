var model = new FwBase.Wtf.Model("a", {});

var tableModel = {model : model, headers: {}
};

var SAMPLE_MENU_FUNC = function(eventObj){
	alert(eventObj);
};
var menumd = {
	groups : [
			{menus : [{id:'add1',name:'Add1', events:[{name:"click", listener: SAMPLE_MENU_FUNC}]}, {id:'edit',name:'Edit', events:[{name:"click", listener: SAMPLE_MENU_FUNC}]}, {id:'del',name:'Delete', events:[{name:"click", listener: SAMPLE_MENU_FUNC}]}]},
			{menus : [{id:'save2',name:'Save2', events:[{name:"click", listener: SAMPLE_MENU_FUNC}]}, {id:'disable',name:'Disable', events:[{name:"click", listener: SAMPLE_MENU_FUNC}]}]},
			{menus : [{id:'help',name:'Help', menus:[{id:'samplea', name:'Samplea'}, {id:'sampleb', name:'Sampleb', disabled:true}, {divider:true}, {id:'more', name:'More', menus:[{id:'linka', name:'Linka'}, {id:'linkb', name:'Linkb'}]}]}]}
	]
};
app.applyMetadata("top_menu", menumd);