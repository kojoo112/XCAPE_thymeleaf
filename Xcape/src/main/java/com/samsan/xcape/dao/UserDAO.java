package com.samsan.xcape.dao;

import com.samsan.xcape.vo.TokenWithUserIdVO;
import com.samsan.xcape.vo.XcapeUser;
import org.apache.ibatis.annotations.Mapper;
import org.springframework.stereotype.Repository;

@Mapper
@Repository
public interface UserDAO {

    int getUserCount(String email);

    void signUp(XcapeUser xcapeUser);

    XcapeUser findUserByEmail(String email);

    void registRefreshToken(TokenWithUserIdVO refreshToken);
}
