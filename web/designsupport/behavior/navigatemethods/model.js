wdefine(function(){
	$app.model('listmodel', {lazyInit: true});
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