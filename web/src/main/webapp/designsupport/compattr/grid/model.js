wdefine(function(){
	/*== For Designer Start {metadata:'attrgrid'}, the objects existing can be freely edited, but don't add new object here ==*/
	$app.metadata({id: 'columnsgrid', 
				columns: [
			        {id: 'id', name:'Id',index:'id', width:80, sorttype:"int", search:true},
			        {id: 'Name', name:'Name',index:'invdate', width:90, sorttype:"date", formatter:"date"},
			        {id: 'Visible', name:'Visbile',index:'invdate', width:90, sorttype:"date", formatter:"date"},
			        {id: 'Type', name:'Type',index:'tax', width:80, align:"right",sorttype:"float"},        
			        {id: 'Sortable', name:'Sortable',index:'total', width:80,align:"right",sorttype:"float"}
				],
				editable : true,
				height: '100%',
				multiselect : false,
				autowidth : false,
				multiSort : true,
				minHeight: 150,
				pagination: null
			}
	);
	/*== For Designer End ==*/
});