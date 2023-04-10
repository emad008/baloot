package baloot.controller;

import baloot.model.Commodity;
import baloot.model.Provider;
import baloot.repository.CommodityRepository;
import baloot.repository.ProviderRepository;
import org.json.JSONObject;

import java.awt.image.AreaAveragingScaleFilter;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class ProviderController {
    private final ProviderRepository providerRepository;
    private final CommodityRepository commodityRepository;

    public ProviderController(
        ProviderRepository providerRepository,
        CommodityRepository commodityRepository
    ) {
        this.providerRepository = providerRepository;
        this.commodityRepository = commodityRepository;
    }

    public JSONObject addProvider(JSONObject providerData) {
        this.providerRepository.save(
            new Provider(
                providerData.getInt("id"),
                providerData.getString("name"),
                providerData.getString("registryDate")
            )
        );

        return providerData;
    }

    public JSONObject getProviderById(JSONObject providerData) {
        Provider provider = this.providerRepository.findById(
            providerData.getInt("id")
        );

        List<Map<String, Object>> commoditiesDescription = new ArrayList<>();

        for (Commodity commodity: this.commodityRepository.listByProviderId(provider.getId()))
            commoditiesDescription.add(commodity.describe());

        Map<String, Object> providerDescription = new HashMap<>(provider.describe());
        providerDescription.put("commodities", commoditiesDescription);

        return new JSONObject(providerDescription);
    }
//
//    public JSONObject getAllProviders() {
//        List<Provider> providers = this.providerRepository.list();
//
//        List<Map<String, Object>> providersData = new ArrayList<>();
//
//        for (Provider provider: providers) {
//            providersData.add(
//                provider.describe()
//            );
//        }
//
//        return new JSONObject(providersData);
//    }
}
