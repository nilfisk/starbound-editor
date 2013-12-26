package de.perdoctus.starbound.base;

import de.perdoctus.starbound.types.base.Asset;
import de.perdoctus.starbound.types.base.AssetGroup;
import javafx.collections.ListChangeListener;
import javafx.collections.ObservableList;
import javafx.collections.transformation.FilteredList;
import javafx.event.EventHandler;
import javafx.scene.control.Accordion;
import javafx.scene.control.ListCell;
import javafx.scene.control.ListView;
import javafx.scene.control.TitledPane;
import javafx.scene.input.MouseEvent;
import javafx.util.Callback;

import java.text.Collator;
import java.util.HashMap;
import java.util.Map;
import java.util.function.Function;
import java.util.logging.Logger;

/**
 * @author Christoph Giesche
 */
public class AssetAccordionCtrl {


	private static final Logger LOGGER = Logger.getLogger(AssetAccordionCtrl.class.getName());
	private final Accordion accAssetList;
	private final ObservableList<Asset> assets;
	private Map<AssetGroup, TitledPane> groupPanes = new HashMap<>();
	private Map<AssetGroup, ListView<Asset>> groupListViews = new HashMap<>();
	private Function<Asset, Void> onAssetSelected;

	public AssetAccordionCtrl(final Accordion accAssetList, final ObservableList<Asset> assets) {
		this.accAssetList = accAssetList;
		this.assets = assets;

		assets.addListener(new ListChangeListener<Asset>() {
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

	private void assetAdded(final Asset asset) {
		final AssetGroup assetGroup = asset.getAssetType().getAssetGroup();
		if (!groupPanes.containsKey(assetGroup)) {
			final TitledPane assetGroupTitledPane = createGroupPane(assetGroup);
			groupPanes.put(assetGroup, assetGroupTitledPane);
			final ListView<Asset> assetGroupListView = createGroupListView(assetGroup);
			groupListViews.put(assetGroup, assetGroupListView);
			assetGroupTitledPane.setContent(assetGroupListView);
			accAssetList.getPanes().add(assetGroupTitledPane);
		}
	}

	private void assetRemoved(final Asset asset) {
		final AssetGroup assetGroup = asset.getAssetType().getAssetGroup();
		final TitledPane titledPane = groupPanes.get(assetGroup);
		final ListView<Asset> listView = (ListView<Asset>) titledPane.getContent();
		if (listView.getItems().isEmpty()) {
			accAssetList.getPanes().remove(titledPane);
			groupPanes.remove(assetGroup);
			groupListViews.remove(assetGroup);
		}
	}

	private ListView<Asset> createGroupListView(final AssetGroup assetGroup) {
		final ListView<Asset> assetGroupList = new ListView<>();
		final ObservableList<Asset> filteredAssets = assets
				.filtered(asset -> assetGroup == asset.getAssetType().getAssetGroup())
				.sorted((asset1, asset2) -> Collator.getInstance().compare(asset1.assetTitleProperty().get(), asset2.assetTitleProperty().get()));

		assetGroupList.setItems(filteredAssets);
		assetGroupList.setCellFactory(new Callback<ListView<Asset>, ListCell<Asset>>() {
			@Override
			public ListCell<Asset> call(final ListView<Asset> param) {
				return new AssetListCell();
			}
		});
		assetGroupList.setOnMouseClicked(new EventHandler<MouseEvent>() {
			@Override
			public void handle(final MouseEvent event) {
				if (event.getClickCount() == 2) {
					fireOnAssetSelected(assetGroupList.getSelectionModel().getSelectedItem());
				}
			}
		});

		return assetGroupList;
	}

	private TitledPane createGroupPane(final AssetGroup assetGroup) {
		final TitledPane titledPane = new TitledPane();
		titledPane.setText(assetGroup.name());
		return titledPane;
	}

	public void setOnAssetSelected(final Function<Asset, Void> onAssetSelected) {
		this.onAssetSelected = onAssetSelected;
	}

	private void fireOnAssetSelected(final Asset selectedItem) {
		if (onAssetSelected != null) {
			onAssetSelected.apply(selectedItem);
		}
	}

}
