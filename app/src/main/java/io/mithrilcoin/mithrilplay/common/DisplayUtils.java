package io.mithrilcoin.mithrilplay.common;

import android.content.Context;
import android.util.TypedValue;


public class DisplayUtils {

    public static int getPX(Context context, int dp) {
		try{
			return (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, dp, context.getResources().getDisplayMetrics());

		}catch (Exception e){

		}
		return dp;
    }

	public static int getPX(int dp) {
		try {
			return (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, dp, CommonApplication.getResource().getDisplayMetrics());

		}catch (Exception e){

		}
		return dp;
    }

	/**
	 * @param dip dip
	 * @return pixel
	 */
	public static float toPixel2(float dip) {
		return TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, dip, CommonApplication.getResource().getDisplayMetrics());
	}

}
