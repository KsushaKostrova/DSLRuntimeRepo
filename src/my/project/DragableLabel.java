package my.project;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.datatransfer.Transferable;
import java.awt.dnd.DnDConstants;
import java.awt.dnd.DragGestureEvent;
import java.awt.dnd.DragGestureListener;
import java.awt.dnd.DragSource;
import java.awt.dnd.DragSourceContext;
import java.awt.dnd.DragSourceDragEvent;
import java.awt.dnd.DragSourceDropEvent;
import java.awt.dnd.DragSourceEvent;
import java.awt.dnd.DragSourceListener;
import java.awt.dnd.InvalidDnDOperationException;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;

import javax.swing.JLabel;
import javax.swing.border.Border;
import javax.swing.border.LineBorder;

public class DragableLabel extends JLabel {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private DragSource dragSource;
	private DragGestureListener dgListener;
	private DragSourceListener dsListener;
	private boolean isDropped = false;
//	private DropPanel targetDropPanel;
//	private String parentClass;
//	
//	public String getParentClass() {
//		return parentClass;
//	}
	
	public void setIsDropped (boolean isDropped) {
		this.isDropped = isDropped;
	}

	public DragableLabel(String name, DropPanel targetDropPanel) {
		this.setText(name);
//		this.targetDropPanel = targetDropPanel;
		this.dragSource = DragSource.getDefaultDragSource();
		this.dgListener = new DragGestureListener() {

			@Override
			public void dragGestureRecognized(DragGestureEvent dge) {
				try {
					Transferable transferable = new StringTransferable(name);
					dragSource.startDrag(dge, DragSource.DefaultCopyNoDrop, transferable, dsListener);
				} catch (InvalidDnDOperationException e) {
					System.out.println(e);
				}
			}
		};
		this.dsListener = new DragSourceListener() {

			@Override
			public void dropActionChanged(DragSourceDragEvent dsde) {
				// TODO Auto-generated method stub

			}

			@Override
			public void dragOver(DragSourceDragEvent dsde) {
				// TODO Auto-generated method stub

			}

			@Override
			public void dragExit(DragSourceEvent dse) {
				// TODO Auto-generated method stub

			}

			@Override
			public void dragEnter(DragSourceDragEvent dsde) {
				DragSourceContext context = dsde.getDragSourceContext();
				int myaction = dsde.getDropAction();
				if ((myaction & DnDConstants.ACTION_COPY) != 0) {
					context.setCursor(DragSource.DefaultCopyDrop);
				} else {
					context.setCursor(DragSource.DefaultCopyNoDrop);
				}
			}

			@Override
			public void dragDropEnd(DragSourceDropEvent dsde) {
				if (dsde.getDropSuccess() == false) {
					return;
				}
				int dropAction = dsde.getDropAction();
				isDropped = true;
				if (dropAction == DnDConstants.ACTION_MOVE) {
				}
			}
		};
		this.dragSource.createDefaultDragGestureRecognizer(this, DnDConstants.ACTION_COPY_OR_MOVE, this.dgListener);
		
		addMouseListener(new MouseAdapter() {
			public void mouseClicked(MouseEvent me) {
				if (isDropped) {
					targetDropPanel.remove(DragableLabel.this);					
				} else {
					DragableLabel label = new DragableLabel(DragableLabel.this.getText(), targetDropPanel);
					Border border = new LineBorder(Color.GRAY);
					label.setHorizontalAlignment(JLabel.CENTER);
					label.setBorder(border);
			//		label.setPreferredSize(new Dimension(30,  30));
					label.setIsDropped(true);
					targetDropPanel.add(label);
				}
				targetDropPanel.revalidate();
				targetDropPanel.repaint();
			}
		});
	}
}
