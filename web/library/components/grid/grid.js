define(["base/base", "./jqgrid", "css!./jqgrid", "css!./jqgrid-override"], function(tablebase, tablectrl){
	FwBase.Wtf.View.Controls.Grid = function(){
		FwBase.Wtf.View.Controls.BaseControl.apply(this, arguments);
	};
	$.extend(FwBase.Wtf.View.Controls.Grid.prototype, FwBase.Wtf.View.Controls.BaseControl.prototype, 
		{
			template: _.template($('#sys_atom_controls_grid').html()),
			postInit : function(){
				this.model = this.ctx.model(this.metadata.model);
				if(this.model){
					this.listenTo(this.model, "clear", this.clearPage);
					this.listenTo(this.model, "add", this.addRow);
					this.listenTo(this.model, "remove", this.deleteRow);
					this.listenTo(this.model, "change", this.changeRow);
					this.listenTo(this.model, "pagechange", this.pageChange)
					this.listenTo(this.model, "pagination", this.pagination)
//					this.listenTo(this.model, "page", this.selectRow);
//					this.listenTo(this.model, "unselect", this.unselectRow);
					
				}
				this.paginationEle = this.el.children("#table_pagination_" + this.instance);
				var pageSize = this.metadata.pagination? this.metadata.pagination.rowNum : null;
				this.gridObj = this.el.children('#table' + this.instance).jqGrid({
					datatype: "json",
				    height: this.metadata.height,
				    minHeight: this.metadata.minHeight,
				    rowNum: pageSize,
				    rowList: this.metadata.pagination? this.metadata.pagination.rowList : null,
				    colNames: getColNames(this.metadata),
				    colModel: this.metadata.columns,
				    pager: this.metadata.pagination? this.paginationEle : null,
				    viewrecords: true,
				    hidegrid:false,
				    altRows: this.metadata.altRows,
				    multiselect: this.metadata.multiselect,
				    emptyrecords: "No records to view",
				    loadtext: "Loading...",
					pgtext : "Page {0} of {1}",
					recordtext: "Record {0} to {1}, Total: {2}",
					autowidth: this.metadata.autowidth,
					cellEdit : this.metadata.editable,
					multiSort : this.metadata.multiSort,
					viewsortcols : [true, 'vertical', true]
				});
				if(this.model){
					this.model.pageSize(pageSize == null ? -1 : pageSize);
					var oThis = this;
					this.gridObj.bind("jqGridPageChange", function(){
						var pageIndex = arguments[1].pageIndex;
						var pageSize = arguments[1].pageSize;
						oThis.model.requestPage({pageIndex: pageIndex, pageSize: pageSize, forceUpdate: true});
					});
				}
				//this.gridObj.jqGrid('navGrid', this.paginationEle ,{add:true,del:false,edit:false,position:'right'});
			},
			repaint : function(obj) {
				this.gridObj.clearGridData();
			},
			addRow : function(obj) {
				var row = obj.row;
				var index = obj.index;
				if(index == null)
					this.gridObj.addRowData(row.cid, row.toJSON(), null, null, false);
				else
					this.gridObj.addRowData(row.cid, row.toJSON(), 'before', this.model.page().at(index), false);
			},
			changeRow : function() {
			},
			deleteRow : function(obj) {
				var row = obj.row;
				this.gridObj.delRowData(row.cid);
			},
			unselectRow : function() {
			},
			showColumn : function(id){
				this.gridObj.jqGrid('navGrid','showCol', id);
			},
			hideColumn : function(id){
				this.gridObj.jqGrid('navGrid','hideCol', id);
			},
			clearPage : function() {
				this.gridObj.clearGridData();
			},
			pageChange : function(){
				this.gridObj.clearGridData();
			},
			pagination : function(pagination){
				this.gridObj.setPagination(pagination);
			},
			makeDefault : function() {
//				if(this.metadata.model == null){
//					this.metadata.model = this.id + "_model";
//					var model = new FwBase.Wtf.Model();
//					$app.model(this.id + "_model", model);
//				}
//				else if($app.model(this.metadata.model) == null){
//					var str = this.metadata.model;
//					var model = new FwBase.Wtf.Model();
//					$app.model(str, model);
//				}
				if(this.metadata.height)
					this.metadata.minHeight = null;
				this.setDefault({pagination: {rowNum : 10, rowList: [10, 15, 30]}, minHeight : 300, altRows : false, multiselect : false, autowidth: true, editable : false, multiSort : true});
			},
			mockMetadata : function() {
				var modelId = this.id + "MockModel";
				var model = new FwBase.Wtf.Model(modelId, {});
				model.mock = true;
				this.ctx.model(model);
				this.setDefault({multiselect : true, columns: columns, model : modelId});
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