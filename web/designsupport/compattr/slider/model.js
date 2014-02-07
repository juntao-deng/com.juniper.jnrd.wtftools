wdefine(function(){
	DesignSupport.eventModelWrapper();
	$app.metadata('styleattr', {value: '', label: 'Style:', labelWidth: 60, filter: false, 
									options:[
										         {value: '', text: 'normal'},
										         {value: 'primary', text: 'primary'},
										         {value: 'info', text: 'info'},
										         {value: 'success', text: 'success'},
										         {value: 'warning', text: 'warning'},
										         {value: 'danger', text: 'danger'},
										         {value: 'inverse', text: 'inverse'},
										         {value: 'link', text: 'link'}
								    ]
	                           }
				);
});