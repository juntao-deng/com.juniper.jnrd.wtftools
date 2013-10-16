define(["base/base", "inputmask"], function(base){	
	FwBase.Wtf.View.Controls.InputBase = function(){
		FwBase.Wtf.View.Controls.BaseControl.apply(this, arguments);
	};
	$.extend(FwBase.Wtf.View.Controls.InputBase.prototype, FwBase.Wtf.View.Controls.BaseControl.prototype, 
		{
			template: _.template($('#sys_atom_controls_input_base').html()),
			postInit : function() {
				this.input = this.getInput();
				this.inputMask();
				this.value(this.metadata.defaultValue);
				var oThis = this;
				this.input.change(function(){
					var obj = {source : oThis};
					oThis.trigger("change", obj);
				});
			},
			getInput : function() {
				return this.el.find('input');
			},
			makeDefault : function() {
				if(this.metadata.label && this.metadata.label != ''){
					this.setDefault({labelWidth:170});
				}
				else{
					this.setDefault({label:'', labelWidth: 0});
				}
				this.makeDefaultFurther();
			},
			makeDefaultFurther: function() {
				
			},
			inputMask : function() {
				this.input.inputmask();
			},
			
			value : function() {
				if(arguments.length == 0)
					return this.input.inputmask("unmaskedvalue");
				else
					this.input.val(arguments[0]);
			}
		}
	);
	return FwBase.Wtf.View.Controls.InputBase;
});