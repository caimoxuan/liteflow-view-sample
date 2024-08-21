package com.cmx.liteflow.liteflowviewsmaple.component;

import com.yomahub.liteflow.core.NodeIfComponent;
import org.springframework.stereotype.Component;

/**
 * IfBuyCmp
 *
 * @author <a href="mailto:dogsong99@163.com">dosong</a>
 * @since 2024/4/23
 */
@Component("boolean1Cmp")
public class Boolean1Cmp extends NodeIfComponent {

    @Override
    public boolean processIf() throws Exception {
        return false;
    }

}
