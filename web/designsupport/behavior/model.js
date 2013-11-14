wdefine(function(){
	$app.metadata('tipoptions',  {value: 'selector', label: 'Help input:', labelWidth: 80, filter: false, width: 160,
									options:[
									         {value: 'selector', text: 'Selector'},
									         {value: 'navigator', text: 'Navigator'}
							    ]
				});
	
	$app.metadata('elementoptions',  {value: '', label: 'Choose element:', labelWidth: 120, filter: false, width: 160, emptyRecord: true,
									options:[
									         {value: 'selector', text: 'Selector'},
									         {value: 'navigator', text: 'Navigator'}
							    ]
				});
});