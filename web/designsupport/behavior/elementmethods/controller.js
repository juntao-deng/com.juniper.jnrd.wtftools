wdefine(function(){
	$app.on('loaded', function(){
		var methods = null;
		var navid = this.reqData('navid');
		if(navid == 'app'){
			methods = DesignSupport.getDesignApp().methodDescs();
		}
		else
			methods = DesignSupport.getDesignApp().component(navid).methodDescs();
//		 {name : 'enable', params: {type: 'boolean'}, desc: 'Set the component enable or not'}
		if(methods != null){
			for(var i = 0; i < methods.length; i ++){
				var paramStr = "";
				var params = methods[i].params;
				if(params != null){
					for(var j = 0; j < params.length; j ++){
						paramStr += params[j].name + "(" + params[j].type + ")";
						if(j != params.length - 1)
							paramStr += ",";
					}
				}
				var row = {methodname: methods[i].name, params: paramStr, methoddesc: methods[i].desc};
				this.model('listmodel').page().add(row);
			}
		}
	});
});