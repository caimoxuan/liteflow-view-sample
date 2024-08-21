package com.cmx.liteflow.liteflowviewsmaple.component;

import cn.hutool.core.collection.ListUtil;
import com.yomahub.liteflow.annotation.LiteflowComponent;
import com.yomahub.liteflow.core.NodeIteratorComponent;

import java.util.Iterator;
import java.util.List;

@LiteflowComponent("iteratorCmp")
public class IteratorCmp extends NodeIteratorComponent {

    @Override
    public Iterator<?> processIterator() throws Exception {
        List<String> list = ListUtil.toList("111", "222", "333");
        return list.listIterator();
    }

}
