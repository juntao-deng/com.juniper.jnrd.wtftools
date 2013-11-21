define(function(){
	 FwBase = {};
	 FwBase.Wtf = {};
	 FwBase.Wtf.Global = {};
	 FwBase.Wtf.Global.Configuration = {
		buttonMode: 0 // 0 all, 1 icon only, 2 text only  	 
	 };
	 FwBase.Wtf.Client = {};
	 FwBase.Wtf.Client.restServices = {};
	 FwBase.Wtf.Client.mockAjax = function(options){
	 	var url = options.url;
	 	var segs = url.substring(1).split("/");
	 	var requireUtil = requirejs;
		if(window.requirelibs){
			requireUtil = requirelibs[segs[0]];
		}
	 	var jsUrl = "../" + segs[1] + "/" + segs[2];
	 	var oThis = this;
	 	requireUtil([jsUrl], function(rest){
	 		var service = FwBase.Wtf.Client.restServices[segs[2]];
	 		if(service == null){
	 			alert("can't find rest service with url:" + url + ",name:" + segs[1]);
	 			return;
	 		}
	 		var method = null;
	 		var params = [];
	 		if(segs[3] == 'ctx'){
	 			method = "root";
	 		}
	 		else if(segs[3] == 'action'){
	 			method = "action";
	 			params.push(segs[4]);
	 		}
	 		else if(segs[4] != null){
	 		}
	 		else{
	 			method = "root_id";
	 		}
	 		var methodName = options.type + "_" + method;
	 		var result = service[methodName].apply(oThis, params);
	 		options.success(result);
	 	});
	 };
	//alias
	 window.$comp = function(){
		return $app.component.apply($app, arguments); 
	 };
	 
	 window.$model = function(){
		return $app.model.apply($app, arguments);
	 };
});