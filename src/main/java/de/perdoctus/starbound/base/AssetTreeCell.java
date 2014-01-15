package de.perdoctus.starbound.base;

import de.perdoctus.starbound.types.base.Asset;
import javafx.scene.control.Label;
import javafx.scene.control.TreeCell;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;

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
				setGraphic(getTreeItem().getGraphic());
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

			final VBox vBox = new VBox();

			final Label e = new Label(asset.getAssetType().getAssetTitle());
			e.setStyle("-fx-font-weight: bold");
			vBox.getChildren().add(e);
			final Label titleLabel = new Label();
			titleLabel.textProperty().bind(asset.assetTitleProperty());
			vBox.getChildren().add(titleLabel);

			getChildren().add(vBox);

		}

	}
}
