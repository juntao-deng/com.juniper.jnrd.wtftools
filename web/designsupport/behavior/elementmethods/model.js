wdefine(function(){
	$app.model('listmodel', {lazyInit:true});
	$app.metadata('listgrid', {columns: [
	                    			        {text: 'Method Name', name:'methodname', width:120},
	                    			        {text: 'Params', name:'params', width:180},
	                    			        {text: 'Method Desc', name:'methoddesc', width: 335}
	                    				],
	                    				editable : false,
	                    				height: '240',
	                    				multiselect : false,
	                    				autowidth : false,
	                    				multiSort : true,
	                    				pagination: null,
	                    				model : 'listmodel'
	                    			}
	                    	);
});