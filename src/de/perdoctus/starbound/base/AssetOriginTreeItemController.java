package de.perdoctus.starbound.base;

import de.perdoctus.starbound.types.base.Asset;
import de.perdoctus.starbound.types.base.AssetGroup;
import de.perdoctus.starbound.types.base.AssetType;
import javafx.collections.ListChangeListener;
import javafx.collections.ObservableList;
import javafx.scene.control.TreeItem;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;

import java.util.HashMap;
import java.util.Map;
import java.util.logging.Logger;

/**
 * @author Christoph Giesche
 */
public class AssetOriginTreeItemController {

	private static final Logger LOGGER = Logger.getLogger(AssetOriginTreeItemController.class.getName());
	private final TreeItem<Object> assetsRootItem;
	private final ObservableList<Asset> assets;
	private final Map<AssetGroup, TreeItem<Object>> assetGroupNodes = new HashMap<>();
	private final Map<AssetType, TreeItem<Object>> assetTypeNodes = new HashMap<>();
	private final Map<Asset, TreeItem<Object>> assetEntryNodes = new HashMap<>();
	private final Map<AssetGroup, ImageView> iconCache = new HashMap<>();

	public AssetOriginTreeItemController(final TreeItem<Object> assetsRootItem, final ObservableList<Asset> assets) {
		this.assetsRootItem = assetsRootItem;
		this.assets = assets;

		this.assets.addListener((ListChangeListener.Change<? extends Asset> change) -> {
			while (change.next()) {
				if (change.wasAdded()) {
					LOGGER.fine(change.getAddedSize() + " Assets were added to List");
					for (Asset addedAsset : change.getAddedSubList()) {
						assetAdded(addedAsset);
					}
				}
				if (change.wasRemoved()) {
					LOGGER.fine(change.getRemovedSize() + " Assets were removed from List");
					for (Asset removedAsset : change.getRemoved()) {
						assetRemoved(removedAsset);
					}
				}
			}
		});
	}

	private void assetAdded(final Asset asset) {
		final AssetType assetType = asset.getAssetType();
		final AssetGroup assetGroup = assetType.getAssetGroup();

		// Add AssetGroup group if necessary
		if (!assetGroupNodes.containsKey(assetGroup)) {
			final TreeItem<Object> assetGroupNode = new TreeItem<>(assetGroup, getIconForGroup(assetGroup));
			assetsRootItem.getChildren().add(assetGroupNode);
			assetGroupNodes.put(assetGroup, assetGroupNode);
		}
		final TreeItem<Object> assetGroupNode = assetGroupNodes.get(assetGroup);

		// Add AssetType group if necessary
		if (!assetTypeNodes.containsKey(assetType)) {
			final TreeItem<Object> assetTypeNode = new TreeItem<>(assetType.getAssetTitle());
			assetGroupNode.getChildren().add(assetTypeNode);
			assetTypeNodes.put(assetType, assetTypeNode);
		}
		final TreeItem<Object> assetTypeNode = assetTypeNodes.get(assetType);

		// Add asset entry to group
		final TreeItem<Object> assetEntryNode = new TreeItem<>();
		assetEntryNode.setValue(asset);
		assetEntryNodes.put(asset, assetEntryNode);
		assetTypeNode.getChildren().add(assetEntryNode);
	}

	private void assetRemoved(final Asset asset) {
		final TreeItem<Object> assetEntryNode = assetEntryNodes.remove(asset);

		final TreeItem<Object> assetTypeNode = assetTypeNodes.get(asset.getAssetType());
		assetTypeNode.getChildren().remove(assetEntryNode);

		if (assetTypeNode.getChildren().isEmpty()) {
			final TreeItem<Object> assetGroupNode = assetGroupNodes.get(asset.getAssetType().getAssetGroup());
			assetGroupNode.getChildren().remove(assetTypeNode);
			assetTypeNodes.remove(asset.getAssetType());

			if (assetGroupNode.getChildren().isEmpty()) {
				assetsRootItem.getChildren().remove(assetGroupNode);
				assetGroupNodes.remove(asset.getAssetType().getAssetGroup());
			}
		}

	}

	private ImageView getIconForGroup(final AssetGroup assetGroup) {
		if (!iconCache.containsKey(assetGroup)) {
			if (assetGroup.getIcon() != null && !assetGroup.getIcon().isEmpty()) {
				iconCache.put(assetGroup, new ImageView(new Image(getClass().getResourceAsStream(assetGroup.getIcon()))));
			}
		}
		return iconCache.get(assetGroup);
	}

	private void refreshView(final ObservableList<Asset> viewAssets) {
		assetsRootItem.getChildren().clear();
		assetEntryNodes.clear();
		assetTypeNodes.clear();
		assetGroupNodes.clear();

		for (final Asset viewAsset : viewAssets) {
			assetAdded(viewAsset);
		}

		assetsRootItem.setExpanded(true);
		assetsRootItem.getChildren().forEach(treeItem -> treeItem.setExpanded(true));
	}

	public void filter(final String text) {
		if (text != null && text.length() > 0) {
			refreshView(assets.filtered(asset -> asset.assetTitleProperty().getValue().contains(text)));
		} else {
			refreshView(assets);
		}
	}

}
