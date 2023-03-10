package com.luan.beerstock.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum BeerType {

	LAGER("Lager"), MALZBIER("Malzbier"), WITBIER("Witbier"), WEISS("Weiss"), ALE("Ale"), IPA("IPA"), STOUT("Stout"),
	PILSEN("Pilsen");

	private final String description;

}
