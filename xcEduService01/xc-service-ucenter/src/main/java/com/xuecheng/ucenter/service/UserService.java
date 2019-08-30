package com.xuecheng.ucenter.service;

import com.xuecheng.framework.domain.ucenter.XcCompanyUser;
import com.xuecheng.framework.domain.ucenter.XcUser;
import com.xuecheng.framework.domain.ucenter.ext.XcUserExt;
import com.xuecheng.ucenter.dao.XcCompanyUserRepository;
import com.xuecheng.ucenter.dao.XcUserRepository;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class UserService {
    @Autowired
    private XcUserRepository xcUserRepository;
    @Autowired
    XcCompanyUserRepository xcCompanyUserRepository;
    public XcUserExt getUserExt(String username) {
         //用户登入之后通过用户名查询用户信息
        XcUser xcUserByUsername = xcUserRepository.findXcUserByUsername(username);
        if(xcUserByUsername==null){
            return  null;
        }
        XcUserExt xcUserExt = new XcUserExt();
        //通过用户id查询公司id
        XcCompanyUser xcCompanyUserByUserId = xcCompanyUserRepository.findXcCompanyUserByUserId(xcUserByUsername.getId());
        if(xcCompanyUserByUserId!=null){
            String companyId = xcCompanyUserByUserId.getCompanyId();
            xcUserExt.setCompanyId(companyId);
        }
        BeanUtils.copyProperties(xcUserByUsername,xcUserExt);
        return  xcUserExt;
    }
}
