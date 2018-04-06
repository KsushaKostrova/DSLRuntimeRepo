package my.project;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.datatransfer.DataFlavor;
import java.awt.dnd.DnDConstants;
import java.awt.dnd.DropTarget;
import java.awt.dnd.DropTargetDragEvent;
import java.awt.dnd.DropTargetDropEvent;
import java.awt.dnd.DropTargetEvent;
import java.awt.dnd.DropTargetListener;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;

import javax.swing.JLabel;
import javax.swing.border.Border;
import javax.swing.border.LineBorder;

//delete this class
public class DropLabel extends JLabel {

	private DropTarget dropTarget;
	private DropTargetListener dtListener;
	private int acceptableActions = DnDConstants.ACTION_COPY;

	public DropLabel(String text) {
		this.setText(text);
		this.dtListener = new DropTargetListener() {

			@Override
			public void dropActionChanged(DropTargetDragEvent dtde) {
				if (!isDragOk(dtde)) {
					dtde.rejectDrag();
					return;
				}
				dtde.acceptDrag(DropLabel.this.acceptableActions);
			}

			@Override
			public void drop(DropTargetDropEvent dtde) {
				DataFlavor[] flavors = StringTransferable.flavors;
				DataFlavor chosen = null;
				if (!dtde.isLocalTransfer()) {
					chosen = StringTransferable.plainTextFlavor;
				} else {
					for (int i = 0; i < flavors.length; i++) {
						if (dtde.isDataFlavorSupported(flavors[i])) {
							chosen = flavors[i];
							break;
						}
					}
				}

				if (chosen == null) {
					dtde.rejectDrop();
					return;
				}

				int sa = dtde.getSourceActions();
				if ((sa & DropLabel.this.acceptableActions) == 0) {
					dtde.rejectDrop();
					return;
				}

				Object data = null;
				try {
					dtde.acceptDrop(DropLabel.this.acceptableActions);
					data = dtde.getTransferable().getTransferData(chosen);
					if (data == null) {
						throw new NullPointerException();
					}
				} catch (Throwable t) {
					dtde.dropComplete(false);
					return;
				}
				
				if (data instanceof String) {
					String s = (String) data;
					System.out.println("HEREEEE");
					DropLabel.this.setText(s);
				} else if (data instanceof InputStream){
					String charset = chosen.getParameter("charset").trim();
					InputStream input = (InputStream) data;
					InputStreamReader isr = null;
					try {
						isr = new InputStreamReader(input, charset);
					} catch (UnsupportedEncodingException uee) {
						isr = new InputStreamReader(input);
					}
					
					StringBuffer str = new StringBuffer();
					int in = -1;
					try {
						while ((in = isr.read()) >= 0) {
							if (in != 0) {
								str.append((char) in);
							}
						}
						DropLabel.this.setText(str.toString());
					} catch (IOException ioe) {
						dtde.dropComplete(false);
						return;
					}
				} else {
					System.out.println("drop: rejecting");
					dtde.dropComplete(false);
					return;
				}
				dtde.dropComplete(true);
			}

			@Override
			public void dragOver(DropTargetDragEvent dtde) {
				if (!isDragOk(dtde)) {
					dtde.rejectDrag();
					return;
				}
				dtde.acceptDrag(DropLabel.this.acceptableActions);
			}

			@Override
			public void dragExit(DropTargetEvent dte) {
				
			}

			@Override
			public void dragEnter(DropTargetDragEvent dtde) {
				if (!isDragOk(dtde)) {
					dtde.rejectDrag();
					return;
				}
//				Border border = new LineBorder(Color.GREEN);
//				DropLabel.this.setBorder(border);
				dtde.acceptDrag(DropLabel.this.acceptableActions);
			}

			private boolean isDragOk(DropTargetDragEvent e) {
				DataFlavor[] flavors = StringTransferable.flavors;
				DataFlavor chosen = null;
				for (int i = 0; i < flavors.length; i++) {
					if (e.isDataFlavorSupported(flavors[i])) {
						chosen = flavors[i];
						break;
					}
				}

				if (chosen == null) {
					return false;
				}

				int sa = e.getSourceActions();
				if ((sa & DropLabel.this.acceptableActions) == 0) {
					return false;
				}
				return true;
			}
		};
		this.dropTarget = new DropTarget(this, this.acceptableActions, this.dtListener, true);
	}

}
