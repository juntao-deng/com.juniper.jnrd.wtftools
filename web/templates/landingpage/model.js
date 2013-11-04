wdefine(function(){
	$app.metadata('queryplan1', {text : 'Filter A', style: 'link'});
	$app.metadata('queryplan2', {text : 'Filter B', style: 'link'});
	$app.metadata('querymore', {text : 'More...', style: 'link'});
	$app.metadata('queryplan', {groups : [
					                      {menus : [
					                                {id:'filter1',name:'Name like \'0\'', menus : [{id:'filter2',name:'Filter2', icon: 'icon-edit'},
					                                	         {divider:true}, 
					                                	         {id:'advanced',name:'Advanced', icon: 'icon-remove'}]
					                                }
					                               ]
					                      }
					                      ],
					            style: 'link'
					});
});