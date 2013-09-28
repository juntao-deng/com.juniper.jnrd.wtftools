<jsp:include page="../../templates/form/form.jsp"></jsp:include>
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

	var formObj = {
		readOnly:true,
		headers:[
			{id:'', name:'', sortable:'false'}, {}, {}
		]
	};
	var menuCtrl = FwBase.Wtf.View.createControl({type:"menu", objMeta: menuObj});
	var tableCtrl = FwBase.Wtf.View.createControl({type:"form", objMeta: formObj});
</script>