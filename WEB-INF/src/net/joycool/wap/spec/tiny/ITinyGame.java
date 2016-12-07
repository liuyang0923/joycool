package net.joycool.wap.spec.tiny;

public interface ITinyGame {
	public int getGameId();
	public String getName();
	public String getGameURL();
	public void init();
	public TinyGame copy();
}
