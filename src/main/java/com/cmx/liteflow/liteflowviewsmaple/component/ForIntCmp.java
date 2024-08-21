package com.cmx.liteflow.liteflowviewsmaple.component;

import com.yomahub.liteflow.annotation.LiteflowComponent;
import com.yomahub.liteflow.core.NodeForComponent;

@LiteflowComponent("forIntCmp")
public class ForIntCmp extends NodeForComponent {

    @Override
    public int processFor() throws Exception {
        return 5;
    }

}
