package flfm.ui;

import java.awt.Component;
import java.awt.Container;
import java.awt.Dimension;
import java.awt.LayoutManager;

public class RecordEditorLayout implements LayoutManager {

	/**
	 * —ñ”(ŒÅ’è)
	 */
	private static final int numCols = 6;
	
	/**
	 * —ñŠÔ‚ÌƒAƒL
	 */
	private int gap = 4;
	
	@Override
	public void layoutContainer(Container parent) {
		int numRows = parent.getComponentCount() / numCols;
		int[] colW = new int[numCols];
		int[] rowH = new int[numRows];
		for (int r = 0; r < numRows; r += 1) {
			for (int c = 0; c < numCols; c += 1) {
				Dimension ps = parent.getComponent(r * numCols + c).
						getPreferredSize();
				colW[c] = Math.max(colW[c], ps.width);
				rowH[r] = Math.max(rowH[r], ps.height);
			}
		}
		int y = 0;
		for (int r = 0; r < numRows; r += 1) {
			int x = 0;
			for (int c = 0; c < numCols; c += 1) {
				if (c > 0) {
					x += gap;
				}
				Component comp = parent.getComponent(r * numCols + c);
				Dimension ps = parent.getComponent(r * numCols + c).
						getPreferredSize();
				if (c < numCols - 1) {
					comp.setBounds(x, y, colW[c], rowH[r]);
				} else {
					comp.setBounds(x, y, ps.width, rowH[r]);
				}
				x += colW[c];
			}
			y += rowH[r];
		}
	}
	
	@Override
	public Dimension preferredLayoutSize(Container parent) {
		int numRows = parent.getComponentCount() / numCols;
		int[] colW = new int[numCols];
		int[] rowH = new int[numRows];
		for (int r = 0; r < numRows; r += 1) {
			for (int c = 0; c < numCols; c += 1) {
				Dimension ps = parent.getComponent(r * numCols + c).
						getPreferredSize();
				colW[c] = Math.max(colW[c], ps.width);
				rowH[r] = Math.max(rowH[r], ps.height);
			}
		}
		int w = 0;
		for (int c = 0; c < numCols; c += 1) {
			if (c > 0) {
				w += gap;
			}
			w += colW[c];
		}
		int h = 0;
		for (int r = 0; r < numRows; r += 1) {
			h += rowH[r];
		}
		return new Dimension(w, h);
	}

	@Override
	public Dimension minimumLayoutSize(Container parent) {
		return new Dimension(100, 100);
	}
	
	@Override
	public void addLayoutComponent(String name, Component comp) {
	}
	
	@Override
	public void removeLayoutComponent(Component comp) {
	}
}