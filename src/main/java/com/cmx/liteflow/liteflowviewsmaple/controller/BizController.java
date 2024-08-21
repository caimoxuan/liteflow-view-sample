package com.cmx.liteflow.liteflowviewsmaple.controller;

import com.cmx.liteflow.liteflowviewsmaple.bean.PriceStepVO;
import com.cmx.liteflow.liteflowviewsmaple.enums.PriceTypeEnum;
import com.cmx.liteflow.liteflowviewsmaple.slot.PriceContext;
import com.yomahub.liteflow.core.FlowExecutor;
import com.yomahub.liteflow.flow.LiteflowResponse;
import jakarta.annotation.Resource;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

@RequestMapping("/api")
@RestController
public class BizController {

    @Resource
    private FlowExecutor flowExecutor;

    @GetMapping("test")
    public String testSwitch() {
        PriceStepVO priceStepVO = new PriceStepVO(null, null , BigDecimal.ZERO, BigDecimal.ZERO, BigDecimal.ZERO, null);
        PriceContext priceContext = new PriceContext();

        List<PriceStepVO> list = new ArrayList<>();
        list.add(priceStepVO);
        priceContext.setPriceStepList(list);
        LiteflowResponse res = flowExecutor.execute2Resp("switchChain", null, priceContext);
        if (!res.isSuccess()) {
            throw new RuntimeException(res.getCause());
        }
        return "success";
    }
}
