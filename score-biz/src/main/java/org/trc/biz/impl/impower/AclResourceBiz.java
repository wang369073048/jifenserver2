package org.trc.biz.impl.impower;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;

import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.Assert;
import org.trc.biz.impower.IAclResourceBiz;
import org.trc.domain.goods.GoodsDO;
import org.trc.domain.impower.AclResource;
import org.trc.domain.impower.AclRoleResourceRelation;
import org.trc.domain.impower.AclUserAccreditInfo;
import org.trc.domain.impower.AclUserAccreditRoleRelation;
import org.trc.enums.ExceptionEnum;
import org.trc.enums.RequestTypeEnum;
import org.trc.enums.ZeroToNineEnum;
import org.trc.exception.CategoryException;
import org.trc.exception.JurisdictionException;
import org.trc.form.impower.JurisdictionTreeNode;
import org.trc.service.impower.IAclResourceService;
import org.trc.service.impower.IAclRoleResourceRelationService;
import org.trc.service.impower.IAclUserAccreditInfoService;
import org.trc.service.impower.IAclUserAccreditRoleRelationService;
import org.trc.util.AssertUtil;
import org.trc.util.StringUtil;

import tk.mybatis.mapper.entity.Example;

import javax.annotation.Resource;
import javax.ws.rs.container.ContainerRequestContext;
import java.util.*;

/**
 * author: hzwzhen
 * JDK-version:  JDK1.8
 * since Date： 2017/7/13
 */
@Service("jurisdictionBiz")
public class AclResourceBiz implements IAclResourceBiz {

    private Logger logger = LoggerFactory.getLogger(AclResourceBiz.class);
    @Resource
    private IAclResourceService jurisdictionService;
    @Resource
    private IAclRoleResourceRelationService roleJurisdictionRelationService;
    @Autowired
    private IAclUserAccreditInfoService userAccreditInfoService;
    @Autowired
    private IAclUserAccreditRoleRelationService userAccreditInfoRoleRelationService;

    @Override
    public List<AclResource> findWholeJurisdiction(){

        AclResource aclResource = new AclResource();
        //aclResource.setBelong(WHOLE_JURISDICTION_ID);
        List<AclResource> wholeAclResourceList = jurisdictionService.select(aclResource);
        AssertUtil.notNull(wholeAclResourceList, "查询全局权限列表,数据库操作失败");
        return wholeAclResourceList;

    }

    @Override
    public List<AclResource> findChannelJurisdiction(){

        AclResource aclResource = new AclResource();
        //aclResource.setBelong(CHANNEL_JURISDICTION_ID);
        List<AclResource> channelAclResourceList = jurisdictionService.select(aclResource);
        AssertUtil.notNull(channelAclResourceList, "查询渠道权限列表, 数据库操作失败");
        return channelAclResourceList;

    }

    @Override
    @Transactional
    public List<AclResource> findWholeJurisdictionAndCheckedByRoleId(Long roleId){

        AssertUtil.notNull(roleId, "根据角色的id,查询被选中的权限,角色id为空");
        // 1.查询对应的权限列表
        List<AclResource> wholeAclResourceList = findWholeJurisdiction();
        //2.查询对应角色被选中权限
        List<Long> JurisdictionIdList = roleJurisdictionRelationService.selectJurisdictionIdList(roleId);
        AssertUtil.notNull(JurisdictionIdList, "查询全局角色对应的权限关系,数据库操作失败");
        //3.赋值checked属性
        for (AclResource aclResource : wholeAclResourceList) {
            for (Long JurisdictionId : JurisdictionIdList) {
                if (aclResource.getCode().equals(JurisdictionId)) {
                    aclResource.setCheck("true");
                }
            }
        }
        return wholeAclResourceList;

    }

    @Override
    @Transactional
    public List<AclResource> findChannelJurisdictionAndCheckedByRoleId(Long roleId){

        AssertUtil.notNull(roleId, "根据角色的id,查询被选中的权限,角色id为空");
        // 1.查询对应的权限列表
        List<AclResource> channelAclResourceList = findChannelJurisdiction();
        //2.查询对应角色被选中权限
        List<Long> JurisdictionIdList = roleJurisdictionRelationService.selectJurisdictionIdList(roleId);
        AssertUtil.notNull(JurisdictionIdList, "查询渠道角色对应的权限关系,数据库操作失败");
        //3.赋值checked属性
        for (AclResource aclResource : channelAclResourceList) {
            for (Long JurisdictionId : JurisdictionIdList) {
                if (aclResource.getId().equals(JurisdictionId)) {
                    aclResource.setCheck("true");
                }
            }
        }
        return channelAclResourceList;

    }

    @Override
    public Boolean authCheck(String userId, String url, String method){
        /*
        * 1.查询用户授权信息表
        * 2.查询用户所拥有的角色
        * 3.查询用户所有角色下的权限
        * 4.查询具体的权限
        * 5.验证权限
        * */
        //1.查询用户授权信息表
        AclUserAccreditInfo aclUserAccreditInfo = userAccreditInfoService.selectOneById(userId);
        try {
            AssertUtil.notNull(aclUserAccreditInfo, "用户授权信息不存在");
        } catch (IllegalArgumentException e) {
            throw new JurisdictionException(ExceptionEnum.SYSTEM_ACCREDIT_QUERY_EXCEPTION, "用户授权信息不存在");
        }
        //2.查询用户所拥有的角色
        List<AclUserAccreditRoleRelation> userRoleRelationList = userAccreditInfoRoleRelationService.selectListByUserAcId(aclUserAccreditInfo.getId());
        if (AssertUtil.collectionIsEmpty(userRoleRelationList)) {
            throw new JurisdictionException(ExceptionEnum.SYSTEM_ACCREDIT_QUERY_EXCEPTION, "用户角色信息不存在");
        }
        Long[] roleIds = new Long[userRoleRelationList.size()];
        for (int i = 0; i < userRoleRelationList.size(); i++) {
            roleIds[i] = userRoleRelationList.get(i).getRoleId();
        }
        //3.查询用户所有角色下的权限
        List<AclRoleResourceRelation> roleJdRelationList = roleJurisdictionRelationService.selectListByRoleIds(roleIds);
        if (AssertUtil.collectionIsEmpty(roleJdRelationList)) {
            throw new JurisdictionException(ExceptionEnum.SYSTEM_ACCREDIT_QUERY_EXCEPTION, "用户权限信息不存在");
        }
        Long[] codes = new Long[roleJdRelationList.size()];
        for (int i = 0; i < roleJdRelationList.size(); i++) {
            codes[i] = roleJdRelationList.get(i).getResourceCode();
        }
        //4.查询具体的权限
        List<AclResource> aclResourceList = jurisdictionService.selectJurisdictionListByCodes(codes);
        if (AssertUtil.collectionIsEmpty(aclResourceList)) {
            throw new JurisdictionException(ExceptionEnum.SYSTEM_ACCREDIT_QUERY_EXCEPTION, "用户权限信息不存在");
        }
        //5.验证权限,正则匹配url，方法类型匹配
        for (AclResource aclResource : aclResourceList) {
            if (url.matches(aclResource.getUrl())) {
                if (aclResource.getMethod().equals(method)) {
                    return true;
                }
            }
        }
        return false;
    }

    @Override
    public Boolean urlCheck(String url) {
        Example example = new Example(AclResource.class);
        List<AclResource> list = jurisdictionService.selectByExample(example);
        for (AclResource aclResource : list) {
            if (url.matches(aclResource.getUrl())) {
                return true;
            }
        }
        return false;
    }

    @Override
    public List<JurisdictionTreeNode> getNodes(Long parentId, boolean isRecursive){
        Example example = new Example(AclResource.class);
        Example.Criteria criteria = example.createCriteria();
        criteria.andEqualTo("parentId", parentId);
        List<AclResource> childCategoryList = jurisdictionService.selectByExample(example);
        List<JurisdictionTreeNode> childNodeList = new ArrayList<>();
        for (AclResource aclResource : childCategoryList) {
            JurisdictionTreeNode treeNode = new JurisdictionTreeNode();
            treeNode.setCode(aclResource.getCode());
            treeNode.setName(aclResource.getName());
            treeNode.setUrl(aclResource.getUrl());
            treeNode.setMethod(aclResource.getMethod());
            treeNode.setParentId(aclResource.getParentId());
            treeNode.setId(aclResource.getId());
            treeNode.setCreateOperator(aclResource.getCreateOperator());
            childNodeList.add(treeNode);
        }
        if (childNodeList.size() == 0) {
            return childNodeList;
        }
        //递归读取资源树
        if (isRecursive == true) {
            for (JurisdictionTreeNode childNode : childNodeList) {
                List<JurisdictionTreeNode> nextChildJurisdictionList = getNodes(childNode.getCode(), isRecursive);
                if (nextChildJurisdictionList.size() > 0) {
                    childNode.setChildren(nextChildJurisdictionList);
                }
            }
        }
        return childNodeList;
    }

    /**
     * 新增资源
     * @description: update by xab 2017-08-07 
     * @param jurisdictionTreeNode
     * @throws Exception
     */
    @Override
    public void saveJurisdiction(JurisdictionTreeNode jurisdictionTreeNode, ContainerRequestContext requestContext){
    	try {
    		validateResourceForAdd(jurisdictionTreeNode);//参数验证
    		AclResource aclResource = new AclResource();
    		//获取到父节点的code
            String parentCode = jurisdictionTreeNode.getParentId().toString();
            String baseCode = null; //同级资源基础code
            
            String requestMethod = 1+"";//二级权限资源操作类型默认为1
            
            //判断新增的是二级资源还是三级资源
            if(parentCode.length()==3){//二级资源
            	baseCode = parentCode;
            }else{//三级资源
            	if(StringUtils.isBlank(jurisdictionTreeNode.getMethod())){
            		throw new JurisdictionException(ExceptionEnum.ACCREDIT_RESOURCE_SAVE_EXCEPTION, "三级权限资源[操作类型]不能为空！");
            	}
            	requestMethod = jurisdictionTreeNode.getMethod();
            	baseCode = parentCode + ZeroToNineEnum.ZERO.getCode() + RequestTypeEnum.getCode(jurisdictionTreeNode.getMethod());
            }
            
            //查询到当前方法,当前父资源下最大的序号,如果存在加1,如果不存在,则初始化
            Example example = new Example(AclResource.class);
            Example.Criteria criteria = example.createCriteria();
            criteria.andLike("code", baseCode+"%");
            criteria.andEqualTo("parentId", parentCode);
            example.orderBy("code").desc();
            List<AclResource> aclResourceList = jurisdictionService.selectByExample(example);
            if (aclResourceList != null && aclResourceList.size() > 0) {
                //存在的情况
                aclResource.setCode(aclResourceList.get(0).getCode() + 1);
            } else {
                //不存在,手动组合,从一开始
                String code = baseCode + ZeroToNineEnum.ZERO.getCode() + ZeroToNineEnum.ONE.getCode();
                aclResource.setCode(Long.parseLong(code));
            }
            
            aclResource.setMethod(requestMethod);
            aclResource.setParentId(jurisdictionTreeNode.getParentId());
            aclResource.setName(jurisdictionTreeNode.getName());
            aclResource.setUrl(jurisdictionTreeNode.getUrl());
            String userId = (String) requestContext.getProperty("userId");
            aclResource.setCreateOperator(userId);
            aclResource.setCreateTime(Calendar.getInstance().getTime());
            aclResource.setUpdateTime(Calendar.getInstance().getTime());
            //aclResource.setIsValid(jurisdictionTreeNode.getIsValid());
            aclResource.setIsDeleted(Integer.valueOf(ZeroToNineEnum.ZERO.getCode()));
            jurisdictionService.insertSelective(aclResource);
        } catch (IllegalArgumentException e) {
            throw new JurisdictionException(ExceptionEnum.ACCREDIT_RESOURCE_SAVE_EXCEPTION, e.getMessage());
        }
      
    }

    /**
     *  编辑资源
     * @description: update by xab 2017-08-07 
     * @param jurisdictionTreeNode
     * @return
     * @throws Exception
     */
    @Override
    public void updateJurisdiction(JurisdictionTreeNode jurisdictionTreeNode){
    	try {
    		validateResourceForUpdate(jurisdictionTreeNode);//参数验证
    		AclResource aclResource = new AclResource();
    		//获取到父节点的code
            String parentCode = jurisdictionTreeNode.getParentId().toString();
            String baseCode = null; //同级资源基础code
            
            String requestMethod = 1+"";//二级权限资源操作类型默认为1
            
            //判断新增的是二级资源还是三级资源
            if(parentCode.length()==3){//二级资源
            	baseCode = parentCode;
            }else{//三级资源
            	if(StringUtils.isBlank(jurisdictionTreeNode.getMethod())){
            		throw new JurisdictionException(ExceptionEnum.ACCREDIT_RESOURCE_SAVE_EXCEPTION, "三级权限资源[操作类型]不能为空！");
            	}
            	requestMethod = jurisdictionTreeNode.getMethod();
            	baseCode = parentCode + ZeroToNineEnum.ZERO.getCode() + RequestTypeEnum.getCode(jurisdictionTreeNode.getMethod());
            }
            
            //查询到当前方法,当前父资源下最大的序号,如果存在加1,如果不存在,则初始化
            Example example = new Example(AclResource.class);
            Example.Criteria criteria = example.createCriteria();
            criteria.andLike("code", baseCode+"%");
            criteria.andEqualTo("parentId", parentCode);
            example.orderBy("code").desc();
            List<AclResource> aclResourceList = jurisdictionService.selectByExample(example);
            if (aclResourceList != null && aclResourceList.size() > 0) {
                //存在的情况
                aclResource.setCode(aclResourceList.get(0).getCode() + 1);
            } else {
                //不存在,手动组合,从一开始
                String code = baseCode + ZeroToNineEnum.ZERO.getCode() + ZeroToNineEnum.ONE.getCode();
                aclResource.setCode(Long.parseLong(code));
            }
            aclResource.setId(jurisdictionTreeNode.getId());
            aclResource.setMethod(requestMethod);
            aclResource.setParentId(jurisdictionTreeNode.getParentId());
            aclResource.setName(jurisdictionTreeNode.getName());
            aclResource.setUrl(jurisdictionTreeNode.getUrl());
            aclResource.setUpdateTime(Calendar.getInstance().getTime());
            //aclResource.setIsValid(jurisdictionTreeNode.getIsValid());
            aclResource.setIsDeleted(Integer.valueOf(ZeroToNineEnum.ZERO.getCode()));
            jurisdictionService.updateByPrimaryKeySelective(aclResource);
        } catch (IllegalArgumentException e) {
            throw new JurisdictionException(ExceptionEnum.ACCREDIT_RESOURCE_SAVE_EXCEPTION, e.getMessage());
        }
    }

    @Override
    public List<Map<String, Object>> getHtmlJurisdiction(String userId) {
        List<Map<String, Object>> JurisdictionList = new ArrayList<>();
        AclUserAccreditInfo aclUserAccreditInfo = userAccreditInfoService.selectOneById(userId);
        try {
            AssertUtil.notNull(aclUserAccreditInfo, "用户授权信息不存在");
        } catch (IllegalArgumentException e) {
            throw new JurisdictionException(ExceptionEnum.SYSTEM_ACCREDIT_QUERY_EXCEPTION, "用户授权信息不存在");
        }
        //2.查询用户所拥有的角色
        List<AclUserAccreditRoleRelation> userRoleRelationList = userAccreditInfoRoleRelationService.selectListByUserAcId(aclUserAccreditInfo.getId());
        if (AssertUtil.collectionIsEmpty(userRoleRelationList)) {
            throw new JurisdictionException(ExceptionEnum.SYSTEM_ACCREDIT_QUERY_EXCEPTION, "用户角色信息不存在");
        }
        Long[] roleIds = new Long[userRoleRelationList.size()];
        for (int i = 0; i < userRoleRelationList.size(); i++) {
            roleIds[i] = userRoleRelationList.get(i).getRoleId();
        }
        //3.查询用户所有角色下的权限
        Example example = new Example(AclRoleResourceRelation.class);
        Example.Criteria criteria = example.createCriteria();
        List<Long> roleIdList = new ArrayList<>();
        Collections.addAll(roleIdList, roleIds);
        criteria.andIn("roleId", roleIdList);
        List<AclRoleResourceRelation> roleJdRelationList = roleJurisdictionRelationService.selectByExample(example);
        if (AssertUtil.collectionIsEmpty(roleJdRelationList)) {
            throw new JurisdictionException(ExceptionEnum.SYSTEM_ACCREDIT_QUERY_EXCEPTION, "用户权限信息不存在");
        }
        Set<Long> resourceCodeSet = new HashSet<>();
        for (AclRoleResourceRelation aclRoleResourceRelation : roleJdRelationList) {
            //取得资源码前3位
            resourceCodeSet.add(aclRoleResourceRelation.getResourceCode() / 100);
        }
        for (Long resourceCode : resourceCodeSet) {
            Map<String, Object> jurisdictionMap = new HashMap<>();
            jurisdictionMap.put("parentCode", resourceCode);
            Set<Long> longSet = new HashSet<>();
            for (AclRoleResourceRelation aclRoleResourceRelation : roleJdRelationList) {
                //取得资源码前3位
                if (resourceCode.equals(aclRoleResourceRelation.getResourceCode() / 100)) {
                    //取得资源码前5位
                    longSet.add(aclRoleResourceRelation.getResourceCode());
                }
            }
            jurisdictionMap.put("codeList", longSet);
            JurisdictionList.add(jurisdictionMap);
        }
        return JurisdictionList;
    }
    
    /**
     * @Description 新增权限资源参数校验
     * @param jurisdictionTreeNode
     */
    private void validateResourceForAdd(JurisdictionTreeNode jurisdictionTreeNode) {
        Assert.isTrue(null!=jurisdictionTreeNode,"参数不能为空!");
//        Assert.hasText(jurisdictionTreeNode.getMethod(),"权限资源[操作类型]不能为空！");
        Assert.isTrue(null!=jurisdictionTreeNode.getParentId(),"权限资源[父节点code]不能为空！");
        Assert.hasText(jurisdictionTreeNode.getName(),"权限资源[名称]不能为空！");
        Assert.hasText(jurisdictionTreeNode.getUrl(),"权限资源[URL路径]不能为空！");
    }
    
    /**
     * @Description 编辑权限资源参数校验
     * @param jurisdictionTreeNode
     */
    private void validateResourceForUpdate(JurisdictionTreeNode jurisdictionTreeNode) {
    	
        Assert.isTrue(null!=jurisdictionTreeNode,"参数不能为空!");
//        Assert.hasText(jurisdictionTreeNode.getMethod(),"权限资源[操作类型]不能为空！");
        Assert.isTrue(null!=jurisdictionTreeNode.getId(),"权限资源[id]不能为空!");
        Assert.isTrue(null!=jurisdictionTreeNode.getParentId(),"权限资源[父节点code]不能为空！");
        Assert.hasText(jurisdictionTreeNode.getName(),"权限资源[名称]不能为空！");
        Assert.hasText(jurisdictionTreeNode.getUrl(),"权限资源[URL路径]不能为空！");
    }
}
