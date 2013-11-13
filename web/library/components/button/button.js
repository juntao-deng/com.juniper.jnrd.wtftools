define(["base/base"], function(base){
	FwBase.Wtf.View.Controls.Button = function(){
		FwBase.Wtf.View.Controls.BaseControl.apply(this, arguments);
	};
	
	FwBase.Wtf.View.Controls.Button.CONST_STYLE_NORMAL = "";
	FwBase.Wtf.View.Controls.Button.CONST_STYLE_PRIMARY = "primary";
	FwBase.Wtf.View.Controls.Button.CONST_STYLE_INFO = "info";
	FwBase.Wtf.View.Controls.Button.CONST_STYLE_SUCCESS = "success";
	FwBase.Wtf.View.Controls.Button.CONST_STYLE_WARNING = "warning";
	FwBase.Wtf.View.Controls.Button.CONST_STYLE_DANGER = "danger";
	FwBase.Wtf.View.Controls.Button.CONST_STYLE_INVERSE = "inverse";
	FwBase.Wtf.View.Controls.Button.CONST_STYLE_LINK = "link";
	$.extend(FwBase.Wtf.View.Controls.Button.prototype, FwBase.Wtf.View.Controls.BaseControl.prototype, 
		{
			template: _.template($('#sys_atom_controls_button').html()),
			postInit : function(){
				this.el.addClass("inline");
				var oThis = this;
				this.el.find("#button").click(function(){
					oThis.trigger("click", {source : oThis});
				});
			},
			makeDefault : function(){
				this.setDefault({icon: null, style : "", text : "Button"});
				this.metadata.cssclass = "btn btn-" + this.metadata.style;
			}
		}
	);
	return FwBase.Wtf.View.Controls.Button;
});