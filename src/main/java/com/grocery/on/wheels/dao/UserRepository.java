package com.grocery.on.wheels.dao;

import java.util.List;

import org.apache.ibatis.annotations.Delete;
import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;
import org.apache.ibatis.annotations.Update;

import com.grocery.on.wheels.model.User;

@Mapper
public interface UserRepository {

    @Select("select * from users")
    public List<User> findAll();

    @Select("SELECT * FROM users WHERE username = #{username}")
    public User findByUsername(String username);

    @Delete("DELETE FROM users WHERE username = #{username}")
    public int deleteByUsername(String username);

    @Insert("INSERT INTO users(username, password, firstName, lastName, emailId, active, role) " +
         " VALUES (#{username}, #{password}, #{firstName}, #{lastName}, #{emailId}, #{active}, #{role})")
    public int insert(User user);

    @Update("Update users set firstName=#{user.firstName}, " 
    		+" lastName=#{user.lastName}, "
    		+" role=#{user.role}, "
         + "emailId=#{user.emailId} "
         + "where username=#{pathusername}")
    public int update(String pathusername, User user);
    
    
    @Update("Update users set active=#{active} "
         + "where username=#{username}")
    public int acitvate(User user);

    @Select("SELECT * FROM users WHERE username = #{username} and password= #{password} and active='Y'")
	public User login(User user);

}