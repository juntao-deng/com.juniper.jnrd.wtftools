define(["mvc_app"], function(){
	FwBase.Wtf.Widget = function(id){
		FwBase.Wtf.Application.call(this, id);
	};
	$.extend(FwBase.Wtf.Widget.prototype, FwBase.Wtf.Application.prototype);
});