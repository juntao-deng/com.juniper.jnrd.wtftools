wdefine(function(){
	$widget.metadata('filterlabel', {text: 'Filtered by:', style: 'primary'});
//	$widget.metadata('selectedlink', {text: '0 items selected', style: 'link'});
	$widget.metadata('queryplan', {groups : [
					                      {menus : [
					                                {id:'filter1',name:'None', menus : [{id:'filter1',name:'Filter1', icon: 'icon-edit'},
					                                                                    {id:'filter2',name:'Filter2', icon: 'icon-edit'},
					                                	         {divider:true}, 
					                                	         {id:'edit',name:'Edit Filters', icon: 'icon-plus'}]
					                                }
					                               ]
					                      }
					                      ],
					            style: 'link'
					});
	
	$widget.metadata('searchinput', {width: 100});
});