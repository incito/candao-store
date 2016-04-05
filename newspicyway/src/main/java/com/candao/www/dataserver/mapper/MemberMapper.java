package com.candao.www.dataserver.mapper;


import com.candao.www.dataserver.entity.Member;

/**
 * Created by lenovo on 2016/3/28.
 */
public interface MemberMapper {
    int insert(Member member);

    int update(Member member);

    Member selectOneByNo(String memberNo);

    int deleteByNo(String memberNo);
}
