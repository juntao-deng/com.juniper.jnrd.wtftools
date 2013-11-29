define(["base/base"], function(base){
	FwBase.Wtf.View.Controls.Form = function(){
		FwBase.Wtf.View.Controls.BaseControl.apply(this, arguments);
		this.elements = [];
	};
	$.extend(FwBase.Wtf.View.Controls.Form.prototype, FwBase.Wtf.View.Controls.BaseControl.prototype, {
		template: _.template($('#sys_atom_controls_form').html()),
		postInit : function(){
			this.elements = buildDefaultElements(this.metadata.elements);
			this.model = this.ctx.model(this.metadata.model);
			if(this.model){
				this.listenTo(this.model, "clear", this.clearPage);
				this.listenTo(this.model, "add", this.addRow);
				this.listenTo(this.model, "remove", this.deleteRow);
				this.listenTo(this.model, "change", this.changeRow);
				this.listenTo(this.model, "pagechange", this.pageChange);
				this.listenTo(this.model, "pagination", this.pagination);
				this.listenTo(this.model, "selection", this.lis_selection);
			}
			var oThis = this;
			var requireArr = getRequiresInputs(this.metadata);
			requireComponent(requireArr, function(){
				for(var i = 0; i < oThis.metadata.elements.length; i ++){
					var elemeta = oThis.metadata.elements[i];
					elemeta.labelWidth = oThis.metadata.labelWidth;
					var container = oThis.el.find(".formelement#" + elemeta.name);
					oThis.elements.push(new FwBase.Wtf.View.Controls[Util.capitalize(elemeta.editortype)](container, elemeta, elemeta.name));
				}
				
			});
		},
		makeDefault : function() {
			
		},
		showColumn : function(id){
			this.gridObj.jqGrid('navGrid','showCol', id);
		},
		hideColumn : function(id){
			this.gridObj.jqGrid('navGrid','hideCol', id);
		},
		/*Listeners begin, private*/
		lis_addRow : function(obj) {
			var row = obj.row;
			var index = obj.index;
			if(index == null)
				this.gridObj.addRowData(row.cid, row.toJSON(), null, null, false);
			else
				this.gridObj.addRowData(row.cid, row.toJSON(), 'before', this.model.page().at(index), false);
		},
		lis_changeRow : function() {
		},
		lis_deleteRow : function(obj) {
			var row = obj.row;
			this.gridObj.delRowData(row.cid);
		},
		lis_unselectRow : function() {
		},
		lis_clearPage : function() {
			this.gridObj.clearGridData();
		},
		lis_pageChange : function(){
			this.gridObj.clearGridData();
		},
		lis_pagination : function(pagination){
			this.gridObj.setPagination(pagination);
		},
		lis_selection : function(selections) {
			var row = selections.rows[0];
			for(var i = 0; i <this.elements.length; i ++){
				this.elements[i].value(row.get(key));
			}
		},
		/*Listeners end*/
		mockMetadata : function() {
			var modelId = this.id + "MockModel";
			var model = new FwBase.Wtf.Model(modelId, {});
			model.mock = true;
			this.ctx.model(model);
			this.setDefault({model: modelId, rows : 2, labelWidth : 120, elements : [
			     {name: 'a', label: 'Text:', nextrow : false, rowSpan : 1, editortype:'input'},
			     {name: 'b', label: 'Dropdown Single:', nextrow : false, rowSpan : 1, editortype:'input_dropdown', options: singleDropdownData.options},
			     {name: 'c', label: 'Dropdown Multi:', nextrow : false, rowSpan : 1, width:600, colspan:2, multiple:true, editortype:'input_dropdown', groups: multiDropdownData.groups},
			     {name: 'd', label: 'Checkbox:', nextrow : false, rowSpan : 1, editortype:'input_checkbox'},
			     {name: 'e', label: 'Checkbox group:', nextrow : false, rowSpan : 1, editortype:'input_checkboxgroup', options:[
			                                                                                     					{value: 'value1', name: 'Value 1'},
			                                                                                    					{value: 'value2', name: 'Value 2'},
			                                                                                    					{value: 'value3', name: 'Value 3'}
			                                                                                    					]},
			     {name: 'f', label: 'Radiobox group:', nextrow : false, rowSpan : 1, editortype:'input_radiogroup', options:[
			                                                                                     					{value: 'value1', name: 'Value 1'},
			                                                                                    					{value: 'value2', name: 'Value 2'},
			                                                                                    					{value: 'value3', name: 'Value 3'}
			                                                                                    					]},
			     {name: 'g', label: 'Input date:', nextrow : false, rowSpan : 1, editortype:'input_date'},
			     {name: 'h', label: 'Input ip:', nextrow : false, rowSpan : 1, editortype:'input_ip'},
			     {name: 'i', label: 'Autocomplete:', nextrow : false, rowSpan : 1, editortype:'input_autocomplete', source: ["ActionScript", "AppleScript", "Asp", "BASIC", "C", "C++", "Clojure", "COBOL", "ColdFusion", "Erlang", "Fortran", "Groovy", "Haskell", "Java", "JavaScript", "Lisp", "Perl", "PHP", "Python", "Ruby", "Scala", "Scheme"]},
			     {name: 'j', label: 'Input toggle:', nextrow : false, rowSpan : 1, editortype:'input_toggle'},
			     {name: 'k', label: 'Input search:', nextrow : false, rowSpan : 1, editortype:'input_search'},
			     {name: 'l', label: 'Input textarea:', nextrow : true, rowSpan : 1, editortype:'input_textarea'},
			     {name: 'm', label: 'Input select:', nextrow : false, rowSpan : 1, editortype:'input_select', options:[
	                                                                                     					{value: 'value1', name: 'Value 1'},
	                                                                                    					{value: 'value2', name: 'Value 2'},
	                                                                                    					{value: 'value3', name: 'Value 3'},
	                                                                                    					{value: 'value4', name: 'Value 4'}
	                                                                                    					]}
			]});
			
		}
	});
	
	function getRequiresInputs(metadata) {
		var arr = ["input_base"];
		if(metadata.elements){
			for(var i = 0; i < metadata.elements.length; i ++){
				var type = metadata.elements[i].editortype;
				if(type == null || type == ""){
					alert("wrong input type");
					return;
				}
				arr.push(type);
			}
		}
		return arr;
	}
	
	function buildDefaultElements(elements){
		if(elements == null)
			return;
		for(var i = 0; i < elements.length; i ++){
			Util.setDefault(elements[i], {visible: true, editortype: 'input', editable: true, datatype: 'string'});
		}
	};
	
	var multiDropdownData = {
			groups:[
				{label:'NFC EAST', options:[
					{value: 'Dallas Cowboys', text: 'Dallas Cowboys'},
					{value: 'New York Giants', text: 'New York Giants'},
					{value: 'Philadelphia Eagles', text: 'Philadelphia Eagles'},
					{value: 'Washington Redskins', text: 'Washington Redskins'}
				]},
				{label:'NFC NORTH', options:[
					{value: 'Chicago Bears', text: 'Chicago Bears'},
					{value: 'Detroit Lions', text: 'Detroit Lions'},
					{value: 'Green Bay Packers', text: 'Green Bay Packers'},
					{value: 'Minnesota Vikings', text: 'Minnesota Vikings'}
				]},
				{label:'NFC SOUTH', options:[
					{value: 'Atlanta Falcons', text: 'Atlanta Falcons'},
					{value: 'Carolina Panthers', text: 'Carolina Panthers'},
					{value: 'New Orleans Saints', text: 'New Orleans Saints'},
					{value: 'Tampa Bay Buccaneers', text: 'Tampa Bay Buccaneers'}
				]},
				{label:'NFC WEST', options:[
					{value: 'Arizona Cardinals', text: 'Arizona Cardinals'},
					{value: 'St. Louis Rams', text: 'St. Louis Rams'},
					{value: 'San Francisco 49ers', text: 'San Francisco 49ers'},
					{value: 'Seattle Seahawks', text: 'Seattle Seahawks'}
				]}
			]
		};
	
	var singleDropdownData = {
		options:[
				{value: 'Dallas Cowboys', text: 'Dallas Cowboys'},
				{value: 'New York Giants', text: 'New York Giants'},
				{value: 'Philadelphia Eagles', text: 'Philadelphia Eagles'},
				{value: 'Washington Redskins', text: 'Washington Redskins'}
			]
	};
	return FwBase.Wtf.View.Controls.Form;
});