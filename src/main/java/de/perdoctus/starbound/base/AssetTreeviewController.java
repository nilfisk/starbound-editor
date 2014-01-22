package de.perdoctus.starbound.base;

import de.perdoctus.starbound.types.base.Asset;
import de.perdoctus.starbound.types.base.AssetOrigin;
import javafx.beans.value.ObservableValue;
import javafx.collections.ObservableList;
import javafx.event.EventHandler;
import javafx.scene.control.TreeCell;
import javafx.scene.control.TreeItem;
import javafx.scene.control.TreeView;
import javafx.scene.input.MouseEvent;
import javafx.util.Callback;

import java.util.function.Function;
import java.util.function.Predicate;

/**
 * @author Christoph Giesche
 */
public class AssetTreeviewController {

	private final AssetOriginTreeItemController coreAssetOriginTreeItemController;
	private final AssetOriginTreeItemController modAssetOriginTreeItemController;
	private Function<Asset, Void> onAssetSelected;

	public AssetTreeviewController(final TreeView<Object> assetTreeView, final ObservableList<Asset> assets) {
		final TreeView<Object> assetTreeView1 = assetTreeView;

		final TreeItem<Object> modAssetsTreeItem = new TreeItem<>("Mod Assets");
		final TreeItem<Object> coreAssetsTreeItem = new TreeItem<>("Core Assets");

		final TreeItem<Object> root = new TreeItem<>("ROOT");
		root.getChildren().add(modAssetsTreeItem);
		root.getChildren().add(coreAssetsTreeItem);

		assetTreeView1.setRoot(root);
		assetTreeView1.setCellFactory(new Callback<TreeView<Object>, TreeCell<Object>>() {
			@Override
			public TreeCell<Object> call(final TreeView<Object> param) {
				return new AssetTreeCell();
			}
		});
		assetTreeView1.setOnMouseClicked(new EventHandler<MouseEvent>() {
			@Override
			public void handle(final MouseEvent event) {
				if (event.getClickCount() == 2) {
					final TreeItem<Object> selectedItem = assetTreeView.getSelectionModel().getSelectedItem();
					if (selectedItem != null) {
						final Object selectedValue = selectedItem.getValue();
						if (selectedValue instanceof Asset) {
							fireOnAssetSelected((Asset) selectedValue);
						}
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