package de.perdoctus.starbound.types.objecteditor.object;

import de.perdoctus.starbound.types.common.Rarity;

import java.util.List;

/**
 * @author Christoph Giesche
 */
public class ObjectDefinition {
	private String objectName;
	private Rarity rarity;
	private Category category;
	private int price;
	private String description;
	private String shortdescription;
	private Race race;
	private String apexDescription;
	private String avianDescription;
	private String floranDescription;
	private String glitchDescription;
	private String humanDescription;
	private String hylotlDescription;
	private List<Orientation> orientations;

	public String getObjectName() {
		return objectName;
	}

	public void setObjectName(final String objectName) {
		this.objectName = objectName;
	}

	public Rarity getRarity() {
		return rarity;
	}

	public void setRarity(final Rarity rarity) {
		this.rarity = rarity;
	}

	public int getPrice() {
		return price;
	}

	public void setPrice(final int price) {
		this.price = price;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(final String description) {
		this.description = description;
	}

	public String getShortdescription() {
		return shortdescription;
	}

	public void setShortdescription(final String shortdescription) {
		this.shortdescription = shortdescription;
	}

	public Race getRace() {
		return race;
	}

	public void setRace(final Race race) {
		this.race = race;
	}

	public String getApexDescription() {
		return apexDescription;
	}

	public void setApexDescription(final String apexDescription) {
		this.apexDescription = apexDescription;
	}

	public String getAvianDescription() {
		return avianDescription;
	}

	public void setAvianDescription(final String avianDescription) {
		this.avianDescription = avianDescription;
	}

	public String getFloranDescription() {
		return floranDescription;
	}

	public void setFloranDescription(final String floranDescription) {
		this.floranDescription = floranDescription;
	}

	public String getGlitchDescription() {
		return glitchDescription;
	}

	public void setGlitchDescription(final String glitchDescription) {
		this.glitchDescription = glitchDescription;
	}

	public String getHumanDescription() {
		return humanDescription;
	}

	public void setHumanDescription(final String humanDescription) {
		this.humanDescription = humanDescription;
	}

	public String getHylotlDescription() {
		return hylotlDescription;
	}

	public void setHylotlDescription(final String hylotlDescription) {
		this.hylotlDescription = hylotlDescription;
	}

	public List<Orientation> getOrientations() {
		return orientations;
	}

	public void setOrientations(final List<Orientation> orientations) {
		this.orientations = orientations;
	}

	@Override
	public String toString() {
		return "ObjectDefinition{" +
				"objectName='" + objectName + '\'' +
				", rarity=" + rarity +
				", price=" + price +
				", description='" + description + '\'' +
				", shortdescription='" + shortdescription + '\'' +
				", race=" + race +
				", apexDescription='" + apexDescription + '\'' +
				", avianDescription='" + avianDescription + '\'' +
				", floranDescription='" + floranDescription + '\'' +
				", glitchDescription='" + glitchDescription + '\'' +
				", humanDescription='" + humanDescription + '\'' +
				", hylotlDescription='" + hylotlDescription + '\'' +
				", orientations=" + orientations +
				'}';
	}
}
