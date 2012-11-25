package com.vjmp.chapter;

import com.vjmp.chapter.Chapter.ChapterState;
import com.vjmp.entities.drawable.TriggerEntity;
import com.vjmp.entities.drawable.TriggerEntity.TriggerType;
import com.vjmp.managers.TriggerEntityManager;

/**
 * 
 * A Triggerek kezelését végzõ osztály. A Chapter állapotát ({@link ChapterState}) változtatja az aktivált 
 * triggereknek megfelelõen
 *
 */
public class TriggerHandler {

	private Chapter chapter = null;
	private TriggerEntityManager triggerEntityManager = null;

	/**
	 * 
	 * @param chapter : {@link Chapter} - a {@link TriggerHandler}-hez tartozó {@link Chapter}.
	 * @param triggerEntityManager : {@link TriggerEntityManager} - a {@link TriggerEntity}-ket tartalmazó manager.
	 */
	public TriggerHandler(Chapter chapter,
			TriggerEntityManager triggerEntityManager) {
		this.chapter = chapter;
		this.triggerEntityManager = triggerEntityManager;

	}

	/**
	 * Frissiti a chapterState-t állapotát a triggereknek megfelelõen.
	 */
	public void update() {
		if (chapter.getChapterState() == ChapterState.RUNNING) {
			for (TriggerEntity trigger : triggerEntityManager) {
				if (trigger.getActivity() == true) {
					switch (trigger.getTriggerType()) {
					case FINISH_LINE:
						chapter.setChapterState(ChapterState.FINISH_SCREEN);
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
