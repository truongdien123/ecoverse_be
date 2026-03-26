package com.fpt.ecoverse_backend.projections;

import com.fpt.ecoverse_backend.entities.WasteItem;

public interface WasteItemWithOrderProjection {
    WasteItem getWasteItem();
    Integer getOrderIndex();
}
