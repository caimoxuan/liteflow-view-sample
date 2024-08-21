package com.cmx.liteflow.liteflowviewsmaple.component;

import cn.hutool.core.collection.ListUtil;
import com.cmx.liteflow.liteflowviewsmaple.bean.ProductPackVO;
import com.cmx.liteflow.liteflowviewsmaple.bean.PromotionInfoVO;
import com.cmx.liteflow.liteflowviewsmaple.bean.PromotionPackVO;
import com.cmx.liteflow.liteflowviewsmaple.slot.PriceContext;
import com.yomahub.liteflow.core.NodeComponent;
import org.apache.commons.collections.CollectionUtils;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;

/**
 * 把商品包的优惠信息转换成以优惠信息为主要维度的对象，以便于后面优惠信息的计算
 */
@Component("promotionConvertCmp")
public class PromotionConvertCmp extends NodeComponent {
    @Override
    public void process() throws Exception {
        PriceContext context = this.getContextBean(PriceContext.class);
        List<PromotionPackVO> promotionPackList = new ArrayList<>();

        PromotionPackVO promotionPack = null;
        for(ProductPackVO pack : context.getProductPackList()){
            if(CollectionUtils.isEmpty(pack.getPromotionList())){
                continue;
            }
            for(PromotionInfoVO promotion : pack.getPromotionList()){
                promotionPack = new PromotionPackVO();
                promotionPack.setId(promotion.getId());
                if(promotionPackList.contains(promotionPack)){
                    promotionPack = promotionPackList.get(promotionPackList.indexOf(promotionPack));
                    if(promotionPack.getRelatedProductPackList().contains(pack)){
                        continue;
                    }else{
                        promotionPack.getRelatedProductPackList().add(pack);
                    }
                }else{
                    BeanUtils.copyProperties(promotion,promotionPack);
                    promotionPack.setRelatedProductPackList(ListUtil.toList(pack));
                    promotionPackList.add(promotionPack);
                }
            }
        }
        context.setPromotionPackList(promotionPackList);
    }

    @Override
    public boolean isAccess() {
        PriceContext context = this.getContextBean(PriceContext.class);
        if(CollectionUtils.isNotEmpty(context.getProductPackList())){
            return true;
        }else{
            return false;
        }

    }
}
