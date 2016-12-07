package jc.credit;

import java.util.Calendar;

public class UserBase {
	public static Calendar c = Calendar.getInstance();
	int userId;
	int birYear;
	int birMonth;
	int birDay;
	int province;
	int city;
	int education;
	int personality;
	int blood;
	String photo;
	int stature;
	int birHour;
	String animals;
	int astro;
	int point;
	int aim;
	int gender;
	
	public int getGender() {
		return gender;
	}
	public String getGenderTest() {
		return gender==0?"女":"男";
	}
	public void setGender(int gender) {
		this.gender = gender;
	}
	public int getAim() {
		return aim;
	}
	public void setAim(int aim) {
		this.aim = aim;
	}
	public int getPoint() {
		return point;
	}
	public void setPoint(int point) {
		this.point = point;
	}
	public int getAstro() {
		return astro;
	}
	public void setAstro(int astro) {
		this.astro = astro;
	}
	public String getAnimals() {
		return animals;
	}
	public void setAnimals(String animals) {
		this.animals = animals;
	}
	public int getBirHour() {
		return birHour;
	}
	public void setBirHour(int birHour) {
		this.birHour = birHour;
	}
	public int getUserId() {
		return userId;
	}
	public void setUserId(int userId) {
		this.userId = userId;
	}
	public int getBirYear() {
		return birYear;
	}
	public void setBirYear(int birYear) {
		this.birYear = birYear;
	}
	public int getBirMonth() {
		return birMonth;
	}
	public void setBirMonth(int birMonth) {
		this.birMonth = birMonth;
	}
	public int getBirDay() {
		return birDay;
	}
	public void setBirDay(int birDay) {
		this.birDay = birDay;
	}
	public int getProvince() {
		return province;
	}
	public void setProvince(int province) {
		this.province = province;
	}
	public int getCity() {
		return city;
	}
	public void setCity(int city) {
		this.city = city;
	}
	public int getEducation() {
		return education;
	}
	public void setEducation(int education) {
		this.education = education;
	}
	public int getPersonality() {
		return personality;
	}
	public void setPersonality(int personality) {
		this.personality = personality;
	}
	public int getBlood() {
		return blood;
	}
	public void setBlood(int blood) {
		this.blood = blood;
	}
	public String getPhoto() {
		return photo;
	}
	public void setPhoto(String photo) {
		this.photo = photo;
	}
	public int getStature() {
		return stature;
	}
	public void setStature(int stature) {
		this.stature = stature;
	}
	public int getAge(){
		if (this.birYear == 0){
			return 0;
		}
		c.setTimeInMillis(System.currentTimeMillis());
		return c.get(c.YEAR) - this.birYear;
	}
}
