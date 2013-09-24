<jsp:include page="../../templates/treelist/treelist.jsp"></jsp:include>
<script>
	var menuObj = {
		groups:[
			[{id:'add',name:'Add'}, {id:'edit',name:'Edit'}, {id:'del',name:'Delete'}],
			[{id:'save',name:'Save'}, {id:'disable',name:'Disable'}]
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