define(["chart_base/chart_base"], function(){	
	FwBase.Wtf.View.Controls.Chart_bar = function(){
		FwBase.Wtf.View.Controls.Chart_base.apply(this, arguments);
	};
	$.extend(FwBase.Wtf.View.Controls.Chart_bar.prototype, FwBase.Wtf.View.Controls.Chart_base.prototype, {
		getChartConfig : function() {
			return {
				chart: {
	                type: 'column'
	            },
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
	            plotOptions: {
	                bar: {
	                    dataLabels: {
	                        enabled: true
	                    }
	                }
	            },
	            credits: {
	                enabled: false
	            },
	            series : this.metadata.series
	        };
		},
		mockMetadata : function(){
			this.setDefault({width: '100%', height: '300', title: 'Monthly Average Rainfall', subtitle: 'Source: WorldClimate.com',
				categories: ['Jan','Feb','Mar','Apr','May','Jun','Jul'], ytitle: 'Rainfall (mm)', valueSuffix: ' mm',
				series: [{
	                name: 'Tokyo',
	                data: [49.9, 71.5, 106.4, 129.2, 144.0, 176.0, 135.6]
	    
	            }, {
	                name: 'New York',
	                data: [83.6, 78.8, 98.5, 93.4, 106.0, 84.5, 105.0]
	    
	            }, {
	                name: 'London',
	                data: [48.9, 38.8, 39.3, 41.4, 47.0, 48.3, 59.0]
	    
	            }, {
	                name: 'Berlin',
	                data: [42.4, 33.2, 34.5, 39.7, 52.6, 75.5, 57.4]
	    
	            }]
			});
		}	
	});
	return FwBase.Wtf.View.Controls.Chart_bar;
});