package de.perdoctus.starbound.base;

import de.perdoctus.starbound.types.base.Asset;
import de.perdoctus.starbound.types.base.AssetOrigin;
import javafx.collections.ObservableList;
import javafx.scene.control.TreeItem;
import javafx.scene.control.TreeView;

import java.util.function.Function;

/**
 * @author Christoph Giesche
 */
public class AssetTreeviewController {

	private final AssetOriginTreeItemController coreAssetOriginTreeItemController;
	private final AssetOriginTreeItemController modAssetOriginTreeItemController;
	private Function<Asset, Void> onAssetSelected;

	public AssetTreeviewController(final TreeView<Object> assetTreeView, final ObservableList<Asset> assets) {

		final TreeItem<Object> modAssetsTreeItem = new TreeItem<>("Mod Assets");
		final TreeItem<Object> coreAssetsTreeItem = new TreeItem<>("Core Assets");

		final TreeItem<Object> root = new TreeItem<>("ROOT");
		root.getChildren().add(modAssetsTreeItem);
		root.getChildren().add(coreAssetsTreeItem);

		assetTreeView.setRoot(root);
		assetTreeView.setCellFactory(param -> new AssetTreeCell());
		assetTreeView.setOnMouseClicked(event -> {
			if (event.getClickCount() == 2) {
				final TreeItem<Object> selectedItem = assetTreeView.getSelectionModel().getSelectedItem();
				if (selectedItem != null) {
					final Object selectedValue = selectedItem.getValue();
					if (selectedValue instanceof Asset) {
						fireOnAssetSelected((Asset) selectedValue);
					}
				}
			}
		});

		coreAssetOriginTreeItemController = new AssetOriginTreeItemController(coreAssetsTreeItem, assets.filtered(asset -> asset.getAssetOrigin() == AssetOrigin.CORE));
		modAssetOriginTreeItemController = new AssetOriginTreeItemController(modAssetsTreeItem, assets.filtered(asset -> asset.getAssetOrigin() == AssetOrigin.MOD));

	}

	public void setOnAssetSelected(final Function<Asset, Void> onAssetSelected) {
		this.onAssetSelected = onAssetSelected;
	}

	private void fireOnAssetSelected(final Asset selectedItem) {
		if (onAssetSelected != null) {
			onAssetSelected.apply(selectedItem);
		}
	}

	public void filter(final String text) {
		coreAssetOriginTreeItemController.filter(text);
		modAssetOriginTreeItemController.filter(text);
	}
}
