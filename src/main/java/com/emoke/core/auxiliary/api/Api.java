package com.emoke.core.auxiliary.api;

import com.emoke.core.auxiliary.annotation.*;
import com.emoke.core.auxiliary.pojo.Test;

import java.io.File;
import java.util.Map;

@ZoneMapping(url = "http://127.0.0.1:8800")
public interface Api {

    @Get(path = "/pass/check/{id}")
    Test get(@Query Map<String, Object> map);

    @Post(path = "/pass/check")
    Test post(@MultipartMap Map<String, File> fileMap);

    @Header(contentType = "form-data",headMapName = "head")
    @Put(path = "/pass/put/{id}")
    Test put(@Query Map<String, Object> map, @PathVariable String id, Map<String, Object> head);

    @Header(contentType = "form-data",headMapName = "head")
    @Delete(path = "/pass/put/{id}")
    Test delete(@Query Map<String, Object> map, @PathVariable String id, Map<String, Object> head);

    @Post(path = "/admin/login")
    Test admin(@Query Map<String, String> fileMap);

}