package nibbles.ui;

import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Rect;

public class AsciiWriter {
	private final Bitmap charset;
	private final int size;
	private final int charsPerRow;
	private final int charsPerColumn;
	private final int charWidth;
	private final int charHeight;
	private int scaledCharWidth;
	private int scaledCharHeight;
	private final Paint p = new Paint();

	public AsciiWriter(Bitmap charset, int charWidth, int charHeight,
			float scaleX, float scaleY) {
		this.charWidth = charWidth;
		this.charHeight = charHeight;
		charsPerRow = charset.getWidth() / charWidth;
		charsPerColumn = charset.getHeight() / charHeight;
		size = charsPerRow * charsPerColumn;
		this.charset = scaleCharset(charset, scaleX, scaleY).extractAlpha();
	}

	private Bitmap scaleCharset(Bitmap charset, float scaleX, float scaleY) {
		scaledCharWidth = Math.round(scaleX * charWidth);
		scaledCharHeight = Math.round(scaleY * charHeight);
		return Bitmap.createScaledBitmap(charset,
				charsPerRow * scaledCharWidth, charsPerColumn
						* scaledCharHeight, true);
	}

	public void write(Canvas canvas, char c, int x, int y, int color) {
		if (c < size) {
			int sX = c % charsPerRow;
			int sY = (c - sX) / charsPerRow * scaledCharHeight;
			sX *= scaledCharWidth;
			Rect srcRect = new Rect(sX, sY, sX + scaledCharWidth, sY + scaledCharHeight);
			Rect dstRect = new Rect(x, y, x + charWidth, y + charHeight);
			p.setColor(color);
			canvas.drawBitmap(charset, srcRect, dstRect, p);
		}
	}
}
