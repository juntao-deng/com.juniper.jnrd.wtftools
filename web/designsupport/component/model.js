wdefine(function(){
	var selectMd = {
		groups : [
		     {label: 'Normal', options:[
		                                {text : "button", value : "button"},
		                                {text : "label", value : "label"},
									    {text : "image", value : "image"},
									    {text : "menu", value : "menu"},
									    {text : "slider", value : "slider"},
									    {text : "spring", value : "spring"},
									    {text : "thumbnails", value : "thumbnails"},
									    ]},
		    {label: 'Container', options:[
		                                {text : "tab", value : "tab"},
									    {text : "card", value : "card"},
									    {text : "wizard", value : "wizard"}
									    ]},
									    
		    {label: 'Input', options:[
		                                {text : "input", value : "input"},
		                                {text : "input_autocomplete", value : "input_autocomplete"},
									    {text : "input_checkbox", value : "input_checkbox"},
									    {text : "input_checkboxgroup", value : "input_checkboxgroup"},
									    {text : "input_date", value : "input_date"},
									    {text : "input_datetime", value : "input_datetime"},
									    {text : "input_time", value : "input_time"},
									    {text : "input_color", value : "input_color"},
									    {text : "input_currency", value : "input_currency"},
									    {text : "input_time", value : "input_time"},
									    {text : "input_dropdown", value : "input_dropdown"},
									    {text : "input_editor", value : "input_editor"},
									    {text : "input_file", value : "input_file"},
									    {text : "input_integer", value : "input_integer"},
									    {text : "input_highlight", value : "input_highlight"},
									    {text : "input_ip", value : "input_ip"},
									    {text : "input_radiogroup", value : "input_radiogroup"},
									    {text : "input_select", value : "input_select"},
									    {text : "input_textarea", value : "input_textarea"},
									    {text : "input_toggle", value : "input_toggle"}
									    ]},
		    {label: 'Data Binding', options:[
		                                {text : "grid", value : "grid"},
									    {text : "tree", value : "tree"},
									    {text : "form", value : "form"},
									    {text : "chart_bar", value : "chart_bar"},
									    {text : "chart_line", value : "chart_line"},
									    {text : "chart_pie", value : "chart_pie"},
									    {text : "chart_area", value : "chart_area"}
									    ]},
		]
	};
	
	$app.metadata("componentinput", selectMd);
});