package com.example.etl.model;

public class OutputRecord {
    private String givenName;
    private String familyName;
    private String birthDateStr;
    private int age;
    private String fullName;
	public String getGivenName() {
		return givenName;
	}
	public void setGivenName(String givenName) {
		this.givenName = givenName;
	}
	public String getFamilyName() {
		return familyName;
	}
	public void setFamilyName(String familyName) {
		this.familyName = familyName;
	}
	public String getBirthDateStr() {
		return birthDateStr;
	}
	public void setBirthDateStr(String birthDateStr) {
		this.birthDateStr = birthDateStr;
	}
	public int getAge() {
		return age;
	}
	public void setAge(int age) {
		this.age = age;
	}
	public String getFullName() {
		return fullName;
	}
	public void setFullName(String fullName) {
		this.fullName = fullName;
	}
}
