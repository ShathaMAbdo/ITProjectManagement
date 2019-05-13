<!-- chart.jsp-->
<%@ page language="java" contentType="text/html; charset=ISO-8859-1"
	pageEncoding="ISO-8859-1"%>
<%@taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=ISO-8859-1">
<title>Remain ToDo charts</title>
<script type="text/javascript">
window.onload = function() {

var dps = [[], []];
var chart = new CanvasJS.Chart("chartContainer", {
	exportEnabled: true,
	animationEnabled: true,
	title: {
		text: "The work actually remaining & planned daily"
	},
	subtitles: [{
		text: "Click Legend to Hide or Unhide Data Series"
	}],
	axisX: {
		title: "Dayes"
	},
	axisY: {
		title: "Time (in Hours)",
		prefix: "h",
		includeZero: false
	},
	toolTip: {
		shared: true
	},
	legend: {
		cursor: "pointer",
		verticalAlign: "top",
		itemclick: toggleDataSeries
	},
	data: [{
		type: "line",
		name: "planned",
		showInLegend: true,
		yValueFormatString: "h#,##0",
		dataPoints: dps[0]
	},
	{
		type: "line",
		name: "actual",
		showInLegend: true,
		yValueFormatString: "h#,##0",
		dataPoints: dps[1]
	}]
});

var yValue;
var label;

<c:forEach items="${dataPointsList}" var="dataPoints" varStatus="loop">
	<c:forEach items="${dataPoints}" var="dataPoint">
		yValue = parseFloat("${dataPoint.y}");
		label = "${dataPoint.label}";
		dps[parseInt("${loop.index}")].push({
			label : label,
			y : yValue,
		});
	</c:forEach>
</c:forEach>

chart.render();

function toggleDataSeries(e) {
	if (typeof (e.dataSeries.visible) === "undefined" || e.dataSeries.visible) {
		e.dataSeries.visible = false;
	} else {
		e.dataSeries.visible = true;
	}
	e.chart.render();
}

}
</script>
</head>
<body>
	<div id="chartContainer" style="height: 600px; width: 100%;"></div>
	<script src="https://canvasjs.com/assets/script/canvasjs.min.js"></script>
</body>
</html>