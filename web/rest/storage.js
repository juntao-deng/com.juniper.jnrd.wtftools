define(function(){
	window.Storage = FwBase.Wtf.Client.Storage = {
		add : function(key, obj) {
			window.localStorage.addItem(key, obj);
		},
		get : function(key){
			window.localStorage.getItem(key);
		}
	};
});