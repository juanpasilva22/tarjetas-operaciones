package com.operation.creditcards.app.utils;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Clase que realiza validaciones en Persona y
 * tarjetas.
 */
public class Validations {
	/**
	 * Validar Nombres y Apellidos.
	 *
	 * @param name
	 * @return boolean
	 */
	public static boolean validateNameOrLastName(String name) {
		return name.matches("[A-Z][a-z]*");
	}

	/**
	 * Validar que el email tenga formato correcto.
	 *
	 * @param email
	 * @return boolean
	 */
	public static boolean validateEmail(String email) {
		final String regex = "^(.+)@(.+)$";
		Pattern pattern = Pattern.compile(regex);
		//searching for occurrences of regex
		Matcher matcher = pattern.matcher(email);
		System.out.println("The Email address " + email + " is " + (matcher.matches() ? "valid" : "invalid"));
		return matcher.matches();
	}

	/**
	 * Validacion del codigo cvv de tarjeta
	 * y tambien el numero de DNI.
	 * @param cvv
	 * @return boolean
	 */
	public boolean validateCvv(String cvv) {
		// validamos que el cvv sea numerico y que
		// la longitud del codigo sea 3 digitos.
		return isNumeric(cvv) && cvv.length() == 3;
	}

	/**
	 * Validacion del numero de DNI.
	 * @param dni
	 * @return boolean
	 */
	public boolean validateDNI(String dni) {
		// validamos que el DNI sea numerico
		return isNumeric(dni);
	}

	/**
	 * Validar que las fechas tengan el formato dd-MM-yyyy.
	 * @param dateStr
	 * @return boolean
	 */
	public static boolean validateDateWithFormat(String dateStr) {
		final String dateFormat = "yyyy-MM-dd";
		DateFormat sdf = new SimpleDateFormat(dateFormat);
		sdf.setLenient(false);
		try {
			sdf.parse(dateStr);
		} catch (ParseException e) {
			return false;
		}
		return true;
	}

	private boolean isNumeric(String strNum) {
		if (strNum == null) {
			return false;
		}
		try {
			double d = Double.parseDouble(strNum);
		} catch (NumberFormatException nfe) {
			return false;
		}
		return true;
	}
	/**
	 * Validaciones para Tarjetas de credito
	 */
	public static boolean containsCardBrand(String inputString) {
		return "VISA".equals(inputString) || "NARA".equals(inputString) ||
				"AMEX".equals(inputString) ? true : false;
	}

	/**
	 * Validar el numero de tarjeta de credito
	 * @param cardNumber
	 * @return boolean
	 */
	public static boolean isValidCreditCard(String cardNumber) {
		int sum = 0;
		boolean alternate = false;

		// Iterate from right to left
		for (int i = cardNumber.length() - 1; i >= 0; i--) {
			int n = Integer.parseInt(cardNumber.substring(i, i + 1));

			// Double every second digit
			if (alternate) {
				n *= 2;
				// If the result is greater than 9, subtract 9
				if (n > 9) {
					n -= 9;
				}
			}

			sum += n;
			alternate = !alternate; // Flip the alternate flag
		}

		// The number is valid if the total sum is a multiple of 10
		return (sum % 10 == 0);
	}
}
