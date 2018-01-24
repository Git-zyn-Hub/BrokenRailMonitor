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
			"18", "19", "20", "21", "22", "23", "24" };// X��ı�ע

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
			pointValue.setLabel(pointValue.getY() + "��");
		}

		// In most cased you can call data model methods in builder-pattern-like
		// manner.
		Line line = new Line(values).setColor(Color.BLUE).setCubic(false);
		List<Line> lines = new ArrayList<Line>();
		line.setHasPoints(true);
		line.setStrokeWidth(2);// �����Ĵ�ϸ��Ĭ����3
		line.setHasLabels(true);// ���ߵ����������Ƿ���ϱ�ע
		// line.setHasLabelsOnlyForSelected(true);//�������������ʾ���ݣ����������line.setHasLabels(true);����Ч��

		line.setPointRadius(3);
		lines.add(line);

		LineChartData data = new LineChartData();
		data.setLines(lines);

		// �����仰���������ϵ�ı�����ɫ��Ĭ������һ��С���飬���ұ���ɫ�͵����ɫһ��
		// �����Ҫԭ����Ч�����Բ��������仰���ҵ���ʾ����͸����
		data.setValueLabelBackgroundColor(Color.TRANSPARENT);
		data.setValueLabelBackgroundEnabled(false);
		data.setValueLabelsTextColor(Color.BLUE);

		LineChartView chart = (LineChartView) vTempe.findViewById(R.id.lcvChart);
		chart.setLineChartData(data);

		// ������
		Axis axisX = new Axis(); // X��
		axisX.setHasTiltedLabels(false); // X������������������б����ʾ����ֱ�ģ�true��б����ʾ
		// axisX.setTextColor(Color.WHITE); //����������ɫ
		axisX.setTextColor(Color.parseColor("#D6D6D9"));// ��ɫ

		axisX.setName("δ�����������"); // �������
		axisX.setTextSize(11);// ���������С
		axisX.setMaxLabelChars(7); // ��༸��X�����꣬��˼�������������X�������ݵĸ���7<=x<=mAxisValues.length
		axisX.setValues(mAxisXValues); // ���X�����������
		data.setAxisXBottom(axisX); // x ���ڵײ�
		// data.setAxisXTop(axisX); //x ���ڶ���
		axisX.setHasLines(true); // x ��ָ���

		Axis axisY = new Axis().setHasLines(true);
		; // Y��
		axisY.setName("");// y���ע
		axisY.setTextSize(11);// ���������С
		axisY.setMaxLabelChars(7);

		List<AxisValue> valuesYAxis = new ArrayList<AxisValue>();
		for (int i = 0; i <= 10; i += 1) {
			AxisValue value = new AxisValue(i);
			String label = String.valueOf(i);
			value.setLabel(label);
			valuesYAxis.add(value);
		}
		axisY.setValues(valuesYAxis);
		data.setAxisYLeft(axisY); // Y�����������

		// ���ÿ��ӻ���ͼ��ʽ�����������Ķ����ǳ��࣬
		Viewport v = new Viewport(chart.getMaximumViewport());
		// ���������֡������ͬ��ťʱ��y��̶����ֵ��Сֵ��һ��
		// ������Թ̶�x�ᣬ��y��仯��Ҳ����x��y�ᶼ�̶���Ҳ���ǹ̶���ʾ�����趨��������ĵ�point��x��y��

		v.top = 10;
		v.bottom = 0;
		v.left = 0;
		v.right = 30;
		// ��仰�ǳ��ؼ��������������ã���ȷ�������ӻ���ʽ
		// ���ǿ������Ϊ�����е����chartʱ��������ͼȫ����ʱ������ӣ�Ҳ���ǵ�ܶ�ܶ࣬����ܽ���
		chart.setMaximumViewport(v);

		chart.setCurrentViewport(v);

		chart.setInteractive(true);
		chart.setZoomType(ZoomType.HORIZONTAL);
		chart.setContainerScrollEnabled(true, ContainerScrollType.HORIZONTAL);
		return vTempe;
	}

	/**
	 * X �����ʾ
	 */
	private void getAxisXLables() {
		for (int i = 0; i < date.length; i++) {
			mAxisXValues.add(new AxisValue(i).setLabel(date[i]));
		}
	}
}
