package ca.etsmtl.applets.etsmobile.utils;

import android.view.MotionEvent;

public class TouchEvent 
{
	private static TouchEvent eventTouch;
	private float xStart, xAfter, yStart, yAfter;
	
	
	private TouchEvent(){
	}
	
	public static TouchEvent getInstance(){
		if(eventTouch==null){
			eventTouch = new TouchEvent();
		}
		return eventTouch;
	}
	
	public int motionEvent(MotionEvent event){
		
		switch(event.getAction()) {
        case(MotionEvent.ACTION_DOWN):
        	xStart = event.getX();
        	yStart = event.getY();
            break;
        case(MotionEvent.ACTION_UP): {
        	xAfter = event.getX();
        	yAfter = event.getY();
        	
        	return isMoveLeftOrRight();
        }
      }
		return 0;
	}
	
	
	/** Determine the motion move left or right
	 * 
	 * @return 0 if not move Left or Right
	 * @return 1 if is Right
	 * @return -1 if is Left
	 */
	private int isMoveLeftOrRight(){
		 float deltaX = xAfter- xStart;
         float deltaY = yAfter-yStart;

         if(Math.abs(deltaX) > Math.abs(deltaY)) {
             if(deltaX>0) 
            	 return -1;
             else return 1;
         } 
         else
        	 return 0;
	}
	
}
