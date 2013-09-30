define(["base/base"], function(base){	
	FwBase.Wtf.View.Controls.InputBase = function(){
		FwBase.Wtf.View.Controls.BaseControl.apply(this, arguments);
	};
	$.extend(FwBase.Wtf.View.Controls.InputBase.prototype, FwBase.Wtf.View.Controls.BaseControl.prototype, 
		{
			registerCallBack : function() {
				this.registerCallBackForPopOver();
			},
			
			registerCallBackForPopOver : function() {
				$('popovers').popover();
			}
		}
	);
	return FwBase.Wtf.View.Controls.InputBase;
});