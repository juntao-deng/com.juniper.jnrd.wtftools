define(function(){
	FwBase.Wtf.View.Controls.Listener = function(){};
	$.extend(FwBase.Wtf.View.Controls.Listener.prototype, {
		addListener : function(key, listener){
			if(this.listenerMap == null)
				this.listenerMap = {};
			if(key instanceof Array){
				key = key.join("_");
			}
			if(this.listenerMap[key] == null)
				this.listenerMap[key] = [];
			this.listenerMap[key].push(listener);
		},
	
		fireEvent : function(key, params){
			if(this.listenerMap == null)
				return;
			if(key instanceof Array){
				key = key.join("_");
			}
			var listeners = this.listenerMap[key];
			if(listeners == null)
				return;
			for(var i = 0; i < listeners.length; i ++){
				listeners[i].fire(params);
			}
		}
	});
	return FwBase.Wtf.View.Controls.Listener;
});