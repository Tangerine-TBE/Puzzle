package com.weilai.jigsawpuzzle.configure;

import android.content.Context;
import android.util.ArrayMap;

/**
 ** DATE: 2022/10/10
 ** Author:tangerine
 ** Description:
 **/
public class Config {
    public static Configurator init(Context context){
        getConfigurations().put(ConfigureType.APPLICATION_CONTEXT.name(),context);
        return Configurator.getInstance();
    }
    public static ArrayMap<String,Object> getConfigurations(){
        return Configurator.getInstance().getConfigs();
    }
    public static Context getApplicationContext(){
        return (Context) getConfigurations().get(ConfigureType.APPLICATION_CONTEXT.name());
    }
}
