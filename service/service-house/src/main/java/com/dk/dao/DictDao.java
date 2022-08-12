package com.dk.dao;

import com.dk.entity.Dict;

import java.util.List;

public interface DictDao {
    /**
     * 根据父id查询该节点下所有子节点
     */
    List<Dict> findListByParentId(Long id);

    /**
     * 根据父id查询该节点下还多少子节点
     */
    Integer childNodesCount(Long id);

    /**
     * 根据编码获取该节点
     */
    Dict getDictByDictCode(String dictCode);

    /**
     * 根据id获取name
     */
    String getNameById(Long id);
}
