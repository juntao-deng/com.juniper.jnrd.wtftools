define(function(){
	window.Storage = FwBase.Wtf.Client.Storage = {
		add : function(key, obj) {
			window.localStorage.setItem(key, obj);
		},
		get : function(key){
			return window.localStorage.getItem(key);
		}
	};
});