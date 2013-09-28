define(function(){
	FwBase.Wtf.View.Controls.Listener = function(){};
	$.extend(FwBase.Wtf.View.Controls.Listener.prototype, {
		addListener : function(key, listener){
			if(this.listenerMap == null)
				this.listenerMap = {};
			if(this.listenerMap[key] == null)
				this.listenerMap[key] = [];
			this.listenerMap[key].push(listener);
		},
	
		fireEvent : function(eventName, params){
			if(this.listenerMap == null)
				return;
			var listeners = this.listenerMap[eventName];
			if(listeners == null)
				return;
			for(var i = 0; i < listeners.length; i ++){
				listeners[i].fire(params);
			}
		}
	});
	return FwBase.Wtf.View.Controls.Listener;
});