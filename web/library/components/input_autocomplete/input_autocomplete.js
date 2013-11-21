define(["input_base/input_base"], function(inputbase){	
	FwBase.Wtf.View.Controls.Input_autocomplete = function(){
		FwBase.Wtf.View.Controls.InputBase.apply(this, arguments);
	};
	$.extend(FwBase.Wtf.View.Controls.Input_autocomplete.prototype, FwBase.Wtf.View.Controls.InputBase.prototype, 
		{
			mockMetadata: function() {
				this.setDefault({source: availableTags});
			},
			makeDefaultFurther : function() {
			},
			inputMask : function() {
				this.input.autocomplete({
				    source: this.metadata.source
				});
			}
		}
	);
	var availableTags = ["ActionScript", "AppleScript", "Asp", "BASIC", "C", "C++", "Clojure", "COBOL", "ColdFusion", "Erlang", "Fortran", "Groovy", "Haskell", "Java", "JavaScript", "Lisp", "Perl", "PHP", "Python", "Ruby", "Scala", "Scheme"];
	return FwBase.Wtf.View.Controls.Input_autocomplete;
});