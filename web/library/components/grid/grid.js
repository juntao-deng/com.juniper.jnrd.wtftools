define(["base/base", "./jqgrid", "css!./jqgrid", "css!./jqgrid-override"], function(tablebase, tablectrl){
	FwBase.Wtf.View.Controls.Grid = function(){
		FwBase.Wtf.View.Controls.BaseControl.apply(this, arguments);
	};
	$.extend(FwBase.Wtf.View.Controls.Grid.prototype, FwBase.Wtf.View.Controls.BaseControl.prototype, 
		{
			template: _.template($('#sys_atom_controls_grid').html()),
			postInit : function(){
				buildDefaultColumns(this.metadata.columns);
				this.model = this.ctx.model(this.metadata.model);
				if(this.model){
					this.listenTo(this.model, "add", this.lis_addRow);
					this.listenTo(this.model, "remove", this.lis_deleteRow);
					this.listenTo(this.model, "cellchange", this.lis_cellChange);
					this.listenTo(this.model, "pagechange", this.lis_pageChange);
					this.listenTo(this.model, "pagination", this.lis_pagination);
					this.listenTo(this.model, "selection", this.lis_selection);
					this.listenTo(this.model, "syncover", this.lis_syncover);
					this.listenTo(this.model, "synching", this.lis_synching);
				}
				this.paginationId = "#table_pagination_" + this.instance;
				this.paginationEle = this.el.children(this.paginationId);
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
				    multiselect: this.metadata.multiSelect,
				    emptyrecords: "No records to view",
				    loadtext: "Loading...",
					pgtext : "Page {0} of {1}",
					recordtext: "Record {0} to {1}, Total: {2}",
					autowidth: this.metadata.autowidth,
					cellEdit : this.metadata.editable,
					multiSort : this.metadata.multiSort,
					viewsortcols : [true, 'vertical', true],
//					onSelectRow : this.fireOnSelectRow,
//					onSelectAll : this.fireOnSelectAll,
					beforeSelectRow: this.fireOnBeforeSelectRow,
					onCellSelect : this.fireOnCellSelect,
					ondblClickRow : this.fireOnDblclickRow,
					onRightClickRow: this.fireOnRightClickRow,
					onSortCol : this.fireOnSortCol
					
				});
				this.gridObj[0].objOwner = this;
				this.gridObj.bind('jqGridSelectRow', this.fireOnSelectRow);
				this.gridObj.bind('jqGridSelectAll', this.fireOnSelectAll);
				this.gridObj.bind('jqGridDblClickRow', this.fireOnDblClickRow);
				if(this.model){
					this.model.pageSize(pageSize == null ? -1 : pageSize);
					var oThis = this;
					this.gridObj.bind("jqGridPageChange", function(){
						var pageIndex = arguments[1].pageIndex;
//						var pageSize = arguments[1].pageSize;
						oThis.model.currentPage(null, pageIndex);
					});
				}
				
				this.createNavBar();
				
//				this.gridObj.jqGrid('navGrid', this.paginationEle).jqGrid('navButtonAdd', this.paginationEle, { caption:"NewButton", buttonicon:"ui-icon-newwin", onClickButton:null, position: "last", title:"", cursor: "pointer"});
//				this.gridObj.jqGrid('navGrid', paginationId ,{add:false,del:false,edit:false,view:false,position:'left'});
			},
			createNavBar : function(){
				this.navBar = this.paginationEle.find(this.paginationId + "_left");
				var div = $("<div></div>");
				this.navBar.append(div);
				var attr = {
						display: "inline-block",
						marginLeft : "20px"
				};
				var oThis = this;
				var search = $('<div><i class="icon-search"></i></div>').css(attr);
				var refresh = $('<div><i class="icon-refresh"></i></div>').css(attr).click(function(){
					if(oThis.model)
						oThis.model.reload();
				});
				div.append(search);
				div.append(refresh);
			},
			repaint : function(obj) {
				this.gridObj.clearGridData();
			},
			showColumn : function(id){
				this.gridObj.jqGrid('navGrid','showCol', id);
			},
			hideColumn : function(id){
				this.gridObj.jqGrid('navGrid','hideCol', id);
			},
			makeDefault : function() {
				if(this.metadata.height)
					this.metadata.minHeight = null;
				this.setDefault({pagination: {rowNum : 10, rowList: [10, 15, 30]}, minHeight : 300, altRows : false, multiSelect : false, autowidth: true, editable : false, multiSort : true});
			},
			mockMetadata : function() {
				var modelId = this.id + "MockModel";
				var model = new FwBase.Wtf.Model(modelId, {});
				model.mock = true;
				this.ctx.model(model);
				this.setDefault({multiselect : true, columns: columns, model : modelId});
			},
			/*Fire events start, private*/
			fireOnSelectRow : function() {
				var sel = arguments[2];
				var id = arguments[1];
				if(sel){
					this.objOwner.model.select(id, !this.objOwner.metadata.multiSelect);
				}
				else{
					this.objOwner.model.unselect(id);
				}
			},
			fireOnSelectAll : function() {
				var sel = arguments[2];
				var ids = arguments[1];
				if(sel){
					this.objOwner.model.select(ids, !this.objOwner.metadata.multiSelect);
				}
				else{
					this.objOwner.model.unselect(ids);
				}
			},
			fireOnDblClickRow : function() {
				var rowId = arguments[1];
				var row = this.objOwner.model.row(rowId);
				var options = {source: this.objOwner, eventCtx: {}, rowIndex: (arguments[2] - 1), rowId: rowId, row: row};
				this.objOwner.trigger('dblclick', options);
			},
			/*Fire events end*/
			/*Listeners start, private */
			lis_addRow : function(obj) {
				if(obj.page.synching)
					return;
				var row = obj.row;
				var index = obj.index;
				var id = row.id;
				if(id == null)
					id = row.cid;
				if(index == null)
					this.gridObj.addRowData(id, row.toJSON(), null, null, false);
				else
					this.gridObj.addRowData(id, row.toJSON(), 'before', this.model.page().at(index), false);
			},
			lis_cellChange : function(options) {
				var row = options.row;
				var index = options.index;
				var id = row.id;
				if(id == null)
					id = row.cid;
				var row = options.row;
				var changedAttr = options.changedAttr;
				this.gridObj.setRowData(id, changedAttr);
			},
			lis_deleteRow : function(obj) {
				var row = obj.row;
				var id = row.id;
				if(id == null)
					id = row.cid;
				this.gridObj.delRowData(id);
			},
			lis_unselectRow : function() {
			},
			lis_pageChange : function(){
				this.gridObj.clearGridData();
			},
			lis_pagination : function(pagination){
				this.gridObj.setPagination(pagination);
			},
			lis_selection : function(selections) {
			},
			lis_synching : function(){
				this.gridObj.clearGridData();
			},
			lis_syncover : function(options) {
				if(!options.current)
					return;
				var rows = this.model.rows();
				if(rows == null || rows.length == 0)
					return;
				for(var i = 0; i < rows.length; i ++){
					var row = rows[i];
					var id = row.id;
					if(id == null)
						id = row.cid;
					this.gridObj.addRowData(id, row.toJSON(), null, null, false);
				}
			}
			/*Listeners end*/
		}
	);
	function getColNames(metadata) {
		var arr = [];
		for(var i = 0; i < metadata.columns.length; i ++){
			arr.push(metadata.columns[i].text);
		}
		return arr;
	}
	function buildDefaultColumns(columns){
		if(columns == null)
			return;
		for(var i = 0; i < columns.length; i ++){
			var align = "left";
			var dt = columns[i].datatype;
			if(dt == 'float' || dt == 'int')
				align = "right";
			Util.setDefault(columns[i], {visible: true, editortype: 'input', sortable: true, editable: true, datatype: 'string', formatter: 'string', align: align});
		}
	};
	var columns = [
        {name: 'id', text:'Id', width:80},
        {name: 'name', text:'Name', width:100},
        {name: 'code', text:'Code', width:90, formatter:"date"},
        {name: 'amount', text:'Amount', width:80, align:"right",sorttype:"float", formatter:"number"},
        {name: 'tax', text:'Tax',index:'tax', width:80, align:"right",sorttype:"float"},        
        {name: 'total', text:'Total',index:'total', width:80,align:"right",sorttype:"float"},        
        {name: 'note', text:'Notes',index:'note', width:150, sortable:false}
	];
	
	return FwBase.Wtf.View.Controls.Grid;
});