package ca.etsmtl.applets.etsmobile.views;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Iterator;
import java.util.List;
import java.util.Locale;
import java.util.Observable;
import java.util.Observer;
import java.util.TimeZone;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Point;
import android.graphics.Rect;
import android.graphics.Typeface;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;
import ca.etsmtl.applets.etsmobile.R;

/**
 * NumGridView is view that renders a grid, where each cell contains a number.
 * This view has attributes to specify the number of cells, and whether the
 * cells should be square or rectangular (stretch to fill all available space).
 * It adapts the font size for the numbers automatically.
 * 
 * @author Maarten Pennings 2011 oct 15
 * 
 */
@SuppressLint({ "DrawAllocation", "DrawAllocation" })
public class NumGridView extends View implements Observer {

	/**
	 * Interface definition for a callback to be invoked when a cell of the grid
	 * is touched.
	 */
	public interface OnCellTouchListener {
		/**
		 * Called when a cell of the grid has been clicked.
		 * 
		 * @param v
		 *            The NumGridView whose cell was clicked.
		 * @param x
		 *            The horizontal coordinate of the cell in the grid.
		 * @param y
		 *            The vertical coordinate of the cell in the grid.
		 */
		void onCellTouch(NumGridView v, int x, int y);
	}

	final int nbCellulesY = 6; // on va générer toujours un grilles 7 x 6
								// Member variables
	protected Paint mPaintBg; // Holds the style for painting the cell
								// background
	protected Paint mPaintFg; // Holds the style for painting the cell
	// foreground
	protected boolean mStretch; // Can cells be stretched (non-square)
	protected int mCellCountX; // Width of the grid in cells (set in XML)
	protected int mCellCountY; // Height of the grid in cells (set in XML)
	protected int[][] mCells; // Contains number to be shown in each grid cell
	protected int mCellWidth; // Width of a cell in pixels
	protected int mCellHeight; // Height of a cell in pixels
	protected int mOffsetX; // Horizontal offset in pixels (to center the grid)

	protected int mOffsetY; // Vertical offset in pixels (to center the grid)

	protected Paint mPaintBorders;
	private Calendar current;

	private Point selectedCell;

	private List<Calendar> days;

	/**
	 * Member field holding the call back interface for touches on the view
	 * (which are mapped to cell coordinates). It has a public setter (
	 * <code>setOnCellTouchListener</code>), and it is used in overridden
	 * <code>dispatchTouchEvent</code>.
	 */
	protected OnCellTouchListener mOnCellTouchListener;

	/**
	 * The constructor as called by the XML inflater.
	 * 
	 * Maybe we should add the simpler and more complex variant that View has:
	 * <code>public NumGridView(Context context)</code> or
	 * <code>public NumGridView(Context context, AttributeSet attrs, int defStyle)</code>
	 * 
	 */
	public NumGridView(final Context context, final AttributeSet attrs) {
		// Init the base class
		super(context, attrs);

		mPaintBorders = new Paint();
		mPaintBorders.setAntiAlias(true);
		mPaintBorders.setColor(getResources().getColor(
				R.color.calendar_cell_border_color));
		mPaintBorders.setStyle(Paint.Style.STROKE);
		mPaintBorders.setStrokeWidth(3);

		// Setup paint background
		mPaintBg = new Paint();
		mPaintBg.setAntiAlias(true);
		mPaintBg.setColor(getResources().getColor(
				R.color.calendar_cell_background_color));

		mPaintBg.setStyle(Paint.Style.FILL_AND_STROKE);

		// Setup paint foreground
		mPaintFg = new Paint();
		mPaintFg.setAntiAlias(true);
		mPaintFg.setColor(getResources().getColor(
				R.color.calendar_cell_text_color));
		mPaintFg.setTypeface(Typeface.create(Typeface.SERIF, Typeface.BOLD));
		mPaintFg.setTextAlign(Paint.Align.CENTER);

		// Get the XML attributes
		final TypedArray a = context.obtainStyledAttributes(attrs,
				R.styleable.NumGridView);
		mStretch = a.getBoolean(R.styleable.NumGridView_stretch, false);
		mCellCountX = a.getInt(R.styleable.NumGridView_cellCountX, 7);
		mCellCountY = a.getInt(R.styleable.NumGridView_cellCountY, 6);
		a.recycle();

		selectedCell = new Point(-1, -1);
		// Setup the grid cells
		mCells = new int[mCellCountX][mCellCountY];
		for (int y = 0; y < mCellCountY; y++) {
			for (int x = 0; x < mCellCountX; x++) {
				mCells[x][y] = 0;

			}
		}
	}

	/**
	 * Goal: trap touches and notify our customer via the
	 * <code>mOnCellTouchListener</code> listener.
	 * 
	 * Notes (1) This override maps the raw coordinates to cell coordinates. (2)
	 * This view <em>overrides</em> the dispatcher, instead of registering
	 * itself as listener. This allows our customers to register an event
	 * listener for raw coordinates. (3) This override calls our own listener,
	 * then passes the event to the super class which handles call backs using
	 * raw coordinates.
	 */
	@Override
	public boolean dispatchTouchEvent(final MotionEvent event) {
		// First dispatch calls to our cell touch listener...
		if (mOnCellTouchListener != null) {
			final int x = (int) event.getX() - mOffsetX;
			final int y = (int) event.getY() - mOffsetY;
			if (0 <= x && x < mCellWidth * mCellCountX && 0 <= y
					&& y < mCellHeight * mCellCountY) {
				// Touch was on cell (not on padding area)
				mOnCellTouchListener.onCellTouch(this, x / mCellWidth, y
						/ mCellHeight);
			}
		}
		// ... next dispatch calls from the super class
		return super.dispatchTouchEvent(event);
	}

	/**
	 * Gets the value contained in cell (<code>y</code>,<code>y</code>) of the
	 * grid.
	 * 
	 * @param x
	 *            The horizontal coordinate [0..<code>mCellCountX</code>) of the
	 *            cell
	 * @param y
	 *            The vertical coordinate [0..<code>mCellCountY</code>) of the
	 *            cell
	 * @return The value of the cell (<code>x</code>,<code>y</code>)
	 */
	public int getCell(final int x, final int y) {
		if (!(0 <= x && x < mCellCountX)) {
			throw new IllegalArgumentException(
					"getCell: x coordinate out of range");
		}
		if (!(0 <= y && y < mCellCountY)) {
			throw new IllegalArgumentException(
					"getCell: y coordinate out of range");
		}
		return mCells[x][y];
	}

	/**
	 * Callback called when NumGridView object should draw itself. Draws all the
	 * cells (background) and writes the current cell value centered
	 */
	@Override
	protected void onDraw(final Canvas canvas) {
		super.onDraw(canvas);

		// Compute text origin inside the cell
		final float fontsize = mPaintFg.descent() - mPaintFg.ascent();
		final int tx = mCellWidth / 2;
		final int ty = (int) (mCellHeight / 2 + fontsize / 2 - mPaintFg
				.descent());

		final Iterator<Calendar> it = days.iterator();
		Calendar c, now;

		now = Calendar.getInstance(TimeZone.getTimeZone("Canada/Eastern"),
				Locale.CANADA_FRENCH);
		// Draw all cells
		for (int y = 0; y < mCellCountY; y++) {
			for (int x = 0; x < mCellCountX; x++) {
				// Draw a rectangle
				final int dx = x * mCellWidth + mOffsetX;
				final int dy = y * mCellHeight + mOffsetY;

				c = it.next();

				if (selectedCell.equals(x, y)) {
					mPaintFg.setAlpha(100);
					mPaintBg.setColor(getResources().getColor(
							R.color.calendar_selected_cell_background_color));
					mPaintFg.setColor(getResources().getColor(
							R.color.calendar_selected_cell_text_color));
				} else if (c.get(Calendar.MONTH) == now.get(Calendar.MONTH)
						&& c.get(Calendar.DAY_OF_MONTH) == now
								.get(Calendar.DAY_OF_MONTH)) {
					mPaintFg.setAlpha(100);
					mPaintBg.setColor(getResources().getColor(
							R.color.calendar_current_day_cell_background_color));
					mPaintFg.setColor(getResources().getColor(
							R.color.calendar_current_day_cell_text_color));
				} else if (c.get(Calendar.MONTH) == current.get(Calendar.MONTH)) {
					mPaintFg.setAlpha(100);
					mPaintBg.setColor(getResources().getColor(
							R.color.calendar_cell_background_color));
					mPaintFg.setColor(getResources().getColor(
							R.color.calendar_cell_text_color));

				} else {
					mPaintBg.setColor(getResources().getColor(
							R.color.calendar_cell_background_color));
					mPaintFg.setColor(getResources().getColor(
							R.color.calendar_cell_text_color));
					mPaintFg.setAlpha(50);
				}

//				if(c.getTime().equals(object))
				
				canvas.drawRect(new Rect(dx + 1, dy + 1, dx + mCellWidth - 2,
						dy + mCellHeight - 2), mPaintBorders);

				canvas.drawRect(new Rect(dx + 1, dy + 1, dx + mCellWidth - 2,
						dy + mCellHeight - 2), mPaintBg);

				// Draw the cell value
				final int v = mCells[x][y];
				canvas.drawText("" + v, dx + tx, dy + ty, mPaintFg);
				// // Test code to draw ascender stick and descender stick
				Paint p = new Paint();
				p.setColor(0xFFff0000);
				canvas.drawRect(new Rect(dx + tx, dy + ty, dx + tx - 8, dy + ty
						+ (int) mPaintFg.ascent()), p);
				canvas.drawRect(new Rect(dx + tx, dy + ty, dx + tx + 8, dy + ty
						+ (int) mPaintFg.descent()), p);
			}
		}
	}

	/**
	 * Callback called when parent calls us to find out our size. We should call
	 * setMeasuredDimension in response.
	 */
	@Override
	protected void onMeasure(final int widthMeasureSpec,
			final int heightMeasureSpec) {
		// Extract the Ms (MesaureSpec) parameters
		final int widthMsMode = MeasureSpec.getMode(widthMeasureSpec);
		final int widthMsSize = MeasureSpec.getSize(widthMeasureSpec);
		final int heightMsMode = MeasureSpec.getMode(heightMeasureSpec);
		final int heightMsSize = MeasureSpec.getSize(heightMeasureSpec);

		final int defaultSizeX = 32;
		final int defaultSizeY = 32;

		// Determine view width and height: either default size or passed size
		final int vw = widthMsMode == MeasureSpec.UNSPECIFIED ? mCellCountX
				* defaultSizeX : widthMsSize;
		final int vh = heightMsMode == MeasureSpec.UNSPECIFIED ? mCellCountY
				* defaultSizeY : heightMsSize;

		// Determine cell width and height, assuming stretch is allowed
		final double cw = vw / mCellCountX;
		final double ch = vh / mCellCountY;

		// Determine cell width and height adhering to stretch attribute
		if (mStretch) {
			mCellWidth = (int) Math.floor(cw);
			mCellHeight = (int) Math.floor(ch);
		} else {
			final double size = Math.min(cw, ch);
			mCellWidth = (int) Math.floor(size);
			mCellHeight = (int) Math.floor(size);
		}

		// Determine offset

		mOffsetX = (vw - mCellWidth * mCellCountX) / 2;

		// Satisfy contract by calling setMeasuredDimension
		setMeasuredDimension(mOffsetX + mCellCountX * mCellWidth + mOffsetX,
				nbCellulesY * mCellHeight);

		// Set font size
		final float specified_fontsize = mPaintFg.getTextSize();
		final float measured_fontsize = mPaintFg.descent() - mPaintFg.ascent();
		final float font_factor = specified_fontsize / measured_fontsize;
		mPaintFg.setTextSize(mCellHeight * 0.8f * font_factor);
	}

	/**
	 * Sets cell (<code>x</code>,<code>y</code>) of the grid to contain value
	 * <code>v</code>.
	 * 
	 * @param x
	 *            The horizontal coordinate [0..<code>mCellCountX</code>) of the
	 *            cell
	 * @param y
	 *            The vertical coordinate [0..<code>mCellCountY</code>) of the
	 *            cell
	 * @param v
	 *            The value for cell (<code>x</code>,<code>y</code>)
	 */
	public void setCell(final int x, final int y, final int v) {
		if (!(0 <= x && x < mCellCountX)) {
			throw new IllegalArgumentException(
					"setCell: x coordinate out of range");
		}
		if (!(0 <= y && y < mCellCountY)) {
			throw new IllegalArgumentException(
					"setCell: y coordinate out of range");
		}
		mCells[x][y] = v;
		invalidate();
	}

	/**
	 * Register a callback to be invoked when a cell of the grid is clicked.
	 * 
	 * @param listener
	 *            The callback that will run
	 */
	public void setOnCellTouchListener(final OnCellTouchListener listener) {
		mOnCellTouchListener = listener;
	}

	public void setSelectedCell(final Point p) {

		selectedCell = p;

	}

	@Override
	public void update(final Observable observable, final Object data) {
		current = (Calendar) data;
		days = new ArrayList<Calendar>();

		final Calendar firstdayofmonth = (Calendar) current.clone();
		final Calendar lastdayofmonth = (Calendar) current.clone();

		firstdayofmonth.set(Calendar.DAY_OF_MONTH,
				current.getActualMinimum(Calendar.DAY_OF_MONTH));
		lastdayofmonth.set(Calendar.DAY_OF_MONTH,
				current.getActualMaximum(Calendar.DAY_OF_MONTH));

		switch (firstdayofmonth.get(Calendar.DAY_OF_WEEK)) {

		case Calendar.MONDAY:
			firstdayofmonth.add(Calendar.DAY_OF_MONTH, -1);
			break;
		case Calendar.TUESDAY:
			firstdayofmonth.add(Calendar.DAY_OF_MONTH, -2);
			break;
		case Calendar.WEDNESDAY:
			firstdayofmonth.add(Calendar.DAY_OF_MONTH, -3);
			break;
		case Calendar.THURSDAY:
			firstdayofmonth.add(Calendar.DAY_OF_MONTH, -4);
			break;
		case Calendar.FRIDAY:
			firstdayofmonth.add(Calendar.DAY_OF_MONTH, -5);
			break;
		case Calendar.SATURDAY:
			firstdayofmonth.add(Calendar.DAY_OF_MONTH, -6);
			break;
		}

		switch (lastdayofmonth.get(Calendar.DAY_OF_WEEK)) {
		case Calendar.SUNDAY:
			lastdayofmonth.add(Calendar.DAY_OF_MONTH, 6);
			break;
		case Calendar.MONDAY:
			lastdayofmonth.add(Calendar.DAY_OF_MONTH, 5);
			break;
		case Calendar.TUESDAY:
			lastdayofmonth.add(Calendar.DAY_OF_MONTH, 4);
			break;
		case Calendar.WEDNESDAY:
			lastdayofmonth.add(Calendar.DAY_OF_MONTH, 3);
			break;
		case Calendar.THURSDAY:
			lastdayofmonth.add(Calendar.DAY_OF_MONTH, 2);
			break;
		case Calendar.FRIDAY:
			lastdayofmonth.add(Calendar.DAY_OF_MONTH, 1);
			break;

		}

		while (firstdayofmonth.compareTo(lastdayofmonth) <= 0) {

			days.add((Calendar) firstdayofmonth.clone());
			firstdayofmonth.add(Calendar.DAY_OF_MONTH, 1);
		}

		mCellCountY = days.size() / 7;

		mCells = new int[mCellCountX][mCellCountY];

		final Iterator<Calendar> it = days.iterator();

		for (int y = 0; y < mCellCountY; y++) {
			for (int x = 0; x < mCellCountX; x++) {
				mCells[x][y] = it.next().get(Calendar.DAY_OF_MONTH);

			}
		}
		this.invalidate();

	}

}
