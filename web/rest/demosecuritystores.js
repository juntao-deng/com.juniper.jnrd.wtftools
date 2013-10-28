define(["storage"], function(){
	var serviceName = "demosecuritystores";
	FwBase.Wtf.Client.restServices[serviceName] = {
		GET_root : function(params) {
			if(Storage.get(serviceName) != null){
				return Storage.get(serviceName);
			}
			return [{"id":"1","user":"admin","password":"admin","icon":"icon-dashboard"},
			        {"id":"2","user":"super","password":"super","icon":"icon-dashboard"}
			];
		},
		GET_root_id : function(id) {
			var list = Storage.get(serviceName);
			if(list != null){
				for(var i = 0; i < list.length; i ++){
					var obj = list[i];
					if(obj['id'] == id)
						return obj;
				}
			}
			return null;
		},
		POST_root : function(obj){
			var list = Storage.get(serviceName);
			if(list == null){
				list = [];
				Storage.add(serviceName, list);
			}
			list.push(obj);
		},
		DELETE_root : function(id) {
			var list = Storage.get(serviceName);
			if(list != null){
				for(var i = 0; i < list.length; i ++){
					var obj = list[i];
					list.splice(i, 1);
				}
			}
		},
		PUT_root : function(obj) {
		},
		POST_action_name : function(action){
			
		}
	};
});