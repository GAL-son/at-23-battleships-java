package com.battleships.server.api.model;

import java.util.List;

/**
 * Container used to store list of fields that contain ships
 */
public class ShipFields {
    List<Field> shipFields;

    /**
     * Constructor for ShipFields
     * @param fields List of Fields that contain ships
     */
    public ShipFields(List<Field> fields) {
        this.shipFields = fields;
    }

    /**
     * Gets stored list of Fields
     * @return List of Fields containing ships
     */
    public List<Field> getShipFields() {
        return shipFields;
    }

    /**
     * Gets number of fields that are occupied by ships
     * @return number of fields that are occupied by ships
     */
    public int getSize() {
        return this.shipFields.size();
    }
}
