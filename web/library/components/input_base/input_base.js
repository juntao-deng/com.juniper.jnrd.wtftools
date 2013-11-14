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
				
				this.readOnly(this.metadata.readOnly);
			},
			getInput : function() {
				return this.el.find('input');
			},
			makeDefault : function() {
				if(this.metadata.label && this.metadata.label != ''){
					this.setDefault({width:null, required: false, readOnly: false, labelWidth:170});
				}
				else{
					this.setDefault({width:null, required: false, readOnly: false, label:'', labelWidth: 0});
				}
				this.makeDefaultFurther();
			},
			makeDefaultFurther: function() {
				
			},
			inputMask : function() {
				this.masked = true;
				this.input.inputmask();
			},
			
			value : function() {
				if(arguments.length == 0){
					if(this.masked)
						return this.input.inputmask("unmaskedvalue");
					return this.input.val();
				}
				else
					this.input.val(arguments[0]);
			},
			
			readOnly : function(){
				if(arguments.length == 0){
					return this.input.attr('readonly') != null;
				}
				else{
					if(arguments[0])
						this.input.attr('readonly', 'readonly');
					else
						this.input.removeAttr('readonly');
				}
			}
		}
	);
	return FwBase.Wtf.View.Controls.InputBase;
});