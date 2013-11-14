wdefine(function(){
	var topApp = DesignSupport.getDesignApp();
	//---------------------selector options
	var selectorOptions = {
			groups : [
			          {label: 'Context', options:[]},
			          {label: 'Components', options:[]},
			          {label: 'Models', options:[]},
			          {label: 'Widgets', options:[]},
			]
	};
	selectorOptions.groups[0].options.push({value: 'app', text: 'Current App'});
	var components = topApp.components();
	var options = selectorOptions.groups[1].options;
	for(var i = 0; i < components.length; i ++){
		options.push({value: components[i].id, text: components[i].id});
	}
	var models = topApp.models();
	options = selectorOptions.groups[2].options;
	for(var i = 0; i < models.length; i ++){
		options.push({value: models[i].id, text: models[i].id});
	}
	var widgets = topApp.widgets();
	options = selectorOptions.groups[3].options;
	for(var i = 0; i < widgets.length; i ++){
		options.push({value: widgets[i].id, text: widgets[i].id});
	}
	//------------------------------------------------
	
	var navigateOptions = [
     {value: 'navigateTo', text: 'navigateTo'},
     {value: 'navigateToDialog', text: 'navigateToDialog'},
     {value: 'navigateToCard', text: 'navigateToCard'},
     {value: 'navigateToStack', text: 'navigateToStack'}
	];
	
	$app.component('tipoptions').on('valuechange', function(options){
		var elementOptions = this.ctx.component('elementoptions');
		if(options.value == 'selector'){
			elementOptions.datas(selectorOptions);
		}
		else if(options.value == "navigator"){
			elementOptions.datas(navigateOptions);
		}
	});
	
	$app.component('elementoptions').on('valuechange', function(){
	});
});