package ch.hemisoft.immo.domain;

public interface Ownable {
	User getOwner();
	void setOwner(User owner);
}
