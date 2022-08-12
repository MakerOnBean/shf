package com.dk.service;

import com.dk.entity.UserFollow;
import com.dk.vo.UserFollowVo;
import com.github.pagehelper.PageInfo;

public interface UserFollowService extends BaseService<UserFollow> {
    void follow(Long houseId, Long userId);

    Boolean isFollow(Long houseId, Long userId);

    PageInfo<UserFollowVo> findPageList(Integer pageNum, Integer pageSize, Long userId);

    void cancelFollowed(Long id);
}
