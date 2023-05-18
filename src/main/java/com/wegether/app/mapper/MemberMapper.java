package com.wegether.app.mapper;

import com.wegether.app.domain.vo.MemberVO;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.Optional;

@Mapper
public interface MemberMapper {
    //아이디 중복검사
    public Optional<MemberVO> selectByMemberId(String memberId);

    // 회원가입
    public void insert(MemberVO memberVO);

    // 로그인
    public Optional<Long> selectByMemberIdAndMemberPassword(String memberId,String memberPassword);

}
