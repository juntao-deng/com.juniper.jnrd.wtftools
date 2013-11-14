wdefine(function(){
	$app.component('tipoptions').on('valuechange', function(options){
		var elementOptions = this.ctx.component('elementoptions');
		if(options.value == 'selector'){
			elementOptions.data();
		}
		else if(options.value == "navigator"){
			elementOptions.data();
		}
	});
	
	$app.component('elementoptions').on('valuechange', function(){
		
	});
});