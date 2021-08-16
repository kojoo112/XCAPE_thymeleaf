package com.samsan.xcape.enums;

import com.fasterxml.jackson.annotation.JsonFormat;

import java.util.Arrays;
import java.util.List;

@JsonFormat(shape = JsonFormat.Shape.OBJECT)
public enum MerchantAdvanced {
    XCAPE("XCAPE", Arrays.asList(MerchantList.MRC001, MerchantList.MRC002, MerchantList.MRC003, MerchantList.MRC004));

    MerchantAdvanced(String storeName, List<MerchantList> merchantLists) {
        this.storeName = storeName;
        this.merchantLists = merchantLists;
    }

    private String storeName;
    private List<MerchantList> merchantLists;
}
