wdefine(function(){
	$app.metadata('columnsgrid', {columns: [
			        {text: 'Name', name:'name', width:80},
			        {text: 'Text', name:'text', width: 80},
			        {text: 'Visible', name:'visible',width:90},
			        {text: 'Type', name:'type', width:80},        
			        {text: 'Sortable', name:'sortable', width:80}
				],
				editable : false,
				height: '195',
				multiselect : false,
				autowidth : true,
				multiSort : true,
				minHeight: 150,
				pagination: null,
				model : 'columnsModel'
			}
	);
	
	$app.metadata('columnmenu', {handler: null, buttonMode: 1, groups : [
					{menus : [{id:'add',name:'Add', icon:'icon-plus'}, {id:'edit',name:'Edit', icon: 'icon-edit'}, {id:'del',name:'Delete', icon: ' icon-minus'}]},
					{menus : [{id:'up',name:'Up', icon: 'icon-arrow-up'}, {id:'down', name:'Down', icon: 'icon-arrow-down'}]}]});
	
	var events = [
					{value: 'click', name: 'Click'},
					{value: 'doubleclick', name: 'Double Click'},
					{value: 'beforecelledit', name: 'Before Edit'}
				];
	$app.metadata('actionsdropdown', {label:'&nbsp;&nbsp;Select Actions:', labelWidth:120, multiple: true, width: 400, options: events, hint:'&nbsp;&nbsp;&nbsp;&nbsp;See Controller.js'});
});