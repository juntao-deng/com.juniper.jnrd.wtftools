define(["base/base", "./jqgrid", "css!./jqgrid", "css!./jqgrid-override"], function(tablebase, tablectrl){
	FwBase.Wtf.View.Controls.Grid = function(){
		FwBase.Wtf.View.Controls.BaseControl.apply(this, arguments);
	};
	$.extend(FwBase.Wtf.View.Controls.Grid.prototype, FwBase.Wtf.View.Controls.BaseControl.prototype, 
		{
			template: _.template($('#sys_atom_controls_grid').html()),
			postInit : function(){
				this.model = this.metadata.model;
//				this.listenTo(this.model, "pagechange", this.repaint);
//				this.listenTo(this.model, "add", this.addRow);
//				this.listenTo(this.model, "delete", this.deleteRow);
//				this.listenTo(this.model, "change", this.changeRow);
//				this.listenTo(this.model, "select", this.selectRow);
//				this.listenTo(this.model, "unselect", this.unselectRow);
				this.paginationEle = this.el.children("#table_pagination");
				this.gridObj = this.el.children('#table').jqGrid({
					datatype: "json",
				    height: this.metadata.height,
				    minHeight: this.metadata.minHeight,
				    rowNum: this.metadata.pagination? this.metadata.pagination.rowNum : null,
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
					autowidth: this.metadata.autowidth,
					cellEdit : this.metadata.editable,
					multiSort : this.metadata.multiSort,
					viewsortcols : [true, 'vertical', true]
				});
				//this.gridObj.jqGrid('navGrid', this.paginationEle ,{add:true,del:false,edit:false,position:'right'});
			},
			repaint : function() {
			},
			addRow : function() {
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
				this.setDefault({height : 'auto', pagination: {rowNum : 10, rowList: [10, 20, 30]}, minHeight : 250, altRows : false, multiselect : false, autowidth: true, editable : false, multiSort : true});
			},
			mockMetadata : function() {
				this.setDefault({multiselect : true, columns: models});
			}
		}
	);
	function getColNames(metadata) {
		var arr = [];
		for(var i = 0; i < metadata.columns.length; i ++){
			arr.push(metadata.columns[i].name);
		}
		return arr;
	}
	var models = [
			        {id: 'id', name:'Inv No',index:'id', width:80, sorttype:"int", search:true},
			        {id: 'invdate', name:'Date',index:'invdate', width:90, sorttype:"date", formatter:"date"},
			        {id: 'name', name:'Client',index:'name', width:100},
			        {id: 'amout', name:'Amount',index:'amount', width:80, align:"right",sorttype:"float", formatter:"number"},
			        {id: 'tax', name:'Tax',index:'tax', width:80, align:"right",sorttype:"float"},        
			        {id: 'total', name:'Total',index:'total', width:80,align:"right",sorttype:"float"},        
			        {id: 'note', name:'Notes',index:'note', width:150, sortable:false}
				];
	
	var mydata3 = [
	       		{id:"12345",name:"Desktop Computer",note:"note",stock:"Yes",ship:"FedEx", sdate:"2007-12-03"},
	       		{id:"23456",name:"Laptop",note:"Long text ",stock:"Yes",ship:"InTime",sdate:"2007-12-03"},
	       		{id:"34567",name:"LCD Monitor",note:"note3",stock:"Yes",ship:"TNT",sdate:"2007-12-03"},
	       		{id:"45678",name:"Speakers",note:"note",stock:"No",ship:"ARAMEX",sdate:"2007-12-03"},
	       		{id:"56789",name:"Laser Printer",note:"note2",stock:"Yes",ship:"FedEx",sdate:"2007-12-03"},
	       		{id:"67890",name:"Play Station",note:"note3",stock:"No", ship:"FedEx",sdate:"2007-12-03"},
	       		{id:"76543",name:"Mobile Telephone",note:"note",stock:"Yes",ship:"ARAMEX",sdate:"2007-12-03"},
	       		{id:"87654",name:"Server",note:"note2",stock:"Yes",ship:"TNT",sdate:"2007-12-03"},
	       		{id:"98765",name:"Matrix Printer",note:"note3",stock:"No", ship:"FedEx",sdate:"2007-12-03"}
	       		];
	
	return FwBase.Wtf.View.Controls.Grid;
});