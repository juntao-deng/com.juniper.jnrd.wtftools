define(["base/base", "css!./label"], function(base){
	FwBase.Wtf.View.Controls.Label = function(){
		FwBase.Wtf.View.Controls.BaseControl.apply(this, arguments);
	};
	
	FwBase.Wtf.View.Controls.Label.CONST_STYLE_NORMAL = "normal";
	FwBase.Wtf.View.Controls.Label.CONST_STYLE_PRIMARY = "primary";
	FwBase.Wtf.View.Controls.Label.CONST_STYLE_INFO = "info";
	FwBase.Wtf.View.Controls.Label.CONST_STYLE_SUCCESS = "success";
	FwBase.Wtf.View.Controls.Label.CONST_STYLE_WARNING = "warning";
	FwBase.Wtf.View.Controls.Label.CONST_STYLE_DANGER = "danger";
	FwBase.Wtf.View.Controls.Label.CONST_STYLE_INVERSE = "inverse";
	FwBase.Wtf.View.Controls.Label.CONST_STYLE_LINK = "link";
	$.extend(FwBase.Wtf.View.Controls.Label.prototype, FwBase.Wtf.View.Controls.BaseControl.prototype, 
		{
			template: _.template($('#sys_atom_controls_label').html()),
			postInit : function(){
				this.el.addClass("wtfinline");
				var oThis = this;
				this.el.children("#label").click(function(){
					oThis.trigger("click", {source : oThis});
				});
			},
			makeDefault : function(){
				this.setDefault({icon: null, style : "normal", text : "Label"});
				this.metadata.cssclass = "label label-" + this.metadata.style;
			}
		}
	);
	return FwBase.Wtf.View.Controls.Label;
});