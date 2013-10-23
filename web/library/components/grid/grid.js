define(["base/base", "./jqgrid", "css!./jqgrid", "css!./jqgrid-override"], function(tablebase, tablectrl){
	FwBase.Wtf.View.Controls.Grid = function(){
		FwBase.Wtf.View.Controls.BaseControl.apply(this, arguments);
	};
	$.extend(FwBase.Wtf.View.Controls.Grid.prototype, FwBase.Wtf.View.Controls.BaseControl.prototype, 
		{
			template: _.template($('#sys_atom_controls_grid').html()),
			postInit : function(){
				this.model = $app.model(this.metadata.model);
				if(this.model){
//					this.listenTo(this.model, "pagechange", this.repaint);
					this.listenTo(this.model, "add", this.addRow);
//				this.listenTo(this.model, "delete", this.deleteRow);
//				this.listenTo(this.model, "change", this.changeRow);
//				this.listenTo(this.model, "select", this.selectRow);
//				this.listenTo(this.model, "unselect", this.unselectRow);
					
				}
				this.paginationEle = this.el.children("#table_pagination_" + this.id);
				this.gridObj = this.el.children('#table').jqGrid({
					datatype: "json",
				    height: this.metadata.height,
				    minHeight: this.metadata.minHeight,
				    rowNum: this.metadata.pagination? this.metadata.pagination.rowNum : null,
				    rowList: this.metadata.pagination? this.metadata.pagination.rowList : null,
				    colNames: getColNames(this.metadata),
				    colModel: this.metadata.columns,
				    pager: this.metadata.pagination? this.paginationEle : null,
				    viewrecords: false,
				    hidegrid:false,
				    altRows: this.metadata.altRows,
				    multiselect: this.metadata.multiselect,
				    emptyrecords: "No records to view",
				    loadtext: "Loading...",
					pgtext : "Page {0} of {1}",
					autowidth: this.metadata.autowidth,
					cellEdit : this.metadata.editable,
					multiSort : this.metadata.multiSort,
					viewsortcols : [true, 'vertical', true]
				});
				//this.gridObj.jqGrid('navGrid', this.paginationEle ,{add:true,del:false,edit:false,position:'right'});
			},
			repaint : function() {
			},
			addRow : function(obj) {
				var row = obj.row;
				var index = obj.index;
				if(index == null || index == -1)
					this.gridObj.addRowData(row.cid, row.toJSON());
				
			},
			changeRow : function() {
			},
			selectRow : function() {
			},
			unselectRow : function() {
			},
			showColumn : function(id){
				this.gridObj.jqGrid('navGrid','showCol', id);
			},
			hideColumn : function(id){
				this.gridObj.jqGrid('navGrid','hideCol', id);
			},
			makeDefault : function() {
				if(this.metadata.model == null){
					this.metadata.model = this.id + "_model";
					var model = new FwBase.Wtf.Model();
					$app.model(this.id + "_model", model);
				}
				else if($app.model(this.metadata.model) == null){
					var str = this.metadata.model;
					var model = new FwBase.Wtf.Model();
					$app.model(str, model);
				}
				this.setDefault({pagination: {rowNum : 10, rowList: [10, 20, 30]}, minHeight : 250, altRows : false, multiselect : false, autowidth: true, editable : false, multiSort : true});
			},
			mockMetadata : function() {
				var model = new FwBase.Wtf.Model(this.id + "_model", {});
				$app.model(model);
				this.setDefault({multiselect : true, columns: columns, model : model});
			}
		}
	);
	function getColNames(metadata) {
		var arr = [];
		for(var i = 0; i < metadata.columns.length; i ++){
			arr.push(metadata.columns[i].text);
		}
		return arr;
	}
	columns = [
        {name: 'id', text:'Inv No',index:'id', width:80, sorttype:"int", search:true},
        {name: 'invdate', text:'Date',index:'invdate', width:90, sorttype:"date", formatter:"date"},
        {name: 'name', text:'Client',index:'name', width:100},
        {name: 'amount', text:'Amount',index:'amount', width:80, align:"right",sorttype:"float", formatter:"number"},
        {name: 'tax', text:'Tax',index:'tax', width:80, align:"right",sorttype:"float"},        
        {name: 'total', text:'Total',index:'total', width:80,align:"right",sorttype:"float"},        
        {name: 'note', text:'Notes',index:'note', width:150, sortable:false}
	];
	
	
	return FwBase.Wtf.View.Controls.Grid;
});