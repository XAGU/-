package com.xiaolian.amigo.data.network.model.userbill;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import lombok.Data;

@Data
public class QueryBillListRespDTO {
    private ArrayList<HashMap<String, Object>> detailList;
}
