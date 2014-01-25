package de.perdoctus.starbound.types.codex;

import com.sun.javafx.binding.StringConstant;
import de.perdoctus.starbound.types.base.Asset;
import de.perdoctus.starbound.types.common.Rarity;
import javafx.beans.binding.Bindings;
import javafx.beans.binding.StringExpression;

/**
 * @author Christoph Giesche
 */
public class CodexItem extends Asset {
	private String codexId;
	private String title;
	private String inventoryIcon;
	private CodexKind codexkind;
	private String itemName;
	private Rarity rarity;
	private String description;
	private String shortdescription;

	public CodexItem() {
	}

	public CodexItem(final String codexId, final String itemName) {
		this.codexId = codexId;
		this.itemName = itemName;
	}

	public String getCodexId() {
		return codexId;
	}

	public void setCodexId(final String codexId) {
		this.codexId = codexId;
	}

	public String getTitle() {
		return title;
	}

	public void setTitle(final String title) {
		this.title = title;
	}

	public String getInventoryIcon() {
		return inventoryIcon;
	}

	public void setInventoryIcon(final String inventoryIcon) {
		this.inventoryIcon = inventoryIcon;
	}

	public CodexKind getCodexkind() {
		return codexkind;
	}

	public void setCodexkind(final CodexKind codexkind) {
		this.codexkind = codexkind;
	}

	public String getItemName() {
		return itemName;
	}

	public void setItemName(final String itemName) {
		this.itemName = itemName;
	}

	public Rarity getRarity() {
		return rarity;
	}

	public void setRarity(final Rarity rarity) {
		this.rarity = rarity;
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

	@Override
	public String toString() {
		return "CodexItem{" +
				"codexId='" + codexId + '\'' +
				", title='" + title + '\'' +
				", inventoryIcon='" + inventoryIcon + '\'' +
				", codexkind=" + codexkind +
				", itemName='" + itemName + '\'' +
				", rarity=" + rarity +
				", description='" + description + '\'' +
				", shortdescription='" + shortdescription + '\'' +
				'}';
	}

	@Override
	public StringExpression assetTitleProperty() {
		return Bindings.concat(title);
	}

	@Override
	public StringExpression assetIdProperty() {
		return StringConstant.valueOf(codexId);
	}

	@Override
	public StringExpression iconImageProperty() {
		return null;
	}
}
