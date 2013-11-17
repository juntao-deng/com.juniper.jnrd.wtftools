wdefine(function(){
	$app.metadata('tipoptions',  {value: 'selector', label: 'Help input:', labelWidth: 80, filter: false, width: 160,
									options:[
									         {value: 'selector', text: 'Selector'},
									         {value: 'navigator', text: 'Navigator'}
							    ]
				});
	
	$app.metadata('elementoptions',  {value: '', label: 'Choose element:', labelWidth: 120, filter: false, width: 160,
									options:[
									         {value: 'selector', text: 'Selector'},
									         {value: 'navigator', text: 'Navigator'}
							    ]
				});
	
	$app.metadata('listgrid', {columns: [
	                    			        {text: 'Web Context', name:'webctx', width:80},
	                    			        {text: 'App Id', name:'appid', width: 80}
	                    				],
	                    				editable : false,
	                    				height: '240',
	                    				multiselect : false,
	                    				autowidth : true,
	                    				multiSort : true,
	                    				pagination: null,
	                    				model : 'listmodel'
	                    			}
	                    	);
});