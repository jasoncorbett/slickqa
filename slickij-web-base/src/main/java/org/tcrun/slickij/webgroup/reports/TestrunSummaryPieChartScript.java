package org.tcrun.slickij.webgroup.reports;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import org.apache.wicket.markup.html.basic.Label;
import org.apache.wicket.model.LoadableDetachableModel;
import org.tcrun.slickij.api.data.ResultStatus;
import org.tcrun.slickij.api.data.TestRunSummary;


/**
 *
 * @author jcorbett
 */
public class TestrunSummaryPieChartScript extends Label
{
	private String chartId;
	private LoadableDetatchableTestrunSummaryModel model;
	private static final Map<String, String> statusColors;

	static {
		statusColors = new HashMap<String, String>();
		statusColors.put(ResultStatus.PASS.toString(), "#499c6e");
		statusColors.put(ResultStatus.FAIL.toString(), "#a6000d");
		statusColors.put(ResultStatus.BROKEN_TEST.toString(), "#e99235");
		statusColors.put(ResultStatus.NOT_TESTED.toString(), "#195364");
		statusColors.put(ResultStatus.SKIPPED.toString(), "#e2e457");
		statusColors.put(ResultStatus.NO_RESULT.toString(), "#9dd0f3");
		statusColors.put(ResultStatus.CANCELLED.toString(), "#9235e9");
	}

	public TestrunSummaryPieChartScript(String id, final String chartId, final LoadableDetatchableTestrunSummaryModel model)
	{
		super(id);
		this.model = model;
		this.chartId = chartId;
		this.setDefaultModel(new LoadableDetachableModel<String>(getChartScript())
		{
			@Override
			protected String load()
			{
				return getChartScript();
			}
		});
		this.setEscapeModelStrings(false);
	}

	public String getChartScript()
	{
		TestRunSummary summary = model.getObject();
		List<String> statusOrdered = summary.getStatusListOrdered();
		StringBuilder scriptBuffer = new StringBuilder();
		scriptBuffer.append("\n\t\tgoogle.load(\"visualization\", \"1\", {packages:[\"corechart\"]});\n");
		scriptBuffer.append("\t\tgoogle.setOnLoadCallback(drawChart);\n");
		scriptBuffer.append("\t\tfunction drawChart() {\n");
		scriptBuffer.append("\t\t\tvar data = new google.visualization.DataTable();\n");
		scriptBuffer.append("\t\t\tdata.addColumn('string', 'Result Type');\n");
		scriptBuffer.append("\t\t\tdata.addColumn('number', 'Number of Results');\n");
		scriptBuffer.append("\t\t\tdata.addRows(" + statusOrdered.size() + ");\n");
		for(int i = 0; i < statusOrdered.size(); i++)
		{
			String status = statusOrdered.get(i);
			scriptBuffer.append("\t\t\tdata.setValue(");
			scriptBuffer.append(i);
			scriptBuffer.append(",0,'");
			scriptBuffer.append(status.replace("_", " "));
			scriptBuffer.append("');\n");
			scriptBuffer.append("\t\t\tdata.setValue(");
			scriptBuffer.append(i);
			scriptBuffer.append(",1,");
			scriptBuffer.append(summary.getResultsByStatus().get(status));
			scriptBuffer.append(");\n");
		}
		scriptBuffer.append("\t\t\tnew google.visualization.PieChart(document.getElementById('" + chartId + "')).\n");
		scriptBuffer.append("\t\t\t    draw(data, {is3D: true, backgroundColor: '#1b1b1a', legendTextStyle: {color: 'd3d3d2'}, fontName: 'Gruppo', colors: [");
		for(String status : statusOrdered)
		{
			scriptBuffer.append("'");
			scriptBuffer.append(statusColors.get(status));
			scriptBuffer.append("',");
		}
		scriptBuffer.append("]});\n");
		scriptBuffer.append("\t\t}\n");
		return scriptBuffer.toString();
	}
}

