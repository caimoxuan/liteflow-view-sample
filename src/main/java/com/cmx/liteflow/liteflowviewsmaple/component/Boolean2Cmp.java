package com.cmx.liteflow.liteflowviewsmaple.component;

import com.yomahub.liteflow.core.NodeIfComponent;
import org.springframework.stereotype.Component;


@Component("boolean2Cmp")
public class Boolean2Cmp extends NodeIfComponent {

    @Override
    public boolean processIf() throws Exception {
        return true;
    }

}
