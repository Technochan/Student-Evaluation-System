package com.ztai.controller;

import java.util.ArrayList;
import java.util.List;

import com.opensymphony.xwork2.ActionSupport;
import com.ztai.model.ProgressCard;
import com.ztai.service.ProgressCardService;

public class LoadProgressCardAction extends ActionSupport {
	
	ProgressCardService progressCardService;
	
	public LoadProgressCardAction() {
		progressCardService = new ProgressCardService();
	}
	
	List<ProgressCard> progressCardsInfo = new ArrayList<>();
	
	@Override
	public String execute() throws Exception {
		System.out.println("progress info called");
		progressCardsInfo = progressCardService.loadProgressCards();
		System.out.println("Progress cards info get");
		System.out.println(progressCardsInfo.get(0).getProgressCardTotalSections()+1);
		return SUCCESS;
	}

	public ProgressCardService getProgressCardService() {
		return progressCardService;
	}

	public void setProgressCardService(ProgressCardService progressCardService) {
		this.progressCardService = progressCardService;
	}

	public List<ProgressCard> getProgressCardsInfo() {
		return progressCardsInfo;
	}

	public void setProgressCardsInfo(List<ProgressCard> progressCardsInfo) {
		this.progressCardsInfo = progressCardsInfo;
	}
	
}
