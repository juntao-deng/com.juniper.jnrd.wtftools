define(["base/base", "highcharts"], function(charts){	
	FwBase.Wtf.View.Controls.Chart_area = function(){
		FwBase.Wtf.View.Controls.BaseControl.apply(this, arguments);
	};
	$.extend(FwBase.Wtf.View.Controls.Chart_area.prototype, FwBase.Wtf.View.Controls.BaseControl.prototype, 
		{
			template: _.template($('#sys_atom_controls_chart_area').html()),
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
				this.setDefault({title: 'Area Chart', width: '100%', height: '200'});
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
				//this.chartEle.highcharts
				this.el.children("#chart_area").highcharts({
			            chart: {
			                type: 'area'
			            },
			            title: {
			                text: this.metadata.title
			            },
			            subtitle: {
			                text: this.metadata.subtitle
			            },
			            xAxis: {
			                categories: this.metadata.categories,
			                tickmarkPlacement: 'on',
			                title: {
			                    enabled: false
			                }
			            },
			            yAxis: {
			                title: {
			                    text: this.metadata.ytitle
			                },
			                labels: {
			                    formatter: function() {
			                        return this.value;
			                    }
			                }
			            },
			            tooltip: {
			                shared: true,
			                valueSuffix: this.metadata.valueSuffix
			            },
			            plotOptions: {
			                area: {
			                    stacking: 'normal',
			                    lineColor: '#666666',
			                    lineWidth: 1,
			                    marker: {
			                        lineWidth: 1,
			                        lineColor: '#666666'
			                    }
			                }
			            },
			            series : this.metadata.series
			        });
				this.chart = this.el.children("#chart_area").highcharts();
			},
			datas : function(data) {
				for(var i = 0; i < data.length; i ++){
					this.chart.series[i].update(data[i], false);
				}
				this.chart.redraw();
			},
			/* listeners begin*/
			lis_syncover : function(options) {
				var key = options.key;
				if(key == this.model.currentKey){
					var rows = this.model.rows();
					var datas = calculateDatas(this, rows);
					this.datas(datas);
				}
			}
		}
	);
	
	function calculateDatas(chartObj, rows){
		return [{name: 'Maximized', data:rows[0].get('values')}, {name: 'Assigned', data:rows[1].get('values')}, {name: 'Used', data:rows[2].get('values')}];
	}

	return FwBase.Wtf.View.Controls.Chart_area;
});