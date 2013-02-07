package nibbles.ui;

import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Rect;

public class AsciiCharWriter {
	private final Bitmap scaledCharset;
	private final int size;
	private final int charsPerRow;
	private final int charsPerColumn;
	private final int width;
	private final int height;
	private int scaledWidth;
	private int scaledHeight;
	private final Paint p = new Paint();

	public AsciiCharWriter(Bitmap charset, int width, int height, float scaleX,
			float scaleY) {
		this.width = width;
		this.height = height;
		charsPerRow = charset.getWidth() / width;
		charsPerColumn = charset.getHeight() / height;
		size = charsPerRow * charsPerColumn;
		
		scaledWidth = Math.round(scaleX * width);
		scaledHeight = Math.round(scaleY * height);
		scaledCharset = scaleCharset(charset).extractAlpha();
	}

	private Bitmap scaleCharset(Bitmap charset) {
		return Bitmap.createScaledBitmap(charset, charsPerRow * scaledWidth,
				charsPerColumn * scaledHeight, true);
	}

	public void write(Canvas canvas, char c, int x, int y, int color) {
		if (c < size) {
			int sX = c % charsPerRow;
			int sY = (c - sX) / charsPerRow * scaledHeight;
			sX *= scaledWidth;
			Rect srcRect = new Rect(sX, sY, sX + scaledWidth, sY + scaledHeight);
			Rect dstRect = new Rect(x, y, x + width, y + height);
			p.setColor(color);
			canvas.drawBitmap(scaledCharset, srcRect, dstRect, p);
		}
	}
}
