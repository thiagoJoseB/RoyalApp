package com.example.royalapp.view;

import android.graphics.Canvas;
import com.github.mikephil.charting.charts.PieChart;
import com.github.mikephil.charting.data.PieDataSet;
import com.github.mikephil.charting.interfaces.datasets.IPieDataSet;
import com.github.mikephil.charting.renderer.PieChartRenderer;
import com.github.mikephil.charting.utils.ColorTemplate;
import com.github.mikephil.charting.utils.MPPointF;
import com.github.mikephil.charting.utils.Utils;
import static java.lang.Math.cos;
import static java.lang.Math.sin;

import java.util.List;

public class CustomPieChartRenderer extends PieChartRenderer{

    private float circleRadius;

    public CustomPieChartRenderer(PieChart pieChart, float circleRadius){
        super(pieChart, pieChart.getAnimator(), pieChart.getViewPortHandler());

        this.circleRadius = circleRadius;
    }

    @Override
    public void drawValues(Canvas c) {
        super.drawValues(c);

        MPPointF center = mChart.getCenterCircleBox();

        float radius = mChart.getRadius();
        float rotationAngle = mChart.getRotationAngle();
        float[] drawAngles = mChart.getDrawAngles();
        float[] absoluteAngles = mChart.getAbsoluteAngles();

        float phaseX = mAnimator.getPhaseX();
        float phaseY = mAnimator.getPhaseY();

        float roundedRadius = (radius - radius * mChart.getHoleRadius() / 100f) / 2f;
        float holeRadiusPercent = mChart.getHoleRadius() / 100f;
        float labelRadiusOffset = radius / 10f * 3.6f;

        if (mChart.isDrawHoleEnabled()) {
            labelRadiusOffset = (radius - radius * holeRadiusPercent) / 2f;
            if (!mChart.isDrawSlicesUnderHoleEnabled() && mChart.isDrawRoundedSlicesEnabled()) {
                rotationAngle += roundedRadius * 360 / ((float) (Math.PI * 2 * radius));
            }
        }

        float labelRadius = radius - labelRadiusOffset;

        List<IPieDataSet> dataSets = mChart.getData().getDataSets();

        float angle;
        int xIndex = 0;

        c.save();
        for (int i = 0; i < dataSets.size(); i++) {
            IPieDataSet dataSet = dataSets.get(i);
            float sliceSpace = getSliceSpace(dataSet);
            for (int j = 0; j < dataSet.getEntryCount(); j++) {
                angle = (xIndex == 0)? 0f : absoluteAngles[xIndex - 1] * phaseX;
                float sliceAngle = drawAngles[xIndex];
                float sliceSpaceMiddleAngle = sliceSpace / (Utils.FDEG2RAD * labelRadius);
                angle += (sliceAngle - sliceSpaceMiddleAngle / 2f) / 2f;

                if (dataSet.getValueLineColor() != ColorTemplate.COLOR_NONE) {
                    float transformedAngle = rotationAngle + angle * phaseY;
                    float sliceXBase = (float) cos(transformedAngle * (double)Utils.FDEG2RAD);
                    float sliceYBase = (float) sin(transformedAngle * (double)Utils.FDEG2RAD);
                    float valueLinePart1OffsetPercentage = dataSet.getValueLinePart1OffsetPercentage() / 100f;
                    float line1Radius = mChart.isDrawHoleEnabled()?
                        (radius - radius * holeRadiusPercent) * valueLinePart1OffsetPercentage + radius * holeRadiusPercent
                            :
                        radius * valueLinePart1OffsetPercentage
                    ;
                    float px = line1Radius * sliceXBase + center.x;
                    float py = line1Radius * sliceYBase + center.y;

                    if (dataSet.isUsingSliceColorAsValueLineColor()) {
                        mRenderPaint.setColor(dataSet.getColor(j));
                    }
                    c.drawCircle(px, py, circleRadius, mRenderPaint);
                }

                xIndex++;
            }
        }
        MPPointF.recycleInstance(center);
        c.restore();
    }
}