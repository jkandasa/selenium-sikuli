package com.redhat.qe.sikuli.grid;

import java.util.Map;
import java.util.Optional;

import org.openqa.grid.internal.utils.DefaultCapabilityMatcher;

/**
 * @author Jeeva Kandasamy (jkandasa)
 */

public class SikuliCapabilityMatcher extends DefaultCapabilityMatcher {

    // Sikuli capability identifier key
    public static final String SIKULI_ENABLED = "sikuliEnabled";

    @Override
    public boolean matches(Map<String, Object> nodeCapability, Map<String, Object> requestedCapability) {

        boolean basicChecks = super.matches(nodeCapability, requestedCapability);

        // If the request does not have the special capability, we return what the DefaultCapabilityMatcher returns
        if (requestedCapability.containsKey(SIKULI_ENABLED)) {
            return basicChecks && isSikuliEnabled(nodeCapability) && isSikuliEnabled(requestedCapability);
        } else {
            return basicChecks;
        }
    }

    private boolean isSikuliEnabled(Map<String, Object> capabilities) {
        return Boolean.parseBoolean(Optional.ofNullable(capabilities.get(SIKULI_ENABLED)).orElse("false").toString());
    }

}
