define(["base/base", "highcharts"], function(){	
	FwBase.Wtf.View.Controls.Chart_base = function(){
		FwBase.Wtf.View.Controls.BaseControl.apply(this, arguments);
	};
	$.extend(FwBase.Wtf.View.Controls.Chart_base.prototype, FwBase.Wtf.View.Controls.BaseControl.prototype, 
		{
			template: _.template($('#sys_atom_controls_chart_base').html()),
			mockMetadata : function(){
				this.setDefault({width: '100%', height: '300', title: 'Historic and Estimated Worldwide Population Growth by Region', subtitle: 'Source: Wikipedia.org',
					categories: ['1750', '1800', '1850', '1900', '1950', '1999', '2050'], ytitle: 'Billions', valueSuffix: ' millions',
		            series: [{
		                name: 'Asia',
		                data: [502, 635, 809, 947, 1402, 3634, 5268]
		            }, {
		                name: 'Africa',
		                data: [106, 107, 111, 133, 221, 767, 1766]
		            }, {
		                name: 'Europe',
		                data: [163, 203, 276, 408, 547, 729, 628]
		            }, {
		                name: 'America',
		                data: [18, 31, 54, 156, 339, 818, 1201]
		            }, {
		                name: 'Oceania',
		                data: [2, 2, 2, 6, 13, 30, 46]
		            }]});
			},
			makeDefault : function() {
				this.setDefault({title: 'Chart', width: '100%', height: '200', series: []});
			},
			postInit : function() {
				this.model = this.ctx.model(this.metadata.model);
				if(this.model){
					this.listenTo(this.model, "clear", this.lis_clearPage);
					this.listenTo(this.model, "add", this.lis_addRow);
					this.listenTo(this.model, "remove", this.lis_deleteRow);
					this.listenTo(this.model, "change", this.lis_changeRow);
					this.listenTo(this.model, "pagechange", this.lis_pageChange);
					this.listenTo(this.model, "pagination", this.lis_pagination);
					this.listenTo(this.model, "selection", this.lis_selection);
					this.listenTo(this.model, "syncover", this.lis_syncover);
				}
				this.el.children("#chart_base").highcharts(this.getChartConfig());
				this.chart = this.el.children("#chart_base").highcharts();
			},
			datas : function(data) {
				for(var i = 0; i < data.length; i ++){
					this.chart.series[i].update(data[i], false);
				}
				this.chart.redraw();
			},
			series : function(series) {
				var oriS = this.chart.series;
				if(oriS != null){
					for(var i = 0; i < oriS.length; i ++)
						oriS[i].remove();
				}
				if(series != null){
					for(var i = 0; i < series.length; i ++)
						this.chart.addSeries(series[i]);
				}
			},
			/* listeners begin*/
			lis_syncover : function(options) {
				var key = options.key;
				if(key == this.model.currentKey){
					var rows = this.model.rows();
					var datas = this.calculateDatas(rows);
					this.datas(datas);
				}
			},
			/* private*/
			calculateDatas : function(rows){
				var dataArr = [];
				if(this.chart.series == null)
					return dataArr;
				var field = this.metadata.binding.seriesField;
				var valueField = this.metadata.binding.valueField;
				var mapping = this.metadata.binding.mapping;
				for(var i = 0; i < this.chart.series.length; i ++){
					var seriaName = ((mapping == null || mapping.length == 0) ? this.chart.series[i].name : mapping[i]);
					var find = false;
					for(var j = 0; j < rows.length; j ++){
						if(rows[j].get(field) == seriaName){
							find = true;
							dataArr.push({data: rows[j].get(valueField)});
							break;
						}
					}
					if(!find){
						dataArr.push({data: null});
					}
				}
				return dataArr;
			}
			/* private */
		}
	);

	return FwBase.Wtf.View.Controls.Chart_base;
});