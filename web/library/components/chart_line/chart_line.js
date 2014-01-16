define(["chart_base/chart_base"], function(){	
	FwBase.Wtf.View.Controls.Chart_line = function(){
		FwBase.Wtf.View.Controls.Chart_base.apply(this, arguments);
	};
	$.extend(FwBase.Wtf.View.Controls.Chart_line.prototype, FwBase.Wtf.View.Controls.Chart_base.prototype, {
		getChartConfig : function() {
			return {
	            title: {
	                text: this.metadata.title,
	                x: -20 //center
	            },
	            subtitle: {
	                text: this.metadata.subtitle,
	                x: -20
	            },
	            xAxis: {
	                categories: this.metadata.categories,
	            },
	            yAxis: {
	                title: {
	                    text: this.metadata.ytitle
	                },
	                plotLines: [{
	                    value: 0,
	                    width: 1,
	                    color: '#808080'
	                }]
	            },
	            tooltip: {
	                valueSuffix: this.metadata.valueSuffix
	            },
	            legend: {
	                layout: 'vertical',
	                align: 'right',
	                verticalAlign: 'middle',
	                borderWidth: 0
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
				
	});
	return FwBase.Wtf.View.Controls.Chart_line;
});