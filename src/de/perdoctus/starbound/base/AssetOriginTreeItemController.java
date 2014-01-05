package de.perdoctus.starbound.base;

import de.perdoctus.starbound.types.base.Asset;
import de.perdoctus.starbound.types.base.AssetGroup;
import de.perdoctus.starbound.types.base.AssetType;
import javafx.collections.ListChangeListener;
import javafx.collections.ObservableList;
import javafx.scene.control.TreeItem;

import java.util.HashMap;
import java.util.Map;
import java.util.function.Predicate;
import java.util.logging.Logger;

/**
 * @author Christoph Giesche
 */
public class AssetOriginTreeItemController {

	private static final Logger LOGGER = Logger.getLogger(AssetOriginTreeItemController.class.getName());
	private final TreeItem<Object> assetsRootItem;
	private ObservableList<Asset> assets;
	private Map<AssetGroup, TreeItem<Object>> assetGroupNodes = new HashMap<>();
	private Map<AssetType, TreeItem<Object>> assetTypeNodes = new HashMap<>();
	private Map<Asset, TreeItem<Object>> assetEntryNodes = new HashMap<>();

	public AssetOriginTreeItemController(final TreeItem<Object> assetsRootItem) {
		this.assetsRootItem = assetsRootItem;
	}

	private void assetAdded(final Asset asset) {
		final AssetType assetType = asset.getAssetType();
		final AssetGroup assetGroup = assetType.getAssetGroup();

		// Add AssetGroup group if necessary
		if (!assetGroupNodes.containsKey(assetGroup)) {
			final TreeItem<Object> assetGroupNode = new TreeItem<>(assetGroup.name());
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
		final TreeItem assetTypeNode = assetTypeNodes.get(assetType);

		// Add asset entry to group
		final TreeItem<Object> assetEntryNode = new TreeItem<>();
		assetEntryNode.setValue(asset);
		assetEntryNodes.put(asset, assetEntryNode);
		assetTypeNode.getChildren().add(assetEntryNode);
	}

	private void assetRemoved(final Asset asset) {
		final TreeItem<Object> assetEntryNode = assetEntryNodes.get(asset);
		final TreeItem<Object> assetGroupNode = assetGroupNodes.get(asset.getAssetType().getAssetGroup());
		assetEntryNodes.remove(asset);
		assetGroupNode.getChildren().remove(assetEntryNode);

		final AssetGroup assetGroup = asset.getAssetType().getAssetGroup();
		if (assetGroupNode.getChildren().size() == 0) {
			assetsRootItem.getChildren().remove(assetGroupNode);
			assetGroupNodes.remove(assetGroup);
		}
	}

	public ObservableList<Asset> getAssets() {
		return assets;
	}

	public void setAssets(final ObservableList<Asset> assets) {
		this.assets = assets;

		this.assets.addListener(new ListChangeListener<Asset>() {
			@Override
			public void onChanged(final Change<? extends Asset> change) {
				while (change.next()) {
					if (change.wasAdded()) {
						LOGGER.info(change.getAddedSize() + " Assets were added to List");
						for (Asset addedAsset : change.getAddedSubList()) {
							assetAdded(addedAsset);
						}
					}
					if (change.wasRemoved()) {
						LOGGER.info(change.getRemovedSize() + " Assets were removed from List");
						for (Asset removedAsset : change.getRemoved()) {
							assetRemoved(removedAsset);
						}
					}
				}
			}
		});
	}

	public void refreshView() {
		refreshView(this.assets);
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
