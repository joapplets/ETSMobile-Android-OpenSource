package ca.etsmtl.applets.etsmobile.views;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
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
import android.graphics.Rect;
import android.graphics.Typeface;
import android.graphics.drawable.Drawable;
import android.util.AttributeSet;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import ca.etsmtl.applets.etsmobile.R;
import ca.etsmtl.applets.etsmobile.models.ActivityCalendar;
import ca.etsmtl.applets.etsmobile.models.CalendarCell;
import ca.etsmtl.applets.etsmobile.models.JoursRemplaces;
import ca.etsmtl.applets.etsmobile.models.Session;

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
public class NumGridViewWeek extends View implements Observer {

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
		void onCellTouch(NumGridViewWeek v, int x, int y);
	}

	final int nbCellulesY = 1; // on va générer toujours un grilles 7 x 6

	int maxIndicators = 3;
	// Member variables
	protected Paint mPaintBg; // Holds the style for painting the cell
	// background
	protected Paint mPaintFg; // Holds the style for painting the cell
	// foreground
	protected Paint mPaintBorders;
	protected Paint mPaintCourseIndicator;
	protected boolean mStretch; // Can cells be stretched (non-square)
	protected int mCellCountX; // Width of the grid in cells (set in XML)
	protected int mCellCountY; // Height of the grid in cells (set in XML)
	protected CalendarCell[][] mCells; // Contains number to be shown in each
	// grid cell
	protected int mCellWidth; // Width of a cell in pixels
	protected int mCellHeight; // Height of a cell in pixels
	protected int mOffsetX; // Horizontal offset in pixels (to center the grid)

	protected int mOffsetY; // Vertical offset in pixels (to center the grid)
	private Calendar current;

	private CalendarCell currentCell;

	/**
	 * Member field holding the call back interface for touches on the view
	 * (which are mapped to cell coordinates). It has a public setter (
	 * <code>setOnCellTouchListener</code>), and it is used in overridden
	 * <code>dispatchTouchEvent</code>.
	 */
	protected OnCellTouchListener mOnCellTouchListener;

	private ArrayList<Session> sessions = new ArrayList<Session>();

	/**
	 * The constructor as called by the XML inflater.
	 * 
	 * Maybe we should add the simpler and more complex variant that View has:
	 * <code>public NumGridView(Context context)</code> or
	 * <code>public NumGridView(Context context, AttributeSet attrs, int defStyle)</code>
	 * 
	 */
	public NumGridViewWeek(final Context context, final AttributeSet attrs) {
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

		mPaintCourseIndicator = new Paint();
		mPaintCourseIndicator.setAntiAlias(true);
		mPaintCourseIndicator.setStyle(Paint.Style.FILL);

		// Get the XML attributes
		final TypedArray a = context.obtainStyledAttributes(attrs,
				R.styleable.NumGridView);
		mStretch = a.getBoolean(R.styleable.NumGridView_stretch, false);
		mCellCountX = a.getInt(R.styleable.NumGridView_cellCountX, 5);
		mCellCountY = a.getInt(R.styleable.NumGridView_cellCountY, 1);
		a.recycle();

		Log.v("NumGridView", "NumGridView: mCellCountX=" + mCellCountX);
		Log.v("NumGridView", "NumGridView: mCellCountY=" + mCellCountY);

		// Setup the grid cells

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
	public CalendarCell getCell(final int x, final int y) {
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

	public int getCellHeight() {
		return mCellHeight;
	}

	public Calendar getCurrent() {
		return current;
	}

	public CalendarCell getCurrentCell() {
		return currentCell;
	}

	public int getmCellCountY() {
		return mCellCountY;
	}

	public void setSessions(final ArrayList<Session> obj) {
		sessions = obj;
		for (final Session s : sessions) {
			if (this.maxIndicators < s.getMaxActivities()) {
				this.maxIndicators = s.getMaxActivities();
			}
		}
	}

	private List<Session> getSessions(final List<Calendar> days) {

		final List<Session> sessions = new ArrayList<Session>();

		for (final Session s : this.sessions) {
			if (!(days.get(0).getTime().after(s.getDateFinCours()) || days
					.get(days.size() - 1).getTime().before(s.getDateDebut()))) {
				sessions.add(s);
			} else if (days.get(0).getTime().after(s.getDateFinCours())) {
				break;
			}
		}

		return sessions;
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

		Date now;
		CalendarCell cell;

		now = Calendar.getInstance(TimeZone.getTimeZone("Canada/Eastern"),
				Locale.CANADA_FRENCH).getTime();
		// Draw all cells
		for (int y = 0; y < mCellCountY; y++) {
			for (int x = 0; x < mCellCountX; x++) {
				// Draw a rectangle
				final int dx = x * mCellWidth + mOffsetX;
				final int dy = y * mCellHeight + mOffsetY;

				cell = mCells[x][y];

				if (currentCell != null) {
					if (cell.equals(currentCell)) {
						mPaintFg.setAlpha(100);
						mPaintBg.setColor(getResources()
								.getColor(
										R.color.calendar_selected_cell_background_color));
						mPaintFg.setColor(getResources().getColor(
								R.color.calendar_selected_cell_text_color));
					} else if (cell.getDate().getYear() == now.getYear()
							&& cell.getDate().getMonth() == now.getMonth()
							&& cell.getDate().getDate() == now.getDate()) {
						mPaintFg.setAlpha(100);
						mPaintBg.setColor(getResources()
								.getColor(
										R.color.calendar_current_day_cell_background_color));
						mPaintFg.setColor(getResources().getColor(
								R.color.calendar_current_day_cell_text_color));
					} else if (cell.getDate().getMonth() == current.getTime()
							.getMonth()) {
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
				}

				canvas.drawRect(new Rect(dx + 1, dy + 1, dx + mCellWidth - 2,
						dy + mCellHeight - 2), mPaintBorders);

				canvas.drawRect(new Rect(dx + 1, dy + 1, dx + mCellWidth - 2,
						dy + mCellHeight - 2), mPaintBg);

				// Draw the cell value
				final int v = cell.getDate().getDate();
				canvas.drawText("" + v, dx + tx, dy + ty, mPaintFg);

				final Iterator<ActivityCalendar> it = cell.iterator();

				int i = 0;

				final float radius = mCellWidth / (3 * maxIndicators + 1);

				final float startpos = dx
						+ tx
						- ((2 * cell.size() + cell.size() - 1) * radius / 2 - radius);

				ActivityCalendar event;
				while (it.hasNext()) {
					event = it.next();

					final Drawable d = getResources().getDrawable(
							event.getDrawableResId());

					final int left = (int) (startpos + 3 * i * radius - radius);

					final int right = (int) (left + (radius * 2));

					final int bottom = dy + mCellHeight - ty / 4 + 2;

					final int top = (int) (bottom - (2 * radius));

					d.setBounds(left, top, right, bottom);

					d.draw(canvas);

					i++;
				}

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
		mPaintFg.setTextSize(mCellHeight * 0.5f * font_factor);
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
	public void setCell(final int x, final int y, final CalendarCell v) {
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

	public void setCurrentCell(final CalendarCell currentCell) {
		this.currentCell = currentCell;
	}

	public void setCurrentCell(final int x, final int y) {
		currentCell = mCells[x][y];
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

	@Override
	public void update(final Observable observable, final Object data) {

		final long start = new Date().getTime();

		current = (Calendar) data;
		final List<Calendar> days = new ArrayList<Calendar>();

		final Calendar firstDayOfWeek = (Calendar) current.clone();
		final Calendar lastDayOfWeek = (Calendar) current.clone();

		firstDayOfWeek.set(Calendar.DAY_OF_WEEK, current.getFirstDayOfWeek());
		lastDayOfWeek.set(Calendar.DAY_OF_WEEK,
				current.getLeastMaximum(Calendar.DAY_OF_WEEK_IN_MONTH));
		// lastDayOfWeek.set(Calendar.DAY_OF_MONTH,
		// current.getActualMaximum(Calendar.DAY_OF_MONTH));

		switch (firstDayOfWeek.get(Calendar.DAY_OF_WEEK)) {

		case Calendar.MONDAY:
			firstDayOfWeek.add(Calendar.DAY_OF_MONTH, -1);
			break;
		case Calendar.TUESDAY:
			firstDayOfWeek.add(Calendar.DAY_OF_MONTH, -2);
			break;
		case Calendar.WEDNESDAY:
			firstDayOfWeek.add(Calendar.DAY_OF_MONTH, -3);
			break;
		case Calendar.THURSDAY:
			firstDayOfWeek.add(Calendar.DAY_OF_MONTH, -4);
			break;
		case Calendar.FRIDAY:
			firstDayOfWeek.add(Calendar.DAY_OF_MONTH, -5);
			break;
		case Calendar.SATURDAY:
			firstDayOfWeek.add(Calendar.DAY_OF_MONTH, -6);
			break;
		}

		switch (lastDayOfWeek.get(Calendar.DAY_OF_WEEK)) {
		case Calendar.SUNDAY:
			lastDayOfWeek.add(Calendar.DAY_OF_MONTH, 6);
			break;
		case Calendar.MONDAY:
			lastDayOfWeek.add(Calendar.DAY_OF_MONTH, 5);
			break;
		case Calendar.TUESDAY:
			lastDayOfWeek.add(Calendar.DAY_OF_MONTH, 4);
			break;
		case Calendar.WEDNESDAY:
			lastDayOfWeek.add(Calendar.DAY_OF_MONTH, 3);
			break;
		case Calendar.THURSDAY:
			lastDayOfWeek.add(Calendar.DAY_OF_MONTH, 2);
			break;
		case Calendar.FRIDAY:
			lastDayOfWeek.add(Calendar.DAY_OF_MONTH, 1);
			break;

		}

		while (firstDayOfWeek.compareTo(lastDayOfWeek) <= 0) {

			days.add((Calendar) firstDayOfWeek.clone());
			firstDayOfWeek.add(Calendar.DAY_OF_MONTH, 1);
		}
		// TODO
		// mCellCountY = days.size() / 7;
		// mCellCountY = days.size() / mCellCountX;

		mCells = new CalendarCell[mCellCountX][mCellCountY];

		final Iterator<Calendar> it = days.iterator();

		Date now = null;

		if (currentCell == null) {
			now = Calendar.getInstance(TimeZone.getTimeZone("Canada/Eastern"),
					Locale.CANADA_FRENCH).getTime();
		}

		// PossibilitŽ d'avoir 2 sessions dans 1 mois
		final List<Session> sessions = getSessions(days);
		Log.v("NumGridViewWeek", "NumGridViewWeek: setOnCellTouche day.size ="
				+ days.size());
		Log.v("NumGridViewWeek",
				"NumGridViewWeek: setOnCellTouche mCellCountY =" + mCellCountY);
		Log.v("NumGridViewWeek",
				"NumGridViewWeek: setOnCellTouche mCellCountX =" + mCellCountX);
		for (int y = 0; y < mCellCountY; y++) {
			for (int x = 0; x < mCellCountX; x++) {
				Date nextTime = it.next().getTime();
				Calendar cal = GregorianCalendar.getInstance();
				cal.setTimeInMillis(nextTime.getTime());
				if (cal.get(Calendar.DAY_OF_WEEK) != Calendar.SUNDAY
						&& cal.get(Calendar.DAY_OF_WEEK) != Calendar.SATURDAY) {
					mCells[x][y] = new CalendarCell(nextTime);

					for (final Session s : sessions) {

						if (mCells[x][y].getDate().after(s.getDateDebut())
								&& mCells[x][y].getDate().before(
										s.getDateFinCours())) {

							boolean isRemplacee = false;

							for (final JoursRemplaces j : s.getJoursRemplaces()) {

								if (mCells[x][y].getDate().getDate() == j
										.getDateRemplacement().getDate()
										&& mCells[x][y].getDate().getMonth() == j
												.getDateRemplacement()
												.getMonth()
										&& mCells[x][y].getDate().getYear() == j
												.getDateRemplacement()
												.getYear()) {
									if (s.getActivities(String.valueOf(j
											.getDateOrigine().getDay())) != null) {
										mCells[x][y].setEvents(s
												.getActivities(String.valueOf(j
														.getDateOrigine()
														.getDay())));
									} else {
										mCells[x][y].clear();
									}

									isRemplacee = true;
									break;
								}

								if (mCells[x][y].getDate().getDate() == j
										.getDateOrigine().getDate()
										&& mCells[x][y].getDate().getMonth() == j
												.getDateOrigine().getMonth()
										&& mCells[x][y].getDate().getYear() == j
												.getDateOrigine().getYear()) {
									mCells[x][y].clear();

									final ActivityCalendar activity = new ActivityCalendar();

									activity.setCours(j.getDescription().trim());
									activity.setDrawableResId(R.drawable.kal_marker_black);

									mCells[x][y].add(activity);

									isRemplacee = true;
									break;
								}
							}

							if (!isRemplacee
									&& s.getActivities(String
											.valueOf(mCells[x][y].getDate()
													.getDay())) != null) {
								Log.v("NumGridViewWeek",
										"NumGridViewWeek: !=isRemplacee: setEvents");
								mCells[x][y].setEvents(s.getActivities(String
										.valueOf(mCells[x][y].getDate()
												.getDay())));
							}

						}

					}

					if (currentCell == null) {
						Log.v("NumGridView", "NumGridView: x=" + x + " y=" + y
								+ " now.getMonth=" + now.getMonth() + " Month="
								+ mCells[x][y].getDate());
						if (mCells[x][y].getDate().getMonth() == now.getMonth()
								&& mCells[x][y].getDate().getDate() == now
										.getDate()) {
							setCurrentCell(mCells[x][y]);
							Log.v("NumGridView",
									"setCurrentCell NumGridView: x=" + x
											+ " y=" + y + " now.getMonth="
											+ now.getMonth() + " Month="
											+ mCells[x][y].getDate());
						}
					}
				} else {
					x--;
				}
			}
		}

		this.invalidate();

		final long stop = new Date().getTime();
		System.out.println("updated in milliseconds: " + (stop - start));

	}
}
