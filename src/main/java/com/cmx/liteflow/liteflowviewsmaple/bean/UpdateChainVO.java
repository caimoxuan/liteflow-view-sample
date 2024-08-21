package com.cmx.liteflow.liteflowviewsmaple.bean;

import com.cmx.model.CmpProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class UpdateChainVO {

    private String chainId;

    private CmpProperty elJson;

}
