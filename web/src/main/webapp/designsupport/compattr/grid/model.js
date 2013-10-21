wdefine(function(){
//	var columnsModel = {id : 'columnsModel'};
//	$app.model('columnsModel', columnsModel);
	$app.metadata({id: 'columnsgrid', 
				columns: [
			        {text: 'Name', name:'name', width:80},
			        {text: 'Text', name:'text', width: 80},
			        {text: 'Visible', name:'visible',width:90},
			        {text: 'Type', name:'type', width:80},        
			        {text: 'Sortable', name:'sortable', width:80}
				],
				editable : true,
				height: '195',
				multiselect : false,
				autowidth : true,
				multiSort : true,
				minHeight: 150,
				pagination: null,
				model : 'columnsModel'
			}
	);
	/*== For Designer End ==*/
});