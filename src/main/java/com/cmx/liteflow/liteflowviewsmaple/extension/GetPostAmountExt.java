package com.cmx.liteflow.liteflowviewsmaple.extension;

import com.cmx.extension.annotation.ExtensionPoint;
import com.cmx.extension.model.AbstractExtensionNode;
import com.cmx.extension.model.ExtensionData;
import com.cmx.extension.model.ExtensionParam;

@ExtensionPoint(extCode= GetPostAmountExt.EXT_CODE, extDesc = "获取运费金额")
public class GetPostAmountExt extends AbstractExtensionNode<ExtensionData<String>, ExtensionParam> {

    public static final String EXT_CODE = "GET_POST_AMOUNT";

}
