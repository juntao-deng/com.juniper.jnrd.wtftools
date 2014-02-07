wdefine(["../attrmodel"], function(commonModel){
	commonModel();
	$app.model('columnsModel', {lazyInit:true});
	$app.metadata('columnsgrid', {columns: [
			        {text: 'Name', name:'name', width:50},
			        {text: 'Label', name:'label', width: 80},
			        {text: 'Visible', name:'visible', width:80, defaultValue: true},
			        {text: 'Data Type', name:'datatype', width:80, defaultValue: "string"},
			        {text: 'Editor Type', name:'editortype', width:80, defaultValue: 'input'},
			        {text: 'Editable', name:'editable', width:80}
				],
				editable : false,
				height: '220',
				multiselect : false,
				autowidth : true,
				multiSort : true,
				minHeight: 150,
				pagination: null,
				model : 'columnsModel'
			}
	);
	$app.metadata('rowsattr', {defaultValue: 2});
});