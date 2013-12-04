define(["base/base", "inputmask"], function(base){	
	FwBase.Wtf.View.Controls.InputBase = function(){
		FwBase.Wtf.View.Controls.BaseControl.apply(this, arguments);
	};
	$.extend(FwBase.Wtf.View.Controls.InputBase.prototype, FwBase.Wtf.View.Controls.BaseControl.prototype, 
		{
			template: _.template($('#sys_atom_controls_input_base').html()),
			postInit : function() {
//				this.el.addClass("inline");
				this.input = this.getInput();
				this.inputMask();
				this.value(this.metadata.defaultValue);
				var oThis = this;
				this.input.change(function(){
					var obj = {source : oThis, value: oThis.value(), eventCtx: {}};
					oThis.trigger("valuechange", obj);
				});
				this.editableAttr = true;
				this.editable(this.metadata.editable);
			},
			getInput : function() {
				return this.el.find('input');
			},
			makeDefault : function() {
				if(this.metadata.label && this.metadata.label != ''){
					this.setDefault({width:null, required: false, editable: true, labelWidth:100});
				}
				else{
					this.setDefault({width:null, required: false, editable: true, label:'', labelWidth: 0});
				}
				if(typeof this.metadata.labelWidth != "number")
					this.metadata.labelWidth = parseInt(this.metadata.labelWidth);
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
			
			editable : function(){
				if(arguments.length == 0){
					return this.editableAttr;
				}
				else{
					if(arguments[0] == this.editableAttr)
						return;
					this.editableAttr = arguments[0];
					if(this.editableHandler){
						this.editableHandler(arguments[0]);
					}
					else{
						if(!arguments[0])
							this.input.attr('readonly', 'readonly');
						else
							this.input.removeAttr('readonly');
					}
				}
			},
			
			eventDescs : function() {
				return [{value: 'valuechange', text : 'Value Change'}, {value: 'focus', text : 'Focus'}, {value: 'blur', text : 'Blur'}];
			},
			
			/**
			 * return all public methods, for designer
			 */
			methodDescs : function() {
				var methods = FwBase.Wtf.View.Controls.BaseControl.methodDescs.call(this);
				return methods.concat([{name : 'editable', params: {type: 'boolean'}, desc: 'Set the component enable or not'},
				                      {name : 'editable', desc: "Get the component's enable state"}]);
			}
		}
	);
	return FwBase.Wtf.View.Controls.InputBase;
});