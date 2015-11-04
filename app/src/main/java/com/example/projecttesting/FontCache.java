package com.example.projecttesting;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Typeface;
import android.support.annotation.NonNull;
import android.util.AttributeSet;
import android.widget.TextView;

import java.lang.reflect.Type;
import java.util.Collection;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;

public class FontCache {
	private static Map<String, Typeface> fontMap = new HashMap<String, Typeface>();

	public static Typeface getFont(Context context, String fontName) {
		if (fontMap.containsKey(fontName)) {
			return fontMap.get(fontName);
		} else {
			Typeface tf = Typeface.createFromAsset(context.getAssets(), fontName);
			fontMap.put(fontName, tf);
			return tf;

		}
	}
}