package com.weilai.jigsawpuzzle.configure;

import static com.weilai.jigsawpuzzle.configure.ConfigureType.APPLICATION;
import static com.weilai.jigsawpuzzle.configure.ConfigureType.CONFIGURE_READ;

import android.app.Application;
import android.content.Context;
import android.graphics.Paint;
import android.util.ArrayMap;

import com.weilai.jigsawpuzzle.util.SPUtil;
import com.weilai.jigsawpuzzle.util.dao.DaoTool;

/**
 ** DATE: 2022/10/10
 ** Author:tangerine
 ** Description:
 **/
public class Configurator {
    private static final ArrayMap<String ,Object> CONFIGS = new ArrayMap<>();
    private static class Holder{
        private static final Configurator INSTANCE = new Configurator();
    }
    static Configurator getInstance(){
        return  Holder.INSTANCE;
    }
    private Configurator(){
        CONFIGS.put(CONFIGURE_READ.name(),false);
    }
    final <A>A getConfiguration(Enum<ConfigureType> key){
            checkConfiguration();
        return (A) CONFIGS.get(key.name());
    }
    private void checkConfiguration(){
        final  boolean isReady = Boolean.TRUE.equals(CONFIGS.get(CONFIGURE_READ.name()));
        if (!isReady) {
            throw  new RuntimeException("Configuration is not ready yet!");
        }

    }
    public final Configurator withSp(Context context){
        SPUtil.init(context);
        return this;
    }
    public final Configurator withDao(Context context){
        DaoTool.init(context);
        return this;
    }
    public final Configurator withApplication(Application application){
        CONFIGS.put(APPLICATION.name(),application);
        return this;
    }
    final ArrayMap<String,Object> getConfigs(){
        return CONFIGS;
    }
    public final void Configure(){
        CONFIGS.put(CONFIGURE_READ.name(),true);
    }
}
