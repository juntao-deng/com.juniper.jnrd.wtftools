define(["input_base/input_base", "../../../ext-lib/codemirror/lib/codemirror", "css!../../../ext-lib/codemirror/lib/codemirror", "css!./input_highlight"], function(){
	FwBase.Wtf.View.Controls.Input_highlight = function(){
		FwBase.Wtf.View.Controls.InputBase.apply(this, arguments);
	};
	FwBase.Wtf.View.Controls.Input_highlight.CONST_STYLE_DEFAULT = "default";
	FwBase.Wtf.View.Controls.Input_highlight.CONST_STYLE_AMBIANCE = "ambiance";
	$.extend(FwBase.Wtf.View.Controls.Input_highlight.prototype, FwBase.Wtf.View.Controls.InputBase.prototype, 
		{
			template: _.template($('#sys_atom_controls_input_highlight').html()),
			inputMask : function(){
				requirejs(["../../ext-lib/codemirror/mode/javascript/javascript", "../../ext-lib/codemirror/addon/hint/show-hint", 
				           "../../ext-lib/codemirror/addon/hint/javascript-hint", "css!../../ext-lib/codemirror/addon/hint/show-hint",
				           "css!../../ext-lib/codemirror/theme/ambiance"]);
				CodeMirror.commands.autocomplete = function(cm) {
					CodeMirror.showHint(cm, CodeMirror.hint.javascript);
				};
				this.editor = CodeMirror.fromTextArea(this.el.children("#input_highlight")[0], {
					lineNumbers: true,
					extraKeys: {"Ctrl-Space": "autocomplete"},
					theme: this.metadata.theme
				});
				
			},
			makeDefaultFurther : function(){
				this.setDefault({value: '', theme: FwBase.Wtf.View.Controls.Input_highlight.CONST_STYLE_AMBIANCE, editable: true});
			},
			value : function(){
				if(arguments.length == 0)
					return this.editor.getValue();
				else{
					var v = arguments[0];
					if(v == null)
						v = "";
					this.editor.setValue(v);
				}
			},
			editableHandler : function(editable){
				this.editor.setOption('readOnly', (!editable));
			}
		}
	);
	return FwBase.Wtf.View.Controls.Input_highlight;
});