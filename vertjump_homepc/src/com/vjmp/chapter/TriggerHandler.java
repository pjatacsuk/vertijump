package com.vjmp.chapter;

import com.vjmp.chapter.Chapter.ChapterState;
import com.vjmp.entities.drawable.TriggerEntity;
import com.vjmp.entities.drawable.TriggerEntity.TriggerType;
import com.vjmp.managers.TriggerEntityManager;

/**
 * 
 * A Triggerek kezel�s�t v�gz� oszt�ly. A Chapter �llapot�t ({@link ChapterState}) v�ltoztatja az aktiv�lt 
 * triggereknek megfelel�en
 *
 */
public class TriggerHandler {

	private Chapter chapter = null;
	private TriggerEntityManager triggerEntityManager = null;

	/**
	 * 
	 * @param chapter : {@link Chapter} - a {@link TriggerHandler}-hez tartoz� {@link Chapter}.
	 * @param triggerEntityManager : {@link TriggerEntityManager} - a {@link TriggerEntity}-ket tartalmaz� manager.
	 */
	public TriggerHandler(Chapter chapter,
			TriggerEntityManager triggerEntityManager) {
		this.chapter = chapter;
		this.triggerEntityManager = triggerEntityManager;

	}

	/**
	 * Frissiti a chapterState-t �llapot�t a triggereknek megfelel�en.
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
