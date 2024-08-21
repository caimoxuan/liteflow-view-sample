package com.cmx.liteflow.liteflowviewsmaple.extension.loader;

import com.cmx.extension.loader.AbstractExtensionRemoteLoader;
import com.cmx.extension.model.AbstractExtensionNode;
import com.cmx.extension.model.ExtensionData;
import com.cmx.extension.model.ExtensionParam;
import com.cmx.liteflow.liteflowviewsmaple.dal.LocalFileMapper;
import com.cmx.model.ScriptDetail;
import jakarta.annotation.Resource;
import org.springframework.stereotype.Component;

@Component
public class SelfRemoteExtensionLoader extends AbstractExtensionRemoteLoader {

    @Resource
    private LocalFileMapper fileMapper;

    @Override
    public ScriptDetail loadRemoteScript(String loadKey, String extCode) {
        return fileMapper.getLocalFile(loadKey, extCode);
    }

    @Override
    public void saveRemoteScript(String s, String s1, ScriptDetail scriptDetail) {
        fileMapper.saveOrUpdateLocalFile(s, s1, scriptDetail);
    }

    @Override
    public void updateRemoteScript(String s, String s1, ScriptDetail scriptDetail) {
        fileMapper.saveOrUpdateLocalFile(s, s1, scriptDetail);
    }

    @Override
    public <T> AbstractExtensionNode<ExtensionData<T>, ExtensionParam> loadRemoteExtension(String s, String s1, Class<? extends AbstractExtensionNode<ExtensionData<T>, ExtensionParam>> aClass) {
        return super.loadRemoteExtension(s, s1, aClass);
    }

}
