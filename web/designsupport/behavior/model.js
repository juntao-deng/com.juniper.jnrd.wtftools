wdefine(function(){
	$app.metadata('tipoptions',  {value: '', label: 'Help input:', labelWidth: 80, filter: false, width: 160, emptyRecord: true, placeHolder: "",
									options:[
									         {value: 'selector', text: 'Selector'},
									         {value: 'navigator', text: 'Navigator'}
							    ]
				});
	
	$app.metadata('elementoptions',  {value: '', label: 'Choose element:', labelWidth: 120, filter: false, width: 160, emptyRecord: true, placeHolder: "",
									options:[]
				});
});