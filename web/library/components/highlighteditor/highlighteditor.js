define(["base/base", "../../../ext-lib/codemirror/lib/codemirror", "css!../../../ext-lib/codemirror/lib/codemirror", "../../../ext-lib/codemirror/mode/javascript/javascript", "../../../ext-lib/codemirror/addon/hint/show-hint", 
        "../../../ext-lib/codemirror/addon/hint/javascript-hint", "css!../../../ext-lib/codemirror/addon/hint/show-hint",
        "css!../../../ext-lib/codemirror/theme/ambiance"], function(){
	FwBase.Wtf.View.Controls.Highlighteditor = function(){
		FwBase.Wtf.View.Controls.BaseControl.apply(this, arguments);
	};
	FwBase.Wtf.View.Controls.Highlighteditor.CONST_STYLE_AMBIANCE = "ambiance";
	$.extend(FwBase.Wtf.View.Controls.Highlighteditor.prototype, FwBase.Wtf.View.Controls.BaseControl.prototype, 
		{
			template: _.template($('#sys_atom_controls_highlighteditor').html()),
			postInit : function(){
				CodeMirror.commands.autocomplete = function(cm) {
					CodeMirror.showHint(cm, CodeMirror.hint.javascript);
				};
				this.editor = CodeMirror.fromTextArea(this.el.children("#highlighteditor")[0], {
					lineNumbers: true,
					extraKeys: {"Ctrl-Space": "autocomplete"},
					theme: 'ambiance'
				});
			},
			makeDefault : function(){
				
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
			}
		}
	);
	return FwBase.Wtf.View.Controls.Highlighteditor;
});