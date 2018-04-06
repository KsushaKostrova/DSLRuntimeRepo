package my.project;

import java.awt.datatransfer.Clipboard;
import java.awt.datatransfer.ClipboardOwner;
import java.awt.datatransfer.DataFlavor;
import java.awt.datatransfer.Transferable;
import java.awt.datatransfer.UnsupportedFlavorException;
import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.util.Arrays;
import java.util.List;

public class StringTransferable implements Transferable, ClipboardOwner{

	private String string;
	public static final DataFlavor plainTextFlavor = DataFlavor.plainTextFlavor;
	public static final DataFlavor localStringFlavor = DataFlavor.stringFlavor;
	
	public static final DataFlavor[] flavors = {
			StringTransferable.plainTextFlavor,
			StringTransferable.localStringFlavor
	};
	
	public StringTransferable(String text) {
		this.string = text;
	}
	
	private static final List<DataFlavor> flavorList = Arrays.asList(flavors);
	
	public synchronized DataFlavor[] getTransferDataFlavors() {
		return flavors;
	}
	
	public boolean isDataFlavorSupported(DataFlavor flavor) {
		System.out.println(flavor.toString());
		return (flavorList.contains(flavor));
	}
	
	@Override
	public void lostOwnership(Clipboard clipboard, Transferable contents) {		
	}

	@Override
	public Object getTransferData(DataFlavor flavor) throws UnsupportedFlavorException, IOException {
		if (flavor.equals(StringTransferable.plainTextFlavor)) {
			String charset = flavor.getParameter("charset").trim();
			if (charset.equalsIgnoreCase("unicode")) {
				return new ByteArrayInputStream(this.string.getBytes("Unicode"));
			} else {
				return new ByteArrayInputStream(this.string.getBytes("iso8859-1"));
			}
		} else if (StringTransferable.localStringFlavor.equals(flavor)) {
			return this.string;
		} else {
			throw new UnsupportedFlavorException(flavor);
		}
	}
}
