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
	
	$app.component('elementoptions').on('valuechange', function(options){
		if(options.value == "")
			return;
		var value = this.ctx.component('tipoptions').value();
		if(value == 'selector'){
			var url = window.frameCtx + "/../designsupport/behavior/elementmethods";
			FwBase.Wtf.Design.DesignSupport.popDialog(url, {navid: options.value}, {width:700, height: 400, title : 'Choose target method'});
		}
		else{
			var url = window.frameCtx + "/../designsupport/behavior/navigatemethods";
			FwBase.Wtf.Design.DesignSupport.popDialog(url, {navid: options.value}, {width:700, height: 400, title : 'Choose target application'});
		}
	});
	
	$app.on('loaded', function(){
		this.data('eventName', this.reqData('eventName'));
		this.component('tipoptions').value("selector");
		this.component('codeeditor').value(this.reqData('methodContent'));
		this.component('globalcodeeditor').value(this.reqData('methodGlobalContent'));
	});
	
	$app.component('okbt').on('click', function(){
		var funcContent = $app.component('codeeditor').value();
		var globalFuncContent = $app.component('globalcodeeditor').value();
		var key = "method_" + this.ctx.reqData('eventName');
		var data = {methodGlobalContent: globalFuncContent};
		data[key] =  funcContent;
		this.ctx.parent.data(data);
		//DesignSupport.interactWithEclipse({action: 'state'});
		//DesignSupport.interactWithEclipse({action: "updateController", compId: DesignSupport.currParent.attr('id'), eventName: this.ctx.reqData('eventName'), eventContent: funcContent, globalEventContent: globalFuncContent});
		this.ctx.close();
	});
});