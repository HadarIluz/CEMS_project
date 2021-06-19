package common;

import java.io.Serializable;

/**
 * The department defines all the parameters required for sending and
 * transferring a file between client and server.
 * The department was not included
 * in the diagrams and was added to support a file download mechanism and upload
 * a student test solution file. Upload a file when creating a manual test by
 * the teacher.
 * 
 * @author Matar Asaf
 *
 */
public class MyFile implements Serializable {

	private static final long serialVersionUID = 1L;
	private String Description = null;
	private String fileName = null;
	private int size = 0;
	public byte[] mybytearray;


	/**
	 * @param fileName for constructor.
	 */
	public MyFile(String fileName) {
		this.fileName = fileName;
	}
	
	/**
	 * @param size of file.
	 */
	public void initArray(int size) {
		mybytearray = new byte[size];
	}
	
	/* get fileName field */
	public String getFileName() {
		return fileName;
	}
	
	/* set fileName field */
	public void setFileName(String fileName) {
		this.fileName = fileName;
	}
	
	/* get size field */
	public int getSize() {
		return size;
	}
	
	/* set size field */
	public void setSize(int size) {
		this.size = size;
	}
	
	/* get array of bytes mybytearray field */
	public byte[] getMybytearray() {
		return mybytearray;
	}
	
	/* get the byte in index i in an array of bytes mybytearray */
	public byte getMybytearray(int i) {
		return mybytearray[i];
	}
	
	/* set array of bytes mybytearray field */
	public void setMybytearray(byte[] mybytearray) {
		for (int i = 0; i < mybytearray.length; i++)
			this.mybytearray[i] = mybytearray[i];
	}

	/* get Description field */
	public String getDescription() {
		return Description;
	}
	
	/* set Description field */
	public void setDescription(String description) {
		Description = description;
	}
}
