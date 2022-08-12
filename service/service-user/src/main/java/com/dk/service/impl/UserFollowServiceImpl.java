package com.dk.service.impl;

import com.alibaba.dubbo.config.annotation.Reference;
import com.alibaba.dubbo.config.annotation.Service;
import com.dk.dao.BaseDao;
import com.dk.dao.UserFollowDao;
import com.dk.entity.UserFollow;
import com.dk.entity.UserInfo;
import com.dk.service.DictService;
import com.dk.service.UserFollowService;
import com.dk.vo.UserFollowVo;
import com.github.pagehelper.Page;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;

@Service(interfaceClass = UserFollowService.class)
@Transactional
public class UserFollowServiceImpl extends BaseServiceImpl<UserFollow> implements UserFollowService {

    @Autowired
    private UserFollowDao userFollowDao;

    @Reference
    private DictService dictService;

    @Override
    protected BaseDao<UserFollow> getEntityDao() {
        return this.userFollowDao;
    }

    /**
     * 关注房源
     */
    @Override
    public void follow(Long houseId, Long userId) {
        UserFollow userFollow = new UserFollow();
        userFollow.setUserId(userId);
        userFollow.setHouseId(houseId);
        userFollowDao.insert(userFollow);
    }

    @Override
    public Boolean isFollow(Long houseId, Long userId) {
        Integer count = userFollowDao.getCountByUserIdAndHouseId(userId,houseId);
        return count > 0;
    }

    /**
     * 分页查询我关注的房源
     */
    @Override
    public PageInfo<UserFollowVo> findPageList(Integer pageNum, Integer pageSize, Long userId) {
        //开启分页
        PageHelper.startPage(pageNum,pageSize);
        //调用分页方法
        Page<UserFollowVo> page = userFollowDao.findPageList(userId);
        //遍历page填充数据
        page.forEach(userFollowVo -> {
            //房屋类型
            String houseTypeName = dictService.getNameById(userFollowVo.getHouseTypeId());
            userFollowVo.setHouseTypeName(houseTypeName);
            //楼层
            String floorName = dictService.getNameById(userFollowVo.getFloorId());
            userFollowVo.setFloorName(floorName);
            //朝向
            String directionName = dictService.getNameById(userFollowVo.getDirectionId());
            userFollowVo.setDirectionName(directionName);
        });
        return new PageInfo<>(page,5);
    }

    /**
     * 取消关注房源
     * 调用删除方法
     */
    @Override
    public void cancelFollowed(Long id) {
       userFollowDao.delete(id);
    }
}
