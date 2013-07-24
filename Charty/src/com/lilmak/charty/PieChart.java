package com.lilmak.charty;

import android.content.Context;
import android.content.pm.LabeledIntent;
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
	enum LableType {
		NONE, NUMBERS, NAME
	}

	private final static int DEFAULT_WIDTH = 300;
	private final static int DEFAULT_HEIGHT = 300;
	private int[] percentages;
	private String[] sectionNames;
	private int[] sectionColors;
	private int textColor = -1;
	private int textSize = -1;
	private LableType lableType;

	private Paint sectionPaint;
	private Paint textPaint;
	private RectF archRect;
	private int width;
	private int height;

	public PieChart(Context context, AttributeSet attrs) {
		super(context, attrs);
		percentages = new int[] { 100 };
		sectionColors = new int[] { 0xFF33B5E5 };

	}

	/**
	 * Initializes a chart
	 * 
	 * @param percentages
	 *            Percentage per section (sum of percentages must be less than
	 *            or equal to 100)
	 * @param colors
	 *            Color per section
	 * @return
	 */
	public PieInitializer init(int[] percentages, int[] colors) {
		this.percentages = percentages;
		this.sectionColors = colors;
		if (percentages == null || colors == null)
			throw new IllegalArgumentException(
					"Percentages and Colors cant be null ");

		if (this.percentages.length != this.sectionColors.length)
			throw new IllegalArgumentException(
					"Number of Values must be equal to number of SectionColors");
		int sum = 0;
		for (int i = 0; i < this.percentages.length; i++) {
			sum += this.percentages[i];
		}

		if (sum > 100)
			throw new IllegalArgumentException(
					"Sum of percentages must be less than or equal 100");

		this.lableType = LableType.NUMBERS;
		return new PieInitializer();
	}

	public class PieInitializer {

		/**
		 * Sets the type of labels (None,Numbers,Name)
		 * 
		 * @param type
		 *            lableType
		 * @return
		 */
		public PieInitializer withLabelType(LableType type) {
			lableType = type;
			return this;
		}

		/**
		 * Sets the labels' Text color
		 * 
		 * @param color
		 *            Text Color
		 * @return PieInitializer Object
		 */
		public PieInitializer withTextColor(int color) {
			textColor = color;
			return this;
		}

		/**
		 * Sets the labels' Text size
		 * 
		 * @param size
		 *            TextSize
		 * @return PieInitializer Object
		 */
		public PieInitializer withTextSize(int size) {
			textSize = size;
			return this;
		}

		/**
		 * Sets the name for each section (Automatically sets the labelType to
		 * LableType.NAMES)
		 * 
		 * @param names
		 *            names per each section (must be equal to the number of
		 *            sections)
		 * @return PieInitializer Object
		 */
		public PieInitializer withSectionNames(String[] names) {
			sectionNames = names;
			lableType = lableType.NAME;
			if (sectionNames.length != percentages.length)
				throw new IllegalArgumentException(
						"Number of Names should be equal to the number of Sections");
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
		// Draw the first section
		archRect = new RectF(0, 0, getMeasuredWidth(), getMeasuredHeight());

		for (int i = 0; i < percentages.length; i++) {
			angle = 360 * percentages[i] / 100;
			if (i > 0)
				prevAngle += 360 * percentages[i - 1] / 100;
			sectionPaint.setColor(sectionColors[i]);
			canvas.drawArc(archRect, prevAngle, -1 * angle, true, sectionPaint);

			switch (lableType) {
			case NUMBERS:
				drawLabel(canvas, percentages[i] + "%", getMeasuredWidth() / 2,
						getMeasuredHeight() / 2, getMeasuredWidth() / 2,
						prevAngle, angle, textPaint);
				break;
			case NAME:
				drawLabel(canvas, sectionNames[i], getMeasuredWidth() / 2,
						getMeasuredHeight() / 2, getMeasuredWidth() / 2,
						prevAngle, angle, textPaint);
				break;
			case NONE:
				break;
			}

		}

	}

	protected void drawLabel(Canvas canvas, String labelText, int centerX,
			int centerY, float radius, float currentAngle, float angle,
			Paint paint) {

		double halfAngle = Math.toRadians(currentAngle + -1 * angle / 2);
		double sinValue = Math.sin(halfAngle);
		double cosValue = Math.cos(halfAngle);

		int x2 = Math.round(centerX + (float) (radius * cosValue));
		int y2 = Math.round(centerY + (float) (radius * sinValue));

		float widthLabel = paint.measureText(labelText) / 2;
		float heightLable = paint.getTextSize() / 2;
		float xLable = radius + (2.0f / 3) * (x2 - radius);
		float yLable = radius + (2.0f / 3) * (y2 - radius);

		canvas.drawText(labelText, xLable - widthLabel, yLable + heightLable,
				paint);

	}

}
