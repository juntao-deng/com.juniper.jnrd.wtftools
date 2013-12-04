define(function(){
	window.RestApi = FwBase.Wtf.RestApi = {
	 	action : function(options){
	 		params = options.params || {};
	 		if(window.clientMode){
	 			RestApi.syncFromClient.call(this, type, this, options);
	 		}
	 		else{
	 			var restUrl = options.restUrl;
	 			var ctx = $app.webroot;
	 			restUrl = "/" + ctx + "/" + FwBase.Wtf.Model.defaults.resturlbase + "/" + restUrl + "/action/" + options.actionName;
	 			$.post(restUrl, options.params, options.callback);
	 		}
	 	}
	};
});