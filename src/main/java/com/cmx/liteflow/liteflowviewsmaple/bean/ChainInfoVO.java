package com.cmx.liteflow.liteflowviewsmaple.bean;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ChainInfoVO {

    private String chainId;

    private String chainName;

}
