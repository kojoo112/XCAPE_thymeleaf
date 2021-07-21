package com.samsan.xcape.dao;

import com.samsan.xcape.vo.UserVO;
import org.apache.ibatis.annotations.Mapper;
import org.springframework.stereotype.Repository;

@Mapper
@Repository
public interface UserDAO {

    public int getUserCount(String email);

    public void signUp(UserVO userVO);

    UserVO findUserByEmail(String email);
}
