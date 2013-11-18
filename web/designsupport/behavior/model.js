wdefine(function(){
	$app.metadata('tipoptions',  {value: '', label: 'Help input:', labelWidth: 80, filter: false, width: 160, emptyRecord: true, placeHolder: "",
									options:[
									         {value: 'selector', text: 'Selector'},
									         {value: 'navigator', text: 'Navigator'}
							    ]
				});
	
	$app.metadata('elementoptions',  {value: '', label: 'Choose element:', labelWidth: 140, filter: false, width: 160, emptyRecord: true, placeHolder: "",
									options:[]
				});
	
	$app.metadata("codetab", {activeItem: 'item1', items: [{id:'item1', text : 'Function'}, {id:'item2', text : 'Global Functions'}]});
});