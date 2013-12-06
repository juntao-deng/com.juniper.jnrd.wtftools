define(["chart_base/chart_base"], function(){	
	FwBase.Wtf.View.Controls.Chart_area = function(){
		FwBase.Wtf.View.Controls.Chart_base.apply(this, arguments);
	};
	$.extend(FwBase.Wtf.View.Controls.Chart_area.prototype, FwBase.Wtf.View.Controls.Chart_base.prototype, {
		getChartConfig : function() {
			return {
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
		        };
			}
		}
	);

	return FwBase.Wtf.View.Controls.Chart_area;
});