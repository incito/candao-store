package com.candao.www.webroom.service.impl;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.candao.www.data.dao.TbResourceDao;
import com.candao.www.data.dao.TbResourceRoleDao;
import com.candao.www.data.model.TbResource;
import com.candao.www.webroom.model.TreeNode;
import com.candao.www.webroom.service.ResourceService;
@Service
public class ResourceServiceImpl implements ResourceService {
	@Autowired
    private TbResourceDao tbResourceDao;
	@Autowired
    private TbResourceRoleDao tbResourceRoleDao;

	@Override
	public TreeNode getList(String pid) {
		List<Map<String,Object>>  list=	tbResourceDao.find(null);
		TbResource tbResource=tbResourceDao.get("1");
		 TreeNode rootNode = new  TreeNode();
		rootNode.setId(tbResource.getResourcesid());
		rootNode.setText(tbResource.getResourcesname());
		rootNode.setLevel(0);
		rootNode.setLeaf(false);
		if(list.size()>0){
			rootNode = this.constructTree(rootNode, list, 0);
		}
		return rootNode;
	}
    
	public TreeNode constructTree(TreeNode rootNode, List<Map<String,Object>> orgList, int rootLevel){
		List<TreeNode> childNodeList = new ArrayList<TreeNode>();
		//构造根节点
		for(int i=0; i<orgList.size(); i++){
			Map<String,Object> org = orgList.get(i);
			if(org.get("resourcespid").toString().equals(rootNode.getId())){
				TreeNode childNode = new TreeNode();
				//copy Organization to TreeNode
//				System.out.println(org.getId());
				childNode.setId(org.get("resourcesid").toString());
				childNode.setText(org.get("resourcesname").toString());
//				childNode.setSn(org.getSn());
				childNode.setDescription(org.get("resourcesdesc").toString());
				childNode.setParent(rootNode);
				//设置深度
				childNode.setLevel(rootLevel+1);
				childNodeList.add(childNode);
			}
		}
		//设置子节点
		rootNode.setChildren(childNodeList);
		//设置是否叶子节点
		if(childNodeList.size()==0){
			rootNode.setLeaf(true);
		} else {
			rootNode.setLeaf(false);
		}
		//递归构造子节点
		for(int j=0; j<childNodeList.size();j++){
			//进入子节点构造时深度+1
			constructTree(childNodeList.get(j), orgList, ++rootLevel);
			//递归调用返回时，构造子节点的兄弟节点，深度要和该子节点深度一样，因为之前加1，所以要减1
			--rootLevel;
		}
		return rootNode;
	}

	@Override
	public boolean addResourceRole(String roleid, String[] resourceids) {
		tbResourceRoleDao.delete(roleid);
		return tbResourceRoleDao.inserts(roleid, resourceids)>0;
	}

	@Override
	public List<String> getRoleResource(String roleid) {
		return tbResourceRoleDao.getRoleResource(roleid);
	}
}