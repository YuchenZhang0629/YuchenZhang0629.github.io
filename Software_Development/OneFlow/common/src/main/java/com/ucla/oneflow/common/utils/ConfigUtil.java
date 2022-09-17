package com.ucla.oneflow.common.utils;


import com.google.gson.Gson;
import com.google.gson.stream.JsonReader;
import com.ucla.oneflow.model.VO.ConfigVO;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.io.FileNotFoundException;
import java.io.FileReader;


@Component
// Confiduring VO classes
public class ConfigUtil {
    @Value("${jsonConfigPath}")
    private String jsonConfigPath;
    public ConfigVO getConfigVO() throws FileNotFoundException {
        Gson gson = new Gson();
        JsonReader reader = new JsonReader(new FileReader(jsonConfigPath));
        ConfigVO configVO = gson.fromJson(reader,ConfigVO.class);
        return configVO;
    }
}
