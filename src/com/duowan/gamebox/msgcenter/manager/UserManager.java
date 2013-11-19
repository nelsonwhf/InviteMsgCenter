package com.duowan.gamebox.msgcenter.manager;

import com.duowan.gamebox.msgcenter.manager.vo.User;

public interface UserManager {

	public abstract void generateToken(User user);

	public abstract User getUserByToken(String token);

	public abstract User getUserByIdAndType(String userId, String userType);

	public abstract String getTokenByIdAndType(String userId, String userType);

	public abstract void addUser(User user);

	public abstract void removeUser(User user);

	public abstract User removeUserByToken(String token);

	public abstract User removeUserByIdAndType(String userId, String userType);

	public abstract void updateUser(User user);

}