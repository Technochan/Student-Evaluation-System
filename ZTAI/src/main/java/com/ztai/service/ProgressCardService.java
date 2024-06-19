package com.ztai.service;

import java.util.List;

import com.ztai.dao.ProgressCardDAO;
import com.ztai.dao.RedisManager;
import com.ztai.model.ProgressCard;

public class ProgressCardService {
	
	ProgressCardDAO progressCardDAO;
	
	public ProgressCardService() {
		progressCardDAO = new ProgressCardDAO();
	}

	public List<ProgressCard> loadProgressCards() {
		if(RedisManager.getInstance().checkKey("progressCardInfo")) {
			return RedisManager.getInstance().getProgressCardInfo();
		} else {
			System.out.println("Redis not found");
			 List<ProgressCard> report = progressCardDAO.getProgressCardsInfo();
			 RedisManager.getInstance().setProfressCardInfo(report);
			 System.out.println("Redis updated");
			 return report;
		}
		
	}

}
