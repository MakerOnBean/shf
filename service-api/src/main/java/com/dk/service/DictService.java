package com.dk.service;

import com.dk.entity.Dict;

import java.util.List;
import java.util.Map;
import java.util.Objects;

public interface DictService {

    /**
     * 查询数据字典中的数据，通过ZTree格式返回
     */
    List<Map<String, Object>> findZnodes(Long id);

    /**
     * 根据编码获取该节点下的所有子节点
     */
    List<Dict> findListByDictCode(String dictCode);

    /**
     * 根据id获取该节点下所有子节点
     */
    List<Dict> findListByParentId(Long id);

    /**
     * 通过id获取name
     */
    String getNameById(Long id);
}
