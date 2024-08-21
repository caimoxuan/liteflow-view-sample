package com.cmx.liteflow.liteflowviewsmaple.component;

import com.yomahub.liteflow.annotation.LiteflowComponent;
import com.yomahub.liteflow.core.NodeBreakComponent;

@LiteflowComponent("breakCmp")
public class BreakCmp extends NodeBreakComponent {

    @Override
    public boolean processBreak() throws Exception {
        return false;
    }
}
