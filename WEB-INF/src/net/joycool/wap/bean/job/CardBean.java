/**
 * 
 */
package net.joycool.wap.bean.job;

/**
 * @author zhul_2006-07-10 新增机会卡游戏 
 * 此类记录卡的具体信息
 * 
 */
public class CardBean {

	private int id;

	private int typeId;

	private int appearRate;

	private int valueType;

	private int actionValue;

	private int actionfield;

	private int actionDirection;

	private String description;
	
	private String cardName;
	

	/**
	 * @return Returns the typeName.
	 */
	public String getCardName() {
		return cardName;
	}

	/**
	 * @param typeName The typeName to set.
	 */
	public void setCardName(String cardName) {
		this.cardName = cardName;
	}

	/**
	 * @return Returns the actionDirection.
	 */
	public int getActionDirection() {
		return actionDirection;
	}

	/**
	 * @param actionDirection
	 *            The actionDirection to set.
	 */
	public void setActionDirection(int actionDirection) {
		this.actionDirection = actionDirection;
	}

	/**
	 * @return Returns the actionfield.
	 */
	public int getActionfield() {
		return actionfield;
	}

	/**
	 * @param actionfield
	 *            The actionfield to set.
	 */
	public void setActionfield(int actionfield) {
		this.actionfield = actionfield;
	}

	/**
	 * @return Returns the actionValue.
	 */
	public int getActionValue() {
		return actionValue;
	}

	/**
	 * @param actionValue
	 *            The actionValue to set.
	 */
	public void setActionValue(int actionValue) {
		this.actionValue = actionValue;
	}

	/**
	 * @return Returns the appearRate.
	 */
	public int getAppearRate() {
		return appearRate;
	}

	/**
	 * @param appearRate
	 *            The appearRate to set.
	 */
	public void setAppearRate(int appearRate) {
		this.appearRate = appearRate;
	}

	/**
	 * @return Returns the description.
	 */
	public String getDescription() {
		return description;
	}

	/**
	 * @param description
	 *            The description to set.
	 */
	public void setDescription(String description) {
		this.description = description;
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
	 * @return Returns the typeId.
	 */
	public int getTypeId() {
		return typeId;
	}

	/**
	 * @param typeId
	 *            The typeId to set.
	 */
	public void setTypeId(int typeId) {
		this.typeId = typeId;
	}

	/**
	 * @return Returns the valueType.
	 */
	public int getValueType() {
		return valueType;
	}

	/**
	 * @param valueType
	 *            The valueType to set.
	 */
	public void setValueType(int valueType) {
		this.valueType = valueType;
	}

}
