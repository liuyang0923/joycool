package net.joycool.wap.bean.video;

public class VideoFileBean {
	int id;

	int videoId;

	String file;

	String fileType;

	int size;

	/**
	 * @return Returns the file.
	 */
	public String getFile() {
		return file;
	}

	/**
	 * @param file
	 *            The file to set.
	 */
	public void setFile(String file) {
		this.file = file;
	}

	/**
	 * @return Returns the fileType.
	 */
	public String getFileType() {
		return fileType;
	}

	/**
	 * @param fileType
	 *            The fileType to set.
	 */
	public void setFileType(String fileType) {
		this.fileType = fileType;
	}

	/**
	 * @return Returns the id.
	 */
	public int getId() {
		return id;
	}

	/**
	 * @param id
	 *            The id to set.
	 */
	public void setId(int id) {
		this.id = id;
	}

	/**
	 * @return Returns the size.
	 */
	public int getSize() {
		return size;
	}

	/**
	 * @param size
	 *            The size to set.
	 */
	public void setSize(int size) {
		this.size = size;
	}

	/**
	 * @return Returns the videoId.
	 */
	public int getVideoId() {
		return videoId;
	}

	/**
	 * @param videoId
	 *            The videoId to set.
	 */
	public void setVideoId(int videoId) {
		this.videoId = videoId;
	}

}
