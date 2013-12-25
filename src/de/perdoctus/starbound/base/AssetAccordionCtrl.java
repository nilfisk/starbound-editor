package de.perdoctus.starbound.base;

import de.perdoctus.starbound.types.base.Asset;
import de.perdoctus.starbound.types.base.AssetGroup;
import javafx.collections.FXCollections;
import javafx.event.EventHandler;
import javafx.scene.control.*;
import javafx.scene.control.Label;
import javafx.scene.input.MouseEvent;
import javafx.util.Callback;

import java.awt.*;
import java.text.Collator;
import java.util.*;
import java.util.List;
import java.util.concurrent.Callable;
import java.util.function.Function;
import java.util.function.Predicate;

/**
 * @author Christoph Giesche
 */
public class AssetAccordionCtrl {

	private final Accordion accAssetList;
	private Map<AssetGroup, TitledPane> groupPanes = new HashMap<>();
	private Function<Asset, Void> onAssetSelected;

	public AssetAccordionCtrl(final Accordion accAssetList) {
		this.accAssetList = accAssetList;
	}

	public void updateView(final List<Asset> Assets) {
		resetView();
		createGroupPanes(Assets);
		accAssetList.getPanes().addAll(groupPanes.values());
	}

	private void resetView() {
		groupPanes.clear();
		accAssetList.getPanes().clear();
	}

	private void createGroupPanes(final List<Asset> Assets) {
		final Map<AssetGroup, List<Asset>> groupedAssets = groupAssets(Assets);
		for (Map.Entry<AssetGroup, List<Asset>> groupedAssetsEntry : groupedAssets.entrySet()) {
			final ListView<Asset> assetsList = new ListView<Asset>(FXCollections.observableArrayList(groupedAssetsEntry.getValue()));
			assetsList.setCellFactory(param -> new AssetListCell());
			final AssetGroup assetGroup = groupedAssetsEntry.getKey();
			// Todo: Sortierung dynmaisch
			assetsList.itemsProperty().getValue().sort((o1, o2) -> Collator.getInstance().compare(o1.assetTitle(), o2.assetTitle()));
			assetsList.setOnMouseClicked(event -> {
				if (event.getClickCount() >= 2) {
					publishEditEvent(assetsList.getSelectionModel().getSelectedItem());
				}
			});
			final TitledPane assetGroupPane = new TitledPane(assetGroup.name(), assetsList);
			groupPanes.put(assetGroup, assetGroupPane);
		}
	}

	public void setOnAssetSelected(final Function<Asset, Void> onAssetSelected) {
		this.onAssetSelected = onAssetSelected;
	}

	private void publishEditEvent(final Asset selectedItem) {
		if (onAssetSelected != null) {
			System.out.println("JOJOJO!");
			onAssetSelected.apply(selectedItem);
		}
	}

	//TODO: work it harder make it better
	private Map<AssetGroup, List<Asset>> groupAssets(final List<Asset> Assets) {
		final HashMap<AssetGroup, List<Asset>> assetsGrouped = new HashMap<>();
		for (final Asset Asset : Assets) {
			final AssetGroup assetGroup = Asset.getEditorType().getAssetGroup();
			if (assetsGrouped.containsKey(assetGroup)) {
				assetsGrouped.get(assetGroup).add(Asset);
			} else {
				final LinkedList<Asset> assetsGroup = new LinkedList<>();
				assetsGroup.add(Asset);
				assetsGrouped.put(assetGroup, assetsGroup);
			}
		}
		return assetsGrouped;
	}

}
