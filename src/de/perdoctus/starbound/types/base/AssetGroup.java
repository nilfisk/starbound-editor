package de.perdoctus.starbound.types.base;

/**
 * @author Christoph Giesche
 */
public class AssetGroup {

	private String title;
	private String icon;

	public String getTitle() {

		return title;
	}

	public void setTitle(final String title) {
		this.title = title;
	}

	public String getIcon() {
		return icon;
	}

	public void setIcon(final String icon) {
		this.icon = icon;
	}

	@Override
	public boolean equals(final Object o) {
		if (this == o) return true;
		if (o == null || getClass() != o.getClass()) return false;

		final AssetGroup that = (AssetGroup) o;

		if (icon != null ? !icon.equals(that.icon) : that.icon != null) return false;
		if (!title.equals(that.title)) return false;

		return true;
	}

	@Override
	public int hashCode() {
		int result = title.hashCode();
		result = 31 * result + (icon != null ? icon.hashCode() : 0);
		return result;
	}

	@Override
	public String toString() {
		return title;
	}
}
