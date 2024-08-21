package com.cmx.liteflow.liteflowviewsmaple.extension;

import com.cmx.extension.annotation.ExtensionPoint;
import com.cmx.extension.model.AbstractExtensionNode;
import com.cmx.extension.model.ExtensionData;
import com.cmx.extension.model.ExtensionParam;

@ExtensionPoint(extCode= PostAmountCheckExt.EXT_CODE, extDesc = "获取运费校验金额")
public class PostAmountCheckExt extends AbstractExtensionNode<ExtensionData<String>, ExtensionParam> {
    public static final String EXT_CODE = "POST_FEE_CHECK_EXT";

}
