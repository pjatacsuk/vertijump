package com.vjmp;

import com.vjmp.chapter.Chapter;

public class UpdateThread implements Runnable{
	Chapter chapter = null;
	
	public UpdateThread(Chapter chapter) {
		this.chapter = chapter;
	}
	@Override
	public void run() {
		while(true) {
			go();
		}
		
	}
	private synchronized void go() {
		if(chapter.isReadyToUpdate) {
			chapter.update();
			chapter.isReadyToUpdate = false;
		}
		
	}
	public void update() {
		this.notify();
	}
	public void setChapter(Chapter chapter) {
		this.chapter = chapter;
	}

}
