wdefine(function(){
	$widget.metadata('tagscope', {label: 'Tags:', labelWidth: '30', width: '190', options: [{text: 'Show Relevant Tags', value: 'relevant'}, {text: 'Show All Tags', value: 'all'}]});
	$widget.metadata('tag_add', {icon:'icon-plus-sign', text: '', style: ''});
	$widget.metadata('tag_del', {icon:'icon-remove-sign', text: '', style: ''});
	$widget.model('tagsmodel', {url:'tags'});
	$widget.metadata('tagstree', {model: 'tagsmodel', binding: {nameField: 'tagname', folderField: 'folder'}});
});