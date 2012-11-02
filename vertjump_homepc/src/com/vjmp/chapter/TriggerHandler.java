package com.vjmp.chapter;

import com.vjmp.chapter.Chapter.ChapterState;
import com.vjmp.entities.drawable.TriggerEntity;
import com.vjmp.entities.drawable.TriggerEntity.TriggerType;
import com.vjmp.managers.TriggerEntityManager;

public class TriggerHandler {

	private Chapter chapter = null;
	private TriggerEntityManager triggerEntityManager = null;

	public TriggerHandler(Chapter chapter,
			TriggerEntityManager triggerEntityManager) {
		this.chapter = chapter;
		this.triggerEntityManager = triggerEntityManager;

	}

	public void update() {
		if (chapter.getChapterState() == ChapterState.RUNNING) {
			for (TriggerEntity trigger : triggerEntityManager) {
				if (trigger.getActivity() == true) {
					switch (trigger.getTriggerType()) {
					case FINISH_LINE:
						chapter.setChapterState(ChapterState.FINISHED);
						break;
					case SPIKE:
						chapter.setChapterState(ChapterState.DIED);
						break;
					case MESSAGE_BOX:

						break;
					}
				}

			}
		}
	}

}
