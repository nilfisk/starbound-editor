package de.perdoctus.starbound.base;

import de.perdoctus.starbound.types.base.Asset;
import javafx.scene.Node;
import javafx.scene.control.Label;
import javafx.scene.control.ListCell;
import javafx.scene.layout.Pane;
import javafx.scene.layout.VBox;

/**
 * @author Christoph Giesche
 */
public class AssetListCell extends ListCell<Asset> {
	@Override
	protected void updateItem(final Asset item, final boolean empty) {
		super.updateItem(item, empty);
		if (!empty) {
			setGraphic(new AssetTitlePane(item));
		}
	}

	private static class AssetTitlePane extends VBox {

		public AssetTitlePane(final Asset asset) {
			super();
			final Label e = new Label(asset.getClass().getSimpleName());
			e.setStyle("-fx-font-weight: bold");
			getChildren().add(e);
			getChildren().add(new Label(asset.assetTitle()));
		}

	}
}
