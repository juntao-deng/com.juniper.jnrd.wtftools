define(["base/base", "../../../ext-lib/codemirror/lib/codemirror", "css!../../../ext-lib/codemirror/lib/codemirror"], function(){
	FwBase.Wtf.View.Controls.Highlighteditor = function(){
		FwBase.Wtf.View.Controls.BaseControl.apply(this, arguments);
	};
	FwBase.Wtf.View.Controls.Highlighteditor.CONST_STYLE_AMBIANCE = "ambiance";
	$.extend(FwBase.Wtf.View.Controls.Highlighteditor.prototype, FwBase.Wtf.View.Controls.BaseControl.prototype, 
		{
			template: _.template($('#sys_atom_controls_highlighteditor').html()),
			postInit : function(){
				var oThis = this;
				requirejs(["../../ext-lib/codemirror/mode/javascript/javascript", "../../ext-lib/codemirror/addon/hint/show-hint", 
				           "../../ext-lib/codemirror/addon/hint/javascript-hint", "css!../../ext-lib/codemirror/addon/hint/show-hint",
				           "css!../../ext-lib/codemirror/theme/ambiance"], function(){
					CodeMirror.commands.autocomplete = function(cm) {
						CodeMirror.showHint(cm, CodeMirror.hint.javascript);
					};
					oThis.editor = CodeMirror.fromTextArea(oThis.el.children("#highlighteditor")[0], {
						lineNumbers: true,
						extraKeys: {"Ctrl-Space": "autocomplete"},
						theme: 'ambiance'
					});
				});
			},
			makeDefault : function(){
				
			},
			value : function(){
				if(arguments.length == 0)
					return this.editor.getValue();
				else
					this.editor.setValue(arguments[0]);
			}
		}
	);
	return FwBase.Wtf.View.Controls.Highlighteditor;
});