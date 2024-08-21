package com.cmx.liteflow.liteflowviewsmaple.component;

import com.cmx.extension.Extensions;
import com.cmx.extension.annotation.ExtensionCmp;
import com.cmx.extension.model.ExtensionParam;
import com.cmx.liteflow.liteflowviewsmaple.bean.PriceStepVO;
import com.cmx.liteflow.liteflowviewsmaple.enums.PriceTypeEnum;
import com.cmx.liteflow.liteflowviewsmaple.extension.GetPostAmountExt;
import com.cmx.liteflow.liteflowviewsmaple.extension.PostAmountCheckExt;
import com.cmx.liteflow.liteflowviewsmaple.slot.PriceContext;
import com.yomahub.liteflow.core.NodeComponent;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;
import java.util.Optional;

/**
 * 国内运费计算组件
 */
@Component("postageCmp")
@ExtensionCmp(nodes = {PostAmountCheckExt.class, GetPostAmountExt.class})
public class PostageCmp extends NodeComponent {
    @Override
    public void process() throws Exception {
        PriceContext context = this.getContextBean(PriceContext.class);

        // 通过扩展点获取校验的运费金额 默认99
        /**这里Mock运费的策略是：满99免运费，不满99需要10块钱运费**/
        String checkAmtStr = Extensions.execute("00000100", PostAmountCheckExt.EXT_CODE, new ExtensionParam(), PostAmountCheckExt.class);
        BigDecimal triggerPrice = Optional.ofNullable(checkAmtStr).map(BigDecimal::new)
                .orElse(new BigDecimal(99));

        // 获取运费金额 默认10
        String postAmtStr = Extensions.execute("00000100", GetPostAmountExt.EXT_CODE, new ExtensionParam(), GetPostAmountExt.class);
        BigDecimal postage = Optional.ofNullable(postAmtStr).map(BigDecimal::new)
                .orElse(new BigDecimal(10));

        //先把运费加上去
        BigDecimal prePrice = context.getLastestPriceStep().getCurrPrice();
        BigDecimal currPrice = prePrice.add(postage);

        context.addPriceStep(new PriceStepVO(PriceTypeEnum.POSTAGE,
                null,
                prePrice,
                currPrice.subtract(prePrice),
                currPrice,
                PriceTypeEnum.POSTAGE.getName()));

        //判断运费是否满99了，满了99就去掉运费
        if(prePrice.compareTo(triggerPrice) >= 0){
            prePrice = context.getLastestPriceStep().getCurrPrice();
            currPrice = currPrice.subtract(postage);

            context.addPriceStep(new PriceStepVO(PriceTypeEnum.POSTAGE_FREE,
                    null,
                    prePrice,
                    currPrice.subtract(prePrice),
                    currPrice,
                    PriceTypeEnum.POSTAGE_FREE.getName()));
        }
    }
}
