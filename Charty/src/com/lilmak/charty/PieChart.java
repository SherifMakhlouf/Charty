package com.lilmak.charty;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.RectF;
import android.util.AttributeSet;
import android.view.View;

public class PieChart extends View {
	/**
	 * The type of the data drawn over the chart
	 */
	enum HintMode {
		NONE, NUMBERS, NAME
	}

	private final static int DEFAULT_WIDTH = 300;
	private final static int DEFAULT_HEIGHT = 300;
	private int[] percentages;
	private String[] sectionNames;
	private int[] sectionColors;
	private int textColor = -1;
	private int textSize = -1;
	private HintMode hintMode;
	private final int defaultTextSize;

	private Paint sectionPaint;
	private Paint textPaint;
	private RectF archRect;
	private int width;
	private int height;

	public PieChart(Context context, AttributeSet attrs) {
		super(context, attrs);
		defaultTextSize = (int) getResources().getDimension(
				R.dimen.defaultTextSize);
		percentages = new int[] { 10, 20, 30, 40 };
	}

	public PieInitializer init(int[] percentages) {
		this.percentages = percentages;
		this.hintMode = hintMode.NUMBERS;
		return new PieInitializer();
	}

	public class PieInitializer {
		public PieInitializer inMode(HintMode mode) {
			hintMode = mode;
			return this;
		}

		public PieInitializer withNames(String[] names) {
			sectionNames = names;
			return this;
		}

		public PieInitializer withTextColor(int color) {
			textColor = color;
			return this;
		}

		public PieInitializer withTextSize(int size) {
			textSize = size;
			return this;
		}

		/**
		 * set names is less than the number of section last name will be used
		 * multiple times if names length is larger it will throw an exception
		 * 
		 * @param names
		 *            names per each section
		 * @return
		 */
		public PieInitializer withSectionNames(String[] names) {
			sectionNames = names;
			return this;
		}

		/**
		 * 
		 * @param colors
		 * @return
		 */
		public PieInitializer withSectionColors(int[] colors) {
			sectionColors = colors;
			return this;
		}
	}

	@Override
	protected void onAttachedToWindow() {
		super.onAttachedToWindow();
		sectionPaint = new Paint();
		textPaint = new Paint();

		textPaint.setColor((textColor != -1 ? textColor : getResources()
				.getColor(R.color.defaultTextColor)));
		textPaint.setTextSize((textSize != -1 ? textSize : getResources()
				.getDimension(R.dimen.defaultTextSize)));

	}

	@Override
	protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
		if (MeasureSpec.getMode(widthMeasureSpec) == MeasureSpec.UNSPECIFIED
				|| MeasureSpec.getMode(heightMeasureSpec) == MeasureSpec.UNSPECIFIED)
			setMeasuredDimension(DEFAULT_WIDTH, DEFAULT_HEIGHT);
		else {
			width = MeasureSpec.getSize(widthMeasureSpec);
			height = MeasureSpec.getSize(heightMeasureSpec);
			setMeasuredDimension(width, height);
		}

	}

	@Override
	protected void onDraw(Canvas canvas) {

		sectionPaint.setColor(0xFF33B5E5);
		sectionPaint.setAntiAlias(true);
		int angle = 360 * percentages[0] / 100;
		int prevAngle = 0;
		archRect = new RectF(0, 0, getMeasuredWidth(), getMeasuredHeight());
		canvas.drawArc(archRect, 0, angle, true, sectionPaint);
		for (int i = 1; i < percentages.length; i++) {
			angle = 360 * percentages[i] / 100;
			prevAngle += 360 * percentages[i - 1] / 100;
			canvas.drawArc(archRect, prevAngle, angle, true, sectionPaint);
		}

		

		// canvas.drawText("30%", 271.35f, 238.16f, paint);
		/*
		 * drawLabel(canvas, "10%", 150, 150, 150, 150, 0, 36, 150, 150,
		 * Color.WHITE, paint, false, true); drawLabel(canvas, "30%", 150, 150,
		 * 150, 150, 36, 108, 150, 150, Color.WHITE, paint, false, true);
		 * drawLabel(canvas, "30%", 150, 150, 150, 150, 144, 108, 150, 150,
		 * Color.WHITE, paint, false, true); drawLabel(canvas, "30%", 150, 150,
		 * 150, 150, 252, 108, 150, 150, Color.WHITE, paint, false, true);
		 */
	}

}
