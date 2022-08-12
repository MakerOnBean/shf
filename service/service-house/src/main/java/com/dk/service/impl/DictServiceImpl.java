package com.dk.service.impl;

import com.alibaba.dubbo.config.annotation.Service;
import com.dk.dao.DictDao;
import com.dk.entity.Dict;
import com.dk.service.DictService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;

import java.util.*;

@Service(interfaceClass = DictService.class)
@Transactional
public class DictServiceImpl implements DictService {

    @Autowired
    private DictDao dictDao;

    @Override
    public List<Map<String, Object>> findZnodes(Long id) {
        //根据父id查询该节点下所有子节点
        List<Dict> dictList = dictDao.findListByParentId(id);
        //创建返回的list
        List<Map<String, Object>> zNodes = new ArrayList<>();
        dictList.forEach(dict -> {
            Map<String, Object> map = new HashMap<>();
            map.put("id", dict.getId());
            map.put("name", dict.getName());
            //判断该节点是否是父节点
            Integer count = dictDao.childNodesCount(dict.getId());
            map.put("isParent", count > 0 ? true : false);
            zNodes.add(map);
        });
        return zNodes;
    }

    @Override
    public List<Dict> findListByDictCode(String dictCode) {
        Dict dict = dictDao.getDictByDictCode(dictCode);
        if (dict == null) return null;
        return dictDao.findListByParentId(dict.getId());
    }

    @Override
    public List<Dict> findListByParentId(Long id) {
        return dictDao.findListByParentId(id);
    }

    @Override
    public String getNameById(Long id) {
        return dictDao.getNameById(id);
    }
}
