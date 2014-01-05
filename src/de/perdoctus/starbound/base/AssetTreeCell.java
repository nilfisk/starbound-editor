package de.perdoctus.starbound.base;

import de.perdoctus.starbound.types.base.Asset;
import de.perdoctus.starbound.types.base.AssetOrigin;
import javafx.beans.property.ObjectProperty;
import javafx.scene.control.Cell;
import javafx.scene.control.Label;
import javafx.scene.control.ListCell;
import javafx.scene.control.TreeCell;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.shape.Rectangle;

/**
 * @author Christoph Giesche
 */
public class AssetTreeCell extends TreeCell<Object> {

	@Override
	protected void updateItem(final Object item, final boolean empty) {
		super.updateItem(item, empty);
		if (!empty) {
			if (item instanceof Asset) {
				setText(null);
				setGraphic(new AssetTitlePane((Asset) item));
			} else {
				setGraphic(null);
				setText(item.toString());
			}
		} else {
			setText(null);
			setGraphic(null);
		}
	}

	private static class AssetTitlePane extends HBox {

		public AssetTitlePane(final Asset asset) {
			super();
			setSpacing(5.0);
			disableProperty().bind(asset.overwrittenProperty());

			final VBox vBox = new VBox();

			final Label e = new Label(asset.getAssetType().getAssetTitle());
			e.setStyle("-fx-font-weight: bold");
			vBox.getChildren().add(e);
			final Label titleLabel = new Label();
			titleLabel.textProperty().bind(asset.assetTitleProperty());
			vBox.getChildren().add(titleLabel);

			final Rectangle marker = new Rectangle(10, 10);
			marker.setStyle("-fx-fill: darkgray");
			if (asset.getAssetOrigin() == AssetOrigin.MOD) {
				marker.setStyle("-fx-fill: #9dffaa");
			}

			getChildren().add(marker);
			getChildren().add(vBox);

		}

	}
}
