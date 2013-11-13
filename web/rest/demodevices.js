define(["storage"], function(){
	var serviceName = "demodevices";
	FwBase.Wtf.Client.restServices[serviceName] = {
		GET_root : function(params) {
			if(Storage.get(serviceName) != null){
				return $.evalJSON(Storage.get(serviceName));
				rerurn [{id :'1', name :'dd'}, {}];
			}
			return null;
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
		POST_action : function(action){
			var list = [];
			for(var i = 0; i < 10; i ++)
				list.push({id: i, name:'name' + i, address: 'address_' + i});
			Storage.add(serviceName, $.toJSON(list));
		}
	};
});