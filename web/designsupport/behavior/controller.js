wdefine(function(){
//	var selectorOptions = 
	$app.component('tipoptions').on('valuechange', function(options){
		var elementOptions = this.ctx.component('elementoptions');
		if(options.value == 'selector'){
			elementOptions.datas(null);
		}
		else if(options.value == "navigator"){
			elementOptions.datas(null);
		}
	});
	
	$app.component('elementoptions').on('valuechange', function(){
	});
});