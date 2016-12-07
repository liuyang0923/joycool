package net.joycool.wap.spec.bottle;

public class BottleReply {

		private int 	id;			//id主键
		private int 	bottleId;	//瓶子id
		private int 	userId;		//浏览者id
		private long 	viewTime;	//浏览的时间
		private String 	reply;		//留言内容
		
		public int getId() {
			return id;
		}
		public void setId(int id) {
			this.id = id;
		}
		public int getBottleId() {
			return bottleId;
		}
		public void setBottleId(int bottleId) {
			this.bottleId = bottleId;
		}
		public int getUserId() {
			return userId;
		}
		public void setUserId(int userId) {
			this.userId = userId;
		}
		public long getViewTime() {
			return viewTime;
		}
		public void setViewTime(long viewTime) {
			this.viewTime = viewTime;
		}
		public String getReply() {
			return reply;
		}
		public void setReply(String reply) {
			this.reply = reply;
		}
		

		
}
