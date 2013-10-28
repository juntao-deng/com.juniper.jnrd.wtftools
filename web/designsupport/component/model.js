wdefine(function(){
	var selectMd = {
		id : 'componentinput', 
		groups : [
		     {label: 'Normal', options:[
		                                {name : "button", value : "button"},
									    {name : "image", value : "image"},
									    {name : "menu", value : "menu"},
									    {name : "slider", value : "slider"}
									    ]},
		    {label: 'Container', options:[
		                                {name : "tab", value : "tab"},
									    {name : "card", value : "card"},
									    {name : "wizard", value : "wizard"}
									    ]},
									    
		    {label: 'Input', options:[
		                                {name : "input", value : "input"},
									    {name : "input_alpha", value : "input_alpha"},
									    {name : "input_checkbox", value : "input_checkbox"},
									    {name : "input_checkboxgroup", value : "input_checkboxgroup"},
									    {name : "input_color", value : "input_color"},
									    {name : "input_currency", value : "input_currency"},
									    {name : "input_date", value : "input_date"},
									    {name : "input_daterange", value : "input_daterange"},
									    {name : "input_datetime", value : "input_datetime"},
									    {name : "input_dropdown", value : "input_dropdown"},
									    {name : "input_editor", value : "input_editor"},
									    {name : "input_file", value : "input_file"},
									    {name : "input_integer", value : "input_integer"},
									    {name : "input_ip", value : "input_ip"},
									    {name : "input_mail", value : "input_mail"},
									    {name : "input_phone", value : "input_phone"},
									    {name : "input_radiogroup", value : "input_radiogroup"},
									    {name : "input_select", value : "input_select"},
									    {name : "input_textarea", value : "input_textarea"},
									    {name : "input_time", value : "input_time"},
									    {name : "input_toggle", value : "input_toggle"},
									    {name : "input_url", value : "input_url"}
									    ]},
		    {label: 'Data Binding', options:[
		                                {name : "grid", value : "grid"},
									    {name : "tree", value : "tree"},
									    {name : "form", value : "form"},
									    {name : "thumbnails", value : "thumbnails"},
									    {name : "chart_bar", value : "chart_bar"},
									    {name : "chart_linear", value : "chart_linear"},
									    {name : "chart_pie", value : "chart_pie"}
									    ]},
		]
	};
	
	$app.metadata(selectMd);
});