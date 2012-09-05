package org.cts.chess.txtchess.gae.db;

import java.sql.Date;
import javax.jdo.annotations.IdGeneratorStrategy;
import javax.jdo.annotations.PersistenceCapable;
import javax.jdo.annotations.IdentityType;
import javax.jdo.annotations.Persistent;
import javax.jdo.annotations.PrimaryKey;

@PersistenceCapable(identityType = IdentityType.APPLICATION)
public class Challenge {
@PrimaryKey
@Persistent(valueStrategy=IdGeneratorStrategy.IDENTITY)
private Long id;

@Persistent
private String createdBy;

@Persistent
private String opponent;

@Persistent
private String creatorColor;

@Persistent
private int min_rating;

@Persistent 
private int max_rating;

@Persistent
private Date createdOn;


public Challenge(String createdBy, String opponent,
		int creatorColor, int min_rating, int max_rating, Date createdOn) {
	this.createdBy = createdBy;
	this.opponent = opponent;
	this.creatorColor = creatorColor==1?"white":"black";
	this.min_rating = Math.min(min_rating,max_rating);
	this.max_rating = Math.max(min_rating,max_rating);
	this.createdOn = createdOn;
}



public String getCreatedBy() {
	return createdBy;
}



public void setCreatedBy(String createdBy) {
	this.createdBy = createdBy;
}



public String getOpponent() {
	return opponent;
}



public void setOpponent(String opponent) {
	this.opponent = opponent;
}



public String getCreatorColor() {
	return creatorColor;
}



public void setCreatorColor(String creatorColor) {
	this.creatorColor = creatorColor;
}



public int getMin_rating() {
	return min_rating;
}



public void setMin_rating(int min_rating) {
	this.min_rating = min_rating;
}



public int getMax_rating() {
	return max_rating;
}



public void setMax_rating(int max_rating) {
	this.max_rating = max_rating;
}



public Date getCreatedOn() {
	return createdOn;
}



public void setCreatedOn(Date createdOn) {
	this.createdOn = createdOn;
}



public Long getId() {
	return id;
}



public String getOpponentColor()
{
	return getCreatorColor()=="white"?"Black":"White";
}
}
