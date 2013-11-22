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
				if(!this.el.hasClass('line'))
					this.el.addClass("wtfinline");
				this.button = this.el.find("#button");
				var oThis = this;
				this.button.click(function(){
					if(!oThis.enable())
						return;
					oThis.trigger("click", {source : oThis, eventCtx: {}});
				});
				
				this.button.dblclick(function(){
					if(!oThis.enable())
						return;
					oThis.trigger("dbclick", {source : oThis, eventCtx: {}});
				});
			},
			makeDefault : function(){
				this.setDefault({icon: null, style : "", text : "Button"});
				this.metadata.cssclass = "btn btn-" + this.metadata.style;
			},
			
			doEnable : function(enable) {
				if(enable)
					this.button.removeClass("disabled");
				else
					this.button.addClass("disabled");
			},
			
			eventDescs : function() {
				return [{value: 'click', text : 'Click'}, {value: 'dblclick', text : 'Dblclick'}];
			},
			
			/**
			 * return all public methods, for designer
			 */
			methodDescs : function() {
				var methods = FwBase.Wtf.View.Controls.BaseControl.methodDescs.call(this);
				return methods.concat([{name : 'enable', params: {type: 'boolean'}, desc: 'Set the component enable or not'},
				                      {name : 'enable', desc: "Get the component's enable state"}]);
			}
		}
	);
	return FwBase.Wtf.View.Controls.Button;
});