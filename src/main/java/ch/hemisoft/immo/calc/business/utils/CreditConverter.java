package ch.hemisoft.immo.calc.business.utils;

import ch.hemisoft.immo.domain.ActiveCredit;
import ch.hemisoft.immo.domain.Credit;
import ch.hemisoft.immo.domain.NotActiveCredit;
import ch.hemisoft.immo.domain.Property;

public class CreditConverter {
	public static NotActiveCredit convert(ActiveCredit creditToConvert) {
		Credit tempCredit = creditToConvert;
		Property property = tempCredit.getProperty();
		NotActiveCredit convertedCredit = new NotActiveCredit(property);
		convertedCredit.setInterestRateNominalInPercent(tempCredit.getInterestRateNominalInPercent());
		convertedCredit.setNameOfInstitution(tempCredit.getNameOfInstitution());
		convertedCredit.setRedemptionAtBeginInPercent(tempCredit.getRedemptionAtBeginInPercent());
		convertedCredit.setSpecialRedemptionEachYearInPercent(tempCredit.getSpecialRedemptionEachYearInPercent());
		return convertedCredit;
	}
	
	public static ActiveCredit convert(NotActiveCredit creditToConvert) {
		Credit tempCredit = creditToConvert;
		Property property = tempCredit.getProperty();
		ActiveCredit convertedCredit = new ActiveCredit(property);
		convertedCredit.setInterestRateNominalInPercent(tempCredit.getInterestRateNominalInPercent());
		convertedCredit.setNameOfInstitution(tempCredit.getNameOfInstitution());
		convertedCredit.setRedemptionAtBeginInPercent(tempCredit.getRedemptionAtBeginInPercent());
		convertedCredit.setSpecialRedemptionEachYearInPercent(tempCredit.getSpecialRedemptionEachYearInPercent());
		return convertedCredit;
	}
}
