<jsp:include page="../../templates/treelist/treelist.jsp"></jsp:include>
<script>
	EVENT_ADD = {
		fire : function(param){
			FwBase.Wtf.View.showDialog('/apps/simplelist/adddialog');
		}
	};
	var menuObj = {
		id : "main_menu",
		groups:[
				{menus : [{id:'add',name:'Add', events:[{name:"click", listener: EVENT_ADD}]}, {id:'edit',name:'Edit'}, {id:'del',name:'Delete'}]},
				{menus : [{id:'save',name:'Save'}, {id:'disable',name:'Disable'}]}
			  ]
	};

	var gridObj = {
		readOnly:true,
		headers:[
			{id:'', name:'', sortable:'false'}, {}, {}
		]
	};

	var treeObj = {
	};
	var menuCtrl = FwBase.Wtf.View.createControl({type:"menu", objMeta: menuObj});
	var treeCtrl = FwBase.Wtf.View.createControl({type:"tree", objMeta: treeObj});
</script>