package com.terrytec.brokenrailmonitor.datafragments;

import java.util.ArrayList;
import java.util.List;

import com.terrytec.brokenrailmonitor.R;

import android.R.string;
import android.graphics.Color;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import lecho.lib.hellocharts.gesture.ContainerScrollType;
import lecho.lib.hellocharts.gesture.ZoomType;
import lecho.lib.hellocharts.model.Axis;
import lecho.lib.hellocharts.model.AxisValue;
import lecho.lib.hellocharts.model.Line;
import lecho.lib.hellocharts.model.LineChartData;
import lecho.lib.hellocharts.model.PointValue;
import lecho.lib.hellocharts.model.Viewport;
import lecho.lib.hellocharts.view.LineChartView;

public class TemperatureFragment extends BaseFragment {

	private View vTempe;
	private List<AxisValue> mAxisXValues = new ArrayList<AxisValue>();
	String[] date = { "0", "1", "2", "3", "4", "5", "6", "7", "8", "9", "10", "11", "12", "13", "14", "15", "16", "17",
			"18", "19", "20", "21", "22", "23", "24" };// X轴的标注

	public TemperatureFragment(int resID) {
		super(resID);
	}

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		Log.e("zyn", this.getClass().getName() + " onCreate");
		getAxisXLables();
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
		vTempe = super.onCreateView(inflater, container, savedInstanceState);

		List<PointValue> values = new ArrayList<PointValue>();
		values.add(new PointValue(0, 2));
		values.add(new PointValue(1, 4));
		values.add(new PointValue(2, 3));
		values.add(new PointValue(3, 4));
		values.add(new PointValue(4, 4));
		values.add(new PointValue(5, 4));
		values.add(new PointValue(6, 4));
		values.add(new PointValue(7, 4));
		values.add(new PointValue(8, 4));
		values.add(new PointValue(9, 4));
		values.add(new PointValue(10, 4));
		values.add(new PointValue(11, 4));
		values.add(new PointValue(12, 4));
		values.add(new PointValue(13, 4));
		values.add(new PointValue(14, 4));
		values.add(new PointValue(15, 4));
		values.add(new PointValue(16, 4));
		values.add(new PointValue(17, 4));
		values.add(new PointValue(18, 4));
		values.add(new PointValue(19, 4));
		values.add(new PointValue(20, 4));
		
		for (PointValue pointValue : values) {
			pointValue.setLabel(pointValue.getY() + "℃");
		}

		// In most cased you can call data model methods in builder-pattern-like
		// manner.
		Line line = new Line(values).setColor(Color.BLUE).setCubic(false);
		List<Line> lines = new ArrayList<Line>();
		line.setHasPoints(true);
		line.setStrokeWidth(2);// 线条的粗细，默认是3
		line.setHasLabels(true);// 曲线的数据坐标是否加上备注
		// line.setHasLabelsOnlyForSelected(true);//点击数据坐标提示数据（设置了这个line.setHasLabels(true);就无效）

		line.setPointRadius(3);
		lines.add(line);

		LineChartData data = new LineChartData();
		data.setLines(lines);

		// 这两句话设置折线上点的背景颜色，默认是有一个小方块，而且背景色和点的颜色一样
		// 如果想要原来的效果可以不用这两句话，我的显示的是透明的
		data.setValueLabelBackgroundColor(Color.TRANSPARENT);
		data.setValueLabelBackgroundEnabled(false);
		data.setValueLabelsTextColor(Color.BLUE);

		LineChartView chart = (LineChartView) vTempe.findViewById(R.id.lcvChart);
		chart.setLineChartData(data);

		// 坐标轴
		Axis axisX = new Axis(); // X轴
		axisX.setHasTiltedLabels(false); // X轴下面坐标轴字体是斜的显示还是直的，true是斜的显示
		// axisX.setTextColor(Color.WHITE); //设置字体颜色
		axisX.setTextColor(Color.parseColor("#D6D6D9"));// 灰色

		axisX.setName("未来几天的天气"); // 表格名称
		axisX.setTextSize(11);// 设置字体大小
		axisX.setMaxLabelChars(7); // 最多几个X轴坐标，意思就是你的缩放让X轴上数据的个数7<=x<=mAxisValues.length
		axisX.setValues(mAxisXValues); // 填充X轴的坐标名称
		data.setAxisXBottom(axisX); // x 轴在底部
		// data.setAxisXTop(axisX); //x 轴在顶部
		axisX.setHasLines(true); // x 轴分割线

		Axis axisY = new Axis().setHasLines(true);
		; // Y轴
		axisY.setName("");// y轴标注
		axisY.setTextSize(11);// 设置字体大小
		axisY.setMaxLabelChars(7);

		List<AxisValue> valuesYAxis = new ArrayList<AxisValue>();
		for (int i = 0; i <= 10; i += 1) {
			AxisValue value = new AxisValue(i);
			String label = String.valueOf(i);
			value.setLabel(label);
			valuesYAxis.add(value);
		}
		axisY.setValues(valuesYAxis);
		data.setAxisYLeft(axisY); // Y轴设置在左边

		// 设置可视化视图样式，这里能做的东西非常多，
		Viewport v = new Viewport(chart.getMaximumViewport());
		// 我设置两种。点击不同按钮时，y轴固定最大值最小值不一样
		// 这里可以固定x轴，让y轴变化，也可以x轴y轴都固定，也就是固定显示在你设定的区间里的点point（x，y）

		v.top = 10;
		v.bottom = 0;
		v.left = 0;
		v.right = 30;
		// 这句话非常关键，上面两种设置，来确定最大可视化样式
		// 我们可以理解为，所有点放在chart时，整个视图全看到时候的样子，也就是点很多很多，距离很紧密
		chart.setMaximumViewport(v);

		chart.setCurrentViewport(v);

		chart.setInteractive(true);
		chart.setZoomType(ZoomType.HORIZONTAL);
		chart.setContainerScrollEnabled(true, ContainerScrollType.HORIZONTAL);
		return vTempe;
	}

	/**
	 * X 轴的显示
	 */
	private void getAxisXLables() {
		for (int i = 0; i < date.length; i++) {
			mAxisXValues.add(new AxisValue(i).setLabel(date[i]));
		}
	}
}
