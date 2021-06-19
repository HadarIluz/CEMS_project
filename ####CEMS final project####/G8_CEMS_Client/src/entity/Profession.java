package entity;

import java.io.Serializable;

/**
 * Entity class - define Profession in the CEMS system.
 * The department and all its fields were included in the diagrams.
 * The class is used to store data for teaches`s professions.
 * @author Hadar Iluz
 *
 */
@SuppressWarnings("serial")
public class Profession implements Serializable {
	private String professionID;
	private String professionName;
	/* constructor */
	public Profession(String professionID) {
		this.professionID = professionID;
	}
	/* constructor */
	public Profession(String professionID, String professionName) {
		this.professionID = professionID;
		this.professionName = professionName;
	}
	/*get ProfessionID field*/
	public String getProfessionID() {
		return professionID;
	}
	/*set ProfessionID field*/
	public void setProfessionID(String professionID) {
		this.professionID = professionID;
	}
	/*get ProfessionName field*/
	public String getProfessionName() {
		return professionName;
	}
	/*set ProfessionName field*/
	public void setProfessionName(String professionName) {
		this.professionName = professionName;
	}
}
