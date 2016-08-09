package com.candao.www.webroom.service.impl;

import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.candao.common.page.Page;
import com.candao.common.utils.ValidateUtils;
import com.candao.www.data.dao.TbTemplateDao;
import com.candao.www.data.dao.TdishDao;
import com.candao.www.data.model.Ttemplate;
import com.candao.www.webroom.service.TemplateService;
@Service
public class TemplateServiceImpl implements TemplateService {
	@Autowired
    private TbTemplateDao tbTemplateDao;
	@Autowired
    private TdishDao tdishDao;
	
	@Override
	public Page<Map<String, Object>> grid(Map<String, Object> params, int current, int pagesize) {
		return tbTemplateDao.page(params, current, pagesize);
	}

	@Override
	public boolean save(Ttemplate tbTemplate,String articleids) {
		if(!ValidateUtils.isEmpty(articleids)){
			tdishDao.updateArticleStatus(0, articleids.split(","));
		}
		return tbTemplateDao.insert(tbTemplate)>0;
	}

	@Override
	public boolean update(Ttemplate tbTemplate,String articleids,String oldarticleids) {
		if(!ValidateUtils.isEmpty(oldarticleids)){
			tdishDao.updateArticleStatus(1, oldarticleids.split(","));
			}
		if(!ValidateUtils.isEmpty(articleids)){
			tdishDao.updateArticleStatus(0, articleids.split(","));
			}
		return tbTemplateDao.update(tbTemplate)>0;
	}

	@Override
	public Ttemplate findById(String id) {
		return tbTemplateDao.get(id);
	}

	@Override
	public boolean deleteById(String id) {
		return tbTemplateDao.delete(id)>0;
	}

	@Override
	public boolean updateStatus(String id, int status) {
		Ttemplate tbTemplate=tbTemplateDao.get(id);
		tbTemplate.setStatus(status);
		return tbTemplateDao.update(tbTemplate)>0;
	}
	
	@Override
	public List<Map<String,Object>> getTemplates(Map<String, Object> params) {
		// TODO Auto-generated method stub
		return tbTemplateDao.find(params);
	}

	@Override
	public List<Ttemplate> validateTemplate(Map<String, Object> params) {
		// TODO Auto-generated method stub
		return tbTemplateDao.validateTemplate(params);
	}

	@Override
	public boolean updateTemplates(List<Map<String, Object>> list) {
		// TODO Auto-generated method stub
		return tbTemplateDao.updateTemplates(list)>0;
	}

}
