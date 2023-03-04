package baloot.controller;

import baloot.model.Provider;
import baloot.repository.ProviderRepository;
import org.json.JSONObject;

public class ProviderController {
    private final ProviderRepository providerRepository;

    public ProviderController(ProviderRepository providerRepository) {
        this.providerRepository = providerRepository;
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
}
