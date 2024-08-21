package com.cmx.liteflow.liteflowviewsmaple.component;

import com.yomahub.liteflow.core.NodeIfComponent;
import org.springframework.stereotype.Component;

@Component("boolean3Cmp")
public class Boolean3Cmp extends NodeIfComponent {

    @Override
    public boolean processIf() throws Exception {
        return false;
    }
}
