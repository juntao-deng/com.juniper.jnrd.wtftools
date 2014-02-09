define(["chart_base/chart_base"], function(){	
	FwBase.Wtf.View.Controls.Chart_pie = function(){
		FwBase.Wtf.View.Controls.Chart_base.apply(this, arguments);
	};
	$.extend(FwBase.Wtf.View.Controls.Chart_pie.prototype, FwBase.Wtf.View.Controls.Chart_base.prototype, {
		getChartConfig : function() {
			return {
				chart: {
	                plotBackgroundColor: null,
	                plotBorderWidth: null,
	                plotShadow: false
	            },
	            title: {
	                text: this.metadata.title,
	                x: -20 //center
	            },
	            subtitle: {
	                text: this.metadata.subtitle,
	                x: -20
	            },
	            tooltip: {
	                valueSuffix: this.metadata.valueSuffix //pointFormat: '{series.name}: <b>{point.percentage:.1f}%</b>'
	            },
	            plotOptions: {
	            	pie: {
	                    allowPointSelect: true,
	                    cursor: 'pointer',
	                    dataLabels: {
	                        enabled: false
	                    },
	                    showInLegend: true
	                }
	            },
	            series : this.metadata.series
	        };
		},
		mockMetadata : function(){
			this.setDefault({width: '100%', height: '300', title: 'Browser market shares at a specific website, 2010', subtitle: '',valueSuffix:'%',
				series: [{
	                type: 'pie',
	                name: 'Browser share',
	                data: [
	                    ['Firefox',   45.0],
	                    ['IE',       26.8],
	                    {
	                        name: 'Chrome',
	                        y: 12.8,
	                        sliced: true,
	                        selected: true
	                    },
	                    ['Safari',    8.5],
	                    ['Opera',     6.2],
	                    ['Others',   0.7]
	                ]
	            }]
			});
		},
	});
	return FwBase.Wtf.View.Controls.Chart_pie;
});