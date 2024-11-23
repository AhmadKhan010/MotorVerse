package utils;

import java.util.Map;

public class PremiumCalculator {

	
		private static Map<String, Float> insuranceRates = Map.of(
	        "Collision", 10.0f,
	        "PIP", 7.0f,
	        "Engine Protection", 5.0f,
	        "None", 0.0f
	    );

		public static double calculatePremium(double rentalPrice, int days, String insuranceType) {
        float insuranceRate = insuranceRates.get(insuranceType);
        return insuranceRate * days + (0.1*rentalPrice);
		}
		// Calculate premium without rent days for buying a car:
		public static double calculatePremium(double Price, String insuranceType) {
			float insuranceRate = insuranceRates.get(insuranceType);
			return 0.1 * Price + insuranceRate;
		}
	
	//rentalPrice * days * (1 + insuranceRate / 100);
}
